package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;
import java.util.Map;

public interface PotluckInterface {

	public Long createPotluck(String name, String owner, String description);
	
	public void selectPotluck(Long id);
	
	public void deletePotluck(String user_email);
	
	public void duplicatePotluck(String user_email);
	
	public void changePotluckName(String user_email, String name);
	
	public void changePotluckDescription(String user_email, String description);
	
	public void schedule(Event event);

	public void invite(String email);
	
	public void acceptInvitation(String email);

	public void rejectInvitation(String email);
	
	public void addItem(String item);
	
	public void commitToItem(String item, String email);
	
	public void addRestriction(String restriction);
	
	public PotluckMetadata getMetadata();

	public Collection<Event> getEvents();
	
	public Collection<User> getAttending();

	public Collection<User> getInvited();
	
	public Collection<String> getItems();
	
	public Map<String, User> getCommitments();
	
	public Collection<String> getRestrictions();

	void removePoll(String user_email);

	Collection<Long> getPolls();
	
	void select_poll (Long pollId);
	
	void select_option (Long optionId);

	void setPolls(Collection<Long> polls);

	Long addPoll(String pollName, String pollDescription);
	
	public void changePollName(String user_email, String Name);
	
	public void changePollDescription( String user_email, String Description);
	
	public Collection<Long> getPollOptions();
	
	public String getPollName();

	public String getPollDescription();
	
	public Long addOption(String option_desc);
	
	public void removeOption(String user_email);
	
	public void changeOptionDescription(String user_email, String Description);
	
	public void removeRespondent(String user_email);
	
	public void addRespondent(String user_email);
	
	public Collection<String> getRespondents();

	public String getOptionDescription();

}
