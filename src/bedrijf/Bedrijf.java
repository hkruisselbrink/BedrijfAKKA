package bedrijf;

import actors.*;
import enums.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


public class Bedrijf {

	private static final int AANTAL_KLANTEN = 5;
	private static final int AANTAL_ONTWIKKELAARS = 7;

	private ActorRef klaas;
	private ActorRef secretaresse;

	public void startWerkdag() {

		// Maak een nieuw ActorSysteem aan
		ActorSystem system = ActorSystem.create("Bedrijf");

		klaas = system.actorOf(Props.create(Klaas.class), "Klaas"); 
		secretaresse = system.actorOf(Props.create(Secretaresse.class, klaas), "Secretaresse");

		// Nu weet de klaas wie zijn secretaresse is
		klaas.tell(Bericht.SECRETARESSEZEGTGOEDEMORGEN, secretaresse);
		
		// Ontwikkelaars aanmaken
		for (int i = 0; i < AANTAL_ONTWIKKELAARS; i++) {
			system.actorOf(Props.create(Ontwikkelaar.class, secretaresse, klaas), "Ontwikkelaar" + i);
		}
		// Klanten aanmaken
		for (int i = 0; i < AANTAL_KLANTEN; i++) {
			system.actorOf(Props.create(Klant.class, secretaresse, klaas), "Klant" + i);
		}
	}

	public static void main(String[] args) {
		Bedrijf bedrijf = new Bedrijf();
		bedrijf.startWerkdag();
	}
}
