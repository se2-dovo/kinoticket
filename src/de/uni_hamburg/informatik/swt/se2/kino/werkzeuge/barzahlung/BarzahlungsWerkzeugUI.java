package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.util.Set;

import javax.swing.JDialog;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Platz;
import de.uni_hamburg.informatik.swt.se2.kino.materialien.Vorstellung;

class BarzahlungsWerkzeugUI extends JDialog
{
    public BarzahlungsWerkzeugUI(Set<Platz> plaetze, Vorstellung vorstellung)
    {
        vorstellung.getDatum();
        vorstellung.getFilm();
        vorstellung.getKinosaal();
        vorstellung.getPreisFuerPlaetze(plaetze);

        for (Platz platz : plaetze)
        {
            vorstellung.getPreisFuerPlaetze();
        }
    }
}
