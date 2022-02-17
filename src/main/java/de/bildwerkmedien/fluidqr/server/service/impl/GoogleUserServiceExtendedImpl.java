package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import de.bildwerkmedien.fluidqr.server.security.AuthoritiesConstants;
import de.bildwerkmedien.fluidqr.server.security.SecurityUtils;
import de.bildwerkmedien.fluidqr.server.service.GoogleUserService;
import de.bildwerkmedien.fluidqr.server.service.UserNotAuthenticatedException;
import de.bildwerkmedien.fluidqr.server.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GoogleUser}.
 */
@Service
@Transactional
public class GoogleUserServiceExtendedImpl implements GoogleUserService {

    private final Logger log = LoggerFactory.getLogger(GoogleUserServiceExtendedImpl.class);

    private final GoogleUserRepository googleUserRepository;
    private final UserService userService;

    public GoogleUserServiceExtendedImpl(GoogleUserRepository googleUserRepository, UserService userService) {
        this.googleUserRepository = googleUserRepository;
        this.userService = userService;
    }

    @Override
    public GoogleUser save(GoogleUser googleUser) {
        log.debug("Request to save GoogleUser : {}", googleUser);

        if (!userService.getUserWithAuthorities().isPresent()) {
            throw new UserNotAuthenticatedException();
        }
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            userService.getUserWithAuthorities().ifPresent(googleUser::setUser);
        }

        if (
            this.googleUserRepository.findGoogleUsersByUser(userService.getUserWithAuthorities().orElse(null))
                .stream()
                .filter(googleUsr -> Objects.isNull(googleUsr.getRefreshToken()))
                .count() >
            0
        ) {
            throw new RuntimeException("Please activate all registered users");
        }

        if (Objects.isNull(googleUser.getId())) {
            googleUser.setUser(userService.getUserWithAuthorities().orElse(null));
            googleUser.setEnabled(Boolean.TRUE);
            googleUser.setCreationTime(Instant.now());
        }

        return googleUserRepository.save(googleUser);
    }

    @Override
    public Optional<GoogleUser> partialUpdate(GoogleUser googleUser) {
        log.debug("Request to partially update GoogleUser : {}", googleUser);

        return googleUserRepository
            .findById(googleUser.getId())
            .map(existingGoogleUser -> {
                if (googleUser.getName() != null) {
                    existingGoogleUser.setName(googleUser.getName());
                }
                if (googleUser.getRefreshToken() != null) {
                    existingGoogleUser.setRefreshToken(googleUser.getRefreshToken());
                }
                if (googleUser.getEnabled() != null) {
                    existingGoogleUser.setEnabled(googleUser.getEnabled());
                }
                if (googleUser.getCreationTime() != null) {
                    existingGoogleUser.setCreationTime(googleUser.getCreationTime());
                }

                return existingGoogleUser;
            })
            .map(googleUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoogleUser> findAll() {
        log.debug("Request to get all GoogleUsers");
        return googleUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoogleUser> findOne(Long id) {
        log.debug("Request to get GoogleUser : {}", id);
        return googleUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GoogleUser : {}", id);
        googleUserRepository.deleteById(id);
    }
}
