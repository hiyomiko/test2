package models.smart;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.ebs.ContractAccount;

@Entity
@Table(name = "MERCHANT", schema = "smartora")
public class Merchant extends play.db.jpa.GenericModel {

    @Id
    @SequenceGenerator(name = "merchant_seq", sequenceName = "smartora.merchant_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_seq")
    public Long id;

    @Column(name = "merchant_no")
    public String merchant_no;

    @Column(name = "merchant_name")
    public String merchant_name;

    @OneToMany(mappedBy = "merchant2", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<ContractAccount> contractAccounts = new ArrayList<>(); // 初期化を追加
    
    @ManyToOne
    @JoinColumn(name = "due_date_id", referencedColumnName = "id", insertable = false, updatable = false)
    public DueDateCode dueDateCodeRelation; // フィールド名を変更

//    public Merchant() {
//    }
//
//    public Merchant(String merchant_no, String merchant_name, DueDateCode dueDateCodeRelation) {
//        this.merchant_no = merchant_no;
//        this.merchant_name = merchant_name;
//        this.dueDateCodeRelation = dueDateCodeRelation;
//    }
}