package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

public interface PollInterface {

	public Long createPoll(String pollName, String pollDescription, Long potluckId);
	
	public Long Duplicate(Long destination_potluck_id);
	
	public void selectPoll(Long id);
	
	public void deletePoll(String user_email, Long id);
	
	public Long addOption(String option);
	
	public void changeName(String user_email, String Name);
	
	public void changeDescription(String user_email, String Description);
	
	public void changePotluckId(String user_email, Long Id);
	
	public void removeOption(String user_email, Long option);
	
	public Collection<Long> getOptions();
	
	public String getPollName();

	public String getPollDescription();
	
	public Long getPotluckId();

}
