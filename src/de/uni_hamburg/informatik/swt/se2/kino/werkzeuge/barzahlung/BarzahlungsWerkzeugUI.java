package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;

@SuppressWarnings("serial")
class BarzahlungsWerkzeugUI extends JDialog
{
    private boolean _result;
    private JButton _btnCancel;
    private JButton _btnConfirm;
    private JFormattedTextField _fInpCash;
    private JTextArea _txtCalc;
    private Geldbetrag _toPay;

    public BarzahlungsWerkzeugUI(JFrame frame, String[] ticketliste,
            String film, String kinosal, Geldbetrag zuZahlen)
    {
        super(frame, "Barzahlung");

        _toPay = zuZahlen;

        // ########## Frame init ##########
        this.setPreferredSize(new Dimension(400, 310));
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.pack();
        this.setLocationRelativeTo(frame);
        this.setLayout(new BorderLayout());

        // ########## BL - Center ##########
        JPanel center = new JPanel(new GridLayout(3, 1));

        // ticket list
        {
            JList<String> menuList = new JList<String>(ticketliste);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            menuList.setLayoutOrientation(JList.VERTICAL);
            JScrollPane menuScrollPane = new JScrollPane(menuList);
            menuScrollPane.setMinimumSize(new Dimension(100, 50));
            menuList.setVisibleRowCount(4);
            center.add(menuScrollPane);
        }

        // cash input
        {
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Bargeld :"));
            try
            {
                _fInpCash = new JFormattedTextField(
                        new MaskFormatter("###.##€"));
            }
            catch (java.text.ParseException e)
            {
                //System.err.println(e.getMessage());
            }
            _fInpCash.setColumns(6);
            _fInpCash.getDocument()
                .addDocumentListener(new DocumentListener()
                {
                    @Override
                    public void removeUpdate(DocumentEvent e)
                    {
                        if (_fInpCash.isEditValid()) recalc();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e)
                    {
                        if (_fInpCash.isEditValid()) recalc();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e)
                    {
                        if (_fInpCash.isEditValid()) recalc();
                    }
                });
            inputPanel.add(_fInpCash);
            center.add(inputPanel);
        }

        {
            _txtCalc = new JTextArea();
            center.add(_txtCalc);
        }

        this.add(center, BorderLayout.CENTER);

        // ########## BL - Bottom ##########
        JPanel bottom = new JPanel(new GridLayout(1, 2));

        // cancel & confirm
        {
            // Button - Confirm payment
            _btnConfirm = new JButton("Zahlung abschließen");
            _btnConfirm.setEnabled(false);
            _btnConfirm.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _result = true;
                    dispose();
                };
            });
            bottom.add(_btnConfirm);

            // Button - Cancel payment
            _btnCancel = new JButton("Abbrechen");
            _btnCancel.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _result = false;
                    dispose();
                };
            });
            bottom.add(_btnCancel);
        }
        this.add(bottom, BorderLayout.PAGE_END);
    }

    public boolean getResult()
    {
        return _result;
    }

    private void recalc()
    {
        double inputVal = 0.0;
        try
        {
            inputVal = Double.parseDouble(_fInpCash.getText()
                .replace("€", "")
                .replace(" ", ""));
        }
        catch (java.lang.NumberFormatException e)
        {
            System.err.println(e.getMessage());
            _txtCalc.setText("Bitte Eingabefeld anpassen");
        }

        if (inputVal * 100 < _toPay.getEuroCent())
        {
            _btnConfirm.setEnabled(false);
            _txtCalc.setText("Bitte Eingabefeld anpassen");
        }
        else
        {
            _btnConfirm.setEnabled(true);
            //TODO Update Label
            _txtCalc.setText("Zu zahlen: " + _toPay.getFormatiertenString()
                    + " €\nGegeben: " + inputVal + " €\nRückgeld: "
                    + ((inputVal * 100 - _toPay.getEuroCent()) / 100) + " €");
        }

    }
}
