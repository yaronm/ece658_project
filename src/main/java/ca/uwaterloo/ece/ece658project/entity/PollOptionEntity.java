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
@Table(name = "Options")
@SuppressWarnings("serial")
public class PollOptionEntity implements Serializable {

	@NotNull
	private String optionDescription;

	@NotNull
	private Long pollId;
	
	
	@Id
	@GeneratedValue
	@NotNull
	private Long PollOptionId;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<String> respondents = new LinkedList<>();

	
	public PollOptionEntity() {
	}

	public PollOptionEntity(String optionDescription, Long pollId, Collection<String> respondents) {
		setOptionDescription(optionDescription);
		setPollId(pollId);
		setRespondents(respondents);
	}

	public String getOptionDescription() {
		return optionDescription;
	}

	public void setOptionDescription(String description) {
		this.optionDescription = description;
	}
	
	public Long getPollId() {
		return pollId;
	}

	public void setPollId(Long id) {
		this.pollId = id;
	}
	

	public Collection<String> getRespondents() {
		return respondents;
	}
	
	public void setRespondents(Collection<String> respondents) {
		this.respondents = respondents;
	}
	
	public Long getId() {
		return this.PollOptionId;
	}

}

