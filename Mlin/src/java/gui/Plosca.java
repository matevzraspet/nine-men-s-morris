package gui;

import igrica.Igrica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

/**
 * Pravokotno obmocje, v katerem je narisano igralno polje.
 * <p>
 * <p>
 * X-----X-----X
 * |     |     |
 * | X---X---X |
 * | |   |   | |
 * | | X-X-X | |
 * | | |   | | |
 * X X-X	X-X-X
 * | | |   | | |
 * | | X-X-X | |
 * | |   |   | |
 * | X---X---X |
 * |     |     |
 * X-----X-----X
 **/

public class Plosca extends JPanel implements MouseListener {

	public static Plosca instance;

	public Plosca() {
		super();
		instance = this;
		this.setBackground(Color.white);
		this.addMouseListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	/**
	 * sirina krogca
	 */
	private float circleWidth() {
		return 20f;
	}

	/**
	 * Narisemo krogec na polje (i,j)
	 *
	 * @param g2
	 * @param i
	 * @param j
	 */
	private void paintBELI(Graphics2D g2, int i, int j) {
		float w = circleWidth();
		double x = i - w / 2;
		double y = j - w / 2;
		g2.setColor(Color.white);
		g2.fillOval((int) x, (int) y, (int) w, (int) w);
		g2.setColor(Color.black);
		g2.drawOval((int) x, (int) y, (int) w, (int) w);
	}

	private void paintCRNI(Graphics2D g2, int i, int j) {
		float w = circleWidth();
		double x = i - w / 2;
		double y = j - w / 2;
		g2.setColor(Color.black);
		g2.fillOval((int) x, (int) y, (int) w, (int) w);
	}

	private void paintEmpty(Graphics2D g2, int i, int j) {
		float w = circleWidth();
		double x = i - w / 2;
		double y = j - w / 2;
		g2.setColor(Color.white);
		g2.fillOval((int) x - 1, (int) y - 1, (int) w + 3, (int) w + 3);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		double unit = getHeight() / 8;

		for (int i = 1; i <= 7; i++) {
			if (i == 4) {
				drawVerticalLine(g2, unit, 3 * unit, 4 * unit);
				drawVerticalLine(g2, getHeight() - unit, getHeight() - 3 * unit, 4 * unit);
				drawHorizontalLine(g2, unit, 3 * unit, 4 * unit);
				drawHorizontalLine(g2, getHeight() - unit, getHeight() - 3 * unit, 4 * unit);
				continue;
			}
			drawVerticalLine(g2, i * unit, getHeight() - i * unit, i * unit);
			drawHorizontalLine(g2, unit * i, getWidth() - i * unit, i * unit);
		}
	}

	private void drawVerticalLine(Graphics2D g, double yTop, double yBottom, double x) {
		g.drawLine((int) x, (int) yTop, (int) x, (int) yBottom);
	}

	private void drawHorizontalLine(Graphics2D g, double xLeft, double xRight, double y) {
		g.drawLine((int) xLeft, (int) y, (int) xRight, (int) y);
	}

	/**
	 * Pogledamo naprej fazo igre
	 * Pri pritisku bomo pogledali kdo je na potezi
	 * Nato bomo ali polozili krogec ali pa ga premaknili
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		float unit = getHeight() / 8;

		float xU = x / unit;
		float yU = y / unit;

		x = Math.round(xU);
		y = Math.round(yU);

		int[] fieldCoord = toFieldCoordinates(x, y);

		if (fieldCoord != null) {
			System.out.println(Arrays.toString(fieldCoord));
			Igrica.igra.buttonClick(fieldCoord[0], fieldCoord[1]);
		}
	}

	private int[] toFieldCoordinates(int x, int y) {

		// top row
		if (x == 1 && y == 1) {
			return new int[]{0, 0};
		}
		if (x == 4 && y == 1) {
			return new int[]{1, 0};
		}
		if (x == 7 && y == 1) {
			return new int[]{2, 0};
		}

		// 2 row
		if (x == 2 && y == 2) {
			return new int[]{0, 1};
		}
		if (x == 4 && y == 2) {
			return new int[]{1, 1};
		}
		if (x == 6 && y == 2) {
			return new int[]{2, 1};
		}

		// 3 row
		if (x == 3 && y == 3) {
			return new int[]{0, 2};
		}
		if (x == 4 && y == 3) {
			return new int[]{1, 2};
		}
		if (x == 5 && y == 3) {
			return new int[]{2, 2};
		}

		//4 row
		if (x == 1 && y == 4) {
			return new int[]{7, 0};
		}
		if (x == 2 && y == 4) {
			return new int[]{7, 1};
		}
		if (x == 3 && y == 4) {
			return new int[]{7, 2};
		}
		if (x == 5 && y == 4) {
			return new int[]{3, 2};
		}
		if (x == 6 && y == 4) {
			return new int[]{3, 1};
		}
		if (x == 7 && y == 4) {
			return new int[]{3, 0};
		}

		//5 row
		if (x == 3 && y == 5) {
			return new int[]{6, 2};
		}
		if (x == 4 && y == 5) {
			return new int[]{5, 2};
		}
		if (x == 5 && y == 5) {
			return new int[]{4, 2};
		}

		//6 row
		if (x == 2 && y == 6) {
			return new int[]{6, 1};
		}
		if (x == 4 && y == 6) {
			return new int[]{5, 1};
		}
		if (x == 6 && y == 6) {
			return new int[]{4, 1};
		}

		//7 row
		if (x == 1 && y == 7) {
			return new int[]{6, 0};
		}
		if (x == 4 && y == 7) {
			return new int[]{5, 0};
		}
		if (x == 7 && y == 7) {
			return new int[]{4, 0};
		}

		return null;
	}

	private int[] toBoardCoordinates(int stPolja, int nivo) {
		// top row
		if (stPolja == 0 && nivo == 0) {
			return new int[]{1, 1};
		}
		if (stPolja == 1 && nivo == 0) {
			return new int[]{4, 1};
		}
		if (stPolja == 2 && nivo == 0) {
			return new int[]{7, 1};
		}

		// 2 row
		if (stPolja == 0 && nivo == 1) {
			return new int[]{2, 2};
		}
		if (stPolja == 1 && nivo == 1) {
			return new int[]{4, 2};
		}
		if (stPolja == 2 && nivo == 1) {
			return new int[]{6, 2};
		}

		// 3 row
		if (stPolja == 0 && nivo == 2) {
			return new int[]{3, 3};
		}
		if (stPolja == 1 && nivo == 2) {
			return new int[]{4, 3};
		}
		if (stPolja == 2 && nivo == 2) {
			return new int[]{5, 3};
		}

		//4 row
		if (stPolja == 7 && nivo == 0) {
			return new int[]{1, 4};
		}
		if (stPolja == 7 && nivo == 1) {
			return new int[]{2, 4};
		}
		if (stPolja == 7 && nivo == 2) {
			return new int[]{3, 4};
		}
		if (stPolja == 3 && nivo == 2) {
			return new int[]{5, 4};
		}
		if (stPolja == 3 && nivo == 1) {
			return new int[]{6, 4};
		}
		if (stPolja == 3 && nivo == 0) {
			return new int[]{7, 4};
		}

		//5 row
		if (stPolja == 6 && nivo == 2) {
			return new int[]{3, 5};
		}
		if (stPolja == 5 && nivo == 2) {
			return new int[]{4, 5};
		}
		if (stPolja == 4 && nivo == 2) {
			return new int[]{5, 5};
		}

		//6 row
		if (stPolja == 6 && nivo == 1) {
			return new int[]{2, 6};
		}
		if (stPolja == 5 && nivo == 1) {
			return new int[]{4, 6};
		}
		if (stPolja == 4 && nivo == 1) {
			return new int[]{6, 6};
		}

		//7 row
		if (stPolja == 6 && nivo == 0) {
			return new int[]{1, 7};
		}
		if (stPolja == 5 && nivo == 0) {
			return new int[]{4, 7};
		}
		if (stPolja == 4 && nivo == 0) {
			return new int[]{7, 7};
		}

		return null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void placeBlack(int stevilkaPolja, int nivo) {
		int[] boardCoords = toBoardCoordinates(stevilkaPolja, nivo);

		paintCRNI((Graphics2D) getGraphics(), boardCoords[0] * getWidth() / 8, boardCoords[1] * getWidth() / 8);
	}

	public void placeWhite(int stevilkaPolja, int nivo) {
		int[] boardCoords = toBoardCoordinates(stevilkaPolja, nivo);

		paintBELI((Graphics2D) getGraphics(), boardCoords[0] * getWidth() / 8, boardCoords[1] * getWidth() / 8);
	}

	public void deleteToken(int stevilkaPolja, int nivo) {
		int[] boardCoords = toBoardCoordinates(stevilkaPolja, nivo);

		paintEmpty((Graphics2D) getGraphics(), boardCoords[0] * getWidth() / 8, boardCoords[1] * getWidth() / 8);
	}
}





