package ca.uwaterloo.ece.ece658project.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ca.uwaterloo.ece.ece658project.UserManagerBean;

@Entity
@Table(name = "USERS")
@SuppressWarnings("serial")
public class UserEntity implements Serializable {
	private static final Logger logger = Logger.getLogger(UserManagerBean.class.getName());
	@NotNull
	private String name;

	@Id
	@NotNull
	private String email;
	
	private String password;
	
	private String salt;

	@ManyToMany(mappedBy = "users")
	@NotNull
	private Collection<PotluckEntity> potlucks = new LinkedList<>();
	
	public UserEntity() {
	}

	public UserEntity(String name, String email, String password, Object salt) {
		setName(name);
		logger.info("set name"+name);
		setEmail(email);
		logger.info("set email"+email);
		setPassword(password);
		logger.info("set password"+password);
		setSalt(salt);
		logger.info("set salt"+salt.toString());
		return;
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
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public void setSalt(Object salt) {
		this.salt = salt.toString();
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
