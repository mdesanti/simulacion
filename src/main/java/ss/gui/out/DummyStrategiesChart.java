package ss.gui.out;

import java.util.List;

import ss.api.Project;
import ss.apiImpl.charts.RealTimePlotter;

public class DummyStrategiesChart implements RealTimePlotter {

	@Override
	public void setProjects(List<Project> projects) {

	}

	@Override
	public void removeProject(Project p) {
	}

	@Override
	public void addProject(Project project) {
	}

	@Override
	public void updateTime() {
	}

	@Override
	public void restart(List<Project> projects) {
	}

	@Override
	public RealTimePlotter newInstance(List<Project> projects) {
		return new DummyStrategiesChart();
	}

}
