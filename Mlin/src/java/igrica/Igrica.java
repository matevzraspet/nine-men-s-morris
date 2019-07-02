package igrica;

import gui.Okno;
import gui.Plosca;
import logika.*;

import java.util.List;

public class Igrica {

	private static final int testPotez = 17;

	public static Igrica igra;
	public static IgralnoPolje polje;

	private boolean odstranjujesKamne = false;
	public static int poteza = 0;

	private int stevilkaIzvirnegaPolja = -1;
	private int izvirniNivo = -1;

	private int stCrnihZetonov = 9;
	private int stBelihZetonov = 9;
	public static int vrstaIgre = -1; //0 - igralec proti igralcu, 1- igralec proti racunalniku, 2- racunalnik proti igralcu

	public static void main(String[] args) {
		igra = new Igrica();
		polje = new IgralnoPolje();
		Okno okno = new Okno();
		okno.pack();
		okno.setVisible(true);
		Racunalnik.polje = polje;
	}

	/**
	 * se sprozi ko pritisnemo gumb
	 */
	public void buttonClick(int stevilkaPolja, int nivo) {
		if (vrstaIgre == -1) {
			System.out.println("Najprej izberi vrsto igre.");
			return;
		}

		System.out.println("st crnih: " + stCrnihZetonov);
		System.out.println("st belih: " + stBelihZetonov);
		if (odstranjujesKamne) {
			if (polje.odstraniKamen(nivo, stevilkaPolja)) {
				odstranjujesKamne = false;
				System.out.println("uspesno odstranjen kamen");
				Plosca.instance.deleteToken(stevilkaPolja, nivo);
				if (!polje.crniNaPotezi) {
					stBelihZetonov--;
				} else {
					stCrnihZetonov--;
				}
				racNaVrsti();
			}
			return;
		}

		if (poteza <= testPotez) {
			prvaFaza(stevilkaPolja, nivo);
		} else if (polje.crniNaPotezi && stCrnihZetonov == 3 || !polje.crniNaPotezi && stBelihZetonov == 3) {
			tretjaFaza(stevilkaPolja, nivo);
		} else {
			drugaFaza(stevilkaPolja, nivo);
		}
		if (stBelihZetonov <= 2) {
			System.out.println();
			System.out.println("Crni zmaga!!!!!!!!!!!!!1");
		}
		if (stCrnihZetonov <= 2) {
			System.out.println();
			System.out.println("Beli zmaga!!!!!!!!!!!");
		}
	}

	/**
	 * racunalnikova poteza
	 */
	private void racNaVrsti() {
		if (vrstaIgre != 1 || odstranjujesKamne)
			return;

		if (poteza <= testPotez) {
			Pozicija poteza = Racunalnik.pridobiNaslednjoPotezo();
			int rez = polje.poloziKamen(poteza.nivo, poteza.stPolja);
			if (rez == 1) {
				Pozicija zaOdstranit = kajOdstraniti();
				Plosca.instance.deleteToken(zaOdstranit.stPolja, zaOdstranit.nivo);
				polje.trenutnoStanje[zaOdstranit.nivo][zaOdstranit.stPolja] = StanjePolja.PRAZNO;
				stBelihZetonov--;
			}
			Plosca.instance.placeBlack(poteza.stPolja, poteza.nivo);
		} else if (izvirniNivo == -1) {
			Premik premik = Racunalnik.pridobiNaslednjiPremik(stCrnihZetonov <= 3);
			int rez = polje.premakniKamen(premik.start.nivo, premik.start.stPolja, premik.end.nivo, premik.end.stPolja, true);
			if (rez == 1) {
				Pozicija zaOdstranit = kajOdstraniti();
				Plosca.instance.deleteToken(zaOdstranit.stPolja, zaOdstranit.nivo);
				polje.trenutnoStanje[zaOdstranit.nivo][zaOdstranit.stPolja] = StanjePolja.PRAZNO;
				stBelihZetonov--;
			}
			Plosca.instance.placeBlack(premik.end.stPolja, premik.end.nivo);
			Plosca.instance.deleteToken(premik.start.stPolja, premik.start.nivo);
		} else {
			return;
		}
		this.poteza++;
	}

	/**
	 * faza postavljanja zetonov
	 */
	private void prvaFaza(int stevilkaPolja, int nivo) {
		int uspesnost = polje.poloziKamen(nivo, stevilkaPolja);
		switch (uspesnost) {
			case -1:
				System.out.println("poteza ni uspela, poskusi se enkrat.");
				return;
			case 0:
				System.out.println("uspesna poteza");
				break;
			case 1:
				System.out.println("mlin");
				odstranjujesKamne = true;
				break;
		}

		if (!polje.crniNaPotezi)
			Plosca.instance.placeBlack(stevilkaPolja, nivo);
		else
			Plosca.instance.placeWhite(stevilkaPolja, nivo);
		poteza++;
		racNaVrsti();

	}

