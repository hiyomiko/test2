package models;

import javax.validation.constraints.Min;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.annotation.constraint.CsvEquals;
import com.github.mygreen.supercsv.annotation.constraint.CsvRequire;

import cellprocessor.CsvCustomConstraint;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotBlank;

@CsvBean(header = true) // Mark class as CSV bean with header
public class CsvUser {

	@CsvColumn(number = 1, label = "Name")
	@CsvRequire // Required field
	@NotBlank // Cannot be empty string
	@CsvCustomConstraint(value = 10)
	private String name;

	@CsvColumn(number = 2, label = "Email")
	@Email // Validate email format
	private String email;

	@CsvColumn(number = 3, label = "Age")
	@CsvRequire
    @Min(value = 0, message = "Age must be a non-negative number.") // メッセージを追加
	private Integer age;
	
	@CsvEquals(value = "1")
	@CsvRequire
	@CsvColumn(number = 4, label = "Type")
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// Getters and setters omitted
}
