package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link de.bildwerkmedien.fluidqr.server.domain.QrCode}.
 */
public interface QrCodeService {
    /**
     * Save a qrCode.
     *
     * @param qrCode the entity to save.
     * @return the persisted entity.
     */
    QrCode save(QrCode qrCode);

    /**
     * Updates a qrCode.
     *
     * @param qrCode the entity to update.
     * @return the persisted entity.
     */
    QrCode update(QrCode qrCode);

    /**
     * Partially updates a qrCode.
     *
     * @param qrCode the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QrCode> partialUpdate(QrCode qrCode);

    /**
     * Get all the qrCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QrCode> findAll(Pageable pageable);

    /**
     * Get the "id" qrCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QrCode> findOne(Long id);

    /**
     * Delete the "id" qrCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
