package ss.gui.out;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ss.api.Simulator;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JMenuBar menuBar;
	private Frame frame;
	private Simulator simulator;
	private GamePanel gamePanel;

	public MenuBar(Frame frame, Simulator simulator, GamePanel gamePanel) {
		this.frame = frame;
		this.simulator = simulator;
		this.gamePanel = gamePanel;

		menuBar = new JMenuBar();
		JMenu simulatorMenu = new JMenu("Simulador");
		simulatorMenu.setMnemonic(KeyEvent.VK_S);

		JMenuItem newGame = new JMenuItem("Simular");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				getSimulator().start();
			}
		});

		simulatorMenu.add(newGame);
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

	private Simulator getSimulator() {
		return simulator;
	}

	private GamePanel getGamePanel() {
		return gamePanel;
	}

}
