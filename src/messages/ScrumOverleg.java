package messages;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;

public class ScrumOverleg extends Overleg{

	private Set<ActorRef> ontwikkelaars;
	
	public ScrumOverleg() {
		super();
		ontwikkelaars = new HashSet<ActorRef>();
	}
	
	public void addOntwikkelaar(ActorRef ar) {
		ontwikkelaars.add(ar);
	}
	
	public Set<ActorRef> getOntwikkelaars() {
		return ontwikkelaars;
	}
}
