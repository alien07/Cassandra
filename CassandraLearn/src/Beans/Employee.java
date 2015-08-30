package Beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.anotation.ModelType;
import com.anotation.Property;

@ModelType("employee")
public class Employee implements Serializable {

	/**
	 * 
	 */
	@Property("EMP_ID")
	int id;
	@Property("EMP_CITY")
	String city;
	@Property("EMP_EMAIL")
	String email;
	@Property("EMP_LAST_NAME")
	String lastname;
	@Property("EMP_NAME")
	String name;
	@Property("EMP_PHONE")
	String phone;
	@Property("EMP_SALARY")
	BigDecimal salary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

}
