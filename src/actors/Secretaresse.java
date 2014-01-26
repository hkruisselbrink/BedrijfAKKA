package actors;

import enums.*;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class Secretaresse extends UntypedActor {

	private ActorRef klaas;
	private Set<ActorRef> wachtendeOntwikkelaars;
	private Set<ActorRef> wachtendeKlanten;

	private boolean inOverleg;

	public Secretaresse(ActorRef klaas) {
		wachtendeOntwikkelaars = new HashSet<ActorRef>();
		wachtendeKlanten = new HashSet<ActorRef>();
		inOverleg = false;
		this.klaas = klaas;
	}

	@Override
	public void onReceive(Object bericht) throws Exception {
		Bericht b = null;
		if(bericht instanceof Bericht) {
			b = (Bericht) bericht;
		}
		switch (b) {
		case IKWILSCRUMOVERLEG : 
			if(!inOverleg && wachtendeOntwikkelaars.size() < 4) {
				if(wachtendeKlanten.size() > 0) {
					inOverleg = true;
					for(ActorRef ontwikkelaar : wachtendeOntwikkelaars) {
						ontwikkelaar.tell(Bericht.GAWEERWERKEN, getSelf());
					}
					wachtendeOntwikkelaars.clear();
					getSender().tell(Bericht.UITNODIGINGOVERLEG, klaas);
					for(ActorRef klant : wachtendeKlanten) {
						klaas.tell(Bericht.PLANKLANT, klant);
						klant.tell(Bericht.UITNODIGINGOVERLEG, klaas);
					}
				} else {
					wachtendeOntwikkelaars.add(getSender());
					if(wachtendeOntwikkelaars.size() == 3) {
						inOverleg = true;
						for(ActorRef ontwikkelaar : wachtendeOntwikkelaars) {
							ontwikkelaar.tell(Bericht.UITNODIGINGOVERLEG, klaas);
						}
						wachtendeOntwikkelaars.clear();
					}
				}
				
			} else {
				getSender().tell(Bericht.GAWEERWERKEN, getSelf());
			}
			break;
		case IKWILKLANTOVERLEG :
			wachtendeKlanten.add(getSender());
			if(wachtendeOntwikkelaars.size() > 0 && !inOverleg) {
				inOverleg = true;
				for(ActorRef ontwikkelaar : wachtendeOntwikkelaars) {
					if(wachtendeOntwikkelaars.size() != 1) {
						ontwikkelaar.tell(Bericht.GAWEERWERKEN, getSelf());
					}
				}
				for(ActorRef klant : wachtendeKlanten) {
					klaas.tell(Bericht.PLANKLANT, klant);
					klant.tell(Bericht.UITNODIGINGOVERLEG, klaas);
				}
			}
			
			break;
		case KLAASKLAARMETOVERLEG :
			System.out.println("Klaas klaar met een scrumoverleg");
			inOverleg = false;
			break;
		default:
			break;
		}	
	}

	@Override
	public void postStop() throws Exception {
		// TODO Auto-generated method stub
		super.postStop();
	}

	@Override
	public void preStart() throws Exception {
		System.out.println("Secretaresse gestart");
		super.preStart();
	}

}