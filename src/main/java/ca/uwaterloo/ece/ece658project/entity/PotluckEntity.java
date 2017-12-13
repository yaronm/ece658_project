package ca.uwaterloo.ece.ece658project.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
@SuppressWarnings("serial")
public class PotluckEntity implements Serializable {

	@Id
	@GeneratedValue
	@NotNull
	private Long id;

	private String name;
	
	private String description;

	@ManyToMany
    private Map<String, UserEntity> users = new HashMap<>();

	private String ownerEmail;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> attendingEmails = new HashSet<>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> invitedEmails = new HashSet<>();
	
	@ElementCollection(fetch=FetchType.EAGER)
    private Map<String, String> eventDescriptions = new HashMap<>();

	@ElementCollection(fetch=FetchType.EAGER)
    private Map<String, Date> eventTimes = new HashMap<>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<String> necessaryItems = new LinkedList<>();

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> commitedItems = new HashMap<>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<String> restrictions = new LinkedList<>();

	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<Long> polls= new LinkedList<>();

	
	public PotluckEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Map<String, UserEntity> users) {
		this.users = users;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public Collection<String> getAttendingEmails() {
		return attendingEmails;
	}

	public void setAttendingEmails(Set<String> attendingEmails) {
		this.attendingEmails = attendingEmails;
	}
	
	public Collection<Long> getPolls() {
		return polls;
	}

	public void setPolls(Collection<Long> polls) {
		this.polls = polls;
	}

	public Set<String> getInvitedEmails() {
		return invitedEmails;
	}

	public void setInvitedEmails(Set<String> invitedEmails) {
		this.invitedEmails = invitedEmails;
	}

	public Map<String, Date> getEventTimes() {
		return eventTimes;
	}

	public void setEventTimes(Map<String, Date> eventTimes) {
		this.eventTimes = eventTimes;
	}

	public Map<String, String> getEventDescriptions() {
		return eventDescriptions;
	}

	public void setEventDescriptions(Map<String, String> eventDescriptions) {
		this.eventDescriptions = eventDescriptions;
	}

	public Collection<String> getNecessaryItems() {
		return necessaryItems;
	}

	public void setNecessaryItems(Collection<String> necessaryItems) {
		this.necessaryItems = necessaryItems;
	}

	public Map<String, String> getCommitedItems() {
		return commitedItems;
	}

	public void setCommitedItems(Map<String, String> commitedItems) {
		this.commitedItems = commitedItems;
	}

	public Collection<String> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Collection<String> restrictions) {
		this.restrictions = restrictions;
	}

}
