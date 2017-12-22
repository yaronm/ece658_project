package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;
import java.util.Map;

import ca.uwaterloo.ece.ece658project.exception.LoginException;

public interface AuthenticatedPotluckInterface {

	public Long createPotluck(String name, String owner, String description) throws LoginException;
	
	public void selectPotluck(Long id) throws LoginException;
	
	public void deletePotluck(String user_email) throws LoginException;
	
	public Long duplicatePotluck(String user_email) throws LoginException;
	
	public void changePotluckName(String user_email, String name) throws LoginException;
	
	public void changePotluckDescription(String user_email, String description) throws LoginException;
	
	public void schedule(Event event) throws LoginException;

	public void invite(String email) throws LoginException;
	
	public void acceptInvitation(String email) throws LoginException;

	public void rejectInvitation(String email) throws LoginException;
	
	public void addItem(String item) throws LoginException;
	
	public void commitToItem(String item, String email) throws LoginException;
	
	public void addRestriction(String restriction) throws LoginException;
	
	public PotluckMetadata getMetadata() throws LoginException;

	public Collection<Event> getEvents() throws LoginException;
	
	public Collection<User> getAttending() throws LoginException;

	public Collection<User> getInvited() throws LoginException;
	
	public Collection<String> getItems() throws LoginException;
	
	public Map<String, User> getCommitments() throws LoginException;
	
	public Collection<String> getRestrictions() throws LoginException;

	void removePoll(String user_email) throws LoginException;

	Collection<Long> getPolls() throws LoginException;
	
	void select_poll (Long pollId) throws LoginException;
	
	void select_option (Long optionId) throws LoginException;

	void setPolls(Collection<Long> polls) throws LoginException;

	Long addPoll(String pollName, String pollDescription) throws LoginException;
	
	public void changePollName(String user_email, String Name) throws LoginException;
	
	public void changePollDescription( String user_email, String Description) throws LoginException;
	
	public Collection<Long> getPollOptions() throws LoginException;
	
	public String getPollName() throws LoginException;

	public String getPollDescription() throws LoginException;
	
	public Long addOption(String option_desc) throws LoginException;
	
	public void removeOption(String user_email) throws LoginException;
	
	public void changeOptionDescription(String user_email, String Description) throws LoginException;
	
	public void removeRespondent(String user_email) throws LoginException;
	
	public void addRespondent(String user_email) throws LoginException;
	
	public Collection<String> getRespondents() throws LoginException;

	public String getOptionDescription() throws LoginException;

}
