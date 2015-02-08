import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class DataStore {
	private static DataStore instance;

	private Map<String, DateCountMap> messagesPerPersonPerDate;

	public DataStore() {
		messagesPerPersonPerDate = new HashMap<String, DateCountMap>();
	}

	public static DataStore getInstance() {
		if (instance == null) {
			instance = new DataStore();
		}
		return instance;
	}

	public void storeMessage(Message msg) {
		if (msg != null) {
			// System.out.println("Storing message: "+msg.toString());

			// Messages per person per day
			DateCountMap internalMap = messagesPerPersonPerDate.get(msg.person);
			if (internalMap == null) {
				internalMap = new DateCountMap();
				messagesPerPersonPerDate.put(msg.person, internalMap);
			}
			internalMap.increaseOccurence(msg.getDate());
		}
	}

	public String getMessagePerDayToString() {
		int totalCount = getTotalMessages();
		String output = "MessagesPerDayPerPerson\n";
		for (String s : messagesPerPersonPerDate.keySet()) {
			int personCount = messagesPerPersonPerDate.get(s).totalCount();
			output = output
					+ String.format(
							"%s : %d - %.2f%%\n",
							s,
							personCount,
							(((float) personCount) / ((float) totalCount)) * 100);
		}
		return output;
	}

	public void buildChart() {
		TimeSeriesCollection data = new TimeSeriesCollection();
		for (String s : messagesPerPersonPerDate.keySet()) {
			TimeSeries timeSeries = messagesPerPersonPerDate.get(s)
					.getMessagesPerDay(s);
			data.addSeries(timeSeries);
		}
		JFreeChart chart = ChartFactory.createXYLineChart("TicTacToe", "Date",
				"Messages send", data);
		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setSeriesPaint(0, Color.RED);
		plot.getRenderer().setSeriesPaint(0, Color.GREEN);
		plot.getRenderer().setSeriesPaint(0, Color.BLUE);
		
		try {
			ChartUtilities.saveChartAsPNG(new File("TestChart.png"), chart,
					1920, 1200);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTotalMessages() {
		int count = 0;
		for (String s : messagesPerPersonPerDate.keySet()) {
			count = count + messagesPerPersonPerDate.get(s).totalCount();
		}
		return count;
	}
	
	public Map<String,DateCountMap> getMessagesPerPersonDate(){
		return messagesPerPersonPerDate;
	}
}
