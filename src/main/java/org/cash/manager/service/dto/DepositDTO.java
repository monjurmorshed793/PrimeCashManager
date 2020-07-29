package org.cash.manager.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;
import org.cash.manager.domain.enumeration.DepositMedium;

/**
 * A DTO for the {@link org.cash.manager.domain.Deposit} entity.
 */
public class DepositDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String loginId;

    
    private Integer depositNo;

    @NotNull
    private String depositBy;

    @NotNull
    private LocalDate depositDate;

    @NotNull
    private DepositMedium medium;

    @NotNull
    private BigDecimal amount;

    
    @Lob
    private String note;

    private Boolean isPosted;

    private Instant postDate;

    private String createdBy;

    private Instant createdOn;

    private String modifiedBy;

    private Instant modifiedOn;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Integer getDepositNo() {
        return depositNo;
    }

    public void setDepositNo(Integer depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositBy() {
        return depositBy;
    }

    public void setDepositBy(String depositBy) {
        this.depositBy = depositBy;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
    }

    public DepositMedium getMedium() {
        return medium;
    }

    public void setMedium(DepositMedium medium) {
        this.medium = medium;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isIsPosted() {
        return isPosted;
    }

    public void setIsPosted(Boolean isPosted) {
        this.isPosted = isPosted;
    }

    public Instant getPostDate() {
        return postDate;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DepositDTO depositDTO = (DepositDTO) o;
        if (depositDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depositDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepositDTO{" +
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
