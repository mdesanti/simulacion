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
//		repaint();
	
	}
	
	@Override
	public void updateIterationDuration(Project project) {
		frame.updateIterationDuration(project);
//		repaint();
	}

	@Override
	public void updateTime(int time) {
		
		frame.updateTime(time);
		repaint();
	}

	@Override
	public void updateWorkingProgrammers(Project project) {
		frame.updateProgrammersQty(project);
	}

	@Override
	public void updateCost(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateIterationEstimate(Project project) {
		frame.updateIterationEstimate(project);
		repaint();

	}
	
	@Override
	public void updateFinishedProjects(int qty) {
		frame.updateFinishedProjects(qty);
		repaint();
		
	}
	
	private void repaint() {
		frame.repaint();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("Error en thread");
			System.exit(1);
		}
	}

}
