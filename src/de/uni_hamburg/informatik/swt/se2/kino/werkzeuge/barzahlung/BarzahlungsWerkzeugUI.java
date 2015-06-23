package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
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
class BarzahlungsWerkzeugUI extends JDialog {
	private boolean _result;
	private JButton _btnCancel;
	private JButton _btnConfirm;
	private JFormattedTextField _fInpCash;
	private JTextArea _txtCalc;

	private Geldbetrag _toPay;
	private String[] _ticketArray;
	private String _movie;
	private String _hall;

	private final String txtNOCALC = "\n\n Bitte Eingabefeld anpassen!\n";
	private final String txtCALC = ""
			+ " Zu zahlen:  %6.2f €\n"
			+ " --------------------\n"
			+ " Gegeben:    %6.2f €\n"
			+ " Rückgeld:   %6.2f €";

	public BarzahlungsWerkzeugUI(JFrame frame, String[] ticketliste,
			String film, String kinosaal, Geldbetrag zuZahlen) {

		super(frame, "Barzahlung - " + film);

		_toPay = zuZahlen;
		_ticketArray = ticketliste;
		_movie = film;
		_hall = kinosaal;

		// ########## Frame init ##########
		this.setMinimumSize(new Dimension(
				400, 400));
		this.setPreferredSize(new Dimension(
				500, 450));
		this.setMaximumSize(new Dimension(
				1920, 800));
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setLocationRelativeTo(frame);
		this.getContentPane()
		.setLayout(new BorderLayout(
				0, 10));

		// ########## BL - Center ##########
		JPanel center = new JPanel();
		this.initCenter(center);
		this.getContentPane()
		.add(center, BorderLayout.CENTER);

		// ########## BL - Bottom ##########
		JPanel bottom = new JPanel();
		this.initBottom(bottom);
		this.getContentPane()
		.add(bottom, BorderLayout.PAGE_END);

		this.pack();
		this.recalc();
	}

	public boolean getResult() {
		return _result;
	}

	private void initBottom(JComponent comp) {
		comp.setLayout(new BoxLayout(
				comp, BoxLayout.LINE_AXIS));

		// Button - Confirm payment
		_btnConfirm = new JButton(
				"Zahlung abschließen");
		_btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_result = true;
				BarzahlungsWerkzeugUI.this.dispose();
			};
		});
		_btnConfirm.setEnabled(false);

		comp.add(Box.createHorizontalGlue());
		comp.add(_btnConfirm);

		// Button - Cancel payment
		_btnCancel = new JButton(
				"Abbrechen");
		_btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_result = false;
				BarzahlungsWerkzeugUI.this.dispose();
			};
		});

		comp.add(Box.createRigidArea(new Dimension(
				10, 0)));
		comp.add(_btnCancel);
	}

	private void initCenter(JComponent comp) {
		comp.setLayout(new BoxLayout(
				comp, BoxLayout.PAGE_AXIS));

		// ticket list
		JList<String> menuList = new JList<String>(
				_ticketArray);
		menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		menuList.setLayoutOrientation(JList.VERTICAL);
		menuList.setVisibleRowCount(4);

		JScrollPane menuScrollPane = new JScrollPane(
				menuList);
		menuScrollPane.setMinimumSize(new Dimension(
				290, 60));

		JPanel pnlTickets = new JPanel();
		pnlTickets.setLayout(new GridLayout(
				1, 1));
		pnlTickets.setBorder(BorderFactory.createTitledBorder("Tickets"));
		pnlTickets.add(menuScrollPane);

		pnlTickets.setMinimumSize(new Dimension(
				300, 66));
		comp.add(pnlTickets);

		// cash input
		try {
			_fInpCash = new JFormattedTextField(
					new MaskFormatter(
							"###.##€"));
		}
		catch (java.text.ParseException e) {
			System.err.println(e.getMessage());
		}
		_fInpCash.setColumns(6);
		_fInpCash.getDocument()
		.addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (_fInpCash.isEditValid())
					BarzahlungsWerkzeugUI.this.recalc();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (_fInpCash.isEditValid())
					BarzahlungsWerkzeugUI.this.recalc();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (_fInpCash.isEditValid())
					BarzahlungsWerkzeugUI.this.recalc();
			}
		});
		_fInpCash.setText("000.00");
		_fInpCash.setMaximumSize(new Dimension(
				80, 30));
		_fInpCash.setMinimumSize(new Dimension(
				80, 30));

		JLabel lbl = new JLabel(
				"Zahlung: ");

		JPanel pnlInput = new JPanel();
		pnlInput.setMinimumSize(new Dimension(
				150, 24));
		pnlInput.setMaximumSize(new Dimension(
				1920, 24));

		pnlInput.setLayout(new BoxLayout(
				pnlInput, BoxLayout.LINE_AXIS));

		pnlInput.add(Box.createHorizontalGlue());
		pnlInput.add(lbl);
		pnlInput.add(Box.createRigidArea(new Dimension(
				20, 0)));
		pnlInput.add(_fInpCash);
		pnlInput.add(Box.createRigidArea(new Dimension(
				10, 0)));

		comp.add(Box.createRigidArea(new Dimension(
				0, 14)));
		comp.add(pnlInput);

		// calc field
		_txtCalc = new JTextArea();
		_txtCalc.setFont(new Font(
				Font.MONOSPACED, Font.BOLD, 16));
		_txtCalc.setBackground(this.getBackground());

		JPanel pnlCalc = new JPanel(
				new BorderLayout());
		pnlCalc.setBorder(BorderFactory.createTitledBorder("Rechnung"));
		pnlCalc.add(_txtCalc);

		pnlCalc.setMinimumSize(new Dimension(
				300, 86));
		pnlCalc.setMaximumSize(new Dimension(
				1920, 86));
		comp.add(pnlCalc);
	}

	private void recalc() {
		double inputVal = 0.0;
		try {
			inputVal = Double.parseDouble(_fInpCash.getText()
					.replace("€", "")
					.replace(" ", ""));
		}
		catch (java.lang.NumberFormatException e) {
			System.err.println(e.getMessage());
			_txtCalc.setText(String.format(txtNOCALC));
		}

		if (inputVal * 100 < _toPay.getEuroCent()) {
			_btnConfirm.setEnabled(false);
			_txtCalc.setText(String.format(txtNOCALC));
		}
		else {
			_btnConfirm.setEnabled(true);
			_txtCalc.setText(String.format(txtCALC,
					_toPay.getBetragInDouble(),
					inputVal,
					inputVal - _toPay.getBetragInDouble()));
		}

	}
}
