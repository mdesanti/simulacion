package ss.gui.out;

import ss.api.Project;

public class SimulationFrameListener implements SimulationListener {

	private Frame frame;

	public SimulationFrameListener(Frame frame) {
		this.frame = frame;
	}

	@Override
	public void updateIdleProgrammers(int qty) {
		frame.updateIdleProgrammers(qty);
		 repaint();

	}

	@Override
	public void updateIterationDuration(Project project) {
		frame.updateIterationDuration(project);
		 repaint();
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
		frame.updateCost(project);
		repaint();

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
	
	@Override
	public void reset() {
		frame.restart();
		
	}

	private void repaint() {
		frame.repaint();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Error en thread");
			System.exit(1);
		}
	}

	@Override
	public void removeProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProject(Project project) {
		// TODO Auto-generated method stub
		
	}

}
