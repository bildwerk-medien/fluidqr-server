package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.service.RedirectionQueryExtendedService;
import de.bildwerkmedien.fluidqr.server.service.RedirectionService;
import de.bildwerkmedien.fluidqr.server.service.dto.RedirectionCriteria;
import de.bildwerkmedien.fluidqr.server.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Redirection}.
 */
@RestController
@RequestMapping("/api")
public class RedirectionResource {

    private final Logger log = LoggerFactory.getLogger(RedirectionResource.class);

    private static final String ENTITY_NAME = "redirection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RedirectionService redirectionService;

    private final RedirectionQueryExtendedService redirectionQueryService;

    public RedirectionResource(@Qualifier("redirectionServiceExtendedImpl") RedirectionService redirectionService, RedirectionQueryExtendedService redirectionQueryService) {
        this.redirectionService = redirectionService;
        this.redirectionQueryService = redirectionQueryService;
    }

    /**
     * {@code POST  /redirections} : Create a new redirection.
     *
     * @param redirection the redirection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new redirection, or with status {@code 400 (Bad Request)} if the redirection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/redirections")
    public ResponseEntity<Redirection> createRedirection(@Valid @RequestBody Redirection redirection) throws URISyntaxException {
        log.debug("REST request to save Redirection : {}", redirection);
        if (redirection.getId() != null) {
            throw new BadRequestAlertException("A new redirection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Redirection result = redirectionService.save(redirection);
        return ResponseEntity.created(new URI("/api/redirections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /redirections} : Updates an existing redirection.
     *
     * @param redirection the redirection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redirection,
     * or with status {@code 400 (Bad Request)} if the redirection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the redirection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/redirections")
    public ResponseEntity<Redirection> updateRedirection(@Valid @RequestBody Redirection redirection) throws URISyntaxException {
        log.debug("REST request to update Redirection : {}", redirection);
        if (redirection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Redirection result = redirectionService.save(redirection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, redirection.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /redirections} : get all the redirections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of redirections in body.
     */
    @GetMapping("/redirections")
    public ResponseEntity<List<Redirection>> getAllRedirections(RedirectionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Redirections by criteria: {}", criteria);
        Page<Redirection> page = redirectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /redirections/count} : count all the redirections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/redirections/count")
    public ResponseEntity<Long> countRedirections(RedirectionCriteria criteria) {
        log.debug("REST request to count Redirections by criteria: {}", criteria);
        return ResponseEntity.ok().body(redirectionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /redirections/:id} : get the "id" redirection.
     *
     * @param id the id of the redirection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the redirection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/redirections/{id}")
    public ResponseEntity<Redirection> getRedirection(@PathVariable Long id) {
        log.debug("REST request to get Redirection : {}", id);
        Optional<Redirection> redirection = redirectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(redirection);
    }

    /**
     * {@code DELETE  /redirections/:id} : delete the "id" redirection.
     *
     * @param id the id of the redirection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/redirections/{id}")
    public ResponseEntity<Void> deleteRedirection(@PathVariable Long id) {
        log.debug("REST request to delete Redirection : {}", id);
        redirectionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
