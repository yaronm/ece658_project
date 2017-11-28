package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

	@OneToMany(cascade = CascadeType.ALL)
	@NotNull
	private Collection<Potluck> potlucks;

	public User() {
	}

	public User(String name, String email) {
		setName(name);
		setEmail(email);
		setPotlucks(new LinkedList<Potluck>());
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

	public Collection<Potluck> getPotlucks() {
		return potlucks;
	}

	public void setPotlucks(Collection<Potluck> potlucks) {
		this.potlucks = potlucks;
	}

}
