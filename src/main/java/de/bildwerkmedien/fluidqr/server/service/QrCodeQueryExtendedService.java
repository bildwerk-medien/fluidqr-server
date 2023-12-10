package de.bildwerkmedien.fluidqr.server.service;

import de.bildwerkmedien.fluidqr.server.domain.*;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.security.AuthoritiesConstants;
import de.bildwerkmedien.fluidqr.server.security.SecurityUtils;
import de.bildwerkmedien.fluidqr.server.service.criteria.QrCodeCriteria;
import de.bildwerkmedien.fluidqr.server.service.impl.QrCodeServiceExtendedImpl;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service for executing complex queries for {@link QrCode} entities in the database.
 * The main input is a {@link QrCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QrCode} or a {@link Page} of {@link QrCode} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QrCodeQueryExtendedService extends QrCodeQueryService {

    private final Logger log = LoggerFactory.getLogger(QrCodeQueryExtendedService.class);

    private final QrCodeRepository qrCodeRepository;
    private final UserService userService;
    private final QrCodeServiceExtendedImpl qrCodeService;

    public QrCodeQueryExtendedService(QrCodeRepository qrCodeRepository, UserService userService, QrCodeServiceExtendedImpl qrCodeService) {
        super(qrCodeRepository);
        this.qrCodeRepository = qrCodeRepository;
        this.userService = userService;
        this.qrCodeService = qrCodeService;
    }

    /**
     * Return a {@link Page} of {@link QrCode} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QrCode> findByCriteria(QrCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        addUserCriteria(criteria);
        final Specification<QrCode> specification = createSpecification(criteria);
        List<QrCode> qrCodePage = qrCodeRepository.findAll(specification);
        qrCodePage.forEach(qrCode -> {
            findCurrentRedirect(qrCode);
            qrCode.setLink(qrCodeService.getBaseUrl() + qrCode.getCode());
        });
        return qrCodePage;
    }

    @Transactional(readOnly = true)
    public QrCode findByCode(String code) {
        log.debug("find by code : {}", code);
        QrCodeCriteria qrCodeCriteria = new QrCodeCriteria();
        StringFilter stringFilter = new StringFilter();
        stringFilter.setEquals(code);
        qrCodeCriteria.setCode(stringFilter);
        final Specification<QrCode> specification = createSpecification(qrCodeCriteria);
        List<QrCode> qrCodePage = qrCodeRepository.findAll(specification);

        if (qrCodePage.size() == 1) {
            QrCode qrCode = qrCodePage.get(0);
            findCurrentRedirect(qrCode);
            qrCode.setLink(qrCodeService.getBaseUrl() + qrCodePage.get(0).getCode());
            return qrCode;
        }

        return null;
    }

    /**
     * Return a {@link Page} of {@link QrCode} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QrCode> findByCriteria(QrCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        addUserCriteria(criteria);
        final Specification<QrCode> specification = createSpecification(criteria);
        Page<QrCode> qrCodePage = qrCodeRepository.findAll(specification, page);
        qrCodePage
            .get()
            .forEach(qrCode -> {
                findCurrentRedirect(qrCode);
                qrCode.setLink(qrCodeService.getBaseUrl() + qrCode.getCode());
            });
        return qrCodePage;
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QrCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        addUserCriteria(criteria);
        final Specification<QrCode> specification = createSpecification(criteria);
        return qrCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link QrCodeCriteria} to a {@link Specification}
     *
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
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRedirectionId(),
                            root -> root.join(QrCode_.redirections, JoinType.LEFT).get(Redirection_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(QrCode_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }

    private void findCurrentRedirect(QrCode qrCode) {
        qrCode.setCurrentRedirect(
            qrCode.getRedirections().stream().filter(Redirection::getEnabled).findFirst().orElse(new Redirection()).getUrl()
        );
    }

    private void addUserCriteria(QrCodeCriteria criteria) {
        User user = userService.getUserWithAuthorities().orElseThrow(UserNotAuthenticatedException::new);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            LongFilter longFilter = new LongFilter();
            longFilter.setEquals(user.getId());
            criteria.setUserId(longFilter);
        }
    }
}
