package ss.gui.out;

import ss.api.Project;
import ss.api.Simulator;
import ss.apiImpl.charts.StrategiesChart;

public class SimulationChartListener implements SimulationListener {

	private StrategiesChart chart;
	private Simulator simulator;

	public SimulationChartListener(Simulator simulator) {
		this.simulator = simulator;
	}

	public void setChart(StrategiesChart chart) {
		this.chart = chart;
	}

	@Override
	public void updateIdleProgrammers(int qty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWorkingProgrammers(Project project) {
		chart.updateWorkingProgrammers(project);
		wait(100);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void updateIterationDuration(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		chart.restart(simulator.getProjects());

	}

	@Override
	public void removeProject(Project project) {
		chart.removeProject(project);
	}

	@Override
	public void addProject(Project project) {
		chart.addProject(project);
	}
	
	private void wait(int time){
//		try {
//			Thread.sleep(time);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
