package actors;
import enums.*;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author Hans Kruisselbrink & Joost Elders
 *
 */
public class Ontwikkelaar extends UntypedActor {

	private ActorRef secretaresse;
	private ActorRef klaas;
	private Status status;

	public Ontwikkelaar(ActorRef secretaresse, ActorRef klaas) {
		this.secretaresse = secretaresse;
		this.klaas = klaas;
		
		status = Status.WERKEN;
		gaWeerAanHetWerk();
			
	}

	@Override
	public void onReceive(Object bericht) throws Exception {
		Bericht b = null;
		if(bericht instanceof Bericht) {
			b = (Bericht) bericht;
		}
		switch (b) {
		case UITNODIGINGOVERLEG :
			System.out.println("== " + getSelf().path().name() + " uitgenodigd voor scrumoverleg");
			//Klaas vertellen dat je klaar bent voor gesprek
			klaas.tell(Bericht.ONTWIKKELAARGEREEDVOOROVERLEG, getSelf());
			break;
		case OVERLEGBEGONNEN :
			System.out.println("=> " +getSelf().path().name() + " in klant-/scrumoverleg");
			status = Status.INOVERLEG;
			break;
		case GAWEERWERKEN :
			System.out.println("<= " +getSelf().path().name() + " gaat weer aan het werk");
			status = Status.WERKEN;
			gaWeerAanHetWerk();
			break;
		default:
			break;
		}
	}
	
	public Status getStatus() {
		return status;
	}

	public void gaWeerAanHetWerk() {
		try {
			Thread.sleep((long)(Math.random() * 4000)+4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("<= " +getSelf().path().name() + " wil overleggen");
		secretaresse.tell(Bericht.IKWILSCRUMOVERLEG, getSelf());
		status = Status.WACHTEN;
	}
}
