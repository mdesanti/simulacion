package ss.apiImpl;

import ss.api.Project;
import ss.api.Simulator;

public class SimulatorImpl implements Simulator {
	
	long simulationDays;

	public SimulatorImpl(long simulationDays) {
		super();
		this.simulationDays = simulationDays;
	}

	public void addProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		long today = 1;
		
		while(today < simulationDays) {
			
		}
		
	}

}
