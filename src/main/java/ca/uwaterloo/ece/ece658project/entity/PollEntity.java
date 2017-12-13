package ca.uwaterloo.ece.ece658project.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "POLLS")
@SuppressWarnings("serial")
public class PollEntity implements Serializable {

	@NotNull
	private String pollName;

	@NotNull
	private String pollDescription;

	@NotNull
	private Long potluckId;
	
	
	@Id
	@GeneratedValue
	@NotNull
	private Long pollId;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<Long> options = new LinkedList<>();

	
	public PollEntity() {
	}

	public PollEntity(String pollName, String pollDescription, Long potluckId, Collection<Long>options) {
		setPollName(pollName);
		setPollDescription(pollDescription);
		setPotluckId(potluckId);
		setOptions(options);
	}

	public String getPollName() {
		return pollName;
	}

	public void setPollName(String name) {
		this.pollName = name;
	}

	public String getPollDescription() {
		return pollDescription;
	}

	public void setPollDescription(String description) {
		this.pollDescription = description;
	}
	
	public Long getPotluckId() {
		return potluckId;
	}

	public void setPotluckId(Long id) {
		this.potluckId = id;
	}
	

	public Collection<Long> getOptions() {
		return options;
	}
	
	public void setOptions(Collection<Long> options) {
		this.options = options;
	}
	
	public Long getId() {
		return this.pollId;
	}

}

