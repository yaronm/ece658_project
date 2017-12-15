package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.PollEntity;
import ca.uwaterloo.ece.ece658project.entity.PollOptionEntity;
import ca.uwaterloo.ece.ece658project.interfaces.PollOptionInterface;
import ca.uwaterloo.ece.ece658project.OptionManagerBean;

@Stateless
public class OptionManagerBean implements PollOptionInterface {

	@PersistenceContext
	private EntityManager entityManager;

	//private PollOptionEntity option;
	
	
	@EJB
 	private NotificationSystemBean emailBean;

	@Override
	public Long newOption(String optionDescription, Long pollId) {
		PollOptionEntity option;
		option = new PollOptionEntity();
		option.setOptionDescription(optionDescription);
		option.setPollId(pollId);

		entityManager.persist(option);
		
		return option.getId();
		
	}
	
	@Override
	public Long Duplicate(Long option_id, Long new_poll_id) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		Long new_option_id = newOption(option.getOptionDescription(), new_poll_id);
		return new_option_id; 
	}
	
	/*@Override
	public void selectOption(Long id) {
		option = entityManager.find(PollOptionEntity.class, id);
	}*/
	
	@Override
	public void deleteOption(Long id) {
		PollOptionEntity option = entityManager.find(PollOptionEntity.class, id);
		entityManager.remove(option);
		entityManager.flush();
		
	}
	
	
	@Override
	public void changeDescription(Long option_id, String user_email, String Description) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		PollEntity poll = entityManager.find(PollEntity.class, option.getPollId());
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		option.setOptionDescription(Description);
		entityManager.merge(option);
	}
	
	@Override
	public void changePollId(Long option_id, String user_email, Long Id) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		PollEntity poll = entityManager.find(PollEntity.class, option.getPollId());
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, poll.getPotluckId());
		if (!user_email.equals(potluck.getOwnerEmail())){
			return;
		}
		option.setPollId(Id);
		entityManager.merge(option);
	}
	
	
	@Override
	public Long getPollId(Long option_id) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		return option.getPollId();
	}


	@Override
	public void removeRespondent(Long option_id, String user_email) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		Collection<String> respondents = option.getRespondents();
		respondents.remove(user_email);
		option.setRespondents(respondents);
		entityManager.merge(option);
		
	}

	@Override
	public void addRespondent(Long option_id, String user_email) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
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
	public Collection<String> getRespondents(Long option_id) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		return option.getRespondents();
	}

	@Override
	public String getDescription(Long option_id) {
		PollOptionEntity option;
		option = entityManager.find(PollOptionEntity.class, option_id);
		return option.getOptionDescription();
	}


	/*@Override
	public Long getOptionId() {

		return option.getId();
	}*/
}
