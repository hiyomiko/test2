package models.ebs;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PaymentAccountDetailKey  implements Serializable {
    @Column(name = "contract_number")
	public String contractNumber;

    @Column(name = "account_type")
    public String accountType;

    @Column(name = "start_date")
    public Date startDate;

    @Column(name = "detail_number")
    public int detailNumber;

	@Override
	public int hashCode() {
		return Objects.hash(accountType, contractNumber, detailNumber, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentAccountDetailKey other = (PaymentAccountDetailKey) obj;
		return Objects.equals(accountType, other.accountType) && Objects.equals(contractNumber, other.contractNumber)
				&& detailNumber == other.detailNumber && Objects.equals(startDate, other.startDate);
	}
}