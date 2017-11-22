package potLucky.potLucky;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.AddressException;

import org.testng.annotations.Test;

public class PotluckTest {

  @Test
  public void PotluckBasicInstantiation() {
	boolean ex = false;
	potLucky.Potluck p = null;
	try {
		p = new potLucky.Potluck("yaron", "yaronm93@gmail.com");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == false);
    assert (p.get_owner().equals("yaron"));
    assert (p.get_owner_email().equals("yaronm93@gmail.com"));
  }
  
  @Test
  public void PotluckNoName() {
	boolean ex = false;
	try {
		potLucky.Potluck p = new potLucky.Potluck("", "yaronm93@gmail.com");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == true);
    
  }

  @Test
  public void PotluckNoEmail() {
	boolean ex = false;
	try {
		potLucky.Potluck p = new potLucky.Potluck("Yaron", "");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == true);
    
  }
  
  @Test
  public void PotluckInvalidEmail() {
	boolean ex = false;
	try {
		potLucky.Potluck p = new potLucky.Potluck("Yaron", "yaron");
	}catch(AddressException e) {
		ex = true;
	}
	assert (ex == true);
    
  }
  
  @Test
  public void from_templateBasic() {
	  boolean ex = false;
	  potLucky.Potluck p = null;
	  potLucky.Potluck template = null;
	  Map<String, String> to_invite = null;
	  Map<String, String> locations = null;
	  Map<String, Map<String, Integer>> to_add = null;
	  Map<String, String> new_restrictions = null;
	  Map<String, Date>new_times = null;
	  try {
		p = new potLucky.Potluck("yaron", "yaronm93@gmail.com");
		template = new potLucky.Potluck("yaron", "yaronm93@gmail.com");
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
  
  @Test
  public void from_templateGoingNotGoingNoCommitted() {
	  boolean ex = false;
	  potLucky.Potluck p = null;
	  potLucky.Potluck template = null;
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
		p = new potLucky.Potluck("yaron", "yaronm93@gmail.com");
		template = new potLucky.Potluck("yaron", "yaronm93@gmail.com");
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
  
  @Test
  public void add_committed_items() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_going() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_invited() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_locations() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_necessary_items() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_not_going() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_owner() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_restrictions() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_times() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_to_necessary() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void add_to_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void change_owner_email() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void clear_description() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_committed_items() {
    throw new RuntimeException("Test not implemented");
  }


  @Test
  public void get_going() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_id() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_info() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_invited() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_locations() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_necessary_items() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_not_going() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_owner() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_owner_email() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_restrictions() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_times() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void get_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void has_committed() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_committed() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_committed_user() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_from_necessary() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_from_uncommitted() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_going() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_invited() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_locations() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_restrictions() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void remove_times() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void replace_owner() {
    throw new RuntimeException("Test not implemented");
  }

}
