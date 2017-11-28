package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
@SuppressWarnings("serial")
public class User implements Serializable {

	@NotNull
	private String name;

	@Id
	@NotNull
	private String email;

	public User() {
	}

	public User(String name, String email) {
		setName(name);
		setEmail(email);
	}

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

}
