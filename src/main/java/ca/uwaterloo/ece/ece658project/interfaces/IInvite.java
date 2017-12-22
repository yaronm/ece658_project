package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;
import java.util.Map;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.UserEntity;
import ca.uwaterloo.ece.ece658project.interfaces.User;

public interface IInvite {

	public void invite(String email, PotluckEntity potluck);

	public void acceptInvitation(String email, PotluckEntity potluck);

	public void rejectInvitation(String email, PotluckEntity potluck);

	public Collection<User> getAttending(PotluckEntity potluck);

	public Collection<User> getInvited(PotluckEntity potluck);

	public Map<String, UserEntity> getUsers(PotluckEntity potluck);

}