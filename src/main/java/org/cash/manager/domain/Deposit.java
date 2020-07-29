package org.cash.manager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.cash.manager.domain.enumeration.DepositMedium;

/**
 * A Deposit.
 */
@Entity
@Table(name = "deposit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Deposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "login_id", nullable = false)
    private String loginId;

    
    @Column(name = "deposit_no", unique = true)
    private Integer depositNo;

    @NotNull
    @Column(name = "deposit_by", nullable = false)
    private String depositBy;

    @NotNull
    @Column(name = "deposit_date", nullable = false)
    private LocalDate depositDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "medium", nullable = false)
    private DepositMedium medium;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    
    @Lob
    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "is_posted")
    private Boolean isPosted;

    @Column(name = "post_date")
    private Instant postDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_on")
    private Instant modifiedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public Deposit loginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Integer getDepositNo() {
        return depositNo;
    }

    public Deposit depositNo(Integer depositNo) {
        this.depositNo = depositNo;
        return this;
    }

    public void setDepositNo(Integer depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositBy() {
        return depositBy;
    }

    public Deposit depositBy(String depositBy) {
        this.depositBy = depositBy;
        return this;
    }

    public void setDepositBy(String depositBy) {
        this.depositBy = depositBy;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    public Deposit depositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
        return this;
    }

    public void setDepositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
    }

    public DepositMedium getMedium() {
        return medium;
    }

    public Deposit medium(DepositMedium medium) {
        this.medium = medium;
        return this;
    }

    public void setMedium(DepositMedium medium) {
        this.medium = medium;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Deposit amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public Deposit note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isIsPosted() {
        return isPosted;
    }

    public Deposit isPosted(Boolean isPosted) {
        this.isPosted = isPosted;
        return this;
    }

    public void setIsPosted(Boolean isPosted) {
        this.isPosted = isPosted;
    }

    public Instant getPostDate() {
        return postDate;
    }

    public Deposit postDate(Instant postDate) {
        this.postDate = postDate;
        return this;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Deposit createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Deposit createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Deposit modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public Deposit modifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deposit)) {
            return false;
        }
        return id != null && id.equals(((Deposit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Deposit{" +
            "id=" + getId() +
            ", loginId='" + getLoginId() + "'" +
            ", depositNo=" + getDepositNo() +
            ", depositBy='" + getDepositBy() + "'" +
            ", depositDate='" + getDepositDate() + "'" +
            ", medium='" + getMedium() + "'" +
            ", amount=" + getAmount() +
            ", note='" + getNote() + "'" +
            ", isPosted='" + isIsPosted() + "'" +
            ", postDate='" + getPostDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            "}";
    }
}
