package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.util.Set;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Platz;
import de.uni_hamburg.informatik.swt.se2.kino.materialien.Vorstellung;

public class BarzahlungsWerkzeug
{

    public static boolean performPayment(Set<Platz> plaetze,
            Vorstellung vorstellung)
    {
        BarzahlungsWerkzeugUI cashWindow = new BarzahlungsWerkzeugUI(null,
                plaetze, vorstellung);
        cashWindow.setVisible(true);
        return cashWindow.getResult();
    }

}
