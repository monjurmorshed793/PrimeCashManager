package org.cash.manager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.cash.manager.domain.enumeration.DepositMedium;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.cash.manager.domain.Deposit} entity. This class is used
 * in {@link org.cash.manager.web.rest.DepositResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /deposits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepositCriteria implements Serializable, Criteria {
    /**
     * Class for filtering DepositMedium
     */
    public static class DepositMediumFilter extends Filter<DepositMedium> {

        public DepositMediumFilter() {
        }

        public DepositMediumFilter(DepositMediumFilter filter) {
            super(filter);
        }

        @Override
        public DepositMediumFilter copy() {
            return new DepositMediumFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter loginId;

    private IntegerFilter depositNo;

    private StringFilter depositBy;

    private LocalDateFilter depositDate;

    private DepositMediumFilter medium;

    private BigDecimalFilter amount;

    private BooleanFilter isPosted;

    private InstantFilter postDate;

    private StringFilter createdBy;

    private InstantFilter createdOn;

    private StringFilter modifiedBy;

    private InstantFilter modifiedOn;

    public DepositCriteria() {
    }

    public DepositCriteria(DepositCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loginId = other.loginId == null ? null : other.loginId.copy();
        this.depositNo = other.depositNo == null ? null : other.depositNo.copy();
        this.depositBy = other.depositBy == null ? null : other.depositBy.copy();
        this.depositDate = other.depositDate == null ? null : other.depositDate.copy();
        this.medium = other.medium == null ? null : other.medium.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.isPosted = other.isPosted == null ? null : other.isPosted.copy();
        this.postDate = other.postDate == null ? null : other.postDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.modifiedBy = other.modifiedBy == null ? null : other.modifiedBy.copy();
        this.modifiedOn = other.modifiedOn == null ? null : other.modifiedOn.copy();
    }

    @Override
    public DepositCriteria copy() {
        return new DepositCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLoginId() {
        return loginId;
    }

    public void setLoginId(StringFilter loginId) {
        this.loginId = loginId;
    }

    public IntegerFilter getDepositNo() {
        return depositNo;
    }

    public void setDepositNo(IntegerFilter depositNo) {
        this.depositNo = depositNo;
    }

    public StringFilter getDepositBy() {
        return depositBy;
    }

    public void setDepositBy(StringFilter depositBy) {
        this.depositBy = depositBy;
    }

    public LocalDateFilter getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDateFilter depositDate) {
        this.depositDate = depositDate;
    }

    public DepositMediumFilter getMedium() {
        return medium;
    }

    public void setMedium(DepositMediumFilter medium) {
        this.medium = medium;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public BooleanFilter getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(BooleanFilter isPosted) {
        this.isPosted = isPosted;
    }

    public InstantFilter getPostDate() {
        return postDate;
    }

    public void setPostDate(InstantFilter postDate) {
        this.postDate = postDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public StringFilter getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(StringFilter modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public InstantFilter getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(InstantFilter modifiedOn) {
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
        final DepositCriteria that = (DepositCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(loginId, that.loginId) &&
            Objects.equals(depositNo, that.depositNo) &&
            Objects.equals(depositBy, that.depositBy) &&
            Objects.equals(depositDate, that.depositDate) &&
            Objects.equals(medium, that.medium) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(isPosted, that.isPosted) &&
            Objects.equals(postDate, that.postDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(modifiedBy, that.modifiedBy) &&
            Objects.equals(modifiedOn, that.modifiedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        loginId,
        depositNo,
        depositBy,
        depositDate,
        medium,
        amount,
        isPosted,
        postDate,
        createdBy,
        createdOn,
        modifiedBy,
        modifiedOn
        );
    }

    @Override
    public String toString() {
        return "DepositCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (loginId != null ? "loginId=" + loginId + ", " : "") +
                (depositNo != null ? "depositNo=" + depositNo + ", " : "") +
                (depositBy != null ? "depositBy=" + depositBy + ", " : "") +
                (depositDate != null ? "depositDate=" + depositDate + ", " : "") +
                (medium != null ? "medium=" + medium + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (isPosted != null ? "isPosted=" + isPosted + ", " : "") +
                (postDate != null ? "postDate=" + postDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "") +
                (modifiedOn != null ? "modifiedOn=" + modifiedOn + ", " : "") +
            "}";
    }

}
