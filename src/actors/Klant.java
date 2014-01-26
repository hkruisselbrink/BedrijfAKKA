package actors;

import enums.*;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class Klant extends UntypedActor {
	
	private ActorRef secretaresse;
	private ActorRef klaas;
	private Status status;
	
	public Klant(ActorRef secretaresse, ActorRef klaas) {
		this.secretaresse = secretaresse;
		this.klaas = klaas;
		this.status = Status.THUIS;
		
		gaWeerNaarHuis();
	}

	@Override
	public void onReceive(Object bericht) throws Exception {
		Bericht b = null;
		if(bericht instanceof Bericht) {
			b = (Bericht) bericht;
		}
		switch (b) {
		case UITNODIGINGOVERLEG :
			status = Status.ONDERWEG;
			System.out.println("=> " +getSelf().path().name() + " is onderweg naar bedrijf");
			gaNaarBedrijf();
			getSender().tell(Bericht.KLANTGEREEDVOOROVERLEG, getSelf());
			status = Status.WACHTEN;
			break;
		case SCRUMOVERLEGBEGONNEN :
			System.out.println("=> " +getSelf().path().name() + " in scrumoverleg");
			status = Status.INOVERLEG;
			break;
		case GAWEERNAARHUIS :
			System.out.println("<= " +getSelf().path().name() + " gaat weer naar huis");
			status = Status.THUIS;
			gaWeerNaarHuis();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void postStop() throws Exception {
		super.postStop();
	}

	@Override
	public void preStart() throws Exception {
		// TODO Auto-generated method stub
		super.preStart();
	}
	
	public void gaNaarBedrijf() {
		try {
			Thread.sleep((long)(Math.random() * 3000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void gaWeerNaarHuis() {
		try {
			Thread.sleep((long)(Math.random() * 6000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("== " +getSelf().path().name() + " heeft een bug gevonden");
		secretaresse.tell(Bericht.IKWILKLANTOVERLEG, getSelf());
		status = Status.WACHTENOPUITNODIGING;
	}
}