package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.UserEntity;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PollInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckMetadata;
import ca.uwaterloo.ece.ece658project.interfaces.User;
import ca.uwaterloo.ece.ece658project.entity.PollEntity;


@Stateful
public class PotluckFacadeBean implements PotluckInterface {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private UserManagerBean userManager;
	
	@Inject
	protected PollInterface pollManager;

	private PotluckEntity potluck;
	
	private Long poll;
	
	private Long option;

	@EJB
 	private NotificationSystemBean emailBean;

	@Override
	public Long createPotluck(String name, String owner, String description) {
		potluck = new PotluckEntity();
		potluck.setName(name);
		potluck.setDescription(description);
		potluck.setOwnerEmail(owner);

		UserEntity user = entityManager.find(UserEntity.class, owner);
		System.out.println("User: " + user);
		potluck.getUsers().put(owner, user);
		user.getPotlucks().add(potluck);
		entityManager.persist(potluck);
		return potluck.getId();
	}
	
	@Override
	public void changePotluckName(String user_email, String name) {
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		potluck.setName(name);
		entityManager.merge(potluck);
	}

	@Override
	public void changePotluckDescription(String user_email, String description) {
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		potluck.setDescription(description);
		entityManager.merge(potluck);
	}
	
	@Override
	public void selectPotluck(Long id) {
		potluck = entityManager.find(PotluckEntity.class, id);
	}

	@Override
	public void deletePotluck(Long id) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void duplicatePotluck(String user_email) {
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		PotluckEntity old_potluck = entityManager.find(PotluckEntity.class, potluck.getId());
		Long new_potluck_id = createPotluck(potluck.getName(), potluck.getOwnerEmail(), potluck.getDescription());

		
		//add event_times && descriptions
		 Map<String, String> descriptions = old_potluck.getEventDescriptions();
		 Map<String, Date> times = old_potluck.getEventTimes();
		 for (String tit : descriptions.keySet()) {
			 Event event = new Event(tit, times.get(tit), descriptions.get(tit));
			 this.schedule(event);
		 }
		
		//add restrictions
		Collection<String> rest = old_potluck.getRestrictions();
		for(String r : rest) {
			this.addRestriction(r);
		}
		
		
		//add necessary items
		Collection<String> items_to_add = old_potluck.getNecessaryItems();
		for (String it: items_to_add) {
			this.addItem(it);
		}
		
		//add polls
		Collection<Long> polls_to_add = old_potluck.getPolls();
		for (Long it: polls_to_add) {
			//pollManager.selectPoll(it);
			pollManager.Duplicate(it, new_potluck_id);
		}
		
		//invite users
		Map<String, UserEntity> to_invite = old_potluck.getUsers();
		for (String inv : to_invite.keySet()) {
			this.invite(inv);
		}
		this.selectPotluck(old_potluck.getId());
		
	}

	@Override
	public void schedule(Event event) {
		potluck.getEventTimes().put(event.getTitle(), event.getDate());
		potluck.getEventDescriptions().put(event.getTitle(), event.getDescription());
		entityManager.merge(potluck);
		for (UserEntity user : potluck.getUsers().values()){
			String message = "Hello " + user.getName() +"\n\nYour potluck "+potluck.getName()+" has a new time and description."+
				" The new description is: \n"+event.getTitle()+": "+ event.getDescription()+
				"\nAnd the new time is: \n" + event.getDate()+"\n\nWarm regards, \nThe PotLucky Team";
			emailBean.sendEmail(user.getEmail(), "Potluck time and description added", message);
		}
	}

	@Override
	public void invite(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (!user.getPotlucks().contains(potluck)) {
			user.getPotlucks().add(potluck);
		}
		potluck.getUsers().put(user.getEmail(), user);
		potluck.getAttendingEmails().remove(user.getEmail());
		potluck.getInvitedEmails().add(user.getEmail());
		entityManager.merge(potluck);
		String message = "Hello " + user.getName() +"\n\nYou have been invited to a new potluck!\n"+
			"The potluck was created by " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			"\nPlease log in to your account to view the potluck details.\n\nWarm regards, \nThe PotLucky Team";
		emailBean.sendEmail(email, "Potluck invitation", message);
	}

	@Override
	public void acceptInvitation(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (!user.getPotlucks().contains(potluck)) {
			user.getPotlucks().add(potluck);
		}
		potluck.getUsers().put(user.getEmail(), user);
		potluck.getAttendingEmails().add(user.getEmail());
		potluck.getInvitedEmails().remove(user.getEmail());
		entityManager.merge(potluck);
		
		String message = "Hello " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			"\n\n" + user.getName()+" is now attending your potluck "+potluck.getName()+"!\n\n"+
			"Warm regards, \nThe PotLucky Team";
		emailBean.sendEmail(potluck.getOwnerEmail(), "Potluck invitation accepted", message);
	}

	@Override
	public void rejectInvitation(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (potluck.getOwnerEmail() != user.getEmail()) {
			user.getPotlucks().remove(potluck);
			potluck.getUsers().remove(user.getEmail());
		}
		potluck.getAttendingEmails().remove(user.getEmail());
		potluck.getInvitedEmails().remove(user.getEmail());
		entityManager.merge(potluck);

		String message = "Hello " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			",\n\n" + user.getName()+" will not be attending your potluck "+potluck.getName()+".\n\n"+
			"Warm regards, \nThe PotLucky Team";
		emailBean.sendEmail(potluck.getOwnerEmail(), "Potluck invitation rejected", message);
	}

