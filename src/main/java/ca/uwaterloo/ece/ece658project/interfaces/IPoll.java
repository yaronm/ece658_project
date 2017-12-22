package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

public interface IPoll {

	public Long createPoll(String pollName, String pollDescription, Long potluckId);
	
	public Long Duplicate(Long pollId, Long destination_potluck_id);
	
	//public void selectPoll(Long id);
	
	public void deletePoll(Long id);
	
	public void changeName(Long pollId, String user_email, String Name);
	
	public void changeDescription(Long pollId, String user_email, String Description);
	
	public void changePotluckId(Long pollId, String user_email, Long Id);
	
	public Collection<Long> getOptions(Long pollId);
	
	public String getPollName(Long pollId);

	public String getPollDescription(Long pollId);
	
	public Long getPotluckId(Long pollId);
	
	public Long addOption(Long pollId, String option);
	
	public void removeOption(Long pollId, String user_email, Long option);
	
	public void changeOptionDescription(Long option_id, String user_email, String Description);
	
	public void removeRespondent(Long option_id, String user_email);
	
	public void addRespondent(Long option_id, String user_email);
	
	public Collection<String> getRespondents(Long option_id);

	public String getOptionDescription(Long option_id);


}
