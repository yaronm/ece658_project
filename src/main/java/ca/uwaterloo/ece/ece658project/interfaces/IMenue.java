package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;
import java.util.Map;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.interfaces.User;

public interface IMenue {

	public void addItem(String item, PotluckEntity potluck);

	public void commitToItem(String item, String email, PotluckEntity potluck);

	public Collection<String> getItems(PotluckEntity potluck);

	public Map<String, User> getCommitments(PotluckEntity potluck);

}