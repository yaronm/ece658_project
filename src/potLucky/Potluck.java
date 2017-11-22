package potLucky;


import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Potluck implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    // Persistent Fields:
    @Id @GeneratedValue
    Long id;
    private String owner;
    private String owner_email;
    private String description;
    private Map<String, String> restrictions;//<restriction name, details>
    private Map<String, Date> times;//<time name, time>
    private Map<String, String> locations;//<location name, location>
    private Map<String, String> going;//<name, email_address>
    private Map<String, String> not_going;//<name, email_address>
    private Map<String, String> invited;//<name, email_address>
    private Map<String, Map<String, Integer>> necessary_items;//<Category,<name, amount>>
    private Map<String, Map<String, Map<String, Integer>>> committed_items;//<Category, <name, <who committed, number committed to>>>
    private List<String> uncommitted;//invitee's name
    
    
 
    // Constructors:
    /*
     * Creates an empty potluck with the given owner and owner email
    */
    public Potluck(String owner, String owner_email) throws AddressException{
    	InternetAddress email_address = new InternetAddress(owner_email);
    	email_address.validate();
    	this.owner = owner;
    	this.owner_email = owner_email;
    	description = "";
    	times = new HashMap<String, Date>();
    	locations = new HashMap<String, String>();
    	going = new HashMap<String, String>();
    	not_going = new HashMap<String, String>();
    	invited = new HashMap<String, String>();
    	necessary_items = new HashMap<String, Map<String,Integer>>();
    	committed_items = new HashMap<String, Map<String, Map<String,Integer>>>();
    	uncommitted = new ArrayList<String>();
    	uncommitted.add(owner);
    	going.put(owner, owner_email);
        
    }
 
    /*
     * Creates a potluck from an old template.
     * Any non-filled values of temp are initialized
     * to empty values. The time Map is initialized 
     * to be empty regardless of the template.
     * Does not copy owner info
     */
    public void from_template(Potluck temp) {
    	description = temp.get_description();
        if (temp.get_locations() != null && !temp.get_locations().isEmpty())
        	locations.putAll(temp.get_locations());
        if (temp.get_invited() != null && !temp.get_invited().isEmpty())
        	invited.putAll(temp.get_invited());
        //people who responded before still need to respond again
        if (temp.get_going() != null && !temp.get_going().isEmpty())
        	invited.putAll(temp.get_going());
        if (temp.get_not_going() != null && !temp.get_not_going().isEmpty())
        	invited.putAll(temp.get_not_going());
        
        if (temp.get_necessary_items()!= null && !temp.get_necessary_items().isEmpty())
        	necessary_items.putAll(temp.get_necessary_items());
        
        //previously committed items should be moved to necessary items 
        //since we do not know whether the same people are coming 
        if (temp.get_committed_items() != null && !temp.get_committed_items().isEmpty()) {
	        Map<String, Map<String, Map<String,Integer>>> com_cop = temp.get_committed_items();
	        for (Map.Entry<String, Map<String, Map<String,Integer>>> entry : com_cop.entrySet()) {
				String category = entry.getKey();
	        	for (Map.Entry<String, Map<String,Integer>> it: entry.getValue().entrySet()) {
	        		String item = it.getKey();
	        		Integer needed = 0;
	        		for (Integer need_ind : it.getValue().values()) {
	        			needed += need_ind;
	        		}
	        		add_to_necessary(category, item, needed);
	        	}
	        }
        }
        
        invite_all(invited);
    }
 
    
    //getters
    
    /*
     * returns the generated id of the potluck
     */
    public long get_id() {
    	return id;
    }
    
    /*
     * returns the owner/organizer of the potluck
     */
    public String get_owner() {
    	return this.owner;
    }
    
    /*
     * returns the email of the owner/organizer of the potluck
     */
    public String get_owner_email() {
    	return this.owner_email;
    }
    
    /*
     * returns the mapping of what items have been committed to being brought
     * and by whom. Format of return value:
     * Map<Category, <name, <who committed, number committed to>>>
     */
    public Map<String, Map<String, Map<String,Integer>>> get_committed_items() {
        return (Map<String, Map<String, Map<String, Integer>>>)this.committed_items;
    }
    
    /*
     * returns the description of the potluck
     */    
    public String get_description() {
        return this.description;
    }

    /*
     * returns the dietary restrictions associated with the potluck
     * Format of the return value:
     * Map<Title of restriction, verbose description of restriction>
     */
    public Map<String, String> get_restrictions() {
        return (Map<String, String>) this.restrictions;
    }
    
    /*
     * Returns any times associated with the potluck
     * e.g start time, end time, preparation time, dropoff time, etc..
     * return format:
     * Map<Time description, Time>
     */
    public Map<String, Date> get_times() {
    	return (Map<String, Date>) this.times;
    }

    /*
     * Returns any locations associated with the potluck
     * e.g event location, preparation location, dropoff location, etc...
     * return format:
     * Map<Location description, Location>
     */
    public Map<String, String> get_locations() {
    	return (Map<String, String>) this.locations;
	}
    
    /*
     * returns the email addresses and names of any guests who
     * have responded that they are attending the potluck
     * format of output:
     * Map<Name, Email address>
     */
    public Map<String, String> get_going() {
    	return (Map<String, String>) this.going;
	}

    /*
     * returns the email addresses and names of any guests who
     * have responded that they are not attending the potluck
     * format of output:
     * Map<Name, Email address>
     */
    public Map<String, String> get_not_going() {
    	return (Map<String, String>) this.not_going;
	}

    /*
     * returns the email addresses and names of any guests who
     * have been invited to the potluck, but have not responded
     * to the invitation.
     * format of output:
     * Map<Name, Email address>
     */
    public Map<String, String> get_invited() {
    	return (Map<String, String>) this.invited;
	}
    
    /*
     * returns a map of all items that have been requested, but
     * no one has committed to bringing
     * Format of output:
     * Map<Category, Map<Item name, quantity still needed>> 
     */
    public Map<String, Map<String, Integer>> get_necessary_items() {
    	return (Map<String, Map<String, Integer>>) this.necessary_items;
	}
    
    /*
     * returns the names of guests who have said they are coming
     * but have not committed to bringing anything
     */
    public List<String> get_uncommitted() {
    	return (List <String>) this.uncommitted;
	}

    
    //setters
    
    /*sets the owner and owner_email
     * if either of these is empty returns false and does nothing
     * adds the owner to the going list as well as the uncommitted list
     * If the email address has any other formatting error, throws an exception
     */
    public boolean add_owner(String owner, String owner_email) throws AddressException{
    	if (owner != "" || owner_email == "") {
    		return false;
    	}
    	InternetAddress email_address = new InternetAddress(owner_email);
    	email_address.validate();
    	this.owner = owner;
    	this.owner_email = owner_email;
		this.going.put(owner, owner_email);
		this.uncommitted.add(owner);
		return true;
    }
    
    /*
     * replaces the owner with a new owner and email.
     * removes the owner from the going list, and either
     * removes anything the owner has committed to bring
     * or removes the owner from the uncommitted list
     * if the new owner is not in the going list, adds them
     * to the going list and the uncommitted list
     * 
     * 
     * returns true on success, false on failure.
     * In order to succeed neither parameter may be empty
     * If there is an issue with the email address, but it is
     * not empty, throws an exception
     */
    public boolean replace_owner(String new_owner, String new_owner_email) throws AddressException {
    	if (new_owner.equals("") || new_owner_email.equals(""))
    		return false;
    	InternetAddress email_address = new InternetAddress(new_owner_email);
    	email_address.validate();
    	this.going.remove(owner);
    	this.uncommitted.remove(owner);
    	remove_committed_user(owner);
    	this.owner = new_owner;
    	this.owner_email = new_owner_email;
    	if(!(this.going.containsKey(owner))) {
    		this.going.put(owner, owner_email);
    		this.uncommitted.add(owner);
    	}
    	return true;
    }
    
    /*
     * replaces the owner's email address if the new email is non-empty
     * changes the email address in the going list
     * If there is a format issue with the email address, throws an exception
     */
    public void change_owner_email(String new_email) throws AddressException{
    	if (new_email != "") {
    		InternetAddress email_address = new InternetAddress(new_email);
        	email_address.validate();
    		this.owner_email = new_email;
    		this.going.put(owner, owner_email);
    	}
    }
    
    /*
     * Records that user:user has committed to bring a quantity:quantity 
     * of item: item, which is part of the category: category
     * 
     * if this item is in the necessary list, removes quantity of that item
     * from the necessary list
     * 
     * records that this user is no longer part of the uncommitted list
     * 
     */
    public void add_committed_items(String category, String item, String user, Integer quantity) {
    	//add to committed_items dictionary
    	Map<String, Integer> to_add = new HashMap<String, Integer>();
		to_add.put(user, quantity);
		if (committed_items.containsKey(category)) {
    		if (committed_items.get(category).containsKey(item)) {
    			committed_items.get(category).get(item).put(user, quantity);
    		}else {
    			committed_items.get(category).put(item, to_add);
    		}
    		
    	}else {
    		Map<String, Map<String,Integer>> to_add2 = new HashMap<String, Map<String,Integer>>();
    		to_add2.put(item, to_add);
    		committed_items.put(category, to_add2);
    	}
    		
    	//remove from necessary_items
		if (necessary_items.containsKey(category)) {
    		if (necessary_items.get(category).containsKey(item)) {
    			int needed = necessary_items.get(category).get(item);
    			needed -= quantity;
    			if (needed > 0) {
    				necessary_items.get(category).put(item, needed);
    			}else {
    				necessary_items.get(category).remove(item);
    				if (necessary_items.get(category).isEmpty()) {
    					necessary_items.remove(category);
    				}
    			}
    		}
		}
		
		//remove from uncommitted
		uncommitted.remove(user);
        	
    }
        
    /*
     * Removes any commitments that user: user has made.
     * does not add the user to the uncommitted list
     * updates the necessary list as required
     * 
     */
    public void remove_committed_user(String user) {
    	for (Map.Entry<String, Map<String, Map<String,Integer>>> entry : this.committed_items.entrySet()) {
			String category = entry.getKey();
        	for (Map.Entry<String, Map<String,Integer>> it: entry.getValue().entrySet()) {
        		String item = it.getKey();
        		Integer needed = 0;
        		if (it.getValue().containsKey(user)) {
        			needed = it.getValue().get(user);
        			//<Category, <name, <who committed, number committed to>>>
        			add_to_necessary(category, item, needed);
        			this.committed_items.get(category).get(item).remove(user);
        		}
        	}
    	}
    }
    
    /*removes a specific commitment, specified by the category,
     * item, and user
     * updates the necessary list accordingly
     * 
     */
    public void remove_committed(String category, String item, String user) {
    	if (this.committed_items.containsKey(category) && 
    			this.committed_items.get(category).containsKey(item) && 
    			this.committed_items.get(category).get(item).containsKey(user)) {
	    	int needed = this.committed_items.get(category).get(item).get(user);
	    	add_to_necessary(category, item, needed);
	    	this.committed_items.get(category).get(item).remove(user);
	    	if (this.committed_items.get(category).get(item).isEmpty()) {
	    		this.committed_items.get(category).remove(item);
	    		if(this.committed_items.get(category).isEmpty()) {
	    			this.committed_items.remove(category);
	    		}
	    	}
    	}
    }
    
    /*
     * overwrites the current description with a new description
     */
    public void set_description(String new_description) {
        this.description = new_description;
    }
    
    /*clears the current description
     * 
     */
    public void clear_description() {
        this.description = "";
    }
    
    /*
      * adds 1 or more new dietary restrictions.
      * Input format:
      * Map<Title, details>   
      */
    public void add_restrictions(Map<String, String> new_restrictions) {
        this.restrictions.putAll(new_restrictions);
    }
    
    /*
     * removes 1 or more dietary restrictions based on their
     * titles
     */
    public void remove_restrictions(Set<String> title) {
    	for (String tit : title)
    		this.restrictions.remove(tit);
    }
    
    /*adds multiple times to the event.
     * Input format:
     * Map<Time description, time>
     */
    public void add_times(Map<String, Date> new_times) {
    	this.times.putAll(new_times);
    }
    
    /*
     * removes 1 or more times from the event  if they exits
     */
    public void remove_times(Set<String> title) {
    	for (String tit: title)
    		this.times.remove(tit);
    }
    
    /*
     * adds 1 or more locations to the event.
     * input format:
     * Map<location description, location>
     */
    public void add_locations(Map<String, String> new_locations) {
    	this.locations.putAll(new_locations);
	}
    
    /*
     * removes the locations that have the descriptions
     * in title. If the description is not valid, ignores
     * it
     */
    public void remove_locations(Set<String> title) {
    	for(String tit : title)
    		this.locations.remove(tit);
    }
    
    /*
     * moves 1 or more users to the going list
     * removes them from the invitation or not going list
     * and sets them as uncommitted
     * input format: Set<name>
     * returns a list of anyone not added to going
     */
    public Set<String> add_going(Set<String> new_going) {
    	Set<String> not_added = new HashSet<String>();
    	for (String go : new_going) {
    		if (this.invited.containsKey(go)){
    			this.going.put(go, invited.get(go));
    			this.invited.remove(go);
    			this.uncommitted.add(go);
    		}else if (this.not_going.containsKey(go)) {
    			this.going.put(go, invited.get(go));
    			this.not_going.remove(go);
    			this.uncommitted.add(go);
    		}else {
    			not_added.add(go);
    		}
    	}
    	return not_added;
	}
    /*
     * removes 1 or more users from the going list
     * also removes anything htey have committed to bring
     * places them in the invited list
     * returns a set of anyone not removed
     */
    public Set<String> remove_going(Set<String> to_rem) {
    	Set <String> not_removed= new HashSet<String>();
    	for (String r : to_rem) {
    		if (this.going.containsKey(r)) {
	    		String em = this.going.get(r);
	    		this.invited.put(r, em);
	    		remove_committed_user(r);
	    		this.going.remove(r);
    		}
    		else
    			not_removed.add(r);
    	}
    	return not_removed;
    }

    /*
     * moves 1 or more users to the not-going list
     * removes any commitments
     * removes the user from the going or invited list
     * if the user is in the uncommitted list, removes them 
     * 
     * returns a set of anyone not set to not going
     */
    public Set<String> add_not_going(Set<String> new_not_going) {
    	Set <String> not_added = new HashSet<String>();
    	for (String go : new_not_going) {
    		String em = null;
    		if (this.invited.containsKey(go))
    			em = this.invited.get(go);
    		else if (this.going.containsKey(go))
    			em = this.going.get(go);
    		if (em != null) {
    			
    			this.not_going.put(go, em);
    			this.invited.remove(go);
    			if (this.going.containsKey(go) && !this.uncommitted.contains(go)) {
    				remove_committed_user(go);
    			}
    			this.going.remove(go);
    			this.uncommitted.remove(go);
    		}else
    			not_added.add(go);
    	}
    	return not_added;
	}

    /*
     * invites 1 or more users
     * if a user is already invited, does not do anything
     * 
     * input format:
     * Map<name, email> 
     * returns a set of names of people who were not invited
     */
    public Set<String> add_invited(Map<String, String>to_invite) {
    	Map<String, String> act_inv = new HashMap<String, String>();
    	Set<String> uninvited = new HashSet<String>();
    	for (String inv : to_invite.keySet()) {
    		if(this.invited.containsKey(inv) || 
    				this.going.containsKey(inv) ||
    				this.not_going.containsKey(inv)) {
    			try{
    				InternetAddress email_address = new InternetAddress(to_invite.get(inv));
    				email_address.validate();
    				this.invited.put(inv, to_invite.get(inv));
    				act_inv.put(inv, to_invite.get(inv));
    			}catch (AddressException e) {
    				uninvited.add(inv);
    			}
    		}else
    			uninvited.add(inv);
    	}
    	invite_all(act_inv);
    	return (Set<String>)uninvited;
	}
    
    /*
     * Uninvites 1 or more users based on their names
     * removes them from their commitments
     * input: Set<name>
     * returns a set of people who were not uninvited
     */
    public Set<String> remove_invited(Set<String> to_remove) {
    	Map<String, String> to_uninvite = new HashMap<String, String>();
    	Set<String> non_existent = new HashSet<String>();
    	for (String inv : to_remove) {
    		if (this.invited.containsKey(inv)) {
    			to_uninvite.put(inv, invited.get(inv));
    			this.invited.remove(inv);
    		}else if (this.going.containsKey(inv)) {
    			to_uninvite.put(inv,  going.get(inv));
    			this.going.remove(inv);
    			remove_committed_user(inv);
    			this.uncommitted.remove(inv);
    		}else if (this.not_going.containsKey(inv)) {
    			to_uninvite.put(inv,  not_going.get(inv));
    			this.not_going.remove(inv);
    		}else non_existent.add(inv);
    	}
    	uninvite_all(to_uninvite);
    	return (Set<String>) non_existent;
    }
    
    /*
     * adds one or more items to the necessary list
     * input:
     * Map<Category, Map<item name, quantity required>>
     */
    public void add_necessary_items(Map<String, Map<String, Integer>> to_add) {
    	for (String cat: to_add.keySet()) {
    		for (String it : to_add.get(cat).keySet()) {
    			add_to_necessary(cat, it, to_add.get(cat).get(it));
    		}
    	}
	}
    
    /*
     * adds 1 item to the necessary items. If it already exists,
     * adds the quantities
     */
    public void add_to_necessary(String category, String item, int needed) {
    	Map <String, Integer> to_add = new HashMap<String, Integer>();
    	to_add.put(item, needed);
		if (necessary_items.containsKey(category)) {
    		if (necessary_items.get(category).containsKey(item)) {
    			needed += necessary_items.get(category).get(item);
    		}
    		necessary_items.get(category).put(item, needed);
    	}else {
    		necessary_items.put(category ,to_add);
    	}
    }

    /*
     * removes a certain quantity of an item from the necessary items map
     */
    public void remove_from_necessary(String category, String item, int amount_to_remove) {
    	if (this.necessary_items.containsKey(category)) {
    		if (this.necessary_items.get(category).containsKey(item)) {
    			this.necessary_items.get(category).put(item, this.necessary_items.get(category).get(item)-amount_to_remove);
    			if (this.necessary_items.get(category).get(item) <=0) {
    				this.necessary_items.get(category).remove(item);
    				if (this.necessary_items.get(category).isEmpty()) {
    					this.necessary_items.remove(category);
    				}
    			}
    		}
    	}
    }

    /*
     * removes users' commitments and adds them to the uncommitted
     * list
     */
    public void add_to_uncommitted(List<String> users) {
    	for (String us : users) {
    		if (!this.uncommitted.contains(us) && this.going.containsKey(us)) {
    			remove_committed_user(us);
    			this.uncommitted.add(us);
    		}
    	}
	}
    
    /*
     * removes users from the uncommitted list
     */
    public void remove_from_uncommitted(List<String> users) {
    	for (String us : users) {
    		if (this.going.containsKey(us) && has_committed(us)) {
    			continue;
    		}
    		else if (this.uncommitted.contains(us)) {
    			this.uncommitted.remove(us); 
    		}
    	
    	}
    }
    
    //helper methods

    /*
     * checks whether a user has committed to bringing anything
     */
    private boolean has_committed(String us) {
    	//<Category, <name, <who committed, number committed to>>>
    	for(String category : this.committed_items.keySet()) {
    		for (String it : committed_items.get(category).keySet()) {
    			if (committed_items.get(category).get(it).containsKey(us))
					return true;    			
    		}
    	}
    	return false;
    }
    
    /*
     * serializes the potluck information into a string
     */
    private String get_info() {
    	String Message = new String();
    	Message += "Organizer " + owner + "\n";
    	Message += "Organizer email: " + owner_email+"\n";
    	if (!this.description.equals("")) {
			Message = Message +("Description: ");
			Message = Message + this.description;
			Message += "\n\n";
    	}
    	if (this.times.isEmpty() == false){
    		Message += "Important Times:";
    		for (String time_title : this.times.keySet()) {
    			Message += "\n\t\t" + time_title + ":\t"  + this.times.get(time_title);
    		}
    		Message += "\n";
    	}
    	if (this.locations.isEmpty() == false) {
	    	Message += "Important Locations:";
	    	for (String loc_title : this.locations.keySet()) {
	    		Message += "\n\t\t" + loc_title + ":\t"  + this.locations.get(loc_title);
	    	}
	    	Message += "\n";
    	}
    	if (this.restrictions.isEmpty() == false) {
	    	Message += "Dietary restrictions";
	    	for (String rest_title : this.restrictions.keySet()) {
	    		Message += "\n\t\t" + rest_title + ":\t"  + this.restrictions.get(rest_title);
	    	}
    	}
    	return Message;
    }
    
    /*
     * sends uninvite messages to all users in the to_uninvite list
     */
    private void uninvite_all(Map<String, String> to_univite) {
    	for (String name : to_univite.keySet()) {
    		uninvite(name, to_univite.get(name));
    	}
    }

    /*
     * sends an uninvite notification to a given user
     */
    private void uninvite(String name, String email) {
    	String Subject = "Potluck uninvitation";
    	String Message = "Hello ".concat(name).concat(" you have been uninvited from a potluck! "
    			+ "The details are as follow:\n").concat(get_info())+"\nThanks,\n"+owner;
    	//email Message to email
    }
    
    /*
     * sends invites all users in the map
     */
    private void invite_all(Map<String, String> to_univite) {
    	for (String name : to_univite.keySet()) {
    		invite(name, to_univite.get(name));
    	}
    }
    
    /*sends an invitation to a given user
     * 
     */
    private void invite(String name, String email) {
    	String Subject = "Potluck invitation";
    	String Message = "Hello ".concat(name).concat(" you have been invited to a potluck! "
    			+ "The details are as follow:\n").concat(get_info());
    	//email Message to email
    }
}
	