	@Override
	public void addItem(String item) {
		potluck.getNecessaryItems().add(item);
		entityManager.merge(potluck);
	}

	@Override
	public void commitToItem(String item, String email) {
		potluck.getCommitedItems().put(item, email);
		entityManager.merge(potluck);
		
		String message = "Hello " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			",\n\n" + entityManager.find(UserEntity.class, email).getName()+
			" will bring "+ item + " to your potluck "+potluck.getName()+"!\n\n"+
			"Warm regards, \nThe PotLucky Team";
		emailBean.sendEmail(potluck.getOwnerEmail(), "Potluck item committed", message);
	}

	@Override
	public void addRestriction(String restriction) {
		potluck.getRestrictions().add(restriction);
		entityManager.merge(potluck);
		for (UserEntity user : potluck.getUsers().values()){
			String message = "Hello " + user.getName() +",\n\nYour potluck "+potluck.getName()+" has a new dietary restriction.\n"+
				"The new restriction is: " + restriction + "\n\nWarm regards, \nThe PotLucky Team";
			emailBean.sendEmail(user.getEmail(), "Potluck dietary restriction added", message);
		}
	}

	@Override
	public PotluckMetadata getMetadata() {
		User owner = userManager.getUser(potluck.getOwnerEmail());
		return new PotluckMetadata(potluck.getName(), owner, potluck.getDescription());
	}

	@Override
	public Collection<Event> getEvents() {
		Collection<Event> events = new LinkedList<>();
		for (String title : potluck.getEventTimes().keySet()) {
			events.add(new Event(title, potluck.getEventTimes().get(title), potluck.getEventDescriptions().get(title)));
		}
		return Collections.unmodifiableCollection(events);
	}

	@Override
	public Collection<User> getAttending() {
		Collection<User> attending = new LinkedList<>();
		for (String email : potluck.getAttendingEmails()) {
			User user = userManager.getUser(email);
			attending.add(user);
		}
		return Collections.unmodifiableCollection(attending);

	}

	@Override
	public Collection<User> getInvited() {
		Collection<User> invited = new LinkedList<>();
		for (String email : potluck.getInvitedEmails()) {
			User user = userManager.getUser(email);
			invited.add(user);
		}
		return Collections.unmodifiableCollection(invited);
	}

	@Override
	public Collection<String> getItems() {
		return potluck.getNecessaryItems();
	}

	@Override
	public Map<String, User> getCommitments() {
		Map<String, User> commitments = new HashMap<>();
		for (Map.Entry<String, String> entry : potluck.getCommitedItems().entrySet()) {
			User user = userManager.getUser(entry.getValue());
			commitments.put(entry.getKey(), user);
		}
		return commitments;
	}

	@Override
	public Collection<String> getRestrictions() {
		Collection<String> restrictions = potluck.getRestrictions();
		return restrictions;
	}
	
	@Override
	public Collection<Long> getPolls(){
		Collection<Long> polls = potluck.getPolls();
		return polls;
	}

	@Override
	public void setPolls(Collection<Long> polls){
		potluck.setPolls(polls);
		entityManager.merge(potluck);
	}
	
	@Override
	public Long addPoll(String pollName, String pollDescription){
		poll = pollManager.createPoll(pollName, pollDescription, potluck.getId());
		potluck.getPolls().add(poll);
		
		entityManager.merge(potluck);
		return poll;
	}
	
	@Override
	public void removePoll(){
		Collection<Long> polls = potluck.getPolls();
		polls.remove(poll);
		potluck.setPolls(polls);
		entityManager.merge(potluck);
	}

	@Override
	public void changePollName(String user_email, String Name) {
		pollManager.changeName(poll, user_email, Name);
		
	}

	@Override
	public void changePollDescription(String user_email, String Description) {
		pollManager.changeDescription(poll, user_email, Description);
		
	}

	@Override
	public Collection<Long> getPollOptions() {
		return pollManager.getOptions(poll);
	}

	@Override
	public String getPollName() {
		return pollManager.getPollName(poll);
	}

	@Override
	public String getPollDescription() {
		return pollManager.getPollDescription(poll);
	}

	@Override
	public Long addOption(String option_desc) {
		return pollManager.addOption(poll, option_desc);
	}

	@Override
	public void removeOption(String user_email) {
		pollManager.removeOption(poll, user_email, option);
		
	}

	@Override
	public void changeOptionDescription(String user_email, String Description) {
		pollManager.changeOptionDescription(option, user_email, Description);
		
	}

	@Override
	public void removeRespondent(String user_email) {
		pollManager.removeRespondent(option, user_email);
		
	}

	@Override
	public void addRespondent(String user_email) {
		pollManager.addRespondent(option, user_email);
		
	}

	@Override
	public Collection<String> getRespondents() {
		return pollManager.getRespondents(option);
	}

	@Override
	public String getOptionDescription() {
		return pollManager.getOptionDescription(option);
	}

	@Override
	public void select_poll(Long pollId) {
		this.poll = pollId;
		
	}

	@Override
	public void select_option(Long optionId) {
		this.option = optionId;
		
	}
	

	

}
