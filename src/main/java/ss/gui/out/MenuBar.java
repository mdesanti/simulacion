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
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		final Thread sim = new Thread(new Runnable() {
			@Override
			public void run() {
				getFrame().getSimulator().build();
				getFrame().restart();
				getFrame().getSimulator().start();
			}
		});
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sim.start();
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