package ss;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.ui.RefineryUtilities;

import ss.gui.out.Frame;

public class Start {

	public static void main(String[] args) {
		Frame frame = null;
		try {
			frame = new Frame();
		} catch (IOException e) {
			errorWhileReading("executing simulator.");
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	private static void errorWhileReading(String string) {
		JOptionPane.showMessageDialog(null, "Error at " + string,
				"Simulator Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

}
