package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.util.Set;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Platz;
import de.uni_hamburg.informatik.swt.se2.kino.materialien.Vorstellung;

public class BarzahlungsWerkzeug
{

    public static boolean performPayment(Set<Platz> plaetze,
            Vorstellung vorstellung)
    {

        BarzahlungsWerkzeugUI cashWindow = new BarzahlungsWerkzeugUI(null,
                setUpTickets(plaetze, vorstellung), vorstellung.getFilm()
                    .getTitel(), vorstellung.getKinosaal()
                    .getName(), new Geldbetrag(
                        vorstellung.getPreisFuerPlaetze(plaetze)));

        cashWindow.setVisible(true);
        return cashWindow.getResult();
    }

    private static String[] setUpTickets(Set<Platz> plaetze,
            Vorstellung vorstellung)
    {
        int i = -1;
        String[] ticketliste = new String[plaetze.size()];
        for (Platz platz : plaetze)
        {
            ++i;
            ticketliste[i] = "Film: " + vorstellung.getFilm()
                .getTitel() + " | R:" + platz.getReihe() + " S:"
                    + platz.getSitz() + " | Preis: "
                    + vorstellung.getGeldbetragFuerPlatz(platz)
                        .getFormatiertenString() + "€";
        }

        return ticketliste;
    }
}
