package ss.api;

import java.util.List;

import ss.apiImpl.charts.RealTimePlotter;
import ss.gui.SimulationListener;

public interface Simulator {

	void start(int times);

	void build(SimulationListener listener, int strategy, RealTimePlotter plotter);

	List<Project> getProjects();

	int getIdleProgrammers();

	int getSimulationDays();
	
	int getStrategyID();

}
