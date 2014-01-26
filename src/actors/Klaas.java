package actors;

import enums.*;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


/**
 * @author Hans Kruisselbrink & Joost Elders
 *
 */
public class Klaas extends UntypedActor {

	private ActorRef secretaresse;

	private Set<ActorRef> ontwikkelaarsAanwezig;
	
	private Set<ActorRef> klantenGeplanned;
	private Set<ActorRef> klantenAanwezig;
	

	public Klaas() {
		ontwikkelaarsAanwezig = new HashSet<ActorRef>();
		klantenGeplanned = new HashSet<ActorRef>();
		klantenAanwezig = new HashSet<ActorRef>();
	}

	@Override
	public void onReceive(Object bericht) throws Exception {
		
		Bericht b = null;
		if(bericht instanceof Bericht) {
			b = (Bericht) bericht;
		}
		switch (b) {
		case SECRETARESSEZEGTGOEDEMORGEN :
			secretaresse = getSender();
			break;
		case PLANKLANT :
			klantenGeplanned.add(getSender());
			break;
		case ONTWIKKELAARGEREEDVOOROVERLEG :
			ontwikkelaarsAanwezig.add(getSender());
			if (ontwikkelaarsAanwezig.size() == 3) {
				assert ontwikkelaarsAanwezig.size() == 3 : "te veel/weinig ontwikkelaars gereed voor een overleg";
				for (ActorRef ontwikkelaar : ontwikkelaarsAanwezig) {
					ontwikkelaar.tell(Bericht.OVERLEGBEGONNEN, getSelf());
				}
				
				startScrumOverleg();
				
				for (ActorRef ontwikkelaar : ontwikkelaarsAanwezig) {
					ontwikkelaar.tell(Bericht.GAWEERWERKEN, getSelf());
				}
				
				ontwikkelaarsAanwezig.clear();
				assert ontwikkelaarsAanwezig.size() == 0 : "er zijn nog steeds ontwikkelaars aanwezig";

				secretaresse.tell(Bericht.KLAASKLAARMETOVERLEG, getSelf());
			}
			break;
		case KLANTGEREEDVOOROVERLEG :
			if(klantenGeplanned.contains(getSender())) {
				klantenAanwezig.add(getSender());
			}
			if(klantenAanwezig.size() == klantenGeplanned.size()) {
				for (ActorRef ontwikkelaar : ontwikkelaarsAanwezig) {
					ontwikkelaar.tell(Bericht.OVERLEGBEGONNEN, getSelf());
				}
				for (ActorRef klant : klantenAanwezig) {
					klant.tell(Bericht.OVERLEGBEGONNEN, getSelf());
				}
				
				startScrumOverleg();
				
				for (ActorRef ontwikkelaar : ontwikkelaarsAanwezig) {
					ontwikkelaar.tell(Bericht.GAWEERWERKEN, getSelf());
				}
				for (ActorRef klant : klantenAanwezig) {
					klant.tell(Bericht.GAWEERNAARHUIS, getSelf());
				}
				ontwikkelaarsAanwezig.clear();
				klantenAanwezig.clear();
				klantenGeplanned.clear();
				
				secretaresse.tell(Bericht.KLAASKLAARMETOVERLEG, getSelf());
			}
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
		System.out.println("Klaas gestart");
	}

	public void startScrumOverleg() {
	
		try {
			Thread.sleep((long)(Math.random() * 5000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void justLive() {
		try {
			Thread.sleep((long)(Math.random() * 5000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
