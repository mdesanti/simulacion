package ss.apiImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.api.Simulator;

public class SimulatorImpl implements Simulator {

	private int simulationDays;
	private List<Project> projects;
	private int idleProgrammers;
	private ReasignationStrategy strategy;

	public SimulatorImpl(int simulationDays, List<Project> projects,
			int programmersQty, ReasignationStrategy strategy) {
		this.simulationDays = simulationDays;
		this.projects = projects;
		this.idleProgrammers = programmersQty;
		this.strategy = strategy;
	}

	public void addProject(Project project) {
	}

	public void start() {
		int today = 1;
		int projectsFinished = 0;
		boolean finished = false;
		while (today < simulationDays && !finished) {
			Collections.sort(projects, new ProjectComparator());
			for (Project project : projects) {
				if (!project.finished()) {
					Iteration iteration = project.getCurrentIteration();
					if (iteration.isDelayed()) {
						idleProgrammers -= strategy.reasing(project, projects,
								idleProgrammers);
					} else if (iteration.finished()) {
						project.nextIteration();
					}
					iteration.decreaseLastingDays();
				} else {
					projectsFinished++;
				}
			}
			today++;
			if (projectsFinished == projects.size()) {
				finished = true;
			}
		}

	}

	private class ProjectComparator implements Comparator<Project> {
		@Override
		public int compare(Project p1, Project p2) {
			int diffP1 = p1.getCurrentIteration().getDuration()
					- p1.getCurrentIteration().getEstimate();
			int diffP2 = p2.getCurrentIteration().getDuration()
					- p2.getCurrentIteration().getEstimate();
			boolean delayedP1 = p1.getCurrentIteration().isDelayed();
			boolean delayedP2 = p2.getCurrentIteration().isDelayed();
			if (delayedP1 && !delayedP2) {
				return 1;
			} else if (!delayedP1 && delayedP2) {
				return -1;
			} else {
				return diffP1 - diffP2;
			}

		}
	}

}
