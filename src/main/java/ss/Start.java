package ss;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.simpleframework.xml.strategy.Strategy;

import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.api.Simulator;
import ss.apiImpl.SimulatorImpl;
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

	private static ReasignationStrategy getStrategy(String strategy) {
		// TODO: Instanciar el Distribution Manager
		return null;
	}

	private static List<Project> buildProjects(Configuration config) {
		ss.gui.in.Project[] projects = config.getProjects();
		List<Project> retList = Lists.newArrayList();

		for (int i = 0; i < projects.length; i++) {
			retList.add(projects[i].buildProject());
		}

		return retList;

	}

	private static Properties loadProperties(String fileName) {
		try {
			Properties simulatorProperties = new Properties();
			FileInputStream in;
			in = new FileInputStream(fileName);
			simulatorProperties.load(in);
			in.close();
			return simulatorProperties;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
