package ss.gui.out;

import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Panel al que se le pueden agregar, eliminar y mover im??genes. Al mover una imagen, esta se desplaza hasta las coordenadas destino indicadas. La velocidad con la que se mueven las im??genes puede ser parametrizada. <p> Tener en cuenta que el orden en que las im??genes son pintadas coincide con el orden en que las mismas son agregadas al panel. </p>
 */
public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int pixelsPerStep = 10;
	private int sleepTime = 10;

	private List<Sprite> sprites;

	/**
	 * Crea un nuevo panel con las dimensiones indicadas.
	 * 
	 * @param width Ancho en pixeles del panel.
	 * @param height Altura en pixeles del panel.
	 */
	public GamePanel(int width, int height) {
		setLayout(null);
		setSize(width, height);
		sprites = new LinkedList<Sprite>();
	}

	/**
	 * Agrega al panel una nueva imagen.
	 * 
	 * @param sprite Imagen que se quiere agregar al panel.
	 */
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
		sync();
	}

	/**
	 * Quita del panel una imagen.
	 * 
	 * @param sprite Imagen que se desea eliminar del panel.
	 */
	public void removeSprite(Sprite sprite) {
		sprites.remove(sprite);
		sync();
	}

	/**
	 * Mueve animadamente una imagen.
	 * <p>
	 * La invocaci??n a este m??todo es bloqueante hasta que la imagen llega a las coordenadas destino.
	 * </p>
	 * 
	 * @param sprite Imagen que se desea mover.
	 * @param position Nueva posici??n a donde se debe mover el sprite.
	 */
	public void moveSprite(Sprite sprite, Position position) {
		int xdistance = Math.abs(position.getX() - sprite.getPosition().getX());
		int ydistance = Math.abs(position.getY() - sprite.getPosition().getY());

		while (xdistance > 0 || ydistance > 0) {
			int dx = position.getX() - sprite.getPosition().getX();
			int dy = position.getY() - sprite.getPosition().getY();
			double alpha = Math.atan(ydistance / (double) xdistance);

			double xstep = Math.abs(pixelsPerStep * Math.cos(alpha)); 
			double ystep = Math.abs(pixelsPerStep * Math.sin(alpha));
			
			xstep = (int) Math.ceil(Math.min(xstep, xdistance));
			ystep = (int) Math.ceil(Math.min(ystep, ydistance));
			sprite.setPosition(new Position(sprite.getPosition().getX() + ((int) (Math.signum(dx) * xstep)), sprite.getPosition().getY()
					+ ((int) (Math.signum(dy) * ystep))));
			xdistance -= xstep;
			ydistance -= ystep;
			sync();
			sleep(sleepTime);
		}
	}

	/**
	 * Mueve una imagen hacia una nueva posici??n.
	 * 
	 * @param sprite Imagen que se desea mover.
	 * @param position Nueva posici??n a donde se debe mover el sprite.
	 */
	public void translateSprite(Sprite sprite, Position position) {
		sprite.setPosition(position);
		sync();
	}

	/**
	 * Modifica la imagen asociada a un sprite.
	 * 
	 * @param sprite Sprite al cual se le quiere modificar la imagen.
	 * @param image Nueva imagen que debe tener el sprite.
	 */
	public void changeSpriteImage(Sprite sprite, Image image) {
		sprite.changeImage(image);
		sync();
	}

	/**
	 * Modifica la cantidad de pixels que las im??genes deben avanzar en cada paso cuando son desplazados.
	 */
	public void setPixeslPerStep(int pixeslPerStep) {
		this.pixelsPerStep = pixeslPerStep;
	}

	/**
	 * Modifica la cantidad de milisegundos que se deben esperar entre cada movimiento de una imagen.
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		for (Sprite sprite : sprites) {
			g.drawImage(sprite.getImage(), sprite.getPosition().getX(), sprite.getPosition().getY(), null);
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.exit(1);
		}
	}

	private void sync() {
		paintImmediately(0, 0, getWidth(), getHeight());
	}
}