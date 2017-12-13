package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.PollEntity;
import ca.uwaterloo.ece.ece658project.entity.PollOptionEntity;
import ca.uwaterloo.ece.ece658project.interfaces.PollOptionInterface;
import ca.uwaterloo.ece.ece658project.OptionManagerBean;

@Stateful
public class OptionManagerBean implements PollOptionInterface {

	@PersistenceContext
	private EntityManager entityManager;

	private PollOptionEntity option;
	
	
	@EJB
 	private NotificationSystemBean emailBean;

	@Override
	public Long newOption(String optionDescription, Long pollId) {
		option = new PollOptionEntity();
		option.setOptionDescription(optionDescription);
		option.setPollId(pollId);

		entityManager.persist(option);
		
		return option.getId();
		
	}
	
	@Override
	public Long Duplicate(Long new_poll_id) {
		Long new_option_id = newOption(option.getOptionDescription(), new_poll_id);
		return new_option_id; 
	}
	
	@Override
	public void selectOption(Long id) {
		option = entityManager.find(PollOptionEntity.class, id);
	}
	
	@Override
	public void deleteOption(String user_email, Long id) {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public void changeDescription(String user_email, String Description) {
		PollEntity poll = entityManager.find(PollEntity.class, option.getPollId());
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		option.setOptionDescription(Description);
		entityManager.merge(option);
	}
	
	@Override
	public void changePollId(String user_email, Long Id) {
		PollEntity poll = entityManager.find(PollEntity.class, option.getPollId());
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		option.setPollId(Id);
		entityManager.merge(option);
	}
	
	
	@Override
	public Long getPollId() {
		return option.getPollId();
	}


	@Override
	public void removeRespondent(String user_email) {
		Collection<String> respondents = option.getRespondents();
		respondents.remove(user_email);
		option.setRespondents(respondents);
		entityManager.merge(option);
		
	}

	@Override
	public void addRespondent(String user_email) {		
		Collection<String> respondents = option.getRespondents();
		if (respondents == null || respondents.isEmpty())
		{
			Collection<String> to_add = new LinkedList<String>();
			to_add.add(user_email);
			option.setRespondents(to_add);
		}else{
			respondents.add(user_email);
			option.setRespondents(respondents);
		}
		entityManager.merge(option);
	}

	@Override
	public Collection<String> getRespondents() {
		return option.getRespondents();
	}

	@Override
	public String getDescription() {
		return option.getOptionDescription();
	}


	@Override
	public Long getOptionId() {

		return option.getId();
	}
}
