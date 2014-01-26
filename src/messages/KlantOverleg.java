package messages;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;

public class KlantOverleg extends Overleg{

	private Set<ActorRef> ontwikkelaars;
	private Set<ActorRef> klanten;
	
	public KlantOverleg() {
		super();
		ontwikkelaars = new HashSet<ActorRef>();
		klanten = new HashSet<ActorRef>();
	}
	
	public void addOntwikkelaar(ActorRef ar) {
		ontwikkelaars.add(ar);
	}
	
	public void addKlant(ActorRef ar) {
		klanten.add(ar);
	}
	
	public Set<ActorRef> getOntwikkelaars() {
		return ontwikkelaars;
	}
	
	public Set<ActorRef> getKlanten() {
		return klanten;
	}
}
