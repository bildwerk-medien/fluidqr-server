package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.service.QrCodeService;
import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    public QrCodeServiceImpl(QrCodeRepository qrCodeRepository, UserService userService) {
        this.qrCodeRepository = qrCodeRepository;
        this.userService = userService;
    }

    @Override
    public QrCode save(QrCode qrCode) {
        log.debug("Request to save QrCode : {}", qrCode);
        userService.getUserWithAuthorities().ifPresent(qrCode::setUser);
        QrCode savedQrCode =  qrCodeRepository.save(qrCode);
        savedQrCode.setLink("http://localhost:8080/redirect/" + savedQrCode.getCode());
        savedQrCode.setCurrentRedirect(savedQrCode.getRedirections().stream().filter(Redirection::isEnabled).findFirst().orElse(new Redirection()).getUrl());
        return savedQrCode;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QrCode> findAll(Pageable pageable) {
        log.debug("Request to get all QrCodes");
        Page<QrCode> qrCodePage = qrCodeRepository.findAll(pageable);
        qrCodePage.get().forEach(qrCode -> {
            findCurrentRedirect(qrCode);
            qrCode.setLink("http://localhost:8080/redirect/" + qrCode.getCode());
        });
        return qrCodePage;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<QrCode> findOne(Long id) {
        log.debug("Request to get QrCode : {}", id);
        Optional<QrCode> qrCode = qrCodeRepository.findById(id);
        qrCode.ifPresent(qr -> {
            findCurrentRedirect(qr);
            qr.setLink("http://localhost:8080/redirect/" + qr.getCode());
        });
        return qrCode;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QrCode : {}", id);
        qrCodeRepository.deleteById(id);
    }

    public void findCurrentRedirect(QrCode qrCode) {
        qrCode.setCurrentRedirect(qrCode.getRedirections().stream().filter(Redirection::isEnabled).findFirst().orElse(new Redirection()).getUrl());
    }
}
