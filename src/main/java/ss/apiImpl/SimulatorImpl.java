package ss.apiImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
		int today = 0;
		int projectsFinished = 0;
		int totalProjects = projects.size();
		boolean finished = false;
		int totalCost = 0;
		if (strategy.isSwitchStrategyOnly()) {
			distributeProgrammers();
		}
		while (today < simulationDays && !finished) {
			Collections.sort(projects, new ProjectComparator());
			Iterator<Project> projectIterator = projects.iterator();
			while (projectIterator.hasNext()) {
				Project project = projectIterator.next();
				if (!project.finished()) {
					Iteration iteration = project.getCurrentIteration();
					if (!iteration.finished()) {
						if (iteration.isDelayed()) {
							idleProgrammers -= strategy.reasing(project,
									projects, idleProgrammers);
						}
						if (project.getProgrammersWorking() > 0) {
							iteration.decreaseLastingDays();
						}
					} else {
						project.nextIteration();
					}
				} else {
					totalCost += project.getTotalCost();
					projectIterator.remove();
					idleProgrammers += project.removeProgrammers();
					projectsFinished++;
				}
			}
			today++;
			if (projectsFinished == totalProjects) {
				finished = true;
			}
		}
		System.out.println("Cantidad proyectos originales: " + totalProjects);
		System.out.println("Proyectos terminados: " + projectsFinished);
		System.out.println("Dias de desarrollo: " + today);
		System.out.println("Costo total: " + totalCost);

	}

	private void distributeProgrammers() {
		int copy = idleProgrammers;
		while (copy > 0) {
			for (Project project : projects) {
				project.addProgrammers(1);
				copy--;
				if (copy == 0) {
					break;
				}
			}
		}

		for (Project project : projects) {
			int projectProgrammers = project.getProgrammersWorking();
			int newBackEstimation = DistributionManager.getInstance()
					.getLastingDaysForBackendIssue(projectProgrammers);
			int newFrontEstimation = DistributionManager.getInstance()
					.getLastingDaysForFrontendIssue(projectProgrammers);
			int newIterationEstimation = newBackEstimation + newFrontEstimation;
			project.getCurrentIteration().setEstimate(newIterationEstimation);
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
				return -1;
			} else if (!delayedP1 && delayedP2) {
				return 1;
			} else {
				if ((diffP1 <= 0 && diffP2 <= 0) || (diffP1 >= 0 && diffP2 >= 0)) {
					if (diffP1 < diffP2) {
						return -1;
					} else if (diffP1 > diffP2) {
						return 1;
					} else {
						return 0;
					}
				}
				System.out.println("Shouldn't happen");
				return 0;
			}

		}
	}

	@Override
	public String toString() {
		return "Simulator simulationDays: " + simulationDays + " projectsQty: "
				+ projects.size() + " idleProgrammers: " + idleProgrammers;
	}

}
