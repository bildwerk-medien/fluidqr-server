package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link QrCode}.
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
