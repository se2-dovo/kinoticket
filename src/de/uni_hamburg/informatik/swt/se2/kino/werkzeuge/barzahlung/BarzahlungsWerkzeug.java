package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Platz;
import de.uni_hamburg.informatik.swt.se2.kino.materialien.Vorstellung;

public class BarzahlungsWerkzeug
{

    private BarzahlungsWerkzeugUI _ui;
    private Vorstellung _vorstellung;
    private Set<Platz> _plaetze;
    private Geldbetrag _toPay;

    /*
     * Bereitet das Werkzeug auf den nächsten Verkauf vor
     * @require plaetze != null
     * @require !plaetze.isEmpty()
     * @require vorstellung != null
     * 
     */
    public void setSetting(Vorstellung vorstellung, Set<Platz> plaetze)
    {
        assert plaetze != null : "precondition violated - null ref";
        assert !plaetze.isEmpty() : "precondition violated";
        assert vorstellung != null : "precondition violated - null ref";

        _vorstellung = vorstellung;
        _plaetze = plaetze;
        _toPay = _vorstellung.getGeldbetragFuerPlaetze(_plaetze);
    }

    /*
     * Erzeugt das Fenster für die Barabrechnung und
     * gibt das Ergebnis als Boolean zurück (Kauf abgewickelt, oder nicht)
     */
    public void performPayment()
    {
        _ui = new BarzahlungsWerkzeugUI(null, setUpTickets(_plaetze,
                _vorstellung), _vorstellung.getFilm()
            .getTitel(), _vorstellung.getKinosaal()
            .getName(), new Geldbetrag(
                _vorstellung.getPreisFuerPlaetze(_plaetze)));

        this.createListener();
        this.recalc();
        _ui.show();
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
    private static String[] setUpTickets(Set<Platz> plaetze,
            Vorstellung vorstellung)
    {
        assert plaetze != null : "precondition violated - null ref";
        assert !plaetze.isEmpty() : "precondition violated";
        assert vorstellung != null : "precondition violated - null ref";

        int i = -1;
        String[] ticketliste = new String[plaetze.size()];
        for (Platz platz : plaetze)
        {
            ++i;
            ticketliste[i] = "" + vorstellung.getKinosaal()
                .getName() + " | Reihe: " + platz.getReihe() + " - Platz: "
                    + platz.getSitz() + " | Preis: "
                    + vorstellung.getGeldbetragFuerPlatz(platz)
                        .getFormatiertenString() + "€";
        }

        return ticketliste;
    }

    /*
     * Erzeugt alle nötigen Listener und uebergibt sie den Entsprechenden Komponenten.
     */
    private void createListener()
    {
        _ui.getBtnCancel()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _ui.closeWindow();
                }
            });
        _ui.getBtnConfirm()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _vorstellung.verkaufePlaetze(_plaetze);
                    _ui.closeWindow();
                }
            });

        _ui.getfInpCash()
            .getDocument()
            .addDocumentListener(new DocumentListener()
            {
                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    if (_ui.getfInpCash()
                        .isEditValid()) recalc();
                }

                @Override
                public void insertUpdate(DocumentEvent e)
                {

                    if (_ui.getfInpCash()
                        .isEditValid()) recalc();
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    if (_ui.getfInpCash()
                        .isEditValid()) recalc();
                }
            });
    }

    /*
     * Prüft den Text des Textfelds (_fInpCash) auf ein validen Doublewert
     * und bestimmt, ob der Bestätigungsbutton freigeschaltet wird/ob
     * das Textareal eine Berechnung anzeigen soll.
     */
    private void recalc()
    {
        try
        {
            /*_ui.getfInpCash()
                .commitEdit();
            double inputVal = Double.parseDouble(((String) _ui.getfInpCash()
                .getValue()).replace("€", "")
                .replace(" ", ""));*/

            double inputVal = Double.parseDouble((_ui.getfInpCash().getText()).replace(
                    "€", "")
                .replace(" ", ""));

            if (inputVal * 100 >= _toPay.getEuroCent())
                _ui.setConfirmable(inputVal, _toPay.getBetragInDouble());
            else
                _ui.unsetConfirmable(_toPay.getBetragInDouble());
            return;
        }
        catch (NumberFormatException e1)
        {
            _ui.unsetConfirmable(_toPay.getBetragInDouble());
        }
    }
}
