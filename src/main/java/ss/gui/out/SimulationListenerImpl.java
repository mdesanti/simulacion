package ss.gui.out;

import ss.api.Project;

public class SimulationListenerImpl implements SimulationListener {

	private Frame frame;

	public SimulationListenerImpl(Frame frame) {
		this.frame = frame;
	}

	@Override
	public void updateIdleProgrammers(int qty) {
		frame.updateIdleProgrammers(qty);
		frame.paint();
	}

	@Override
	public void updateTime(int time) {
		
		frame.updateTime(time);
		frame.repaint();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Error en thread");
			System.exit(1);
		}
	}

	@Override
	public void updateWorkingProgrammers(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCost(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateIterationEstimate(Project project) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void updateFinishedProjects(int qty) {
		frame.updateFinishedProjects(qty);
		frame.paint();
		
	}

}
