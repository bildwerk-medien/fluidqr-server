package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.Redirection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Redirection}.
 */
public interface RedirectionService {

    /**
     * Save a redirection.
     *
     * @param redirection the entity to save.
     * @return the persisted entity.
     */
    Redirection save(Redirection redirection);

    /**
     * Get all the redirections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Redirection> findAll(Pageable pageable);


    /**
     * Get the "id" redirection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Redirection> findOne(Long id);

    /**
     * Delete the "id" redirection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
