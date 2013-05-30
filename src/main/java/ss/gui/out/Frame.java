package ss.gui.out;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.Simulator;
import ss.apiImpl.SimulatorImpl;

import com.google.common.collect.Lists;

/**
 * This class is the one in charge of building the frame for the graphic
 * interface
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {
	private static final int CELL_SIZE = 35;
	private int rows = 25;
	private int cols = 40;
	private JPanel simulationPanel;
	private Sprite sprite;
	private JTextArea idleProgrammers;
	private JTextArea finishedProjects;
	private JTextArea totalTime;
	private Simulator simulator;
	private List<ProjectTimes> projectEstimationTimes = new ArrayList<>();
	private List<ProjectTimes> projectDurationTimes = new ArrayList<>();
	private List<ProjectTimes> projectCosts = new ArrayList<>();
	private List<ProjectTimes> iterations = new ArrayList<>();
	private Queue<Project> projects = new LinkedBlockingDeque<>();
	private List<Color> colors = Lists.newArrayList();
	private int MARGINPROJECTSTOP = 130;
	private int INITIALVALUESTOP = 10;
	private int FINAVALUETOP = 30;

	public Frame() throws IOException {
		this.simulator = new SimulatorImpl(new SimulationListenerImpl(this));

		// Initializes MenuBar
		MenuBar menuBar = new MenuBar(this);
		setJMenuBar(menuBar.getMenu());

		// Initializes basic Frame and MapHash of images
		setTitle("Simulador: Distribución de programadores en una Software Factory");
		setLayout(null);
		// setIconImage(ImageUtils.loadImage("resources/images/ss.jpg"));
		initializeFrame();
		initColors();
	}

	private void initColors() {
		colors.add(Color.BLACK);
		colors.add(Color.BLUE);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.GRAY);
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.GREEN);
		colors.add(Color.MAGENTA);
		colors.add(Color.ORANGE);
		colors.add(Color.PINK);
		colors.add(Color.RED);

	}

	/**
	 * Returns the player's sprite
	 * 
	 * @return Sprite
	 */
	public Sprite getPlayerSprite() {
		return this.sprite;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		for (Project project : projects) {
			g2d.setColor(getColor(project.getId()));
			for (int j = 0; j < project.getProgrammersWorking(); j++) {
				int x = 150 + 25 * j;
				int y = MARGINPROJECTSTOP * (project.getId() + 1) + 30;
				Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 15, 15);
				g2d.fill(circle);
			}
		}
	}

	private Color getColor(int id) {
		id = id % colors.size();
		return colors.get(id);
	}

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

	private class ProjectTimes {
		JTextArea area;
		Project project;

		public ProjectTimes(JTextArea area, Project project) {
			this.area = area;
			this.project = project;
		}

		public Project getProject() {
			return project;
		}

		public JTextArea getArea() {
			return area;
		}
	}

	/**
	 * Initializes the graphic panel with sprites and if already existed one,
	 * first removes it in order to refresh
	 * 
	 * @throws IOException
	 *             If there was a problem loading the image
	 */
	public void initializePanel() throws IOException {
		if (existsPanel())
			remove(simulationPanel);
		simulationPanel = new SimulationPanel();
		simulationPanel.setSize(cols * CELL_SIZE, rows * CELL_SIZE);
		simulationPanel.setLayout(null);
		simulationPanel.setBackground(Color.WHITE);
		add(simulationPanel);

		JTextArea area = new JTextArea("Programadores ociosos: ");
		area.setBounds(50, INITIALVALUESTOP, 150, 20);
		simulationPanel.add(area);
		idleProgrammers = new JTextArea(String.valueOf(simulator
				.getIdleProgrammers()));
		idleProgrammers.setBounds(210, INITIALVALUESTOP, 30, 20);
		simulationPanel.add(idleProgrammers);

		area = new JTextArea("Tiempo de simulación: ");
		area.setBounds(270, INITIALVALUESTOP, 150, 20);
		simulationPanel.add(area);

		totalTime = new JTextArea("0");
		totalTime.setBounds(420, INITIALVALUESTOP, 30, 20);
		simulationPanel.add(totalTime);

		area = new JTextArea("Tiempo máximo de simulación: ");
		area.setBounds(480, INITIALVALUESTOP, 200, 20);
		simulationPanel.add(area);

		area = new JTextArea(String.valueOf(simulator.getSimulationDays()));
		area.setBounds(690, INITIALVALUESTOP, 40, 20);
		simulationPanel.add(area);

		area = new JTextArea("Cantidad proyectos: ");
		area.setBounds(50, FINAVALUETOP, 150, 20);
		simulationPanel.add(area);

		area = new JTextArea(String.valueOf(simulator.getProjects().size()));
		area.setBounds(210, FINAVALUETOP, 30, 20);
		simulationPanel.add(area);

		area = new JTextArea("Proyectos terminados: ");
		area.setBounds(270, FINAVALUETOP, 150, 20);
		simulationPanel.add(area);

		finishedProjects = new JTextArea("0");
		finishedProjects.setBounds(420, FINAVALUETOP, 30, 20);
		simulationPanel.add(finishedProjects);

		for (Project project : simulator.getProjects()) {
			int id = project.getId();

			final JTextArea projectName = new JTextArea("Project "
					+ String.valueOf(id));
			projectName.setBounds(50, MARGINPROJECTSTOP * id + 60, 100, 100);
			simulationPanel.add(projectName);
			Iteration iteration = project.getCurrentIteration();
			JTextArea duration = new JTextArea("Duración: ");
			duration.setBounds(150, MARGINPROJECTSTOP * id + 60, 60, 100);
			simulationPanel.add(duration);
			duration = new JTextArea(String.valueOf(iteration.getDuration()));
			duration.setBounds(220, MARGINPROJECTSTOP * id + 60, 40, 100);
			simulationPanel.add(duration);

			JTextArea estimation = new JTextArea("Estimación: ");
			estimation.setBounds(300, MARGINPROJECTSTOP * id + 60, 80, 100);
			simulationPanel.add(estimation);
			estimation = new JTextArea("0");
			estimation.setBounds(390, MARGINPROJECTSTOP * id + 60, 40, 100);
			simulationPanel.add(estimation);

			JTextArea permittedCost = new JTextArea("Costo permitido: ");
			permittedCost.setBounds(470, MARGINPROJECTSTOP * id + 60, 110, 100);
			simulationPanel.add(permittedCost);
			permittedCost = new JTextArea(String.valueOf(project.getMaxCost()));
			permittedCost.setBounds(590, MARGINPROJECTSTOP * id + 60, 40, 100);
			simulationPanel.add(permittedCost);

			JTextArea actualCost = new JTextArea("Costo actual: ");
			actualCost.setBounds(690, MARGINPROJECTSTOP * id + 60, 100, 100);
			simulationPanel.add(actualCost);
			actualCost = new JTextArea("0");
			actualCost.setBounds(800, MARGINPROJECTSTOP * id + 60, 40, 100);
			simulationPanel.add(actualCost);
			
			JTextArea iterationsQty = new JTextArea("Iteraciones: ");
			iterationsQty.setBounds(900, MARGINPROJECTSTOP * id + 60, 100, 100);
			simulationPanel.add(iterationsQty);
			iterationsQty = new JTextArea("0");
			iterationsQty.setBounds(1000, MARGINPROJECTSTOP * id + 60, 20, 100);
			simulationPanel.add(iterationsQty);
			JTextArea iterationsTotal = new JTextArea("/ "+String.valueOf(project.getIterationsQty()));
			iterationsTotal.setBounds(1020, MARGINPROJECTSTOP * id + 60, 40, 100);
			simulationPanel.add(iterationsTotal);

			projectEstimationTimes.add(new ProjectTimes(estimation, project));
			projectDurationTimes.add(new ProjectTimes(duration, project));
			projectCosts.add(new ProjectTimes(actualCost, project));
			iterations.add(new ProjectTimes(iterationsQty, project));
		}
	}

	/**
	 * Returns true if already existed a Panel and false if not.
	 * 
	 * @return boolean
	 */
	public boolean existsPanel() {
		return simulationPanel != null;
	}

	public void restart() {
		repaint();

		// simulator.start();
		try {
			initializePanel();
		} catch (IOException e) {
			errorWhileReading("restart of simulation.");
		}
		// simulationPanel.setPixeslPerStep(5);
		initializeFrame();

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

	public void updateIdleProgrammers(int qty) {
		idleProgrammers.setText(String.valueOf(qty));

	}

	public void updateTime(int time) {
		totalTime.setText(String.valueOf(time));

	}

	public void updateFinishedProjects(int qty) {
		finishedProjects.setText(String.valueOf(qty));
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public void updateIterationEstimate(Project project) {
		for (ProjectTimes projectTime : projectEstimationTimes) {
			if (projectTime.getProject().equals(project)) {
				if (project.getCurrentIteration().getEstimate() == Integer.MAX_VALUE) {
					projectTime.getArea().setText("-");
				} else {
					projectTime.getArea().setText(
							String.valueOf(project.getCurrentIteration()
									.getEstimate()));
				}
				return;
			}

		}

	}

	/**
	 * Returns the actualLevel
	 * 
	 * @return Level
	 */

	public void updateIterationDuration(Project project) {
		for (ProjectTimes projectTime : projectDurationTimes) {
			if (projectTime.getProject().equals(project)) {
				projectTime.getArea().setText(
						String.valueOf(project.getCurrentIteration()
								.getDuration()));
				break;
			}

		}
		
		for (ProjectTimes projectTime : iterations) {
			if (projectTime.getProject().equals(project)) {
				projectTime.getArea().setText(
						String.valueOf(project.getIterationsLeft()));
				return;
			}

		}

	}

	public void updateProgrammersQty(Project project) {
		projects.add(project);
		repaint();
	}

	public void updateCost(Project project) {
		for (ProjectTimes projectTime : projectCosts) {
			if (projectTime.getProject().equals(project)) {
				projectTime.getArea().setText(
						String.valueOf(project.getTotalCost()));
				return;
			}

		}

	}

	private class SimulationPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
			for (ProjectTimes projectTime : iterations) {
				Project project = projectTime.project;
				int x = projectTime.getArea().getX();
				int y = projectTime.getArea().getY();
				if (project.finished()) {
					try {
						Image image = ImageIO.read(new File(
								"resources/images/tick.png"));
						g.drawImage(image, x + 80, y - 8, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Image image = ImageIO.read(new File(
								"resources/images/cross.png"));
						g.drawImage(image, x + 80, y - 10, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}