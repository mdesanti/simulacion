package ss.apiImpl.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import ss.api.Project;

public class StrategiesChart {

	private Updater updater;

	private List<TimeSeries> series;
	private BlockingQueue<Project> projects;
	private BlockingQueue<TimeSeriesProject> timeSeriesProject;

	private class TimeSeriesProject {
		TimeSeries timeSeries;
		Project project;

		public TimeSeriesProject(TimeSeries timeSeries, Project project) {
			this.timeSeries = timeSeries;
			this.project = project;
		}

		public Project getProject() {
			return project;
		}

		public TimeSeries getTimeSeries() {
			return timeSeries;
		}
	}

	public StrategiesChart(List<Project> projects) {
		initialize(projects);
	}

	private void initialize(List<Project> projects) {
		this.series = new ArrayList<>();
		this.projects = new LinkedBlockingQueue<>();
		this.timeSeriesProject = new LinkedBlockingQueue<>();
		for (Project project : projects) {
			this.projects.add(project);
			TimeSeries ts = new TimeSeries("Projecto " + project.getId(),
					Second.class);
			series.add(ts);
			timeSeriesProject.add(new TimeSeriesProject(ts, project));
		}
		updater = new Updater();

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		for (TimeSeries serie : series) {
			dataset.addSeries(serie);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart("Simulador",
				"Tiempo", "Programadores", dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(10);

		JFrame frame = new JFrame("Simulador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChartPanel label = new ChartPanel(chart);
		frame.getContentPane().add(label);
		// Suppose I add combo boxes and buttons here later

		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	public void start() {
		updater.start();
	}

	public void addProject(Project project) {
		projects.add(project);
		TimeSeries ts = new TimeSeries("Projecto " + project.getId(),
				Second.class);
		series.add(ts);
		timeSeriesProject.add(new TimeSeriesProject(ts, project));
	}

	public void restart(List<Project> projects) {
		initialize(projects);
	}

	private class Updater extends Thread {

		public void run() {
			while (true) {
				for (TimeSeriesProject ts : timeSeriesProject) {
					ts.getTimeSeries().addOrUpdate(new Second(),
							ts.getProject().getProgrammersWorking());

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					System.out.println(ex);
				}

			}
		}

	}
}
