package ss.apiImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.api.Simulator;

public class SimulatorImpl implements Simulator {
	
	private long simulationDays;
	private List<Project> projects;
	private int programmers;
	private ReasignationStrategy strategy;
	

	public SimulatorImpl(long simulationDays, List<Project> projects, int programmersQty, ReasignationStrategy strategy) {
		this.simulationDays = simulationDays;
		this.projects = projects;
		this.programmers = programmersQty;
		this.strategy = strategy;
	}

	public void addProject(Project project) {
	}

	public void start() {
		long today = 1;
		while(today < simulationDays) {
			Collections.sort(projects, new ProjectComparator());
			for(Project project: projects) {
				Iteration iteration = project.getCurrentIteration();
				if(iteration.isDelayed()) {
					strategy.reasing(project, from)
				}
			}
		}
		
	}
	
	private class ProjectComparator implements Comparator<Project> {
		@Override
		public int compare(Project p1, Project p2) {
			int diffP1 = p1.getCurrentIteration().getDuration() - p1.getCurrentIteration().getEstimate();
		}
	}

}
