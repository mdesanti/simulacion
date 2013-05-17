package ss.gui.out;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import ss.api.Project;
import ss.api.Simulator;
import ss.apiImpl.SimulatorImpl;

/**
 * This class is the one in charge of building the frame for the graphic
 * interface
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {
	private static final int CELL_SIZE = 30;
	private int rows = 25;
	private int cols = 40;
	private GamePanel gamePanel;
	private Sprite sprite;
	private List<Sprite> sprites;
	private List<Project> projects;
	private JTextArea idleProgrammers;
	private JTextArea totalTime;
	private Simulator simulator;
	private HashMap<Class<?>, Image> classMap;

	public Frame() throws IOException {
		this.simulator = new SimulatorImpl();

		// Initializes MenuBar
		MenuBar menuBar = new MenuBar(this);
		setJMenuBar(menuBar.getMenu());

		// Initializes basic Frame and MapHash of images
		setTitle("Simulador: Distribución de programadores en una Software Factory");
		setLayout(null);
		// setIconImage(ImageUtils.loadImage("resources/images/player.png"));
		initializeFrame();
		initializeClassMap();
	}

	/**
	 * Returns the player's sprite
	 * 
	 * @return Sprite
	 */
	public Sprite getPlayerSprite() {
		return this.sprite;
	}

	private void initializeClassMap() throws IOException {
		// classMap = new HashMap<Class<?>, Image>();
		// classMap.put(Key.class,
		// ImageUtils.loadImage("resources/images/key.png"));
		// classMap.put(BreakableWall.class,
		// ImageUtils.loadImage("resources/images/wall.png"));
		// classMap.put(Wall.class,
		// ImageUtils.loadImage("resources/images/wall.png"));
		// classMap.put(Door.class,
		// ImageUtils.loadImage("resources/images/door.png"));
		// classMap.put(Transporter.class,
		// ImageUtils.loadImage("resources/images/teleporter.png"));
		// classMap.put(Coin.class,
		// ImageUtils.loadImage("resources/images/coin.png"));
		// classMap.put(Destiny.class,
		// ImageUtils.loadImage("resources/images/target.png"));
	}

	// private Image getImage(Cell cell) {
	// Image image = classMap.get(cell.getClass());
	// if (cell instanceof BreakableWall) {
	// String s = String.valueOf(((BreakableWall) cell).getLeftTries());
	// return ImageUtils.drawString(image, s, Color.BLACK);
	// }
	// if (cell instanceof Paintable)
	// image = ImageUtils.colorize(image,
	// Colors.values()[((Paintable) cell).getID()].getColor());
	// return image;
	// }

	/*
	 * Initializes the Frame according to a column and a row and positions it in
	 * the center of the screan
	 */
	private void initializeFrame() {
		setSize(cols * CELL_SIZE - 54, rows * CELL_SIZE - 6);
		Dimension screan = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window = this.getSize();
		setLocation((screan.width - window.width) / 2,
				(screan.height - window.height) / 2);
		paintAll(this.getGraphics());
	}

	/**
	 * Initializes the graphic panel with sprites and if already existed one,
	 * first removes it in order to refresh
	 * 
	 * @throws IOException
	 *             If there was a problem loading the image
	 */
	public void initializePanel() throws IOException {
		if (existsGamePanel())
			remove(gamePanel);
		gamePanel = new GamePanel(cols * CELL_SIZE, rows * CELL_SIZE);
		gamePanel.setBackground(Color.WHITE);
		add(gamePanel);

		JTextArea area = new JTextArea("Programadores ociosos: ");
		area.setBounds(50, 10, 150, 20);
		gamePanel.add(area);
		idleProgrammers = new JTextArea("" + simulator.getIdleProgrammers());
		idleProgrammers.setBounds(210, 10, 30, 20);
		gamePanel.add(idleProgrammers);

		area = new JTextArea("Tiempo de simulación: ");
		area.setBounds(270, 10, 150, 20);
		gamePanel.add(area);

		totalTime = new JTextArea("0");
		totalTime.setBounds(420, 10, 30, 20);
		gamePanel.add(totalTime);

		area = new JTextArea("Tiempo máximo de simulación: ");
		area.setBounds(480, 10, 200, 20);
		gamePanel.add(area);

		area = new JTextArea("" + simulator.getSimulationDays());
		area.setBounds(690, 10, 40, 20);
		gamePanel.add(area);
		for (Project project : simulator.getProjects()) {
			int id = project.getId();
			final JTextArea projectName = new JTextArea("Project " + id);
			projectName.setBounds(50, 80 * id + 60, 100, 80);
			gamePanel.add(projectName);
		}
	}

	/**
	 * Returns true if already existed a Game Panel and false if not.
	 * 
	 * @return boolean
	 */
	public boolean existsGamePanel() {
		return gamePanel != null;
	}

	/**
	 * Searches a sprite in the sprites list and returns it, if it doesn't found
	 * it returns null
	 * 
	 * @param position
	 * @return Sprite
	 */
	public Sprite getSprite(Position position) {
		for (Sprite s : sprites)
			if (s.getPosition().equals(position))
				return s;
		return null;
	}

	/**
	 * Removes a sprite in a point from the game panel
	 * 
	 * @param point
	 */
	public void removeSprite(Point point) {
		Position pos = new Position(CELL_SIZE * (point.y - 1), CELL_SIZE
				* (point.x - 1));
		Sprite s = getSprite(pos);
		gamePanel.removeSprite(s);
	}

	public void restart() {
		repaint();
		simulator.build();
		// simulator.start();
		try {
			initializePanel();
		} catch (IOException e) {
			errorWhileReading("restart of simulation.");
		}
		gamePanel.setPixeslPerStep(5);
		initializeFrame();
		simulator.start();

	}

	/**
	 * Creates a panel of error with a message starting in "Error at ".
	 * 
	 * @param string
	 */
	public void errorWhileReading(String string) {
		JOptionPane.showMessageDialog(null, "Error at " + string,
				"Simulator Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	/**
	 * Returns the game to the main menu with a size of 12x12 and removes the
	 * Game Panel if it exists. Sets the actualLevel in null.
	 */
	public void refreshToMenu() {
		// actualLevel = null;
		// rows = 12;
		// cols = 12;
		// if (existsGamePanel())
		// remove(gamePanel);
		// repaint();
		// setTitle("Slip & Slide");
		// initializeFrame();
	}

	/**
	 * Returns the actualLevel
	 * 
	 * @return Level
	 */

	// public Level getActualLevel() {
	// return this.actualLevel;
	// }
}