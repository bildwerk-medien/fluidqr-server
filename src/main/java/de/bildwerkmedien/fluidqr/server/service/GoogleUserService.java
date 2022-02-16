package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link GoogleUser}.
 */
public interface GoogleUserService {
    /**
     * Save a googleUser.
     *
     * @param googleUser the entity to save.
     * @return the persisted entity.
     */
    GoogleUser save(GoogleUser googleUser);

    /**
     * Partially updates a googleUser.
     *
     * @param googleUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GoogleUser> partialUpdate(GoogleUser googleUser);

    /**
     * Get all the googleUsers.
     *
     * @return the list of entities.
     */
    List<GoogleUser> findAll();

    /**
     * Get the "id" googleUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GoogleUser> findOne(Long id);

    /**
     * Delete the "id" googleUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
