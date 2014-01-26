package actors;

import enums.*;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class Ontwikkelaar extends UntypedActor {

	private ActorRef secretaresse;
	private Status status;

	public Ontwikkelaar(ActorRef secretaresse) {
		this.secretaresse = secretaresse;
		
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
			getSender().tell(Bericht.ONTWIKKELAARGEREEDVOOROVERLEG, getSelf());
			break;
		case SCRUMOVERLEGBEGONNEN :
			System.out.println("=> " +getSelf().path().name() + " in scrumoverleg");
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

	@Override
	public void preStart() throws Exception {
		//		System.out.println(getSelf().path().name() + " gestart");
	}

	@Override
	public void postStop() throws Exception {
		//		System.out.println(getSelf().path().name() + " gestopt");
	}
	
	public Status getStatus() {
		return status;
	}

	public void gaWeerAanHetWerk() {
		try {
			Thread.sleep((long)(Math.random() * 3000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("<= " +getSelf().path().name() + " wil overleggen");
		secretaresse.tell(Bericht.IKWILSCRUMOVERLEG, getSelf());
		status = Status.WACHTEN;
	}
}
