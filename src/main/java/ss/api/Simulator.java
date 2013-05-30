package ss.api;

import java.util.List;

import ss.gui.out.SimulationListener;

public interface Simulator {

	void start(int times);

	void build(SimulationListener listener);

	List<Project> getProjects();

	int getIdleProgrammers();

	int getTotalCost();

	int getFinishedProjects();
	
	String getStrategy();
	
	int getSimulationDays();

}
