package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.AuthenticatedPotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class PotluckListingBean implements Serializable {

	@EJB
	private AuthenticatedPotluckInterface potluckManager;

	@EJB
	private UserManagerBean userManager;

	@Inject
	private SessionBean sessionBean;

	@Inject
	private PotluckBean potluckBean;

	private Map<Long, String> potluckNames = new HashMap<>(), potluckDescriptions = new HashMap<>();

	public PotluckListingBean() {
	}

	@PostConstruct
	public void initialize() {
		User user = userManager.getUser(sessionBean.getEmail());
		try {
			for (Long id : user.getPotlucks()) {
				potluckManager.selectPotluck(id);
				potluckNames.put(id, potluckManager.getMetadata().getName());
				potluckDescriptions.put(id, potluckManager.getMetadata().getDescription());
			}
		}catch(Exception e){
			//donothing
		}
	}

	public Map<Long, String> getPotluckNames() {
		return potluckNames;
	}

	public Collection<Long> getPotlucks() {
		return potluckNames.keySet();
	}

	public void setPotluckNames(Map<Long, String> potluckNames) {
		this.potluckNames = potluckNames;
	}

	public Map<Long, String> getPotluckDescriptions() {
		return potluckDescriptions;
	}

	public void setPotluckDescriptions(Map<Long, String> potluckDescriptions) {
		this.potluckDescriptions = potluckDescriptions;
	}

	public String view(Long potluck) throws LoginException {
		potluckBean.setPotluck(potluck);
		return "potluckview";
	}

	public String edit(Long potluck) throws LoginException {
		potluckBean.setPotluck(potluck);
		return "potluckedit";
	}

	@NotNull
	private String newName;

	private String newDescription;

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		if (newDescription == null) {
			newDescription = "";
		}
		this.newDescription = newDescription;
	}

	public String create() throws LoginException {
		Long id = potluckManager.createPotluck(getNewName(), sessionBean.getEmail(), getNewDescription());
		potluckBean.setPotluck(id);
		return "potluckedit";
	}

}
