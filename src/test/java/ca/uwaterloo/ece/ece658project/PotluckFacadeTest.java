package ca.uwaterloo.ece.ece658project;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.AddressException;

import org.testng.annotations.Test;

public class PotluckFacadeTest {

	/*test basic potluck creation (should create a potluck
	*with the correct owner and owner email)
	*/
  @Test
  public void PotluckBasicInstantiation() {
	boolean ex = false;
	PotluckFacade p = null;
	try {
		p = new PotluckFacade("yaron", "yaronm93@gmail.com");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == false);
    assert (p.get_owner().equals("yaron"));
    assert (p.get_owner_email().equals("yaronm93@gmail.com"));
  }
  
  /*
   * test to ensure the potluck cannot be created when no owner
   * name is given
   */
  @Test
  public void PotluckNoName() {
	boolean ex = false;
	try {
		PotluckFacade p = new PotluckFacade("", "yaronm93@gmail.com");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == true);
    
  }

  /*
   * test to ensure the potluck cannot be created when no owner
   * email is given
   */
  @Test
  public void PotluckNoEmail() {
	boolean ex = false;
	try {
		PotluckFacade p = new PotluckFacade("Yaron", "");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == true);
    
  }
  
  /*
   * test to ensure the potluck cannot be created with an email
   * that does not conform to the email address specification
   */
  @Test
  public void PotluckInvalidEmail() {
	boolean ex = false;
	try {
		PotluckFacade p = new PotluckFacade("Yaron", "yaron");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == true);
    
  }
  
  /*
   * test to make sure the template creator works when no one has
   * responded in the template
   */
  @Test
  public void from_templateBasic() {
	  boolean ex = false;
	  PotluckFacade p = null;
	  PotluckFacade template = null;
	  Map<String, String> to_invite = null;
	  Map<String, String> locations = null;
	  Map<String, Map<String, Integer>> to_add = null;
	  Map<String, String> new_restrictions = null;
	  Map<String, Date>new_times = null;
	  try {
		p = new PotluckFacade("yaron", "yaronm93@gmail.com");
		template = new PotluckFacade("yaron", "yaronm93@gmail.com");
		to_invite = new HashMap<String, String>();
		to_invite.put("yy", "y@y.c");
		to_invite.put("ym", "ym@y.c");
		template.add_invited(to_invite);
		locations = new HashMap<String, String>();
		locations.put("party", "yaron's house");
		locations.put("delivery", "waterloo");
		template.add_locations(locations);
		
		to_add = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> cat_1 = new HashMap<String, Integer>();
		Map<String, Integer> cat_2 = new HashMap<String, Integer>();
		cat_1.put("cake", 3);
		cat_1.put("fruit", 20);
		cat_2.put("steak", 10);
		cat_2.put("chicken", 8);
		to_add.put("desert", cat_1);
		to_add.put("protein", cat_2);
		template.add_necessary_items(to_add);
		new_restrictions = new HashMap<String, String>();
		new_restrictions.put("nut_allergy", "can't eat tree_nuts");
		new_restrictions.put("kosher", "food must be kosher");
		template.add_restrictions(new_restrictions);
		new_times = new HashMap<String, Date>();
		new_times.put("setup", new Date(3));
		new_times.put("party", new Date(100));
		template.add_times(new_times);
		template.set_description("desc");
		p.from_template(template);
	  }catch(AddressException e) {
		ex = true;
	  }
	  assert(p.get_committed_items().isEmpty());
	  assert(p.get_description().equals("desc"));
	  assert(p.get_going().isEmpty());
	  assert(p.get_invited().equals(to_invite));
	  assert(p.get_locations().equals(locations));
	  assert(p.get_necessary_items().equals(to_add));
	  assert(p.get_not_going().isEmpty());
	  assert(p.get_restrictions().equals(new_restrictions));
	  assert(p.get_times().equals(new_times));
	  assert(p.get_uncommitted().isEmpty());
	  assert(ex == false);
  }
  
