package ca.uwaterloo.ece.ece658project.managed;

import java.util.Collection;
import java.util.Map;

import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.User;

public interface IPotluck {

	Long getPotluck();

	void duplicate() throws LoginException;

	void setPotluck(Long id) throws LoginException;

	String getName() throws LoginException;

	String getDescription() throws LoginException;

	Collection<Map<String, String>> getPolls() throws LoginException;

	Collection<Event> getEvents() throws LoginException;

	Collection<User> getAttending() throws LoginException;

	Collection<User> getInvited() throws LoginException;

	Collection<String> getItems() throws LoginException;

	Map<String, User> getCommitments() throws LoginException;

	Collection<User> getUncommitted() throws LoginException;

	Collection<String> getRestrictions() throws LoginException;

	boolean userIsInvited() throws LoginException;

	String acceptInvitation() throws LoginException;

	String getInviteEmail();

	void setInviteEmail(String inviteEmail);

	String invite() throws LoginException;

	String getNewEventTitle();

	void setNewEventTitle(String newEventTitle);

	String getNewEventDate();

	void setNewEventDate(String newEventDate);

	String getNewEventDescription();

	void setNewEventDescription(String newEventDescription);

	String schedule() throws NumberFormatException, LoginException;

	String getNewItem();

	void setNewItem(String newItem);

	String addItem() throws LoginException;

	String commit(String item) throws LoginException;

	String getNewRestriction();

	void setNewRestriction(String newRestriction);

	String addRestriction() throws LoginException;

	void setNewName(String newName);

	String getNewName();

	void changeName() throws LoginException;

	void setNewDescription(String newDescription);

	String getnewDescription();

	void changeDescription() throws LoginException;

}