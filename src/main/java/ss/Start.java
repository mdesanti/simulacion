package ss;

import java.util.List;

import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.api.Simulator;
import ss.apiImpl.SimulatorImpl;
import ss.apiImpl.strategies.ReasignationStrategyImpl;
import ss.gui.in.Configuration;

import com.google.common.collect.Lists;

public class Start {

	public static void main(String[] args) {
		Configuration config = Configuration.fromXML("configuracion.xml");
		List<Project> projects = buildProjects(config);
		int max = 0;

		// Get from projects maximum duration
		for (Project p : projects) {
			if (p.getDuration() > max) {
				max = p.getDuration();
			}
		}

		ReasignationStrategy strategy = getStrategy(config.getStrategy());

		Simulator simulator = new SimulatorImpl(max, projects,
				config.getProgrammersQty(), strategy);
		simulator.start();
	}

	private static ReasignationStrategyImpl getStrategy(String strategy) {
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
				freelanceStrategy);
	}

	private static List<Project> buildProjects(Configuration config) {
		ss.gui.in.Project[] projects = config.getProjects();
		List<Project> retList = Lists.newArrayList();

		for (int i = 0; i < projects.length; i++) {
			retList.add(projects[i].buildProject());
		}

		return retList;

	}

}
