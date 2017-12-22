package ca.uwaterloo.ece.ece658project.managed;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import ca.uwaterloo.ece.ece658project.exception.LoginException;

public interface IPotluckListing {

	void initialize();

	Map<Long, String> getPotluckNames();

	Collection<Long> getPotlucks();

	void setPotluckNames(Map<Long, String> potluckNames);

	Map<Long, String> getPotluckDescriptions();

	void setPotluckDescriptions(Map<Long, String> potluckDescriptions);

	String view(Long potluck) throws LoginException;

	String edit(Long potluck) throws LoginException;

	String getNewName();

	void setNewName(String newName);

	String getNewDescription();

	void setNewDescription(String newDescription);

	String create() throws LoginException;

}