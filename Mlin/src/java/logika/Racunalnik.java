package logika;

import igrica.Igrica;

import java.util.LinkedList;
import java.util.List;

public class Racunalnik {
	// vrednosti polj, katere naj racunalnik preferira
	private static final float vrednostKotov = 1;
	private static final float vrednostSrednjihKotov = 2;
	private static final float vrednostTrojisc = 3;
	private static final float vrednostKrizisc = 4;

	public static IgralnoPolje polje;
	//racunalnikovi zetoni
	private static StanjePolja mojiZetoni = StanjePolja.CRNI;
	//igralcevi zetoni
	private static StanjePolja nasprotnik = StanjePolja.BELI;

	/**
	 * funkcija ki najde najboljsi premik zetona
	 *
	 * @param superPower ali smo v 3. fazi ali ne
	 * @return najboljsi premik
	 */
	public static Premik pridobiNaslednjiPremik(boolean superPower) {
		List<Premik> premikiZaCrni = pridobiMoznePremikeCrni(superPower);
		float max = -Float.MAX_VALUE;
		Premik najPrem = null;

		for (Premik premik : premikiZaCrni) {
			Pozicija s = premik.start;
			Pozicija e = premik.end;
			polje.trenutnoStanje[e.nivo][e.stPolja] = mojiZetoni;
			polje.trenutnoStanje[s.nivo][s.stPolja] = StanjePolja.PRAZNO;
			if (Igrica.polje.naredilMlin(e.nivo, e.stPolja)) {
				polje.trenutnoStanje[e.nivo][e.stPolja] = StanjePolja.PRAZNO;
				polje.trenutnoStanje[s.nivo][s.stPolja] = mojiZetoni;
				return premik;
			}
			float ocena = pridobiOcenoZaPremik(superPower);
			polje.trenutnoStanje[e.nivo][e.stPolja] = StanjePolja.PRAZNO;
			polje.trenutnoStanje[s.nivo][s.stPolja] = mojiZetoni;
			if (ocena > max) {
				max = ocena;
				najPrem = premik;
			}
		}
		return najPrem;
	}

	/**
	 * najdi najboljso potezo
	 * @return najboljsa poteza
	 */
	public static Pozicija pridobiNaslednjoPotezo() {
		List<Pozicija> moznePoteze = pridobiMoznePoteze();
		float max = -Float.MAX_VALUE;
		Pozicija najPot = null;

		for (Pozicija pozicija : moznePoteze) {
			polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = mojiZetoni;
			if (Igrica.polje.naredilMlin(pozicija.nivo, pozicija.stPolja)) {
				polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = StanjePolja.PRAZNO;
				return pozicija;
			}
			float ocena = pridobiOcenoZaPotezo();
			polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = StanjePolja.PRAZNO;

			if (ocena > max) {
				max = ocena;
				najPot = pozicija;
			}
		}
		return najPot;
	}

	/**
	 * funkcija za ocenjevanje premikov
	 */
	private static float pridobiOcenoZaPremik(boolean superPower) {
		return pridobiOcenoZaPremikJaz(1, superPower);
	}

	/**
	 * racunalnikov del rekurzije min max algoritma
	 */
	private static float pridobiOcenoZaPremikJaz(int globina, boolean superPower) {
		if (globina == 0) {
			return oceniSituacijo();
		}
		List<Premik> potezeZaCrnega = pridobiMoznePremikeCrni(superPower);
		float maxOcena = -Float.MAX_VALUE;

		for (Premik premik : potezeZaCrnega) {
			Pozicija s = premik.start;
			Pozicija e = premik.end;
			polje.trenutnoStanje[e.nivo][e.stPolja] = mojiZetoni;
			polje.trenutnoStanje[s.nivo][s.stPolja] = StanjePolja.PRAZNO;
			if (Igrica.polje.naredilMlin(e.nivo, e.stPolja)) {
				polje.trenutnoStanje[e.nivo][e.stPolja] = StanjePolja.PRAZNO;
				polje.trenutnoStanje[s.nivo][s.stPolja] = mojiZetoni;
				return 100;
			}
			float ocena = pridobiOcenoZaPremikNasp(globina, superPower);
			polje.trenutnoStanje[e.nivo][e.stPolja] = StanjePolja.PRAZNO;
			polje.trenutnoStanje[s.nivo][s.stPolja] = mojiZetoni;
			if (ocena > maxOcena) {
				maxOcena = ocena;
			}
		}
		return maxOcena;
	}

