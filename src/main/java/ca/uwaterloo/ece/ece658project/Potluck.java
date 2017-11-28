package ca.uwaterloo.ece.ece658project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Potluck {

	@Id
	@GeneratedValue
	@NotNull
	private Long id;

	@ManyToOne
	@NotNull
	private User owner;

	private String description;

	public Potluck() {
	}
	
	public Potluck(User owner) {
		setOwner(owner);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
