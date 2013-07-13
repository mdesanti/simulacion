package ss.apiImpl.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import ss.api.Project;

@SuppressWarnings("serial")
public class ProgrammersRealTimeChart extends ApplicationFrame implements
		RealTimePlotter {

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

	public ProgrammersRealTimeChart() {
		super("Simulador");
	}

	@Override
	public void setProjects(List<Project> projects) {
		initialize(projects);
	}

	private void initialize(List<Project> projects) {
		this.series = new ArrayList<>();
		this.projects = new LinkedBlockingQueue<>();
		this.timeSeriesProject = new LinkedBlockingQueue<>();
		for (Project project : projects) {
			this.projects.add(project);
			TimeSeries ts = new TimeSeries("Projecto " + project.getId(), Day.class);
			series.add(ts);
			timeSeriesProject.add(new TimeSeriesProject(ts, project));
		}

		dataset = new TimeSeriesCollection();
		for (TimeSeries serie : series) {
			dataset.addSeries(serie);
		}
		chart = ChartFactory.createTimeSeriesChart(null, "Tiempo",
				"Programadores", dataset, true, false, false);
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(new Color(0X000000));
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(10);
		plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		frame = new JFrame("Simulador: Distribución de programadores en una Software Factory");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		label = new ChartPanel(chart);
		label.setPreferredSize(new Dimension(800, 400));
		frame.getContentPane().add(label);

		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	public void removeProject(Project p) {
		TimeSeriesProject toRemove = null;
		for (TimeSeriesProject tsp : timeSeriesProject) {
			if (tsp.project.equals(p)) {
				toRemove = tsp;
			}
		}
		dataset.removeSeries(series.indexOf(toRemove.timeSeries));
		series.remove(toRemove.timeSeries);
		timeSeriesProject.remove(toRemove);
	}

	public void addProject(Project project) {
		projects.add(project);
		TimeSeries ts = new TimeSeries("Projecto " + project.getId(), Day.class);
		series.add(ts);
		timeSeriesProject.add(new TimeSeriesProject(ts, project));
		dataset.addSeries(ts);

	}

	public void restart(List<Project> projects) {
		initialize(projects);
	}

	public void updateTime() {
		dataset = new TimeSeriesCollection();
		for (TimeSeriesProject ts : timeSeriesProject) {
			ts.getTimeSeries().add(day,
					ts.getProject().getProgrammersWorking());
		}
		for (TimeSeriesProject ts : timeSeriesProject) {
			dataset.addSeries(ts.getTimeSeries());
		}
		chart = ChartFactory.createTimeSeriesChart(null, "Tiempo",
				"Programadores", dataset, true, false, false);
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(new Color(0X000000));
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(10);
		plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		frame.getContentPane().removeAll();
		frame.getContentPane().remove(label);
		ChartPanel label = new ChartPanel(chart);
		label.setPreferredSize(new Dimension(800, 400));
		frame.getContentPane().add(label);
		frame.pack();
		day = (Day) day.next();
		repaint();
	}

	public RealTimePlotter newInstance(List<Project> projects) {
		RealTimePlotter rtp = new ProgrammersRealTimeChart();
		rtp.setProjects(projects);
		return rtp;
	}


}
