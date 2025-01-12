package models.smart;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SALES_REPORT", schema = "smartora")
public class SalesReport extends play.db.jpa.GenericModel {

    @Id
    @SequenceGenerator(name = "sales_report_seq", sequenceName = "smartora.sales_report_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sales_report_seq")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", referencedColumnName = "id", insertable = false, updatable = false)
    public Merchant merchant;

    @Temporal(TemporalType.DATE)
    public Date dueDate;

    public Double amount;

    public SalesReport() {
    }

    public SalesReport(Merchant merchant, Date dueDate, Double amount) {
        this.merchant = merchant;
        this.dueDate = dueDate;
        this.amount = amount;
    }

    
}