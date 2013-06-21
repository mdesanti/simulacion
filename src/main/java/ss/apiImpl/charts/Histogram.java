package ss.apiImpl.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

public class Histogram extends JFrame {

	private static final long serialVersionUID = 1L;

	public Histogram(List<Integer> list) {
		JFreeChart chart = createChart(list);

		try {
			// ChartUtilities.saveChartAsPNG(new File("test.png"), chart, 300,
			// 300);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ChartPanel cpanel = new ChartPanel(chart);
		getContentPane().add(cpanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 500, 500);
		this.setTitle("Sample graph");
		this.setVisible(true);
	}

	private JFreeChart createChart(List<Integer> list) {

		HistogramDataset dataset = new HistogramDataset();
		int bin = 30;
		double [] v1 = new double[list.size()];
		for(int i = 0; i < list.size(); i++) {
			v1[i] = (double)list.get(i);
		}
		dataset.addSeries("POS", v1, bin, 0, 1);

		JFreeChart chart = ChartFactory.createHistogram("Histogram Demo", null,
				null, dataset, PlotOrientation.VERTICAL, true, false, false);

		chart.setBackgroundPaint(new Color(230, 230, 230));
		XYPlot xyplot = (XYPlot) chart.getPlot();
		xyplot.setForegroundAlpha(0.7F);
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(new Color(150, 150, 150));
		xyplot.setRangeGridlinePaint(new Color(150, 150, 150));
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
//		xybarrenderer.setShadowVisible(false);
//		xybarrenderer.setBarPainter(new StandardXYBarPainter());
		// xybarrenderer.setDrawBarOutline(false);
		return chart;
	}
}
