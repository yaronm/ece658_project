package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckListingBean implements Serializable {

	@Inject
	private SessionBean sessionBean;
	
	@Inject
	private PotluckBean potluckBean;
	
	private Collection<Potluck> potlucks;
	
	public PotluckListingBean() {
	}
	
	@PostConstruct
	public void initialize() {
		setPotlucks(sessionBean.getUser().getPotlucks());
	}
	
	public Collection<Potluck> getPotlucks() {
		return potlucks;
	}

	public void setPotlucks(Collection<Potluck> potlucks) {
		this.potlucks = potlucks;
	}
	
	public String view(Potluck potluck) {
		potluckBean.setPotluck(potluck);
		return "potluckview";
	}
	
	public String edit(Potluck potluck) {
		potluckBean.setPotluck(potluck);
		return "potluckedit";
	}

}
