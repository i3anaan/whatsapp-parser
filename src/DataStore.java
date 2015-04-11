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
	
	public static void clear(){
		instance = new DataStore();
	}

	public void storeMessage(Message msg) {
		if (msg != null && !msg.person.toLowerCase().contains("heeft een nieuw nummer")) {
			// Messages per person per day
			DateCountMap internalMapPerson = messagesPerPersonPerDate.get(msg.person);
			if (internalMapPerson == null) {
				internalMapPerson = new DateCountMap();
				messagesPerPersonPerDate.put(msg.person, internalMapPerson);
			}
			internalMapPerson.increaseOccurence(msg.getDate());
			/*
			DateCountMap internalMapEveryone = messagesPerPersonPerDate.get("Everyone");
			if (internalMapEveryone == null) {
				internalMapEveryone = new DateCountMap();
				messagesPerPersonPerDate.put("Everyone", internalMapEveryone);
			}
			internalMapEveryone.increaseOccurence(msg.getDate());
			*/
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

	public void setZeroPoints(){
		setZeroPoints(getMinDate(),getMaxDate());
	}
	public void setZeroPoints(Date from, Date to){
		for (String s : messagesPerPersonPerDate.keySet()) {
			messagesPerPersonPerDate.get(s).setZeroPoints(from, to);
		}
	}
	
	public Date getMinDate(){
		Date minDate = null;
		for (String s : messagesPerPersonPerDate.keySet()) {
			Date newDate = messagesPerPersonPerDate.get(s).minDate;
			if(minDate==null || newDate.before(minDate)){
				minDate = newDate;
			}
		}
		return minDate;
	}
	
	public Date getMaxDate(){
		Date maxDate = null;
		for (String s : messagesPerPersonPerDate.keySet()) {
			Date newDate = messagesPerPersonPerDate.get(s).maxDate;
			if(maxDate==null || newDate.after(maxDate)){
				maxDate = newDate;
			}
		}
		return maxDate;
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
