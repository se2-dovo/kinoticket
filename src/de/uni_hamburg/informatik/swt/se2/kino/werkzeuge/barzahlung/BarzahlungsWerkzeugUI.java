package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.text.MaskFormatter;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;

class BarzahlungsWerkzeugUI
{
    // Informationen zum Anzeigen
    private final Geldbetrag _toPay;
    private final String _movie;
    private final String _hall;
    private String[] _ticketArray;

    // Ausgabeformat für _txtCalc
    public static final String TXTNOCALC = "" + " Zu zahlen:  %6.2f €\n"
            + "\n Bitte Eingabefeld anpassen!\n";
    public static final String TXTCALC = "" + " Zu zahlen:  %6.2f €\n"
            + " --------------------\n" + " Gegeben:    %6.2f €\n"
            + " Rückgeld:   %6.2f €";

    // Teil der benötigten Komponenten
    private JFrame _owner;
    private JDialog _dialog;
    private JButton _btnCancel;
    private JButton _btnConfirm;
    private JFormattedTextField _fInpCash;
    private JTextArea _txtCalc;

    /*
     * Initialisert ein neues JDialog-Fenster mit dazugehörigen Komponenten.
     *
     * @param frame Der Besitzer des JDialogs (darf null sein)
     * @param ticketliste Liste mit Tickets
     * @param film Der Film für den die Karten sind
     * @param kinosaal Der Kinosaal, in dem der Film gezeigt wird
     * @param zuZahlen Der Geldbetrag, der zu zahlen ist
     *
     * @require ticketliste != null
     * @require ticketliste.length > 0
     * @require film != null
     * @require kinosaal != null
     * @require zuZahlen.getEuroCent() > 0
     */
    public BarzahlungsWerkzeugUI(JFrame frame, String[] ticketliste,
            String film, String kinosaal, Geldbetrag zuZahlen)
    {

        assert ticketliste != null : "precondition violated - null ref";
        assert ticketliste.length > 0 : "precondition violated";
        assert film != null : "precondition violated - null ref";
        assert kinosaal != null : "precondition violated - null ref";
        assert zuZahlen.getEuroCent() > 0 : "precondition violated";

        _owner = frame;
        _toPay = zuZahlen;
        _ticketArray = ticketliste;
        _movie = film;
        _hall = kinosaal;

        _btnConfirm = this.initButtonConfirm();
        _btnCancel = this.initButtonCancel();
        _txtCalc = this.initTextArea();
        this.initInputField(); // _fInpCash
        _dialog = this.initDialog();
    }

    /*
     * Zeigt das Dialogfenster (return nach Schließen des Dialogs)
     */
    public void show()
    {
        _dialog.setVisible(true);
    }

    /*
     * Initialisert das für BorderLayout.SOUTH gedachte Panel
     *
     * @return Das initialisierte Panel
     *
     * @ensure result instanceof JPanel && not null
     */
    private JPanel initBottom()
    {
        JPanel comp = new JPanel();
        comp.setLayout(new BoxLayout(comp, BoxLayout.LINE_AXIS));

        comp.add(Box.createHorizontalGlue());

        comp.add(_btnConfirm);

        comp.add(Box.createRigidArea(new Dimension(10, 0)));

        comp.add(_btnCancel);

        return comp;
    }

    /*
     * Baut den Button zum Abbrechen des Bezahlvorgangs
     *
     * @return Der initialisierte Button
     *
     * @ensure result instanceof JButton && not null
     */
    private JButton initButtonCancel()
    {
        JButton button = new JButton("Abbrechen");

        return button;
    }

    /*
     * Baut den Button zum Bestätigen des Bezahlvorgangs
     *
     * @return Der initialisierte Button
     *
     * @ensure result instanceof JButton && not null
     */
    private JButton initButtonConfirm()
    {
        JButton button = new JButton("Zahlung abschließen");

        button.setEnabled(false);

        return button;
    }

    /*
     * Baut das Panel für die Kalkulation des Wechselgeldes
     *
     * @return Das initialisierte Panel
     *
     * @ensure result instanceof JPanel && not null
     */
    private JPanel initCalcField()
    {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Rechnung"));
        panel.setMinimumSize(new Dimension(300, 86));
        panel.setMaximumSize(new Dimension(1920, 86));

        panel.add(_txtCalc);

        return panel;
    }

    /*
     * Baut das Panel für das Eingabefeld
     *
     * @return Das initialisierte Panel
     *
     * @ensure result instanceof JPanel && not null
     */
    private JPanel initCashInput()
    {

        JLabel lbl = new JLabel("Zahlung:");

        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(150, 24));
        panel.setMaximumSize(new Dimension(1920, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(Box.createHorizontalGlue());
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(_fInpCash);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));

