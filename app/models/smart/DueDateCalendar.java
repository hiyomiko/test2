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
@Table(name = "DUE_DATE_CALENDAR", schema = "smartora")
public class DueDateCalendar extends play.db.jpa.GenericModel {

    @Id
    @SequenceGenerator(name = "due_date_calendar_seq", sequenceName = "smartora.due_date_calendar_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "due_date_calendar_seq")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "due_date_id", referencedColumnName = "id", insertable = false, updatable = false)
    public DueDateCode dueDateCodeRelation; // フィールド名を変更

    public String dueDateCode;

    @Temporal(TemporalType.DATE)
    public Date dueDateFrom;

    @Temporal(TemporalType.DATE)
    public Date dueDateTo;

    public DueDateCalendar() {
    }

    public DueDateCalendar(DueDateCode dueDateCodeRelation, String dueDateCode, Date dueDateFrom, Date dueDateTo) {
        this.dueDateCodeRelation = dueDateCodeRelation;
        this.dueDateCode = dueDateCode;
        this.dueDateFrom = dueDateFrom;
        this.dueDateTo = dueDateTo;
    }

}