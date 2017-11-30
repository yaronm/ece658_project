package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckListingBean implements Serializable {

	@EJB
	private PotluckInterface potluckManager;

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
		for (Long id : user.getPotlucks()) {
			potluckManager.selectPotluck(id);
			potluckNames.put(id, potluckManager.getMetadata().getName());
			potluckDescriptions.put(id, potluckManager.getMetadata().getDescription());
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

	public String view(Long potluck) {
		potluckBean.setPotluck(potluck);
		return "potluckview";
	}

	public String edit(Long potluck) {
		potluckBean.setPotluck(potluck);
		return "potluckedit";
	}

}
