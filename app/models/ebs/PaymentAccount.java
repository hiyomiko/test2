package models.ebs;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "payment_account", schema = "ebsora") // スキーマを指定
public class PaymentAccount {
    @EmbeddedId
	public PaymentAccountKey id;

    @Column(name = "merchant_number")
	public String merchantNumber;

    // 他のフィールド

    @OneToMany(mappedBy = "paymentAccount") // PaymentAccountDetail 側の paymentAccount フィールドで結合
	public List<PaymentAccountDetail> paymentAccountDetails;
}