	/**
	 * faza premikanja zetonov
	 */
	private void drugaFaza(int stevilkaPolja, int nivo) {
		if (izvirniNivo == -1 && polje.trenutnoStanje[nivo][stevilkaPolja].equals(StanjePolja.PRAZNO)) {
			System.out.println("Ne mores izbrati praznega polja");
			return;
		}
		if (polje.crniNaPotezi && polje.trenutnoStanje[nivo][stevilkaPolja].equals(StanjePolja.BELI)) {
			System.out.println("ne mores prestaviti nasprotnikovega polja");
			return;
		}
		if (!polje.crniNaPotezi && polje.trenutnoStanje[nivo][stevilkaPolja].equals(StanjePolja.CRNI)) {
			System.out.println("ne mores prestaviti nasprotnikovega polja");
			return;
		}
		if (this.izvirniNivo == -1) {
			this.izvirniNivo = nivo;
			this.stevilkaIzvirnegaPolja = stevilkaPolja;
			System.out.println("Premikam s polja: " + stevilkaPolja + " in nivoja: " + nivo);
			return;
		} else if (stevilkaPolja == stevilkaIzvirnegaPolja && nivo == izvirniNivo) {
			System.out.println("Ne mores postaviti nazaj na zacetno polje");
			return;
		}

		int uspesnost = polje.premakniKamen(izvirniNivo, stevilkaIzvirnegaPolja, nivo, stevilkaPolja, false);
		switch (uspesnost) {
			case -1:
				izvirniNivo = -1;
				stevilkaIzvirnegaPolja = -1;
				System.out.println("premik ni uspel, poskusi se enkrat.");
				return;
			case 0:
				System.out.println("uspesen premik");
				break;
			case 1:
				System.out.println("mlin");
				odstranjujesKamne = true;
				break;
		}

		if (!polje.crniNaPotezi) {
			Plosca.instance.placeBlack(stevilkaPolja, nivo);
		} else {
			Plosca.instance.placeWhite(stevilkaPolja, nivo);
		}

		Plosca.instance.deleteToken(stevilkaIzvirnegaPolja, izvirniNivo);
		stevilkaIzvirnegaPolja = -1;
		izvirniNivo = -1;
		poteza++;
		racNaVrsti();
	}

	/**
	 * imamo samo se 3 zetone, lahko prestavljas kamorkoli
	 */
	private void tretjaFaza(int stevilkaPolja, int nivo) {
		if (izvirniNivo == -1 && polje.trenutnoStanje[nivo][stevilkaPolja].equals(StanjePolja.PRAZNO)) {
			System.out.println("Ne mores izbrati praznega polja");
			return;
		}
		if (polje.crniNaPotezi && polje.trenutnoStanje[nivo][stevilkaPolja].equals(StanjePolja.BELI)) {
			System.out.println("ne mores prestaviti nasprotnikovega polja");
			return;
		}
		if (!polje.crniNaPotezi && polje.trenutnoStanje[nivo][stevilkaPolja].equals(StanjePolja.CRNI)) {
			System.out.println("ne mores prestaviti nasprotnikovega polja");
			return;
		}
		if (this.izvirniNivo == -1) {
			this.izvirniNivo = nivo;
			this.stevilkaIzvirnegaPolja = stevilkaPolja;
			System.out.println("Premikam s polja: " + stevilkaPolja + " in nivoja: " + nivo);
			return;
		} else if (stevilkaPolja == stevilkaIzvirnegaPolja && nivo == izvirniNivo) {
			System.out.println("Ne mores postaviti nazaj na zacetno polje");
			return;
		}

		int uspesnost = polje.premakniKamen(izvirniNivo, stevilkaIzvirnegaPolja, nivo, stevilkaPolja, true);
		switch (uspesnost) {
			case -1:
				izvirniNivo = -1;
				stevilkaIzvirnegaPolja = -1;
				System.out.println("premik ni uspel, poskusi se enkrat.");
				return;
			case 0:
				System.out.println("uspesen premik");
				break;
			case 1:
				System.out.println("mlin");
				odstranjujesKamne = true;
				break;
		}

		if (!polje.crniNaPotezi) {
			Plosca.instance.placeBlack(stevilkaPolja, nivo);
		} else {
			Plosca.instance.placeWhite(stevilkaPolja, nivo);
		}

		Plosca.instance.deleteToken(stevilkaIzvirnegaPolja, izvirniNivo);
		stevilkaIzvirnegaPolja = -1;
		izvirniNivo = -1;
		poteza++;
		racNaVrsti();
	}

	/**
	 * da racunalnik ve kaj odstraniti
	 */
	private Pozicija kajOdstraniti() {
		List<Pozicija> beliKamni = Racunalnik.pridobiBeleZetone();
		float max = -Float.MAX_VALUE;
		Pozicija zaOdstranit = null;
		for (Pozicija pozicija : beliKamni) {
			float ocena = Racunalnik.oceniPozicijo(pozicija);
			if (ocena > max) {
				max = ocena;
				zaOdstranit = pozicija;
			}
		}
		return zaOdstranit;
	}
} 
