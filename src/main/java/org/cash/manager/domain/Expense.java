package org.cash.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.cash.manager.domain.enumeration.MonthType;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "login_id", nullable = false)
    private String loginId;

    
    @Column(name = "voucher_no", unique = true)
    private Integer voucherNo;

    @NotNull
    @Column(name = "voucher_date", nullable = false)
    private LocalDate voucherDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private MonthType month;

    
    @Lob
    @Column(name = "notes", nullable = false)
    private String notes;

    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

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

    @OneToMany(mappedBy = "expense")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExpenseDtl> expanseDtls = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("expenses")
    private PayTo payTo;

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

    public Expense loginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Integer getVoucherNo() {
        return voucherNo;
    }

    public Expense voucherNo(Integer voucherNo) {
        this.voucherNo = voucherNo;
        return this;
    }

    public void setVoucherNo(Integer voucherNo) {
        this.voucherNo = voucherNo;
    }

    public LocalDate getVoucherDate() {
        return voucherDate;
    }

    public Expense voucherDate(LocalDate voucherDate) {
        this.voucherDate = voucherDate;
        return this;
    }

    public void setVoucherDate(LocalDate voucherDate) {
        this.voucherDate = voucherDate;
    }

    public MonthType getMonth() {
        return month;
    }

    public Expense month(MonthType month) {
        this.month = month;
        return this;
    }

    public void setMonth(MonthType month) {
        this.month = month;
    }

    public String getNotes() {
        return notes;
    }

    public Expense notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Expense totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean isIsPosted() {
        return isPosted;
    }

    public Expense isPosted(Boolean isPosted) {
        this.isPosted = isPosted;
        return this;
    }

    public void setIsPosted(Boolean isPosted) {
        this.isPosted = isPosted;
    }

    public Instant getPostDate() {
        return postDate;
    }

    public Expense postDate(Instant postDate) {
        this.postDate = postDate;
        return this;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Expense createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Expense createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Expense modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public Expense modifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Set<ExpenseDtl> getExpanseDtls() {
        return expanseDtls;
    }

    public Expense expanseDtls(Set<ExpenseDtl> expenseDtls) {
        this.expanseDtls = expenseDtls;
        return this;
    }

    public Expense addExpanseDtl(ExpenseDtl expenseDtl) {
        this.expanseDtls.add(expenseDtl);
        expenseDtl.setExpense(this);
        return this;
    }

    public Expense removeExpanseDtl(ExpenseDtl expenseDtl) {
        this.expanseDtls.remove(expenseDtl);
        expenseDtl.setExpense(null);
        return this;
    }

    public void setExpanseDtls(Set<ExpenseDtl> expenseDtls) {
        this.expanseDtls = expenseDtls;
    }

    public PayTo getPayTo() {
        return payTo;
    }

    public Expense payTo(PayTo payTo) {
        this.payTo = payTo;
        return this;
    }

    public void setPayTo(PayTo payTo) {
        this.payTo = payTo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return id != null && id.equals(((Expense) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", loginId='" + getLoginId() + "'" +
            ", voucherNo=" + getVoucherNo() +
            ", voucherDate='" + getVoucherDate() + "'" +
            ", month='" + getMonth() + "'" +
            ", notes='" + getNotes() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", isPosted='" + isIsPosted() + "'" +
            ", postDate='" + getPostDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            "}";
    }
}
