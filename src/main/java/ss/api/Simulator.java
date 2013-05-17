package ss.api;

import java.util.List;

public interface Simulator {

	void start();

	void build();

	List<Project> getProjects();

	int getIdleProgrammers();

	int getTotalCost();

	int getFinishedProjects();
	
	String getStrategy();
	
	int getSimulationDays();

}
