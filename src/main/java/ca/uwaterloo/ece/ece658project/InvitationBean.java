package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.UserEntity;
import ca.uwaterloo.ece.ece658project.interfaces.IInvite;
import ca.uwaterloo.ece.ece658project.interfaces.INotify;
import ca.uwaterloo.ece.ece658project.interfaces.User;


@Stateless
public class InvitationBean implements IInvite {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private UserManagerBean userManager;
	
	
	@Inject
 	protected INotify emailBean;



	/* (non-Javadoc)
	 * @see ca.uwaterloo.ece.ece658project.IInvite#invite(java.lang.String, ca.uwaterloo.ece.ece658project.entity.PotluckEntity)
	 */
	@Override
	public void invite(String email, PotluckEntity potluck) {
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

	/* (non-Javadoc)
	 * @see ca.uwaterloo.ece.ece658project.IInvite#acceptInvitation(java.lang.String, ca.uwaterloo.ece.ece658project.entity.PotluckEntity)
	 */
	@Override
	public void acceptInvitation(String email, PotluckEntity potluck) {
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

	/* (non-Javadoc)
	 * @see ca.uwaterloo.ece.ece658project.IInvite#rejectInvitation(java.lang.String, ca.uwaterloo.ece.ece658project.entity.PotluckEntity)
	 */
	@Override
	public void rejectInvitation(String email, PotluckEntity potluck) {
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



	/* (non-Javadoc)
	 * @see ca.uwaterloo.ece.ece658project.IInvite#getAttending(ca.uwaterloo.ece.ece658project.entity.PotluckEntity)
	 */
	@Override
	public Collection<User> getAttending(PotluckEntity potluck) {
		Collection<User> attending = new LinkedList<>();
		for (String email : potluck.getAttendingEmails()) {
			User user = userManager.getUser(email);
			attending.add(user);
		}
		return Collections.unmodifiableCollection(attending);

	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.ece.ece658project.IInvite#getInvited(ca.uwaterloo.ece.ece658project.entity.PotluckEntity)
	 */
	@Override
	public Collection<User> getInvited(PotluckEntity potluck) {
		Collection<User> invited = new LinkedList<>();
		for (String email : potluck.getInvitedEmails()) {
			User user = userManager.getUser(email);
			invited.add(user);
		}
		return Collections.unmodifiableCollection(invited);
	}

	
	/* (non-Javadoc)
	 * @see ca.uwaterloo.ece.ece658project.IInvite#getUsers(ca.uwaterloo.ece.ece658project.entity.PotluckEntity)
	 */
	@Override
	public Map<String, UserEntity> getUsers(PotluckEntity potluck){
		return potluck.getUsers();
	}
	

	

}
