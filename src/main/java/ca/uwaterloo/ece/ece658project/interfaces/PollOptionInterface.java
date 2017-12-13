package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

public interface PollOptionInterface {

	public Long newOption(String optionDescription, Long pollId);
	
	public Long Duplicate(Long new_poll_id);
	
	public void selectOption(Long id);
	
	public void deleteOption(String user_email, Long id);
	
	public void changeDescription(String user_email, String Description);
	
	public void changePollId(String user_email, Long Id);
	
	public void removeRespondent(String user_email);
	
	public void addRespondent(String user_email);
	
	public Collection<String> getRespondents();

	public String getDescription();
	
	public Long getPollId();

	public Long getOptionId();
}
