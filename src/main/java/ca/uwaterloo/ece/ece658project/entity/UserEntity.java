package ca.uwaterloo.ece.ece658project.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
@SuppressWarnings("serial")
public class UserEntity implements Serializable {

	@NotNull
	private String name;

	@Id
	@NotNull
	private String email;

	@ManyToMany(mappedBy = "users")
	@NotNull
	private Collection<PotluckEntity> potlucks = new LinkedList<>();
	
	public UserEntity() {
	}

	public UserEntity(String name, String email) {
		setName(name);
		setEmail(email);
	}

	public Collection<PotluckEntity> getOwned() {
		Collection<PotluckEntity> owned = new LinkedList<>();
		for (PotluckEntity potluck : getPotlucks()) {
			if (potluck.getOwnerEmail() == getEmail()) {
				owned.add(potluck);
			}
		}
		return Collections.unmodifiableCollection(owned);
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

	public Collection<PotluckEntity> getPotlucks() {
		return potlucks;
	}

	public void setPotlucks(Collection<PotluckEntity> potlucks) {
		this.potlucks = potlucks;
	}

}
