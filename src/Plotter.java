import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Plotter {

	public static final int RESOLUTION_X = 2500;
	public static final int RESOLUTION_Y = 1000;

	public static String FILE_PREFIX = Controller.CURRENT_FILE + "__"
			+ DataStore.getInstance().getMinDate().toString().replace(":", "-").replace(" ","_")
			+ "__"
			+ DataStore.getInstance().getMaxDate().toString().replace(":", "-").replace(" ","_")
			+ "__";

	/** Line style: line */
	public static final String STYLE_LINE = "line";
	/** Line style: dashed */
	public static final String STYLE_DASH = "dash";
	/** Line style: dotted */
	public static final String STYLE_DOT = "dot";

	/**
	 * Convert style string to stroke object.
	 * 
	 * @param style
	 *            One of STYLE_xxx.
	 * @return Stroke for <i>style</i> or null if style not supported.
	 */
	private static BasicStroke toStroke(String style) {
		BasicStroke result = null;

		if (style != null) {
			float lineWidth = 2.5f;
			float dash[] = { 5.0f };
			float dot[] = { lineWidth };

			if (style.equalsIgnoreCase(STYLE_LINE)) {
				result = new BasicStroke(lineWidth);
			} else if (style.equalsIgnoreCase(STYLE_DASH)) {
				result = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
			} else if (style.equalsIgnoreCase(STYLE_DOT)) {
				result = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
			}
		}// else: input unavailable

		return result;
	}// toStroke()

	/**
	 * Set color of series.
	 * 
	 * @param chart
	 *            JFreeChart.
	 * @param seriesIndex
	 *            Index of series to set color of (0 = first series)
	 * @param style
	 *            One of STYLE_xxx.
	 */
	public static void setSeriesStyle(JFreeChart chart, int seriesIndex,
			String style, Color color) {
		if (chart != null && style != null) {
			BasicStroke stroke = toStroke(style);

			Plot plot = chart.getPlot();
			if (plot instanceof CategoryPlot) {
				CategoryPlot categoryPlot = chart.getCategoryPlot();
				CategoryItemRenderer cir = categoryPlot.getRenderer();
				try {
					cir.setSeriesStroke(seriesIndex, stroke); // series line
																// style
					if (color != null) {
						cir.setSeriesPaint(seriesIndex, color);
					}
				} catch (Exception e) {
					System.err.println("Error setting style '" + style
							+ "' for series '" + seriesIndex + "' of chart '"
							+ chart + "': " + e);
				}
			} else if (plot instanceof XYPlot) {
				XYPlot xyPlot = chart.getXYPlot();
				XYItemRenderer xyir = xyPlot.getRenderer();
				try {
					xyir.setSeriesStroke(seriesIndex, stroke); // series line
																// style
					if (color != null) {
						xyir.setSeriesPaint(seriesIndex, color);
					}
				} catch (Exception e) {
					System.err.println("Error setting style '" + style
							+ "' for series '" + seriesIndex + "' of chart '"
							+ chart + "': " + e);
				}
			} else {
				System.out
						.println("setSeriesColor() unsupported plot: " + plot);
			}
		}// else: input unavailable
	}// setSeriesStyle()

	public static void buildMessagesPerTimePeriod(Map<String, DateCountMap> map) {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : map.keySet()) {
			TimeSeries timeSeries = map.get(s).getMessagesPerTimePeriod(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Messages per hour - Last 2 weeks", "", "Messages send", data);
		drawChart(chart, FILE_PREFIX + "MessagesPerPersonPerTimePeriod.png");
	}

	public static void buildMessagesPerTimePeriodCumulative(
			Map<String, DateCountMap> map) {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : map.keySet()) {
			TimeSeries timeSeries = map.get(s)
					.getMessagesPerTimePeriodCumulative(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Messages per hour cumulative - Last 2 weeks", "", "Messages send",
				data);
		drawChart(chart, FILE_PREFIX
				+ "MessagesPerPersonPerTimePeriodCumulative.png");
	}

	public static void buildMessagesPerTimePeriodCumulativePerDay(
			Map<String, DateCountMap> map) {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : map.keySet()) {
			TimeSeries timeSeries = map.get(s)
					.getMessagesPerTimePeriodCumulativePerDay(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Messages per hour cumulative per day - Last 2 weeks", "",
				"Messages send", data);
		drawChart(chart, FILE_PREFIX
				+ "MessagesPerPersonPerTimePeriodCumulativePerDay.png");
	}

	// EXTREMELY UGLY COPY BUT LEL EZ

	public static void buildMessagesPerTimePeriodFull(
			Map<String, DateCountMap> map) {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : map.keySet()) {
			TimeSeries timeSeries = map.get(s).getMessagesPerTimePeriodFull(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Messages per hour - Full", "", "Messages send",
				data);
		drawChart(chart, FILE_PREFIX + "MessagesPerPersonPerTimePeriodFull.png");
	}

	public static void buildMessagesPerTimePeriodCumulativeFull(
			Map<String, DateCountMap> map) {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : map.keySet()) {
			TimeSeries timeSeries = map.get(s)
					.getMessagesPerTimePeriodCumulativeFull(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Messages per hour cumulative - Full", "",
				"Messages send", data);
		drawChart(chart, FILE_PREFIX
				+ "MessagesPerPersonPerTimePeriodCumulativeFull.png");
	}

	public static void buildMessagesPerTimePeriodCumulativePerDayFull(
			Map<String, DateCountMap> map) {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : map.keySet()) {
			TimeSeries timeSeries = map.get(s)
					.getMessagesPerTimePeriodCumulativePerDayFull(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Messages per hour cumulative per day - Full", "",
				"Messages send", data);
		drawChart(chart, FILE_PREFIX
				+ "MessagesPerPersonPerTimePeriodCumulativePerDayFull.png");
	}

	public static void drawChart(JFreeChart chart, String name) {

		Font font = new Font("Dialog", Font.PLAIN, 25);
		
		chart.getLegend().setItemFont(font);
		chart.getTitle().setFont(font);
		XYPlot plot = chart.getXYPlot();
		plot.getDomainAxis().setLabelFont(font);
		plot.getRangeAxis().setLabelFont(font);
		plot.getDomainAxis().setTickLabelFont(font);
		plot.getRangeAxis().setTickLabelFont(font);
		
		Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.GREEN,
				Color.YELLOW, Color.BLACK, Color.MAGENTA, Color.CYAN };
		String[] styles = new String[] { STYLE_LINE, STYLE_DASH, STYLE_DOT };
		for (int i = 0; i < plot.getSeriesCount(); i++) {
			setSeriesStyle(chart, i,
					styles[(i / colors.length) % styles.length], colors[(i)
							% colors.length]);
		}
		try {
			ChartUtilities.saveChartAsPNG(new File(name), chart, RESOLUTION_X,
					RESOLUTION_Y);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
