package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import de.bildwerkmedien.fluidqr.server.service.GoogleUserQueryService;
import de.bildwerkmedien.fluidqr.server.service.GoogleUserService;
import de.bildwerkmedien.fluidqr.server.service.criteria.GoogleUserCriteria;
import de.bildwerkmedien.fluidqr.server.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link GoogleUser}.
 */
@RestController
@RequestMapping("/api")
public class GoogleUserResource {

    private final Logger log = LoggerFactory.getLogger(GoogleUserResource.class);

    private static final String ENTITY_NAME = "googleUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoogleUserService googleUserService;

    private final GoogleUserRepository googleUserRepository;

    private final GoogleUserQueryService googleUserQueryService;

    public GoogleUserResource(
        @Qualifier("googleUserServiceExtendedImpl") GoogleUserService googleUserService,
        GoogleUserRepository googleUserRepository,
        GoogleUserQueryService googleUserQueryService
    ) {
        this.googleUserService = googleUserService;
        this.googleUserRepository = googleUserRepository;
        this.googleUserQueryService = googleUserQueryService;
    }

    /**
     * {@code POST  /google-users} : Create a new googleUser.
     *
     * @param googleUser the googleUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new googleUser, or with status {@code 400 (Bad Request)} if the googleUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/google-users")
    public ResponseEntity<GoogleUser> createGoogleUser(@Valid @RequestBody GoogleUser googleUser) throws URISyntaxException {
        log.debug("REST request to save GoogleUser : {}", googleUser);
        if (googleUser.getId() != null) {
            throw new BadRequestAlertException("A new googleUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoogleUser result = googleUserService.save(googleUser);
        return ResponseEntity
            .created(new URI("/api/google-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /google-users/:id} : Updates an existing googleUser.
     *
     * @param id the id of the googleUser to save.
     * @param googleUser the googleUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated googleUser,
     * or with status {@code 400 (Bad Request)} if the googleUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the googleUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/google-users/{id}")
    public ResponseEntity<GoogleUser> updateGoogleUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GoogleUser googleUser
    ) throws URISyntaxException {
        log.debug("REST request to update GoogleUser : {}, {}", id, googleUser);
        if (googleUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, googleUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!googleUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GoogleUser result = googleUserService.save(googleUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, googleUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /google-users/:id} : Partial updates given fields of an existing googleUser, field will ignore if it is null
     *
     * @param id the id of the googleUser to save.
     * @param googleUser the googleUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated googleUser,
     * or with status {@code 400 (Bad Request)} if the googleUser is not valid,
     * or with status {@code 404 (Not Found)} if the googleUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the googleUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/google-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GoogleUser> partialUpdateGoogleUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GoogleUser googleUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update GoogleUser partially : {}, {}", id, googleUser);
        if (googleUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, googleUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!googleUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoogleUser> result = googleUserService.partialUpdate(googleUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, googleUser.getId().toString())
        );
    }

    /**
     * {@code GET  /google-users} : get all the googleUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of googleUsers in body.
     */
    @GetMapping("/google-users")
    public ResponseEntity<List<GoogleUser>> getAllGoogleUsers(GoogleUserCriteria criteria) {
        log.debug("REST request to get GoogleUsers by criteria: {}", criteria);
        List<GoogleUser> entityList = googleUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /google-users/count} : count all the googleUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/google-users/count")
    public ResponseEntity<Long> countGoogleUsers(GoogleUserCriteria criteria) {
        log.debug("REST request to count GoogleUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(googleUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /google-users/:id} : get the "id" googleUser.
     *
     * @param id the id of the googleUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the googleUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/google-users/{id}")
    public ResponseEntity<GoogleUser> getGoogleUser(@PathVariable Long id) {
        log.debug("REST request to get GoogleUser : {}", id);
        Optional<GoogleUser> googleUser = googleUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(googleUser);
    }

    /**
     * {@code DELETE  /google-users/:id} : delete the "id" googleUser.
     *
     * @param id the id of the googleUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/google-users/{id}")
    public ResponseEntity<Void> deleteGoogleUser(@PathVariable Long id) {
        log.debug("REST request to delete GoogleUser : {}", id);
        googleUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
