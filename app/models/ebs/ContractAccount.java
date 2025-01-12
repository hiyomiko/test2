package models.ebs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.smart.Merchant;

@Entity
@Table(name = "CONTRACT_ACCOUNT", schema = "ebsora")
public class ContractAccount extends play.db.jpa.GenericModel {

    @EmbeddedId
    public ContractAccountId id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", referencedColumnName = "merchant_no")
    public Merchant merchant;

    @Column(name = "account_number")
    public int accountNumber;
    
    @Column(name = "apply_start_date")
    @Temporal(TemporalType.DATE)
    public Date applyStartDate;

    @Column(name = "apply_end_date")
    @Temporal(TemporalType.DATE)
    public Date applyEndDate;

    public ContractAccount() {
    }

    public ContractAccount(ContractAccountId id, Merchant merchant, int accountNumber, Date applyStartDate, Date applyEndDate) {
        this.id = id;
        this.merchant = merchant;
        this.accountNumber = accountNumber;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
    }

}