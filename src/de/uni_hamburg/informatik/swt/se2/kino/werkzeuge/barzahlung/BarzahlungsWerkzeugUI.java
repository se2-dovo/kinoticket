package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.MaskFormatter;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Platz;
import de.uni_hamburg.informatik.swt.se2.kino.materialien.Vorstellung;

@SuppressWarnings("serial")
class BarzahlungsWerkzeugUI extends JDialog
{
    private boolean _result;

    public BarzahlungsWerkzeugUI(JFrame frame, Set<Platz> plaetze,
            Vorstellung vorstellung)
    {
        super(frame, "Barzahlung");

        // Init
        this.setPreferredSize(new Dimension(400, 310));
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.pack();
        this.setLocationRelativeTo(frame);
        this.setLayout(new BorderLayout());

        /*vorstellung.getDatum();
        vorstellung.getFilm();
        vorstellung.getKinosaal();
        vorstellung.getPreisFuerPlaetze(plaetze);

        for (Platz platz : plaetze)
        {
            vorstellung.getPreisFuerPlatz(platz);
        }
        */

        JPanel center = new JPanel(new GridLayout(3, 1));

        /*ArrayList<String> tickets = new ArrayList<String>();
        //String[] tickets
        System.out.println("Foo" + plaetze.size());
        for (Platz platz : plaetze)
        {
            tickets.add("Platz: " + platz.getReihe() + "|" + platz.getSitz()
                    + " - " + vorstellung.getPreisFuerPlatz(platz));
        }*/
        String[] foo = {"foas", "asd", "asd", "asd", "asd", "asd", "asd"};
        JList menuList = new JList(foo);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane menuScrollPane = new JScrollPane(menuList);
        menuScrollPane.setMinimumSize(new Dimension(100, 50));
        menuList.setVisibleRowCount(4);

        center.add(menuScrollPane);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Number :");
        JFormattedTextField tf = null;
        try
        {
            tf = new JFormattedTextField(new MaskFormatter("##.###"));
        }
        catch (java.text.ParseException exc)
        {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        tf.setColumns(10);
        tf.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panel.add(label);
        panel.add(tf);
        center.add(panel);
        center.add(menuScrollPane);

        this.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 2));

        JButton button = null;
        // Button - Confirm payment
        button = new JButton("Zahlung abschließen");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _result = true;
                dispose();
            };
        });
        bottom.add(button);

        // Button - Cancel payment
        button = new JButton("Abbrechen");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _result = false;
                dispose();
            };
        });
        bottom.add(button);

        this.add(bottom, BorderLayout.PAGE_END);
    }

    public boolean getResult()
    {
        return _result;

    }
}
