package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.service.QrCodeService;
import de.bildwerkmedien.fluidqr.server.web.rest.errors.BadRequestAlertException;
import de.bildwerkmedien.fluidqr.server.service.dto.QrCodeCriteria;
import de.bildwerkmedien.fluidqr.server.service.QrCodeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.bildwerkmedien.fluidqr.server.domain.QrCode}.
 */
@RestController
@RequestMapping("/api")
public class QrCodeResource {

    private final Logger log = LoggerFactory.getLogger(QrCodeResource.class);

    private static final String ENTITY_NAME = "qrCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QrCodeService qrCodeService;

    private final QrCodeQueryService qrCodeQueryService;

    public QrCodeResource(QrCodeService qrCodeService, QrCodeQueryService qrCodeQueryService) {
        this.qrCodeService = qrCodeService;
        this.qrCodeQueryService = qrCodeQueryService;
    }

    /**
     * {@code POST  /qr-codes} : Create a new qrCode.
     *
     * @param qrCode the qrCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qrCode, or with status {@code 400 (Bad Request)} if the qrCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/qr-codes")
    public ResponseEntity<QrCode> createQrCode(@Valid @RequestBody QrCode qrCode) throws URISyntaxException {
        log.debug("REST request to save QrCode : {}", qrCode);
        if (qrCode.getId() != null) {
            throw new BadRequestAlertException("A new qrCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QrCode result = qrCodeService.save(qrCode);
        return ResponseEntity.created(new URI("/api/qr-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qr-codes} : Updates an existing qrCode.
     *
     * @param qrCode the qrCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qrCode,
     * or with status {@code 400 (Bad Request)} if the qrCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qrCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/qr-codes")
    public ResponseEntity<QrCode> updateQrCode(@Valid @RequestBody QrCode qrCode) throws URISyntaxException {
        log.debug("REST request to update QrCode : {}", qrCode);
        if (qrCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QrCode result = qrCodeService.save(qrCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qrCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /qr-codes} : get all the qrCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qrCodes in body.
     */
    @GetMapping("/qr-codes")
    public ResponseEntity<List<QrCode>> getAllQrCodes(QrCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QrCodes by criteria: {}", criteria);
        Page<QrCode> page = qrCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qr-codes/count} : count all the qrCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/qr-codes/count")
    public ResponseEntity<Long> countQrCodes(QrCodeCriteria criteria) {
        log.debug("REST request to count QrCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(qrCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /qr-codes/:id} : get the "id" qrCode.
     *
     * @param id the id of the qrCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qrCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/qr-codes/{id}")
    public ResponseEntity<QrCode> getQrCode(@PathVariable Long id) {
        log.debug("REST request to get QrCode : {}", id);
        Optional<QrCode> qrCode = qrCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qrCode);
    }

    /**
     * {@code DELETE  /qr-codes/:id} : delete the "id" qrCode.
     *
     * @param id the id of the qrCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/qr-codes/{id}")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long id) {
        log.debug("REST request to delete QrCode : {}", id);
        qrCodeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
