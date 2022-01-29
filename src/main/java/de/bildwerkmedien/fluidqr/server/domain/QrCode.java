package de.bildwerkmedien.fluidqr.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A QrCode.
 */
@Entity
@Table(name = "qr_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QrCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "qrCode")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Redirection> redirections = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JsonIgnoreProperties(value = "qrCodes", allowSetters = true)
    private User user;

//    custom transient fields ### start ###

    @Transient
    @JsonSerialize
    private String link;

    @Transient
    @JsonSerialize
    private String currentRedirect;

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("currentRedirect")
    public String getCurrentRedirect() {
        return currentRedirect;
    }

    public void setCurrentRedirect(String currentRedirect) {
        this.currentRedirect = currentRedirect;
    }

//        custom transient fields ### end ###

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public QrCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Redirection> getRedirections() {
        return redirections;
    }

    public QrCode redirections(Set<Redirection> redirections) {
        this.redirections = redirections;
        return this;
    }

    public QrCode addRedirection(Redirection redirection) {
        this.redirections.add(redirection);
        redirection.setQrCode(this);
        return this;
    }

    public QrCode removeRedirection(Redirection redirection) {
        this.redirections.remove(redirection);
        redirection.setQrCode(null);
        return this;
    }

    public void setRedirections(Set<Redirection> redirections) {
        this.redirections = redirections;
    }

    public User getUser() {
        return user;
    }

    public QrCode user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QrCode)) {
            return false;
        }
        return id != null && id.equals(((QrCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QrCode{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
