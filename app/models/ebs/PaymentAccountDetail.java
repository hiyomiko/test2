package models.ebs;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "payment_account_detail", schema = "ebsora") // スキーマを指定
public class PaymentAccountDetail {
    @EmbeddedId
	public PaymentAccountDetailKey id;

    @ManyToOne
    @JoinColumns({ // 結合カラムを指定
        @JoinColumn(name = "contract_number", referencedColumnName = "contract_number", insertable = false, updatable = false),
        @JoinColumn(name = "account_type", referencedColumnName = "account_type", insertable = false, updatable = false),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date", insertable = false, updatable = false)
    })
    private PaymentAccount paymentAccount;

    // 他のフィールド
}
