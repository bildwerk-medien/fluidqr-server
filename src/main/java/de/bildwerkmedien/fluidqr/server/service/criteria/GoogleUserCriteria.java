package de.bildwerkmedien.fluidqr.server.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link de.bildwerkmedien.fluidqr.server.domain.GoogleUser} entity. This class is used
 * in {@link de.bildwerkmedien.fluidqr.server.web.rest.GoogleUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /google-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GoogleUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter refreshToken;

    private BooleanFilter enabled;

    private InstantFilter creationTime;

    private LongFilter userId;

    private Boolean distinct;

    public GoogleUserCriteria() {}

    public GoogleUserCriteria(GoogleUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.refreshToken = other.refreshToken == null ? null : other.refreshToken.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.creationTime = other.creationTime == null ? null : other.creationTime.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GoogleUserCriteria copy() {
        return new GoogleUserCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getRefreshToken() {
        return refreshToken;
    }

    public StringFilter refreshToken() {
        if (refreshToken == null) {
            refreshToken = new StringFilter();
        }
        return refreshToken;
    }

    public void setRefreshToken(StringFilter refreshToken) {
        this.refreshToken = refreshToken;
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

    public InstantFilter getCreationTime() {
        return creationTime;
    }

    public InstantFilter creationTime() {
        if (creationTime == null) {
            creationTime = new InstantFilter();
        }
        return creationTime;
    }

    public void setCreationTime(InstantFilter creationTime) {
        this.creationTime = creationTime;
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
        final GoogleUserCriteria that = (GoogleUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(refreshToken, that.refreshToken) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(creationTime, that.creationTime) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, refreshToken, enabled, creationTime, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GoogleUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (refreshToken != null ? "refreshToken=" + refreshToken + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (creationTime != null ? "creationTime=" + creationTime + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
