package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.util.Set;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Platz;
import de.uni_hamburg.informatik.swt.se2.kino.materialien.Vorstellung;

public class BarzahlungsWerkzeug {

	/*
	 * Erezeugt das Fenster für die Barabrechnung und
	 * gibt das Ergebnis als Boolean zurück (Kauf abgewickelt, oder nicht)
	 * @require plaetze != null
	 * @require !plaetze.isEmpty()
	 * @require vorstellung != null
	 */
	public static boolean performPayment(Set<Platz> plaetze, Vorstellung vorstellung) {
		assert plaetze != null : "precondition violated - null ref";
		assert !plaetze.isEmpty() : "precondition violated";
		assert vorstellung != null : "precondition violated - null ref";

		BarzahlungsWerkzeugUI cashWindow = new BarzahlungsWerkzeugUI(null, setUpTickets(
				plaetze, vorstellung), vorstellung.getFilm()
			.getTitel(), vorstellung.getKinosaal()
			.getName(), new Geldbetrag(vorstellung.getPreisFuerPlaetze(plaetze)));

		cashWindow.show();
		return cashWindow.getResult();
	}

	/*
	 * Baut ein String-Array aus dem vorliegenden Set
	 * mit Informationen über die Vorstellung.
	 * Die Einträge werden später für die Liste der zu verkaufenden Karten benötigt.
	 *
	 * @require plaetze != null
	 * @require !plaetze.isEmpty()
	 * @require vorstellung != null
	 *
	 * @ensure result.lentgh > 0
	 */
	private static String[] setUpTickets(Set<Platz> plaetze, Vorstellung vorstellung) {
		assert plaetze != null : "precondition violated - null ref";
		assert !plaetze.isEmpty() : "precondition violated";
		assert vorstellung != null : "precondition violated - null ref";

		int i = -1;
		String[] ticketliste = new String[plaetze.size()];
		for (Platz platz : plaetze) {
			++i;
			ticketliste[i] = "" + vorstellung.getKinosaal()
				.getName() + " | Reihe: " + platz.getReihe() + " - Platz: "
					+ platz.getSitz() + " | Preis: "
					+ vorstellung.getGeldbetragFuerPlatz(platz)
						.getFormatiertenString() + "€";
		}

		return ticketliste;
	}
}
