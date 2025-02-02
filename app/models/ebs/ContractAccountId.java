package models.ebs;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ContractAccountId implements Serializable {

    @Column(name = "contract_no")
    public String contractNo;

    @Column(name = "account_type")
    public int accountType;

    public ContractAccountId() {
    }

    public ContractAccountId(String contractNo, int accountType) {
        this.contractNo = contractNo;
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractAccountId that = (ContractAccountId) o;
        return accountType == that.accountType && Objects.equals(contractNo, that.contractNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractNo, accountType);
    }
}