package logika;

public class IgralnoPolje {

	/**
	 * false je crni igralec
	 */
	public boolean crniNaPotezi = false;
	public StanjePolja[][] trenutnoStanje;

	/**
	 * naredimo polje in ga nastavimo da je prazno
	 */
	public IgralnoPolje() {
		trenutnoStanje = new StanjePolja[3][8];
		
		for(int i = 0; i < trenutnoStanje.length; i++) {
			for(int j = 0; j < trenutnoStanje[0].length; j++) {
				trenutnoStanje[i][j] = StanjePolja.PRAZNO;
			}
		}
	}

	/**
	 * Polozi kamen in poglej kaj se zgodi: 1 - mlin, 0 - položeno, -1 - ni bilo uspešno položeno
	 */
	public int poloziKamen(int nivo, int stPolja) {
		if (!trenutnoStanje[nivo][stPolja].equals(StanjePolja.PRAZNO)) {
			return -1;
		}
		if (crniNaPotezi) {
			crniNaPotezi = false;
			trenutnoStanje[nivo][stPolja] = StanjePolja.CRNI;
		} else {
			crniNaPotezi = true;
			trenutnoStanje[nivo][stPolja] = StanjePolja.BELI;
		}

		boolean mlin = naredilMlin(nivo, stPolja);
		return mlin ? 1 : 0;
	}

	/**
	 * premikanje kamnov
	 */
	public int premakniKamen(int zacetniNivo, int zacetnaStPolja, int koncniNivo, int koncnaStPolja, boolean superPower) {
		if(trenutnoStanje[zacetniNivo][zacetnaStPolja].equals(StanjePolja.PRAZNO))
			return -1;
		if(!trenutnoStanje[koncniNivo][koncnaStPolja].equals(StanjePolja.PRAZNO))
			return -1;

		if (superPower || zacetniNivo == koncniNivo && (Math.abs(zacetnaStPolja - koncnaStPolja) == 1 || Math.abs(zacetnaStPolja - koncnaStPolja) == 7)) {
			trenutnoStanje[zacetniNivo][zacetnaStPolja] = StanjePolja.PRAZNO;
			if (crniNaPotezi) {
				trenutnoStanje[koncniNivo][koncnaStPolja] = StanjePolja.CRNI;
				crniNaPotezi = false;
			} else {
				trenutnoStanje[koncniNivo][koncnaStPolja] = StanjePolja.BELI;
				crniNaPotezi = true;
			}
		} else if(zacetniNivo != koncniNivo && zacetnaStPolja %2 == 1 && zacetnaStPolja == koncnaStPolja) {
			trenutnoStanje[zacetniNivo][zacetnaStPolja] = StanjePolja.PRAZNO;
			if (crniNaPotezi) {
				trenutnoStanje[koncniNivo][koncnaStPolja] = StanjePolja.CRNI;
				crniNaPotezi = false;
			} else {
				trenutnoStanje[koncniNivo][koncnaStPolja] = StanjePolja.BELI;
				crniNaPotezi = true;
			}
		} else {
			return -1;
		}
		return naredilMlin(koncniNivo, koncnaStPolja) ? 1 : 0;
	}

	/**
	 * preveri ali poteza naredi mlin
	 */
	public boolean naredilMlin(int nivo, int polje) {
		boolean narediStolpec = preveriStolpce(nivo, polje);
		boolean narediVrstico = preveriVrstico(nivo, polje);

		return narediStolpec || narediVrstico;
	}
	
	private boolean preveriVrstico(int nivo, int polje) {
		if(polje % 2 == 1) {
			int sosed1 = (polje + 1)%8;
			int sosed2 = (polje + 7)%8;
			if(trenutnoStanje[nivo][polje].equals(trenutnoStanje[nivo][sosed1]) && trenutnoStanje[nivo][polje].equals(trenutnoStanje[nivo][sosed2])) {
				return true;
			}
		} else {
			int sosed11 = (polje + 6)%8;
			int sosed12 = (polje + 7)%8;
			
			int sosed21 = (polje + 1)%8;
			int sosed22 = (polje + 2)%8;
			
			if(trenutnoStanje[nivo][polje].equals(trenutnoStanje[nivo][sosed11]) && trenutnoStanje[nivo][polje].equals(trenutnoStanje[nivo][sosed12])) {
				return true;
			}
			if(trenutnoStanje[nivo][polje].equals(trenutnoStanje[nivo][sosed21]) && trenutnoStanje[nivo][polje].equals(trenutnoStanje[nivo][sosed22])) {
				return true;
			}
		}
		return false;
	}
	
	private boolean preveriStolpce(int nivo, int polje) {
		if(polje %2 == 0)
			return false;
		
		if(trenutnoStanje[0][polje].equals(trenutnoStanje[1][polje]) && trenutnoStanje[0][polje].equals(trenutnoStanje[2][polje])) {
			return true;
		}
		
		return false;
	}
	
	public boolean odstraniKamen(int nivo, int polje) {
		if(trenutnoStanje[nivo][polje].equals(StanjePolja.PRAZNO))
			return false;
		if (!crniNaPotezi && trenutnoStanje[nivo][polje].equals(StanjePolja.BELI)) {
			trenutnoStanje[nivo][polje] = StanjePolja.PRAZNO;
			return true;
		}
		if (crniNaPotezi && trenutnoStanje[nivo][polje].equals(StanjePolja.CRNI)) {
			trenutnoStanje[nivo][polje] = StanjePolja.PRAZNO;
			return true;
		}
		return false;
	}
}
