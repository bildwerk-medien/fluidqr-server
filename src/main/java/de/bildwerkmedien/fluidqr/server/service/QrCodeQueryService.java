package de.bildwerkmedien.fluidqr.server.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.domain.*; // for static metamodels
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.service.dto.QrCodeCriteria;

/**
 * Service for executing complex queries for {@link QrCode} entities in the database.
 * The main input is a {@link QrCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QrCode} or a {@link Page} of {@link QrCode} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QrCodeQueryService extends QueryService<QrCode> {

    private final Logger log = LoggerFactory.getLogger(QrCodeQueryService.class);

    private final QrCodeRepository qrCodeRepository;

    public QrCodeQueryService(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    /**
     * Return a {@link List} of {@link QrCode} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QrCode> findByCriteria(QrCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QrCode> specification = createSpecification(criteria);
        return qrCodeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link QrCode} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QrCode> findByCriteria(QrCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QrCode> specification = createSpecification(criteria);
        Page<QrCode> qrCodePage = qrCodeRepository.findAll(specification, page);
        qrCodePage.get().forEach(qrCode -> {
            findCurrentRedirect(qrCode);
            qrCode.setLink("http://localhost:8080/redirect/" + qrCode.getCode());
        });
        return qrCodePage;
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QrCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QrCode> specification = createSpecification(criteria);
        return qrCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link QrCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QrCode> createSpecification(QrCodeCriteria criteria) {
        Specification<QrCode> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QrCode_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), QrCode_.code));
            }
            if (criteria.getRedirectionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRedirectionId(),
                    root -> root.join(QrCode_.redirections, JoinType.LEFT).get(Redirection_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(QrCode_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }

    private void findCurrentRedirect(QrCode qrCode) {
        qrCode.setCurrentRedirect(qrCode.getRedirections().stream().filter(Redirection::isEnabled).findFirst().orElse(new Redirection()).getUrl());
    }
}
