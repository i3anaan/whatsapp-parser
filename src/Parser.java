import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class Parser {
	

	public static String REGEX_MONTHS = "(jan|feb|mrt|apr|mei|jun|jul|aug|sep|okt|nov|dec)";
	public static String REGEX_MSG_START = "(\\d{1,2})\\s"+REGEX_MONTHS+"\\.\\s(\\d{4})?\\s?(\\d\\d):(\\d\\d)\\s-\\s([\\w\\s]*):\\s((.|\\s)*)";
	
	public static void parse(Scanner file){
		Message message = new Message();
		while(file.hasNext()){
			String nextLine = file.next();
			//System.out.println(nextLine);
			if(nextLine.substring(0, Math.min(nextLine.length(), 500)).matches(REGEX_MSG_START)){
				message.storeMessage();
				message.clear();
				message.append(nextLine);
			}else{
				message.append(nextLine);
			}
			//System.out.println("getDate() "+message.getDate());
			//System.out.println("getDateString() "+message.getDateString());
		}
		message.storeMessage();
		System.out.println("Stored messages!");
		System.out.println("MinDate: "+DataStore.getInstance().getMinDate());
		System.out.println("MaxDate: "+DataStore.getInstance().getMaxDate());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DataStore.getInstance().getMaxDate());
		calendar.add(Calendar.HOUR,-14*24);
		Controller.TIME_PERIOD_FROM = calendar.getTime();
		System.out.println("From Date: "+Controller.TIME_PERIOD_FROM);
		
		DataStore.getInstance().setZeroPoints();
		System.out.println("Added empty time");
		
		Plotter.buildMessagesPerTimePeriod(DataStore.getInstance().getMessagesPerPersonDate());
		Plotter.buildMessagesPerTimePeriodCumulative(DataStore.getInstance().getMessagesPerPersonDate());
		Plotter.buildMessagesPerTimePeriodCumulativePerDay(DataStore.getInstance().getMessagesPerPersonDate());
		
		Plotter.buildMessagesPerTimePeriodFull(DataStore.getInstance().getMessagesPerPersonDate());
		Plotter.buildMessagesPerTimePeriodCumulativeFull(DataStore.getInstance().getMessagesPerPersonDate());
		Plotter.buildMessagesPerTimePeriodCumulativePerDayFull(DataStore.getInstance().getMessagesPerPersonDate());
		System.out.println("Generated images!");
		
	}
	
	public static int getMonthInt(String s){
		String[] months = new String[]{"jan","feb","mrt","apr","mei","jun","jul","aug","sep","okt","nov","dec"};
		List<String> list = Arrays.asList(months);
		return list.indexOf(s);		
	}
}
