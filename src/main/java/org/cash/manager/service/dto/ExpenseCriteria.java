package org.cash.manager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.cash.manager.domain.enumeration.MonthType;
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
 * Criteria class for the {@link org.cash.manager.domain.Expense} entity. This class is used
 * in {@link org.cash.manager.web.rest.ExpenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExpenseCriteria implements Serializable, Criteria {
    /**
     * Class for filtering MonthType
     */
    public static class MonthTypeFilter extends Filter<MonthType> {

        public MonthTypeFilter() {
        }

        public MonthTypeFilter(MonthTypeFilter filter) {
            super(filter);
        }

        @Override
        public MonthTypeFilter copy() {
            return new MonthTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter loginId;

    private IntegerFilter voucherNo;

    private LocalDateFilter voucherDate;

    private MonthTypeFilter month;

    private BigDecimalFilter totalAmount;

    private BooleanFilter isPosted;

    private InstantFilter postDate;

    private StringFilter createdBy;

    private InstantFilter createdOn;

    private StringFilter modifiedBy;

    private InstantFilter modifiedOn;

    private LongFilter expanseDtlId;

    private LongFilter payToId;

    public ExpenseCriteria() {
    }

    public ExpenseCriteria(ExpenseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loginId = other.loginId == null ? null : other.loginId.copy();
        this.voucherNo = other.voucherNo == null ? null : other.voucherNo.copy();
        this.voucherDate = other.voucherDate == null ? null : other.voucherDate.copy();
        this.month = other.month == null ? null : other.month.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.isPosted = other.isPosted == null ? null : other.isPosted.copy();
        this.postDate = other.postDate == null ? null : other.postDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.modifiedBy = other.modifiedBy == null ? null : other.modifiedBy.copy();
        this.modifiedOn = other.modifiedOn == null ? null : other.modifiedOn.copy();
        this.expanseDtlId = other.expanseDtlId == null ? null : other.expanseDtlId.copy();
        this.payToId = other.payToId == null ? null : other.payToId.copy();
    }

    @Override
    public ExpenseCriteria copy() {
        return new ExpenseCriteria(this);
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

    public IntegerFilter getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(IntegerFilter voucherNo) {
        this.voucherNo = voucherNo;
    }

    public LocalDateFilter getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(LocalDateFilter voucherDate) {
        this.voucherDate = voucherDate;
    }

    public MonthTypeFilter getMonth() {
        return month;
    }

    public void setMonth(MonthTypeFilter month) {
        this.month = month;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
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

    public LongFilter getExpanseDtlId() {
        return expanseDtlId;
    }

    public void setExpanseDtlId(LongFilter expanseDtlId) {
        this.expanseDtlId = expanseDtlId;
    }

    public LongFilter getPayToId() {
        return payToId;
    }

    public void setPayToId(LongFilter payToId) {
        this.payToId = payToId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExpenseCriteria that = (ExpenseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(loginId, that.loginId) &&
            Objects.equals(voucherNo, that.voucherNo) &&
            Objects.equals(voucherDate, that.voucherDate) &&
            Objects.equals(month, that.month) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(isPosted, that.isPosted) &&
            Objects.equals(postDate, that.postDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(modifiedBy, that.modifiedBy) &&
            Objects.equals(modifiedOn, that.modifiedOn) &&
            Objects.equals(expanseDtlId, that.expanseDtlId) &&
            Objects.equals(payToId, that.payToId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        loginId,
        voucherNo,
        voucherDate,
        month,
        totalAmount,
        isPosted,
        postDate,
        createdBy,
        createdOn,
        modifiedBy,
        modifiedOn,
        expanseDtlId,
        payToId
        );
    }

    @Override
    public String toString() {
        return "ExpenseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (loginId != null ? "loginId=" + loginId + ", " : "") +
                (voucherNo != null ? "voucherNo=" + voucherNo + ", " : "") +
                (voucherDate != null ? "voucherDate=" + voucherDate + ", " : "") +
                (month != null ? "month=" + month + ", " : "") +
                (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
                (isPosted != null ? "isPosted=" + isPosted + ", " : "") +
                (postDate != null ? "postDate=" + postDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "") +
                (modifiedOn != null ? "modifiedOn=" + modifiedOn + ", " : "") +
                (expanseDtlId != null ? "expanseDtlId=" + expanseDtlId + ", " : "") +
                (payToId != null ? "payToId=" + payToId + ", " : "") +
            "}";
    }

}
