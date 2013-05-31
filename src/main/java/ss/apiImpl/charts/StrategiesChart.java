package ss.apiImpl.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.RefineryUtilities;

import ss.api.Project;

public class StrategiesChart {

	private BlockingQueue<Project> projects;
	private BlockingQueue<TimeSeriesProject> timeSeriesProject;
	private TimeSeriesCollection dataset;
	private JFrame frame;

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
		JFreeChart chart = ChartFactory.createTimeSeriesChart("Simulador",
				"Tiempo", "Programadores", dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(10);
		frame = new JFrame("Simulador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChartPanel label = new ChartPanel(chart);
		frame.getContentPane().add(label);
		// Suppose I add combo boxes and buttons here later

		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	private void initialize(List<Project> projects) {
		List<TimeSeries> series = new ArrayList<>();
		this.projects = new LinkedBlockingQueue<>();
		this.timeSeriesProject = new LinkedBlockingQueue<>();
		for (Project project : projects) {
			this.projects.add(project);
			TimeSeries ts = new TimeSeries("Projecto " + project.getId());
			series.add(ts);
			timeSeriesProject.add(new TimeSeriesProject(ts, project));
		}

		dataset = new TimeSeriesCollection();
		for (TimeSeries serie : series) {
			dataset.addSeries(serie);
		}

	}

	public void addProject(Project project) {
		projects.add(project);
		TimeSeries ts = new TimeSeries("Projecto " + project.getId(),
				Second.class);
		dataset.addSeries(ts);
		timeSeriesProject.add(new TimeSeriesProject(ts, project));
	}

	public void removeProject(Project project) {
		Iterator<TimeSeriesProject> it = timeSeriesProject.iterator();
		while (it.hasNext()) {
			TimeSeriesProject next = it.next();
			if (next.getProject().equals(project)) {
				projects.remove(project);
				dataset.removeSeries(next.getTimeSeries());
				it.remove();
				return;
			}
		}
	}

	public void restart(List<Project> projects) {
		frame.setVisible(false);
		initialize(projects);
	}

	public void updateWorkingProgrammers(Project project) {
		for (TimeSeriesProject ts : timeSeriesProject) {
			if (ts.getProject().equals(project)) {
				TimeSeriesDataItem data =ts.getTimeSeries().addOrUpdate(new Millisecond(),
						ts.getProject().getProgrammersWorking());
				System.out.println(data);
				return;
			}
		}

	}
}
