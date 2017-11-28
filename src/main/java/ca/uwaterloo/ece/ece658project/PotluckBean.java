package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckBean implements Serializable {
	
	@Inject
	protected SessionBean sessionBean;

}
