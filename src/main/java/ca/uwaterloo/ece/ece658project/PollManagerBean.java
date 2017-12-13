package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.PollEntity;
import ca.uwaterloo.ece.ece658project.interfaces.PollInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PollOptionInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.PollManagerBean;

@Stateful
public class PollManagerBean implements PollInterface {

	@PersistenceContext
	private EntityManager entityManager;

	private PotluckEntity potluck;
	
	private PollEntity poll;

	@EJB
	private PollOptionInterface optionManager;
	
	@EJB
 	private NotificationSystemBean emailBean;

	@Override
	public Long createPoll(String pollName, String pollDescription, Long potluckId) {
		poll = new PollEntity();
		poll.setPollName(pollName);
		poll.setPollDescription(pollDescription);
		poll.setPotluckId(potluckId);

		entityManager.persist(poll);
		
		potluck = entityManager.find(PotluckEntity.class, potluckId);
		Long poll_id = poll.getId();
		Collection<Long> polls = potluck.getPolls();
		if (polls == null || polls.isEmpty())
		{
			Collection<Long> to_add = new LinkedList<Long>();
			to_add.add(poll_id);
			potluck.setPolls(to_add);
		}else{
			potluck.getPolls().add(poll_id);
		}

		entityManager.merge(potluck);
		

		return poll_id;
		
	}
	
	@Override
	public Long Duplicate(Long destination_potluck_id) {
		PollEntity old_poll = entityManager.find(PollEntity.class, poll.getId());
		Long new_poll_id = createPoll(poll.getPollName(), poll.getPollDescription(), destination_potluck_id);
		Collection<Long> old_ops = old_poll.getOptions();
		Collection<Long> new_ops = new LinkedList<Long>();
		for (Long op:old_ops) {
			optionManager.selectOption(op);
			new_ops.add(optionManager.Duplicate(new_poll_id));			
		}
		poll.setOptions(new_ops);
		
		return new_poll_id; 
	}
	
	@Override
	public void selectPoll(Long id) {
		poll = entityManager.find(PollEntity.class, id);
	}
	
	@Override
	public void deletePoll(String user_email, Long id) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Long addOption(String option) {
		Long op = optionManager.newOption(option, poll.getId());
		Collection<Long> updated_options = new LinkedList<Long>(poll.getOptions());
		updated_options.add(op);
		poll.setOptions(updated_options);
		entityManager.merge(poll);
		return op;
		//TODO add notification
	}
	
	@Override
	public void changeName(String user_email, String Name) {
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		poll.setPollName(Name);
		entityManager.merge(poll);
	}
	
	@Override
	public void changeDescription(String user_email, String Description) {
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		poll.setPollDescription(Description);
		entityManager.merge(poll);
	}
	
	@Override
	public void changePotluckId(String user_email, Long Id) {
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		poll.setPotluckId(Id);
		entityManager.merge(poll);
	}
	
	@Override
	public void removeOption(String user_email, Long option) {
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		optionManager.deleteOption(user_email, option);
		Collection<Long> updated_options = new LinkedList<Long>(poll.getOptions());
		updated_options.remove(option);
		poll.setOptions(updated_options);
		entityManager.merge(poll);
		//@TODO Going to need to remove from table storing responses
	}
	
	@Override
	public Collection<Long> getOptions(){
		return poll.getOptions();
	}
	
	@Override
	public String getPollName() {
		return poll.getPollName();
	}
	
	@Override
	public String getPollDescription() {
		return poll.getPollDescription();
	}
	
	@Override
	public Long getPotluckId() {
		return poll.getPotluckId();
	}
}
