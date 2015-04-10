import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;

@SuppressWarnings("serial")
public class DateCountMap extends CountingMap<Date> {

	public Date minDate;
	public Date maxDate;

	@Override
	public void increaseOccurence(Date key) {
		super.increaseOccurence(key);
		if (minDate == null || key.before(minDate)) {
			minDate = key;
		}
		if (maxDate == null || key.after(maxDate)) {
			maxDate = key;
		}
	}

	public void setZeroPoints(Date minDate, Date maxDate) {
		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();
		min.setTime(minDate);
		max.setTime(maxDate);
		while (min.before(max)) {
			if (!this.containsKey(min.getTime())) {
				this.put(min.getTime(), 0);
			}
			min.add(Controller.PRECISION, 1);
		}
	}

	public TimeSeries getMessagesPerTimePeriod(String name) {
		TimeSeries timeSeries = new TimeSeries(name);
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		for (Date d : list) {
			if ((Controller.TIME_PERIOD_FROM == null || d
					.after(Controller.TIME_PERIOD_FROM))
					&& (Controller.TIME_PERIOD_TO == null || d
							.before(Controller.TIME_PERIOD_TO))) {
				// Only consider messages in the range specified
				timeSeries.addOrUpdate(getTimePeriod(d), this.get(d));
			}
		}
		return timeSeries;
	}

	public TimeSeries getMessagesPerTimePeriodCumulative(String name) {
		TimeSeries timeSeries = new TimeSeries(name);
		int total = 0;
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		for (Date d : list) {
			if ((Controller.TIME_PERIOD_FROM == null || d
					.after(Controller.TIME_PERIOD_FROM))
					&& (Controller.TIME_PERIOD_TO == null || d
							.before(Controller.TIME_PERIOD_TO))) {
				// Only consider messages in the range specified
				total = total + this.get(d);
				timeSeries.addOrUpdate(getTimePeriod(d), total);
			}
		}
		return timeSeries;
	}

	public TimeSeries getMessagesPerTimePeriodCumulativePerDay(String name) {
		TimeSeries timeSeries = new TimeSeries(name);
		int total = 0;
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(minDate);
		for (Date d : list) {
			if ((Controller.TIME_PERIOD_FROM == null || d
					.after(Controller.TIME_PERIOD_FROM))
					&& (Controller.TIME_PERIOD_TO == null || d
							.before(Controller.TIME_PERIOD_TO))) {
				// Only consider messages in the range specified

				if (d.after(calendar.getTime())) {
					calendar.add(Calendar.HOUR, 24);
					total = 0;
				}

				total = total + this.get(d);
				timeSeries.addOrUpdate(getTimePeriod(d), total);
			}
		}
		return timeSeries;
	}

	
	
	
	
	
	// EXTREMELY UGLY COPY BUT LEL EZ

	public TimeSeries getMessagesPerTimePeriodFull(String name) {
		TimeSeries timeSeries = new TimeSeries(name);
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		for (Date d : list) {
			// Only consider messages in the range specified
			timeSeries.addOrUpdate(getTimePeriod(d), this.get(d));
		}
		return timeSeries;
	}

	public TimeSeries getMessagesPerTimePeriodCumulativeFull(String name) {
		TimeSeries timeSeries = new TimeSeries(name);
		int total = 0;
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		for (Date d : list) {
			// Only consider messages in the range specified
			total = total + this.get(d);
			timeSeries.addOrUpdate(getTimePeriod(d), total);
		}
		return timeSeries;
	}

	public TimeSeries getMessagesPerTimePeriodCumulativePerDayFull(String name) {
		TimeSeries timeSeries = new TimeSeries(name);
		int total = 0;
		List<Date> list = new ArrayList<Date>(this.keySet());
		java.util.Collections.sort(list);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(minDate);
		for (Date d : list) {
			// Only consider messages in the range specified

			if (d.after(calendar.getTime())) {
				calendar.add(Calendar.HOUR, 24);
				total = 0;
			}

			total = total + this.get(d);
			timeSeries.addOrUpdate(getTimePeriod(d), total);
		}
		return timeSeries;
	}

	private RegularTimePeriod getTimePeriod(Date d) {
		RegularTimePeriod timePeriod;
		if (Controller.PRECISION == Controller.PRECISION_DAY) {
			timePeriod = new Day(d);
		} else if (Controller.PRECISION == Controller.PRECISION_HOUR) {
			timePeriod = new Hour(d);
		} else {
			timePeriod = new Minute(d);
		}
		return timePeriod;
	}
}
