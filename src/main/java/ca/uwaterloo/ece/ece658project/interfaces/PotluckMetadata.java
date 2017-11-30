package ca.uwaterloo.ece.ece658project.interfaces;

public class PotluckMetadata {

	String name;
	User owner;
	String description;

	public PotluckMetadata(String name, User owner, String description) {
		this.name = name;
		this.owner = owner;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public User getOwner() {
		return owner;
	}

	public String getDescription() {
		return description;
	}
	
}
