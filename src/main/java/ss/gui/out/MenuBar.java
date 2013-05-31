package ss.gui.out;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JMenuBar menuBar;
	private Frame frame;

	public MenuBar(Frame frame) {
		this.frame = frame;

		menuBar = new JMenuBar();
		JMenu simulatorMenu = new JMenu("Simulador");
		simulatorMenu.setMnemonic(KeyEvent.VK_S);

		JMenuItem newGame = new JMenuItem("Simular");
		JMenuItem newGameWithChart = new JMenuItem("Simular con gráfico");
		JMenuItem newGameWithNoFront = new JMenuItem("Simular sin efectos");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				final Thread sim = new Thread(new Runnable() {
					@Override
					public void run() {
						getFrame().getSimulator().build(
								new SimulationFrameListener(getFrame()));
						getFrame().restart();
						getFrame().getSimulator().start(10);
					}
				});
				sim.start();
			}
		});
		newGameWithNoFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				getFrame().getSimulator().build(
						new SimulationDummyListener());
				// getFrame().restart();
				getFrame().getSimulator().start(10);
			}
		});
		
		newGameWithChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				getFrame().getSimulator().build(
						new SimulationChartListener(getFrame().getSimulator()));
				getFrame().getSimulator().start(10);
			}
		});

		/*
		 * Closes the application
		 */
		JMenuItem close = new JMenuItem("Close");
		close.setMnemonic(KeyEvent.VK_C);
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		simulatorMenu.add(newGame);
		simulatorMenu.add(new JSeparator());
		simulatorMenu.add(newGameWithChart);
		simulatorMenu.add(new JSeparator());
		simulatorMenu.add(newGameWithNoFront);
		simulatorMenu.add(new JSeparator());
		simulatorMenu.add(close);
		menuBar.add(simulatorMenu);
	}

	/**
	 * Returns the menuBar
	 * 
	 * @return JMenuBar
	 */
	public JMenuBar getMenu() {
		return menuBar;
	}

	private Frame getFrame() {
		return frame;
	}

}
