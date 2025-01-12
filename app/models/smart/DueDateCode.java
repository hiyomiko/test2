package models.smart;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DUE_DATE_CODE", schema = "smartora")
public class DueDateCode extends play.db.jpa.GenericModel {

    @Id
    @SequenceGenerator(name = "due_date_code_seq", sequenceName = "smartora.due_date_code_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "due_date_code_seq")
    public Long id;

    public String code;

    public String name;

    public DueDateCode() {
    }

    public DueDateCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // getter, setter (省略)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}