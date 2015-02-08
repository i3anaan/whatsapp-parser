import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Parser {
	

	public static String REGEX_MONTHS = "(jan|feb|mar|apr|may|jun|jul|aug|sep|okt|nov|dec)";
	public static String REGEX_MSG_START = "(\\d{1,2})\\s"+REGEX_MONTHS+"\\.\\s(\\d{4})\\s(\\d\\d:\\d\\d)\\s-\\s([\\w\\s]*):\\s((.|\\s)*)";
	
	public static void parse(Scanner file){
		Message message = new Message();
		while(file.hasNext()){
			String nextLine = file.next();
			//System.out.println(nextLine);
			if(nextLine.matches(REGEX_MSG_START)){
				message.storeMessage();
				message.clear();
				message.append(nextLine);
			}else{
				message.append(nextLine);
			}
		}
		message.storeMessage();
		Plotter.buildMessagesPerDay(DataStore.getInstance().getMessagesPerPersonDate());
		Plotter.buildMessagesPerDayCumulative(DataStore.getInstance().getMessagesPerPersonDate());
		
	}
	
	public static int getMonthInt(String s){
		String[] months = new String[]{"jan","feb","mar","apr","may","jun","jul","aug","sep","okt","nov","dec"};
		List<String> list = Arrays.asList(months);
		return list.indexOf(s);		
	}
}
