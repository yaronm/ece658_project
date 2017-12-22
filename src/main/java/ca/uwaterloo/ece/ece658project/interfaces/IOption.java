package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

public interface IOption {

	public Long newOption(String optionDescription, Long pollId);
	
	public Long Duplicate(Long option_id, Long new_poll_id);
	
	//public void selectOption(Long id);
	
	public void deleteOption(Long id);
	
	public void changeDescription(Long option_id, String user_email, String Description);
	
	public void changePollId(Long option_id, String user_email, Long Id);
	
	public void removeRespondent(Long option_id, String user_email);
	
	public void addRespondent(Long option_id, String user_email);
	
	public Collection<String> getRespondents(Long option_id);

	public String getDescription(Long option_id);
	
	public Long getPollId(Long option_id);

}
