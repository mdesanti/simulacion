package ss.apiImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.api.Simulator;
import ss.apiImpl.strategies.ReasignationStrategyImpl;
import ss.gui.in.Configuration;
import ss.gui.out.SimulationListener;

import com.google.common.collect.Lists;

public class SimulatorImpl implements Simulator {

	private int simulationDays;
	private List<Project> projects;
	private int idleProgrammers;
	private ReasignationStrategy strategy;
	private SimulationListener listener;

	public SimulatorImpl(SimulationListener listener) {
		this.listener = listener;
	}

	public void start() {
		// Build method must be called before
		int today = 0;
		int projectsFinished = 0;
		int totalProjects = projects.size();
		boolean finished = false;
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
							listener.updateWorkingProgrammers(project);
							listener.updateIdleProgrammers(idleProgrammers);
							listener.updateIterationEstimate(project);
						}
						if (project.getProgrammersWorking() > 0) {
							iteration.decreaseLastingDays();
						}
					} else {
						project.nextIteration();
						listener.updateIterationDuration(project);
						idleProgrammers += project.removeProgrammers();
						listener.updateIdleProgrammers(idleProgrammers);
					}
				} else {
					projectIterator.remove();
					idleProgrammers += project.removeProgrammers();
					listener.updateIdleProgrammers(idleProgrammers);
					projectsFinished++;
					listener.updateProjectStatus(project);
					listener.updateFinishedProjects(projectsFinished);
				}
			}
			today++;
			listener.updateTime(today);
			if (projectsFinished == totalProjects) {
				finished = true;
			}
		}
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
				if ((diffP1 <= 0 && diffP2 <= 0)
						|| (diffP1 >= 0 && diffP2 >= 0)) {
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

	private static List<Project> buildProjects(Configuration config) {
		ss.gui.in.Project[] projects = config.getProjects();
		List<Project> retList = Lists.newArrayList();

		for (int i = 0; i < projects.length; i++) {
			retList.add(projects[i].buildProject(i));
		}

		return retList;

	}

	public void build() {
		Configuration config = Configuration.fromXML("configuracion.xml");
		this.projects = buildProjects(config);
		this.idleProgrammers = config.getProgrammersQty();
		this.strategy = getStrategy(config.getStrategy());
		// Get from projects maximum duration
		this.simulationDays = 0;
		for (Project p : projects) {
			if (p.getDuration() > simulationDays) {
				simulationDays = p.getDuration();
			}
		}
	}

	private ReasignationStrategyImpl getStrategy(String strategy) {
		String[] strategies = strategy.split(",");
		boolean idleStrategy = false;
		boolean switchStrategy = false;
		boolean freelanceStrategy = false;
		for (int i = 0; i < strategies.length; i++) {
			String strategyString = strategies[i];
			if (strategyString.equals("idle")) {
				idleStrategy = true;
			}
			if (strategyString.equals("switch")) {
				switchStrategy = true;
			}
			if (strategyString.equals("freelance")) {
				freelanceStrategy = true;
			}
		}
		return new ReasignationStrategyImpl(idleStrategy, switchStrategy,
				freelanceStrategy, listener);
	}

	@Override
	public int getFinishedProjects() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIdleProgrammers() {
		return idleProgrammers;
	}

	@Override
	public List<Project> getProjects() {
		return this.projects;
	}

	@Override
	public int getTotalCost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSimulationDays() {
		return simulationDays;
	}
}
