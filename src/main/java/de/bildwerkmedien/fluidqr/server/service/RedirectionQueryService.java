package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.*; // for static metamodels
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.repository.RedirectionRepository;
import de.bildwerkmedien.fluidqr.server.service.criteria.RedirectionCriteria;
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
 * Service for executing complex queries for {@link Redirection} entities in the database.
 * The main input is a {@link RedirectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Redirection} or a {@link Page} of {@link Redirection} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RedirectionQueryService extends QueryService<Redirection> {

    private final Logger log = LoggerFactory.getLogger(RedirectionQueryService.class);

    private final RedirectionRepository redirectionRepository;

    public RedirectionQueryService(RedirectionRepository redirectionRepository) {
        this.redirectionRepository = redirectionRepository;
    }

    /**
     * Return a {@link List} of {@link Redirection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Redirection> findByCriteria(RedirectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Redirection> specification = createSpecification(criteria);
        return redirectionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Redirection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Redirection> findByCriteria(RedirectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Redirection> specification = createSpecification(criteria);
        return redirectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RedirectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Redirection> specification = createSpecification(criteria);
        return redirectionRepository.count(specification);
    }

    /**
     * Function to convert {@link RedirectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Redirection> createSpecification(RedirectionCriteria criteria) {
        Specification<Redirection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Redirection_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Redirection_.description));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Redirection_.code));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Redirection_.url));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Redirection_.enabled));
            }
            if (criteria.getCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreation(), Redirection_.creation));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Redirection_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Redirection_.endDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Redirection_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getQrCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getQrCodeId(), root -> root.join(Redirection_.qrCode, JoinType.LEFT).get(QrCode_.id))
                    );
            }
        }
        return specification;
    }
}
