package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckBean implements Serializable {
	
	@EJB
	protected PotluckFacadeBean potluckFacade;

	@Inject
	protected SessionBean sessionBean;
	
	@NotNull
	private Potluck potluck;
	
	public PotluckBean() {
	}

	public Potluck getPotluck() {
		return potluck;
	}

	public void setPotluck(Potluck potluck) {
		this.potluck = potluck;
	}

}
