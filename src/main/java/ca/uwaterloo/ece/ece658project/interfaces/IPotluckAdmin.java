package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckMetadata;

public interface IPotluckAdmin {

	public Long createPotluck(String name, String owner, String description);

	public void changePotluckName(String user_email, String name, PotluckEntity potluck);

	public void changePotluckDescription(String user_email, String description, PotluckEntity potluck);

	public void deletePotluck(String user_email, PotluckEntity potluck);

	public Long duplicatePotluck(String user_email, PotluckEntity potluck);

	public void addRestriction(String restriction, PotluckEntity potluck);

	public PotluckMetadata getMetadata(PotluckEntity potluck);

	public Collection<String> getRestrictions(PotluckEntity potluck);

}