package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;
import java.util.Map;

public interface PotluckInterface {

	public Long createPotluck(String name, String owner, String description);
	
	public void selectPotluck(Long id);
	
	public void deletePotluck(Long id);
	
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

}
