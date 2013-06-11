package ss.apiImpl.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import ss.api.Project;

public class StrategiesChart extends ApplicationFrame {

	private Updater updater;

	private List<TimeSeries> series;
	private BlockingQueue<Project> projects;
	private BlockingQueue<TimeSeriesProject> timeSeriesProject;
	
	private Day day = new Day();
	
	private JFrame frame;
	private ChartPanel label;
	private JFreeChart chart;
	
	private TimeSeriesCollection dataset;

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
		super("SS");
		initialize(projects);
	}

	private void initialize(List<Project> projects) {
		this.series = new ArrayList<>();
		this.projects = new LinkedBlockingQueue<>();
		this.timeSeriesProject = new LinkedBlockingQueue<>();
		for (Project project : projects) {
			this.projects.add(project);
			TimeSeries ts = new TimeSeries("Projecto " + project.getId(),
					Day.class);
			series.add(ts);
			timeSeriesProject.add(new TimeSeriesProject(ts, project));
		}
		updater = new Updater();

		dataset = new TimeSeriesCollection();
		for (TimeSeries serie : series) {
			dataset.addSeries(serie);
		}
		chart = ChartFactory.createTimeSeriesChart("Simulador",
				"Tiempo", "Programadores", dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(10);

		frame = new JFrame("Simulador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		label = new ChartPanel(chart);
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
				Day.class);
		series.add(ts);
		timeSeriesProject.add(new TimeSeriesProject(ts, project));
		dataset.addSeries(ts);
		
	}

	public void restart(List<Project> projects) {
		initialize(projects);
	}
	
	public void updateTime() {
		for (TimeSeriesProject ts : timeSeriesProject) {
			ts.getTimeSeries().addOrUpdate(day,
					ts.getProject().getProgrammersWorking());
		}
		dataset.removeAllSeries();
		for (TimeSeriesProject ts : timeSeriesProject) {
			dataset.addSeries(ts.getTimeSeries());
		}
		chart = ChartFactory.createTimeSeriesChart("Simulador",
				"Tiempo", "Programadores", dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(10);
		
		frame.revalidate();
		frame.getContentPane().removeAll();
		ChartPanel label = new ChartPanel(chart);
		frame.getContentPane().add(label);
		// Suppose I add combo boxes and buttons here later

		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);

		day = (Day) day.next();
		repaint();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class Updater extends Thread {

		public void run() {
//			while (true) {
//				
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException ex) {
//					System.out.println(ex);
//				}
//				repaint();
//				day = (Day) day.next();
//			}
		}

	}
}