  /*test to make sure the template creator works when there are 
   * people in teh going list and not going list, but no one has committed
   * to bringing anything
   */
  @Test
  public void from_templateGoingNotGoingNoCommitted() {
	  boolean ex = false;
	  PotluckFacade p = null;
	  PotluckFacade template = null;
	  Map<String, String> to_invite = null;
	  Map<String, String> locations = null;
	  Map<String, Map<String, Integer>> to_add = null;
	  Map<String, String> new_restrictions = null;
	  Map<String, Date>new_times = null;
	  Set<String> going = new HashSet<String>();
	  Set<String> not_going = new HashSet<String>();
	  Set<String> not_set_to_going = null;
	  Set<String> not_set_to_not_going = null;
	  
	  try {
		p = new PotluckFacade("yaron", "yaronm93@gmail.com");
		template = new PotluckFacade("yaron", "yaronm93@gmail.com");
		to_invite = new HashMap<String, String>();
		to_invite.put("yy", "y@y.c");
		to_invite.put("ym", "ym@y.c");
		template.add_invited(to_invite);
		locations = new HashMap<String, String>();
		locations.put("party", "yaron's house");
		locations.put("delivery", "waterloo");
		template.add_locations(locations);
		going.add("yy");
		not_going.add("ym");
		going.add("rm");
		not_going.add("yym");
		not_set_to_going = template.add_going(going);
		not_set_to_not_going = template.add_not_going(not_going);
		
		
		to_add = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> cat_1 = new HashMap<String, Integer>();
		Map<String, Integer> cat_2 = new HashMap<String, Integer>();
		cat_1.put("cake", 3);
		cat_1.put("fruit", 20);
		cat_2.put("steak", 10);
		cat_2.put("chicken", 8);
		to_add.put("desert", cat_1);
		to_add.put("protein", cat_2);
		template.add_necessary_items(to_add);
		new_restrictions = new HashMap<String, String>();
		new_restrictions.put("nut_allergy", "can't eat tree_nuts");
		new_restrictions.put("kosher", "food must be kosher");
		template.add_restrictions(new_restrictions);
		new_times = new HashMap<String, Date>();
		new_times.put("setup", new Date(3));
		new_times.put("party", new Date(100));
		template.add_times(new_times);
		template.set_description("desc");
		p.from_template(template);
	  }catch(AddressException e) {
		ex = true;
	  }
	  assert(p.get_committed_items().isEmpty());
	  assert(p.get_description().equals("desc"));
	  assert(p.get_going().isEmpty());
	  assert(p.get_invited().equals(to_invite));
	  assert(p.get_locations().equals(locations));
	  assert(p.get_necessary_items().equals(to_add));
	  assert(p.get_not_going().isEmpty());
	  assert(p.get_restrictions().equals(new_restrictions));
	  assert(p.get_times().equals(new_times));
	  assert(p.get_uncommitted().isEmpty());
	  assert(not_set_to_going.contains("yym"));
	  assert(not_set_to_not_going.contains("rm"));
	  
	  assert(ex == false);
  }
  
  /*test to make sure guests can commit to bring items
   * that are in the neccessary items, if the guest
   * commits to the entire quantity of a given type
   * 
   */
  @Test
  public void add_committed_items_entire_quantity_in_necessary() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*test to make sure guests can commit to bring items
   * that are in the neccessary items when that is the
   * entire category
   * 
   */
  @Test
  public void add_committed_items_entire_category() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*test to make sure guests can commit to bring items
   * that are in the neccessary items when they are only
   * committing to some of hte quantity
   * 
   */
  @Test
  public void add_committed_items_partial_quantity_in_necessary() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*test to make sure guests can commit to bring items
   * that are in the neccessary items and commit to 
   * more than the requested quantity
   * 
   */
  @Test
  public void add_committed_items_more_quantity_than_necessary() {
    throw new RuntimeException("Test not implemented");
  }