	/**
	 * racunalnik simulira kako bo igral igralec
	 */
	private static float pridobiOcenoZaPremikNasp(int globina, boolean superPower) {
		List<Premik> potezeZaBelega = pridobiMoznePremikeBeli(superPower);
		float minOcena = Float.MAX_VALUE;

		for (Premik premik : potezeZaBelega) {
			Pozicija s = premik.start;
			Pozicija e = premik.end;
			polje.trenutnoStanje[e.nivo][e.stPolja] = nasprotnik;
			polje.trenutnoStanje[s.nivo][s.stPolja] = StanjePolja.PRAZNO;
			if (Igrica.polje.naredilMlin(e.nivo, e.stPolja)) {
				polje.trenutnoStanje[e.nivo][e.stPolja] = StanjePolja.PRAZNO;
				polje.trenutnoStanje[s.nivo][s.stPolja] = nasprotnik;
				return -100;
			}
			float ocena = pridobiOcenoZaPremikJaz(globina - 1, superPower);
			polje.trenutnoStanje[e.nivo][e.stPolja] = StanjePolja.PRAZNO;
			polje.trenutnoStanje[s.nivo][s.stPolja] = nasprotnik;
			if (ocena < minOcena) {
				minOcena = ocena;
			}
		}
		return minOcena;
	}

	/**
	 * funkcija za ocenjevanje polaganja zetona
	 */
	private static float pridobiOcenoZaPotezo() {
		return pridobiOcenoZaPotezoJaz(1);
	}

	/**
	 * funkcija za racunalnikov del iskanja poteze
	 */
	private static float pridobiOcenoZaPotezoJaz(int globina) {
		if (globina == 0) {
			return oceniSituacijo();
		}
		List<Pozicija> moznePoteze = pridobiMoznePoteze();

		float maxOcena = -Float.MAX_VALUE;
		for (Pozicija pozicija : moznePoteze) {
			polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = mojiZetoni;
			if (Igrica.polje.naredilMlin(pozicija.nivo, pozicija.stPolja)) {
				polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = StanjePolja.PRAZNO;
				return 100;
			}
			float ocena = pridobiOcenoZaPotezoNasp(globina);
			if (ocena > maxOcena) {
				maxOcena = ocena;
			}

			polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = StanjePolja.PRAZNO;
		}
		return maxOcena;
	}

	/**
	 * racunalnik simulira kako bo igralec postavil zeton
	 */
	private static float pridobiOcenoZaPotezoNasp(int globina) {
		List<Pozicija> moznePoteze = pridobiMoznePoteze();

		float minOcena = Float.MAX_VALUE;

		for (Pozicija pozicija : moznePoteze) {
			polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = nasprotnik;
			if (Igrica.polje.naredilMlin(pozicija.nivo, pozicija.stPolja)) {
				polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = StanjePolja.PRAZNO;
				return -100;

			}
			float ocena = pridobiOcenoZaPotezoJaz(globina - 1);
			if (ocena < minOcena) {
				minOcena = ocena;
			}

			polje.trenutnoStanje[pozicija.nivo][pozicija.stPolja] = StanjePolja.PRAZNO;
		}
		return minOcena;
	}

	/**
	 * vrne oceno plosce glede na postavitev zetonov, racunalnikovi zetoni prispevajo k oceni, igralcevi pa odvzemajo
	 */
	private static float oceniSituacijo() {
		List<Pozicija> crniZetoni = pridobiCrneZetone();
		List<Pozicija> beliZetoni = pridobiBeleZetone();

		float score = 0;

		if (mojiZetoni.equals(StanjePolja.CRNI)) {
			for (Pozicija pozicija : crniZetoni) {
				score += oceniPozicijo(pozicija);
			}
			for (Pozicija pozicija : beliZetoni) {
				score -= oceniPozicijo(pozicija);
			}
		} else {
			for (Pozicija pozicija : crniZetoni) {
				score -= oceniPozicijo(pozicija);
			}
			for (Pozicija pozicija : beliZetoni) {
				score += oceniPozicijo(pozicija);
			}
		}
		return score;
	}

	/**
	 * pridobi pozicije crnih zetonov
	 */
	public static List<Pozicija> pridobiCrneZetone() {
		List<Pozicija> out = new LinkedList<>();

		for (int nivo = 0; nivo < polje.trenutnoStanje.length; nivo++) {
			for (int stPolja = 0; stPolja < polje.trenutnoStanje[0].length; stPolja++) {
				if (polje.trenutnoStanje[nivo][stPolja].equals(StanjePolja.CRNI))
					out.add(new Pozicija(stPolja, nivo));
			}
		}
		return out;
	}

