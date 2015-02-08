import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;


public class DateCountMap extends CountingMap<Date>{
	public TimeSeries getMessagesPerDay(String name){
		TimeSeries timeSeries = new TimeSeries(name);
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		for(Date d : list){
			timeSeries.add(new Day(d),this.get(d));
		}		
		return timeSeries;
	}
	
	public TimeSeries getMessagesPerDayCumulative(String name){
		TimeSeries timeSeries = new TimeSeries(name);
		int total = 0;
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		for(Date d : list){
			total = total + this.get(d);
			timeSeries.add(new Day(d),total);
		}		
		return timeSeries;
	}
}
