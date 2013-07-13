package ss.gui;

import ss.api.Project;

public class SimulationListenerImpl implements SimulationListener {

	private Frame frame;

	public SimulationListenerImpl(Frame frame) {
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
	public void updateInvestment(Project project) {
		frame.updateCost(project);
		repaint();

	}

	@Override
	public void updateIterationEstimate(Project project) {
		frame.updateIterationEstimate(project);
		repaint();

	}

	@Override
	public void addProject(Project project) {
		frame.addProject(project);
		repaint();
	}

	@Override
	public void updateFinishedProjects(int qty) {
		frame.updateFinishedProjects(qty);
		repaint();

	}

	@Override
	public void finishProject(Project project) {
		frame.finishProject(project);
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

}