  /*test to make sure guests can commit to bring items
   * that are not in the necessary items
   * 
   */
  @Test
  public void add_committed_items_not_in_necessary() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*test to make sure template creator works when guests have
   * committed to bringing things in the template
   */
  @Test
  public void from_templateGoingCommitted() {
  }
  
  /*
   * test if we can add someone to the going list if they were invited
   */
  @Test
  public void add_going_invited() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test if we can add someone to the going list if they were never invited
   */
  @Test
  public void add_going_not_invited() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test if we can add someone to the going list if they 
	previously said they weren't going
   */
  @Test
  public void add_going_not_going_prev() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test if we can add someone to the going list if they were already
   * added
   */
  @Test
  public void add_going_already_going() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test what happens when inviting someone with an invalid email format
   */
  @Test
  public void add_invited_invalid_email() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test what happens when inviting someone with empty key as a name
   */
  @Test
  public void add_invited_empty_name() {
    throw new RuntimeException("Test not implemented");
  }
  
  
  /*
   * test what happens when inviting someone who has not been invited
   */
  @Test
  public void add_invited_not_invite_prev() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test what happens when inviting someone who has already responded
   * that they are going
   */
  @Test
  public void add_invited_going() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test what happens when inviting someone who has already responded
   * that they are notgoing
   */
  @Test
  public void add_invited_not_going_prev() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test what happens when inviting someone who has already
   * been invited
   */
  @Test
  public void add_invited_already_invited() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * simple set and get to verify adding locations
   */
  @Test
  public void add_locations() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test adding a necessary_item that doesn't exist yet
   */
  @Test
  public void add_necessary_items_not_preexisting() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test adding a necessary_item that already exists
   */
  @Test
  public void add_necessary_items_preexisting() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test add_not going, when guest was previously just invited
   */
  @Test
  public void add_not_going_previously_in_invited() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test add_not going, when guest has already responded not going
   */
  @Test
  public void add_not_going_already_not_going() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test add_not going, when guest was never invited
   */
  @Test
  public void add_not_going_never_invited() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test add_not going, when guest was previously
   * going, but hadn't committed to bringing anything
   */
  @Test
  public void add_not_going_previously_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test add_not going, when guest was previously
   * going, and had committed to bringing something
   */
  @Test
  public void add_not_going_previously_committed() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * simple get and set test for adding restrictions
   */
  @Test
  public void add_restrictions() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * simple get and set test for adding times
   */
  @Test
  public void add_times() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test adding to uncommitted if user going and in neither
   * committed or uncommitted
   */
  @Test
  public void add_to_uncommitted_going() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test adding to uncommitted if user going and committed
   */
  @Test
  public void add_to_uncommitted_going_committed() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test adding to uncommitted if user going and already
   * uncommitted
   */
  @Test
  public void add_to_uncommitted_going_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test adding to uncommitted if user not going
   */
  @Test
  public void add_to_uncommitted_not_going() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test adding to uncommitted if user in invited
   */
  @Test
  public void add_to_uncommitted_invited() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test adding to uncommitted if user not invited
   */
  @Test
  public void add_to_uncommitted_not_invited() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * simple set and get to ensure changing owner email works
   */
  @Test
  public void change_owner_email() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test replacing owner email with an empty email address
   */
  @Test
  public void change_owner_email_empty_email() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test replacing owner email with an invalid email address
   */
  @Test
  public void change_owner_email_invalid_email() {
    throw new RuntimeException("Test not implemented");
  }
  
