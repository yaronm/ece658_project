package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckListingBean extends PotluckBean implements Serializable {

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
	
}
