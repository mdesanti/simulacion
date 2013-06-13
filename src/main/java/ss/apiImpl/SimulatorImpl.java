package ss.apiImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.api.Simulator;
import ss.apiImpl.charts.StrategiesChart;
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
	private Map<String, LinkedList<Integer>> finishedProjects = new HashMap<>();

	public final static int IDLE_STRATEGY = 0;
	public final static int SWITCH_STRATEGY = 1;
	public final static int FREELANCE_STRATEGY = 2;

	public void start(int totalTimes) {
		// Build method must be called before

		int totalProjects = projects.size();
		int projectsId = totalProjects;
		finishedProjects.put("idle", new LinkedList<Integer>());
		StrategiesChart chart = new StrategiesChart(projects);
		chart.start();
		while (--totalTimes >= 0) {
			int today = 0;
			int projectsFinished = 0;
			boolean finished = false;
			if ((strategy.isSwitchStrategy() || strategy.isFreelanceStrategy())) {
				distributeProgrammers();
				listener.updateIdleProgrammers(idleProgrammers);
			}
			while (today < simulationDays && !finished) {
				Collections.sort(projects, new ProjectComparator());
				Iterator<Project> projectIterator = projects.iterator();
				int projectsQty = projects.size();
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
							int extraTime = iteration.getEstimate()
									- iteration.getDuration();
							extraTime = (extraTime > 0) ? extraTime : 0;
							project.nextIteration(extraTime);
							listener.updateIterationDuration(project);
							if (strategy.isIdleStrategy()) {
								idleProgrammers += project.removeProgrammers();
								listener.updateIdleProgrammers(idleProgrammers);
							}

						}
					} else {
						projectIterator.remove();
						chart.removeProject(project);
						idleProgrammers += project.removeProgrammers();
						listener.updateIdleProgrammers(idleProgrammers);
						projectsFinished++;
						listener.updateFinishedProjects(projectsFinished);
					}
				}
				int newProjectsQty = projects.size();
				int diff = projectsQty - newProjectsQty;
				for (int i = 0; i < diff; i++) {
					Project p = buildProject(projectsId++);
					projects.add(buildProject(projectsId++));
					chart.addProject(p);
				}
				if (diff>0) {
					if ((strategy.isSwitchStrategy() || strategy
							.isFreelanceStrategy())) {
						distributeProgrammers();
						listener.updateIdleProgrammers(idleProgrammers);
					}
				}
				today++;
				listener.updateTime(today);
				chart.updateTime();
				if ((projectsFinished == totalProjects)
						|| (today == simulationDays)) {
					finishedProjects.get(strategy.getStrategy()).push(
							projectsFinished);
					finished = true;
				}
			}
			build(listener, strategy.getStrategyID());
//			chart.restart(projects);
			 chart = new StrategiesChart(projects);
			listener.reset();
		}

	}

	private ReasignationStrategyImpl assignStrategy(int strategy) {
		switch (strategy) {
		case 0:
			return new ReasignationStrategyImpl(true, false, false, listener);
		case 1:
			finishedProjects.put("switch", new LinkedList<Integer>());
			return new ReasignationStrategyImpl(false, true, false, listener);
		default: // Case 2
			finishedProjects.put("freelance", new LinkedList<Integer>());
			return new ReasignationStrategyImpl(false, false, true, listener);
		}
	}

	private void distributeProgrammers() {
		int copy = idleProgrammers;
		while (copy > 0) {
			for (Project project : projects) {
				project.addProgrammers(1);
				listener.updateWorkingProgrammers(project);
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

	private List<Project> buildProjects(int qty) {
		List<Project> retList = Lists.newArrayList();
		for (int i = 0; i < qty; i++) {
			retList.add(buildProject(i));
		}
		return retList;

	}

	private Project buildProject(int id) {
		Deque<ss.api.Iteration> iterations = new LinkedBlockingDeque<>();
		Random r = new Random();
		int iterationsQty = r.nextInt(7) + 1; // (0,7]
		for (int j = 0; j < iterationsQty; j++) {
			int duration = r.nextInt(27 - 20) + 20; // (20,27]
			Iteration it = new IterationImpl(IssueFactory.createBackendIssue(),
					IssueFactory.createFrontEndIssue(), duration);
			iterations.push(it);
		}
		int maxCost = r.nextInt(10 - 5) + 5; // (5,10]
		return new ProjectImpl(iterations, maxCost, id);
	}

	public void build(SimulationListener listener, int strategy) {
		this.listener = listener;
		// Configuration config = Configuration.fromXML("configuracion.xml");
		// this.projects = buildProjects(config);
		this.projects = buildProjects(5);
		// this.idleProgrammers = config.getProgrammersQty();
		this.idleProgrammers = 12;
		// this.strategy = getStrategy(config.getStrategy());
		// Get from projects maximum duration
		this.simulationDays = 365; // In days
		this.strategy = assignStrategy(strategy);
		// for (Project p : projects) {
		// if (p.getDuration() > simulationDays) {
		// simulationDays = p.getDuration();
		// }
		// }
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
	public int getSimulationDays() {
		return simulationDays;
	}

}
