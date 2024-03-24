package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.service.QrCodeQueryService;
import de.bildwerkmedien.fluidqr.server.service.QrCodeService;
import de.bildwerkmedien.fluidqr.server.service.criteria.QrCodeCriteria;
import de.bildwerkmedien.fluidqr.server.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.bildwerkmedien.fluidqr.server.domain.QrCode}.
 */
@RestController
@RequestMapping("/api/qr-codes")
public class QrCodeResource {

    private final Logger log = LoggerFactory.getLogger(QrCodeResource.class);

    private static final String ENTITY_NAME = "qrCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QrCodeService qrCodeService;

    private final QrCodeRepository qrCodeRepository;

    private final QrCodeQueryService qrCodeQueryService;

    public QrCodeResource(QrCodeService qrCodeService, QrCodeRepository qrCodeRepository, QrCodeQueryService qrCodeQueryService) {
        this.qrCodeService = qrCodeService;
        this.qrCodeRepository = qrCodeRepository;
        this.qrCodeQueryService = qrCodeQueryService;
    }

    /**
     * {@code POST  /qr-codes} : Create a new qrCode.
     *
     * @param qrCode the qrCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qrCode, or with status {@code 400 (Bad Request)} if the qrCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QrCode> createQrCode(@Valid @RequestBody QrCode qrCode) throws URISyntaxException {
        log.debug("REST request to save QrCode : {}", qrCode);
        if (qrCode.getId() != null) {
            throw new BadRequestAlertException("A new qrCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QrCode result = qrCodeService.save(qrCode);
        return ResponseEntity
            .created(new URI("/api/qr-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qr-codes/:id} : Updates an existing qrCode.
     *
     * @param id the id of the qrCode to save.
     * @param qrCode the qrCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qrCode,
     * or with status {@code 400 (Bad Request)} if the qrCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qrCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QrCode> updateQrCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QrCode qrCode
    ) throws URISyntaxException {
        log.debug("REST request to update QrCode : {}, {}", id, qrCode);
        if (qrCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qrCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qrCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QrCode result = qrCodeService.update(qrCode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qrCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /qr-codes/:id} : Partial updates given fields of an existing qrCode, field will ignore if it is null
     *
     * @param id the id of the qrCode to save.
     * @param qrCode the qrCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qrCode,
     * or with status {@code 400 (Bad Request)} if the qrCode is not valid,
     * or with status {@code 404 (Not Found)} if the qrCode is not found,
     * or with status {@code 500 (Internal Server Error)} if the qrCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QrCode> partialUpdateQrCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QrCode qrCode
    ) throws URISyntaxException {
        log.debug("REST request to partial update QrCode partially : {}, {}", id, qrCode);
        if (qrCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qrCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qrCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QrCode> result = qrCodeService.partialUpdate(qrCode);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qrCode.getId().toString())
        );
    }

    /**
     * {@code GET  /qr-codes} : get all the qrCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qrCodes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QrCode>> getAllQrCodes(
        QrCodeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
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
    @GetMapping("/count")
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
    @GetMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long id) {
        log.debug("REST request to delete QrCode : {}", id);
        qrCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
