package models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.annotation.constraint.CsvRequire;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotBlank;

@CsvBean(header = true) // Mark class as CSV bean with header
public class CsvUser {

	@CsvColumn(number = 1, label = "Name")
	@CsvRequire // Required field
	@NotBlank // Cannot be empty string
	private String name;

	@CsvColumn(number = 2, label = "Email")
	@Email // Validate email format
	private String email;

	@CsvColumn(number = 3, label = "Age")
    @NotNull(message = "Age is required.") // メッセージを追加
    @Min(value = 0, message = "Age must be a non-negative number.") // メッセージを追加
	private Integer age;

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

	// Getters and setters omitted
}