  /* 
   *simple set and get to ensure clearing description works 
   */
  @Test
  public void clear_description() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test removing someone who has committed to something 
   * not in committed items
   */
  @Test
  public void remove_committed_not_in_committed_items() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing more than person has committed to 
   */
  @Test
  public void remove_committed_more_than_committed_to() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing someone part of quantity a person
   * has committed to bring
   */
  @Test
  public void remove_committed_partial_committed() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test entire quantity of 1 item person has committed to
   * but not everything have committed to
   */
  @Test
  public void remove_committed_entire_quantity() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing a person's only committment
   */
  @Test
  public void remove_committed_only_item() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test removing someone who has committed to something 
   * not in necessary items yet
   */
  @Test
  public void remove_committed_not_in_necessary_items() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test removing user when he has multiple commitments
   */
  @Test
  public void remove_committed_user_multiple_commitments() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing user when he has no commitments
   */
  @Test
  public void remove_committed_user_no_commitments() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing user when he has committed to items
   * still required
   */
  @Test
  public void remove_committed_user_still_required() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing user when he has committed to items
   * not required anymore
   */
  @Test
  public void remove_committed_user_not_required() {
    throw new RuntimeException("Test not implemented");
  }

  /* 
   * test removing from necessary if item not in necessary
   */
  @Test
  public void remove_from_necessary_not_necessary() {
    throw new RuntimeException("Test not implemented");
  }

  /* 
   * test removing more than necessary from necessary
   */
  @Test
  public void remove_from_necessary_more_than_necessary() {
    throw new RuntimeException("Test not implemented");
  }
  
  /* 
   * test removing from less than necessary from necessary
   */
  @Test
  public void remove_from_necessary_less_than_necessary() {
    throw new RuntimeException("Test not implemented");
  }
  
  /* 
   * test removing exactly necessary from necessary
   */
  @Test
  public void remove_from_necessary_exactly_necessary() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing from uncommitted if not in uncommitted
   */
  @Test
  public void remove_from_uncommitted_not_in_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removing from uncommitted if in uncommitted
   */
  @Test
  public void remove_from_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test remove going if not in going
   */
  @Test
  public void remove_going_not_in_going() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test remove going if committed
   */
  @Test
  public void remove_going_committed() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test remove going if uncommitted
   */
  @Test
  public void remove_going_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * remove invited if in invited list
   */
  @Test
  public void remove_invited() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * uninvite if already responded going and committed
   */
  @Test
  public void remove_invited_committed() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * uninvite if already responded going and uncommitted
   */
  @Test
  public void remove_invited_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * uninvite if already responded not going
   */
  @Test
  public void remove_invited_not_going() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * simple remove and get to test locations removal
   */
  @Test
  public void remove_locations() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * remove location not in the list
   */
  @Test
  public void remove_locations_invalid_location() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * simple remove and get to test removal of restrictions 
   */
  @Test
  public void remove_restrictions() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test removal of restrictions when not in list 
   */
  @Test
  public void remove_restrictions_invalid_restriction() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * simple remove and get to test removal of times in list
   */
  @Test
  public void remove_times() {
    throw new RuntimeException("Test not implemented");
  }

  /*
   * test removal of times not in list
   */
  @Test
  public void remove_times_invalid_times() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test replacing owner valid info
   */
  @Test
  public void replace_owner_valid() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test replacing owner with empty name
   */
  @Test
  public void replace_owner_empty_name() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test replacing owner with empty email
   */
  @Test
  public void replace_owner_no_email() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test replacing owner invalid email
   */
  @Test
  public void replace_owner_invalid_email() {
    throw new RuntimeException("Test not implemented");
  }
  
  /*
   * test voting in poll
   */
  @Test
  public void vote_in_poll() {
	  throw new RuntimeException("Test not implemented");  
  }
  
  /*
   * test voting in poll that doesn't exist
   */
  @Test
  public void vote_in_poll_nonexistent_poll() {
	  throw new RuntimeException("Test not implemented");  
  }
  
  /*
   * test removing vote from poll
   */
  @Test
  public void remove_vote() {
	  throw new RuntimeException("Test not implemented");  
  }

  /*
   * test removing vote from poll when the vote is non_existent
   */
  @Test
  public void remove_vote_non_existent_vote() {
	  throw new RuntimeException("Test not implemented");  
  }
  
  /*
   * test removing vote from poll when the poll is non_existent
   */
  @Test
  public void remove_vote_non_existent_poll() {
	  throw new RuntimeException("Test not implemented");  
  }
}
