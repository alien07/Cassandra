package Beans;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
public class Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int emp_id;
	String emp_city;
	String emp_email;
	String emp_last_name;
	String emp_name;
	String emp_phone;
	BigInteger emp_salary;

	public Employee() {
		super();
	}

	public Employee(int emp_id, String emp_city, String emp_email,
			String emp_last_name, String emp_name, String emp_phone,
			BigInteger emp_salary) {
		super();
		this.emp_id = emp_id;
		this.emp_city = emp_city;
		this.emp_email = emp_email;
		this.emp_last_name = emp_last_name;
		this.emp_name = emp_name;
		this.emp_phone = emp_phone;
		this.emp_salary = emp_salary;
	}

	public int getEmp_id() {
		return emp_id;
	}

	@XmlElement
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_city() {
		return emp_city;
	}

	@XmlElement
	public void setEmp_city(String emp_city) {
		this.emp_city = emp_city;
	}

	public String getEmp_last_name() {
		return emp_last_name;
	}

	@XmlElement
	public void setEmp_last_name(String emp_last_name) {
		this.emp_last_name = emp_last_name;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public String getEmp_email() {
		return emp_email;
	}

	public void setEmp_email(String emp_email) {
		this.emp_email = emp_email;
	}

	@XmlElement
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getEmp_phone() {
		return emp_phone;
	}

	@XmlElement
	public void setEmp_phone(String emp_phone) {
		this.emp_phone = emp_phone;
	}

	public BigInteger getEmp_salary() {
		return emp_salary;
	}

	@XmlElement
	public void setEmp_salary(BigInteger emp_salary) {
		this.emp_salary = emp_salary;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
