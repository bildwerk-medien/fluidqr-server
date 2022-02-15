package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import de.bildwerkmedien.fluidqr.server.service.GoogleUserService;
import java.util.List;
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
public class GoogleUserServiceImpl implements GoogleUserService {

    private final Logger log = LoggerFactory.getLogger(GoogleUserServiceImpl.class);

    private final GoogleUserRepository googleUserRepository;

    public GoogleUserServiceImpl(GoogleUserRepository googleUserRepository) {
        this.googleUserRepository = googleUserRepository;
    }

    @Override
    public GoogleUser save(GoogleUser googleUser) {
        log.debug("Request to save GoogleUser : {}", googleUser);
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
