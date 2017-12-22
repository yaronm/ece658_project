package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Collection;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.interfaces.Event;

public interface ISchedule {

	public void schedule(Event event, PotluckEntity potluck);

	public Collection<Event> getEvents(PotluckEntity potluck);

}