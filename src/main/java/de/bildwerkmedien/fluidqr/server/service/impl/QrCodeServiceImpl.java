package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.security.AuthoritiesConstants;
import de.bildwerkmedien.fluidqr.server.security.SecurityUtils;
import de.bildwerkmedien.fluidqr.server.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link QrCode}.
 */
@Service
@Transactional
public class QrCodeServiceImpl implements QrCodeService {

    private final Logger log = LoggerFactory.getLogger(QrCodeServiceImpl.class);

    private final QrCodeRepository qrCodeRepository;
    private final UserService userService;
    private final RedirectionService redirectionService;

    @Value("${application.url.base}")
    private String baseUrl;

    @Value("${application.url.protocol}")
    private String protocol;

    public QrCodeServiceImpl(QrCodeRepository qrCodeRepository, UserService userService, RedirectionService redirectionService) {
        this.qrCodeRepository = qrCodeRepository;
        this.userService = userService;
        this.redirectionService = redirectionService;
    }

    @Override
    public QrCode save(QrCode qrCode) {
        log.debug("Request to save QrCode : {}", qrCode);
        if (qrCode.getId() != null && !findOne(qrCode.getId()).isPresent()) {
            throw new UserNotAuthorizedException();
        }
        if(!userService.getUserWithAuthorities().isPresent()){
            throw new UserNotAuthenticatedException();
        }
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            userService.getUserWithAuthorities().ifPresent(qrCode::setUser);
        }
        QrCode savedQrCode = qrCodeRepository.save(qrCode);
        savedQrCode.setLink(getBaseUrl() + savedQrCode.getCode());
        savedQrCode.setCurrentRedirect(savedQrCode.getRedirections().stream().filter(Redirection::isEnabled).findFirst().orElse(new Redirection()).getUrl());
        return savedQrCode;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<QrCode> findOne(Long id) {
        log.debug("Request to get QrCode : {}", id);
        Optional<QrCode> qrCode = qrCodeRepository.findById(id);
        qrCode.ifPresent(qr -> {
            findCurrentRedirect(qr);
            qr.setLink(getBaseUrl() + qr.getCode());
        });

        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return qrCode;
        }

        if(qrCode.isPresent() && qrCode.get().getUser() != null && qrCode.get().getUser().equals(userService.getUserWithAuthorities().orElse(null))) {
            return qrCode;
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QrCode : {}", id);

       findOne(id).ifPresent(qrCode -> {
           qrCode.getRedirections().forEach(redirection -> redirectionService.delete(redirection.getId()));
           qrCodeRepository.deleteById(qrCode.getId());
       });
    }

    public void findCurrentRedirect(QrCode qrCode) {
        qrCode.setCurrentRedirect(qrCode.getRedirections().stream().filter(Redirection::isEnabled).findFirst().orElse(new Redirection()).getUrl());
    }

    public String getBaseUrl(){
        return this.protocol + this.baseUrl + "/go/";
    }
}
