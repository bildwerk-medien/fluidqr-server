package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.*; // for static metamodels
import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import de.bildwerkmedien.fluidqr.server.service.criteria.GoogleUserCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GoogleUser} entities in the database.
 * The main input is a {@link GoogleUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GoogleUser} or a {@link Page} of {@link GoogleUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GoogleUserQueryService extends QueryService<GoogleUser> {

    private final Logger log = LoggerFactory.getLogger(GoogleUserQueryService.class);

    private final GoogleUserRepository googleUserRepository;

    public GoogleUserQueryService(GoogleUserRepository googleUserRepository) {
        this.googleUserRepository = googleUserRepository;
    }

    /**
     * Return a {@link List} of {@link GoogleUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GoogleUser> findByCriteria(GoogleUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GoogleUser> specification = createSpecification(criteria);
        return googleUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GoogleUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GoogleUser> findByCriteria(GoogleUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GoogleUser> specification = createSpecification(criteria);
        return googleUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GoogleUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GoogleUser> specification = createSpecification(criteria);
        return googleUserRepository.count(specification);
    }

    /**
     * Function to convert {@link GoogleUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GoogleUser> createSpecification(GoogleUserCriteria criteria) {
        Specification<GoogleUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GoogleUser_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), GoogleUser_.name));
            }
            if (criteria.getRefreshToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRefreshToken(), GoogleUser_.refreshToken));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), GoogleUser_.enabled));
            }
            if (criteria.getCreationTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationTime(), GoogleUser_.creationTime));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(GoogleUser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
