package de.bildwerkmedien.fluidqr.server.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link de.bildwerkmedien.fluidqr.server.domain.Redirection} entity. This class is used
 * in {@link de.bildwerkmedien.fluidqr.server.web.rest.RedirectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /redirections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RedirectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter code;

    private StringFilter url;

    private BooleanFilter enabled;

    private ZonedDateTimeFilter creation;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private LongFilter userId;

    private LongFilter qrCodeId;

    private Boolean distinct;

    public RedirectionCriteria() {}

    public RedirectionCriteria(RedirectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.creation = other.creation == null ? null : other.creation.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.qrCodeId = other.qrCodeId == null ? null : other.qrCodeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RedirectionCriteria copy() {
        return new RedirectionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            enabled = new BooleanFilter();
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public ZonedDateTimeFilter getCreation() {
        return creation;
    }

    public ZonedDateTimeFilter creation() {
        if (creation == null) {
            creation = new ZonedDateTimeFilter();
        }
        return creation;
    }

    public void setCreation(ZonedDateTimeFilter creation) {
        this.creation = creation;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public ZonedDateTimeFilter endDate() {
        if (endDate == null) {
            endDate = new ZonedDateTimeFilter();
        }
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getQrCodeId() {
        return qrCodeId;
    }

    public LongFilter qrCodeId() {
        if (qrCodeId == null) {
            qrCodeId = new LongFilter();
        }
        return qrCodeId;
    }

    public void setQrCodeId(LongFilter qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RedirectionCriteria that = (RedirectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(code, that.code) &&
            Objects.equals(url, that.url) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(creation, that.creation) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(qrCodeId, that.qrCodeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, code, url, enabled, creation, startDate, endDate, userId, qrCodeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RedirectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (creation != null ? "creation=" + creation + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (qrCodeId != null ? "qrCodeId=" + qrCodeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
