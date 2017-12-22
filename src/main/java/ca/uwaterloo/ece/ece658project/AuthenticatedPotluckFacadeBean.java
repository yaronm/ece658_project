package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.AuthenticatedPotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckMetadata;
import ca.uwaterloo.ece.ece658project.interfaces.User;


@Stateless
public class AuthenticatedPotluckFacadeBean implements AuthenticatedPotluckInterface {

	@PersistenceContext
	private EntityManager entityManager;
	
	@EJB
	private PotluckInterface facade;
	
	public AuthenticatedPotluckFacadeBean() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}


	@Override
	public Long createPotluck(String name, String owner, String description) throws LoginException{
		if (SecurityUtils.getSubject().isAuthenticated() && SecurityUtils.getSubject().getPrincipal().toString().equals(owner)) {
			return facade.createPotluck(name, owner, description);
		}
		throw new LoginException("Invalid user");
	}
	
	@Override
	public void changePotluckName(String user_email, String name) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.changePotluckName(user_email, name);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public void changePotluckDescription(String user_email, String description) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.changePotluckDescription(user_email, description);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}
	
	@Override
	public void selectPotluck(Long id) throws LoginException {
		PotluckEntity potluck = entityManager.find(PotluckEntity.class, id);
		if (SecurityUtils.getSubject().isAuthenticated()&&potluck.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.selectPotluck(id);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void deletePotluck(String user_email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.deletePotluck(user_email);
		}else {
			throw new LoginException("Invalid user");
		}
	}
	
	@Override
	public Long duplicatePotluck(String user_email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			return facade.duplicatePotluck(user_email);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void schedule(Event event) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.schedule(event);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void invite(String email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.invite(email);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void acceptInvitation(String email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&
				facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())
				&& email.equals(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.acceptInvitation(email);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void rejectInvitation(String email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&
				facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())
				&& email.equals(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.rejectInvitation(email);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void addItem(String item) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.addItem(item);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void commitToItem(String item, String email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())&& email.equals(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.commitToItem(item, email);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void addRestriction(String restriction) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.addRestriction(restriction);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public PotluckMetadata getMetadata() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getMetadata();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public Collection<Event> getEvents() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getEvents();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public Collection<User> getAttending() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getAttending();
		}else {
			throw new LoginException("Invalid user");
		}

	}

	@Override
	public Collection<User> getInvited() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getInvited();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public Collection<String> getItems() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getItems();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public Map<String, User> getCommitments() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getCommitments();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public Collection<String> getRestrictions() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getRestrictions();
		}else {
			throw new LoginException("Invalid user");
		}
	}
	
	@Override
	public Collection<Long> getPolls() throws LoginException{
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getPolls();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void setPolls(Collection<Long> polls) throws LoginException{
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.setPolls(polls);
		}else {
			throw new LoginException("Invalid user");
		}
	}
	
	@Override
	public Long addPoll(String pollName, String pollDescription) throws LoginException{
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.addPoll(pollName, pollDescription);
		}else {
			throw new LoginException("Invalid user");
		}
	}
	
	@Override
	public void removePoll(String user_email) throws LoginException{
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.removePoll(user_email);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void changePollName(String user_email, String Name) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.changePollName(user_email, Name);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public void changePollDescription(String user_email, String Description) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.changePollDescription(user_email, Description);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public Collection<Long> getPollOptions() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getPollOptions();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public String getPollName() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getPollName();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public String getPollDescription() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getPollDescription();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public Long addOption(String option_desc) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.addOption(option_desc);
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void removeOption(String user_email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.removeOption(user_email);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public void changeOptionDescription(String user_email, String Description) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&SecurityUtils.getSubject().getPrincipal().toString().equals(facade.getMetadata().getOwner().getEmail())) {
			facade.changeOptionDescription(user_email, Description);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public void removeRespondent(String user_email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&facade.getRespondents().contains(SecurityUtils.getSubject().getPrincipal().toString())&&
				user_email.equals(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.removeRespondent(user_email);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public void addRespondent(String user_email) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&&
				user_email.equals(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.addRespondent(user_email);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public Collection<String> getRespondents() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getRespondents();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public String getOptionDescription() throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			return facade.getOptionDescription();
		}else {
			throw new LoginException("Invalid user");
		}
	}

	@Override
	public void select_poll(Long pollId) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.select_poll(pollId);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

	@Override
	public void select_option(Long optionId) throws LoginException {
		if (SecurityUtils.getSubject().isAuthenticated()&& facade.getUsers().containsKey(SecurityUtils.getSubject().getPrincipal().toString())) {
			facade.select_option(optionId);
		}else {
			throw new LoginException("Invalid user");
		}
		
	}

}
