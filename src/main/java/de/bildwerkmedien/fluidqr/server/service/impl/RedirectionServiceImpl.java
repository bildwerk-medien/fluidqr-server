package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.security.AuthoritiesConstants;
import de.bildwerkmedien.fluidqr.server.security.SecurityUtils;
import de.bildwerkmedien.fluidqr.server.service.RedirectionService;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.repository.RedirectionRepository;
import de.bildwerkmedien.fluidqr.server.service.UserNotAuthenticatedException;
import de.bildwerkmedien.fluidqr.server.service.UserNotAuthorizedException;
import de.bildwerkmedien.fluidqr.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Redirection}.
 */
@Service
@Transactional
public class RedirectionServiceImpl implements RedirectionService {

    private final Logger log = LoggerFactory.getLogger(RedirectionServiceImpl.class);

    private final RedirectionRepository redirectionRepository;
    private final UserService userService;

    public RedirectionServiceImpl(RedirectionRepository redirectionRepository, UserService userService) {
        this.redirectionRepository = redirectionRepository;
        this.userService = userService;
    }

    @Override
    public Redirection save(Redirection redirection) {
        log.debug("Request to save Redirection : {}", redirection);
        if (redirection.getId() != null && !findOne(redirection.getId()).isPresent()) {
            throw new UserNotAuthorizedException();
        }
        if(!userService.getUserWithAuthorities().isPresent()){
            throw new UserNotAuthenticatedException();
        }

        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            userService.getUserWithAuthorities().ifPresent(redirection::setUser);
        }
        return redirectionRepository.save(redirection);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Redirection> findOne(Long id) {
        log.debug("Request to get Redirection : {}", id);
        Optional<Redirection> redirection =  redirectionRepository.findById(id);

        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return redirection;
        }

        if(redirection.isPresent() && redirection.get().getUser() != null && redirection.get().getUser().equals(userService.getUserWithAuthorities().orElse(null))) {
            return redirection;
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Redirection : {}", id);
        if(findOne(id).isPresent()) {
            redirectionRepository.deleteById(id);
        }
    }
}
