package gui;

import igrica.Igrica;
import logika.StanjePolja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Glavno okno aplikacije, hrani trenutno stanje igre 
 * in nadzoruje potek igre.
 * @author Uporabnik
 *
 */

public class Okno extends JFrame implements ActionListener {
	
	private JPanel plosca;
	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status;

	//Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	
	//ustvari glavno okno
	public Okno() {
		
		super();
		this.setTitle("Nine Men's Morris");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());

		//menu
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Igra");
		menu_bar.add(igra_menu);
		this.add(menu_bar);

		igraClovekRacunalnik = new JMenuItem("Clovek proti racunalniku");
		igra_menu.add(igraClovekRacunalnik);
		igraClovekRacunalnik.addActionListener(this);

		igraRacunalnikClovek = new JMenuItem("Racunalnik proti cloveku");
		igra_menu.add(igraRacunalnikClovek);
		igraRacunalnikClovek.addActionListener(this);

		igraClovekClovek = new JMenuItem("Clovek proti cloveku");
		igra_menu.add(igraClovekClovek);
		igraClovekClovek.addActionListener(this);

		//igralno polje
		plosca = new Plosca();
		this.add(plosca);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch(arg0.getActionCommand()) {
			case "Clovek proti racunalniku":
				Igrica.vrstaIgre = 1;
			break;
			case "Racunalnik proti cloveku":
				Igrica.polje.trenutnoStanje[1][1] = StanjePolja.CRNI;
				Plosca.instance.placeBlack(1, 1);
				Igrica.poteza++;
				Igrica.vrstaIgre = 1;
				break;
			case "Clovek proti cloveku":
				Igrica.vrstaIgre = 0;
				break;
		default:
			System.out.println("Nepoznan ukaz.");
		}
	}
	
}
