package ss.gui.out;

import ss.api.Project;

public interface SimulationListener {

	public void updateIdleProgrammers(int qty);

	public void updateTime(int time);

	public void updateWorkingProgrammers(Project project);

	public void updateCost(Project project);

	public void updateIterationEstimate(Project project);

	public void updateFinishedProjects(int qty);

	public void updateIterationDuration(Project project);

}