	/**
	 * pridobi pozicije belih zetonov
	 */
	public static List<Pozicija> pridobiBeleZetone() {
		List<Pozicija> out = new LinkedList<>();

		for (int nivo = 0; nivo < polje.trenutnoStanje.length; nivo++) {
			for (int stPolja = 0; stPolja < polje.trenutnoStanje[0].length; stPolja++) {
				if (polje.trenutnoStanje[nivo][stPolja].equals(StanjePolja.BELI))
					out.add(new Pozicija(stPolja, nivo));
			}
		}
		return out;
	}

	/**
	 * oceni pozicijo enega zetona
	 */
	public static float oceniPozicijo(Pozicija poz) {
		if (poz.nivo == 0 || poz.nivo == 2) {
			if (poz.stPolja % 2 == 0) {
				return vrednostKotov;
			} else {
				return vrednostTrojisc;
			}
		} else {
			if (poz.stPolja % 2 == 0) {
				return vrednostSrednjihKotov;
			} else {
				return vrednostKrizisc;
			}
		}
	}

	/**
	 * vrne list vseh prostih polj
	 */
	private static List<Pozicija> pridobiMoznePoteze() {
		List<Pozicija> out = new LinkedList<>();

		for (int nivo = 0; nivo < polje.trenutnoStanje.length; nivo++) {
			for (int stPolja = 0; stPolja < polje.trenutnoStanje[0].length; stPolja++) {
				if (polje.trenutnoStanje[nivo][stPolja].equals(StanjePolja.PRAZNO))
					out.add(new Pozicija(stPolja, nivo));
			}
		}
		return out;
	}

	/**
	 * vrne list vseh moznih premikov za crnega
	 * @param superPower ali smo v 3. fazi ali ne
	 */
	private static List<Premik> pridobiMoznePremikeCrni(boolean superPower) {
		List<Pozicija> crni = pridobiCrneZetone();
		return pridobiPremikeIzPozicij(crni, superPower);
	}

	/**
	 * vrne list vseh moznih premikov za belega
	 * @param superPower ali smo v 3. fazi ali ne
	 */
	private static List<Premik> pridobiMoznePremikeBeli(boolean superPower) {
		List<Pozicija> beli = pridobiBeleZetone();
		return pridobiPremikeIzPozicij(beli, superPower);
	}

	/**
	 * pridobi vse mozne premike iz danih pozicij, pomozna funkcija, ki je enaka za oba igralca
	 */
	private static List<Premik> pridobiPremikeIzPozicij(List<Pozicija> pozicije, boolean superPower) {
		List<Premik> out = new LinkedList<>();

		for (Pozicija pozicija : pozicije) {
			if (superPower) {
				List<Pozicija> moznePoz = pridobiMoznePoteze();
				for (Pozicija end : moznePoz) {
					out.add(new Premik(pozicija, end));
				}
				continue;
			}

			if (polje.trenutnoStanje[pozicija.nivo][(pozicija.stPolja + 1) % 8].equals(StanjePolja.PRAZNO)) {
				out.add(new Premik(pozicija, new Pozicija((pozicija.stPolja + 1) % 8, pozicija.nivo)));
			}
			if (polje.trenutnoStanje[pozicija.nivo][(pozicija.stPolja + 7) % 8].equals(StanjePolja.PRAZNO)) {
				out.add(new Premik(pozicija, new Pozicija((pozicija.stPolja + 7) % 8, pozicija.nivo)));
			}
			if (pozicija.stPolja % 2 == 1) {
				if (pozicija.nivo + 1 < 3 && polje.trenutnoStanje[pozicija.nivo + 1][pozicija.stPolja].equals(StanjePolja.PRAZNO)) {
					out.add(new Premik(pozicija, new Pozicija(pozicija.stPolja, pozicija.nivo + 1)));
				}
				if (pozicija.nivo - 1 >= 0 && polje.trenutnoStanje[pozicija.nivo - 1][pozicija.stPolja].equals(StanjePolja.PRAZNO)) {
					out.add(new Premik(pozicija, new Pozicija(pozicija.stPolja, pozicija.nivo - 1)));
				}
			}
		}
		return out;
	}
}
