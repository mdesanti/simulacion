package ss.api;

import java.util.List;

import ss.gui.out.SimulationListener;

public interface Simulator {

	void start(int times);

	void build(SimulationListener listener, int strategy);

	List<Project> getProjects();

	int getIdleProgrammers();

	int getTotalCost();

	int getFinishedProjects();
	
	int getSimulationDays();

}