        return panel;
    }

    /*
     * Initialisert das für BorderLayout.CENTER gedachte Panel
     *
     * @return Das initialisierte Panel
     *
     * @ensure result instanceof JPanel && not null
     */
    private JPanel initCenter()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(this.initTicketList());

        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        panel.add(this.initCashInput());

        panel.add(this.initCalcField());

        return panel;
    }

    /*
     * Instanziert das Dialogfenster selbst
     *
     * @return Das initialiserte Dialogfenster
     *
     * @ensure result instanceof JDialog && not null
     */
    private JDialog initDialog()
    {
        JDialog dialog = new JDialog(_owner, String.format(
                "Barzahlung - %s - %s", _movie, _hall));
        dialog.setMinimumSize(new Dimension(400, 400));
        dialog.setPreferredSize(new Dimension(500, 450));
        dialog.setMaximumSize(new Dimension(1920, 800));
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(_owner);
        dialog.getContentPane()
            .setLayout(new BorderLayout(0, 10));

        // ########## BL - Center ##########
        JPanel pnlcenter = this.initCenter();
        dialog.getContentPane()
            .add(pnlcenter, BorderLayout.CENTER);

        // ########## BL - Bottom ##########
        JPanel pnlbottom = this.initBottom();
        dialog.getContentPane()
            .add(pnlbottom, BorderLayout.PAGE_END);

        dialog.pack();

        return dialog;
    }

    /*
     * Baut das Eingabefeld (_fInputCash)
     */
    private void initInputField()
    {
        try
        {
            _fInpCash = new JFormattedTextField(new MaskFormatter("###.##€"));
        }
        catch (java.text.ParseException e)
        {
            System.err.println(e.getMessage());
        }
        _fInpCash.setColumns(6);

        _fInpCash.setText("000.00");
        _fInpCash.setMaximumSize(new Dimension(80, 30));
        _fInpCash.setMinimumSize(new Dimension(80, 30));
    }

    /*
     * Baut das Textfeld
     *
     * @return Die JTextArea
     *
     * @ensure result instanceof JTextArea && not null
     */
    private JTextArea initTextArea()
    {
        JTextArea textarea = new JTextArea();
        textarea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));

        return textarea;
    }

    /*
     * Baut das JPanel mit der Ticketauflistung
     *
     * @return Das JPanel
     *
     * @ensure result instanceof JPanel && not null
     */
    private JPanel initTicketList()
    {
        JList<String> menuList = new JList<String>(_ticketArray);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setLayoutOrientation(JList.VERTICAL);
        menuList.setVisibleRowCount(4);

        JScrollPane menuScrollPane = new JScrollPane(menuList);
        menuScrollPane.setMinimumSize(new Dimension(290, 60));

        JPanel pnlTickets = new JPanel();
        pnlTickets.setLayout(new GridLayout(1, 1));
        pnlTickets.setBorder(BorderFactory.createTitledBorder("Tickets"));
        pnlTickets.setMinimumSize(new Dimension(300, 66));

        pnlTickets.add(menuScrollPane);

        return pnlTickets;
    }

    /*
     * Schaltet den Bestätigungsbutton frei
     * und bereitet das Textfeld
     *
     * @param input der vorgelegte Betrag
     * @param toPay der zu bezahlende Betrag
     *
     * @require input > 0
     * @require toPay > 0
     */
    public void setConfirmable(double input, double toPay)
    {
        assert input > 0 : "precondition violated";
        assert toPay > 0 : "precondition violated";

        _btnConfirm.setEnabled(true);
        _txtCalc.setText(String.format(TXTCALC, toPay, input, input - toPay));
    }

    /*
     * Blockiert den Bestätigungsbutton
     * und bescheibt das Textfeld
     *
     * @param toPay der zu bezahlende Betrag
     *
     * @require toPay > 0
     */
    public void unsetConfirmable(double toPay)
    {
        assert toPay > 0 : "precondition violated";

        _btnConfirm.setEnabled(false);
        _txtCalc.setText(String.format(TXTNOCALC, toPay));
    }

    JButton getBtnCancel()
    {
        return this._btnCancel;
    }

    JButton getBtnConfirm()
    {
        return this._btnConfirm;
    }

    JFormattedTextField getfInpCash()
    {
        return this._fInpCash;
    }

    JTextArea getTxtCalc()
    {
        return this._txtCalc;
    }

    public void closeWindow()
    {
        _dialog.dispose();
    }
}
