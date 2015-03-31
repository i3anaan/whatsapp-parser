import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Parser {
	

	public static String REGEX_MONTHS = "(jan|feb|mrt|apr|mei|jun|jul|aug|sep|okt|nov|dec)";
	public static String REGEX_MSG_START = "(\\d{1,2})\\s"+REGEX_MONTHS+"\\.\\s(\\d{4})?\\s?(\\d\\d:\\d\\d)\\s-\\s([\\w\\s]*):\\s((.|\\s)*)";
	
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
		}
		message.storeMessage();
		System.out.println("Stored messages!");
		Plotter.buildMessagesPerDay(DataStore.getInstance().getMessagesPerPersonDate());
		Plotter.buildMessagesPerDayCumulative(DataStore.getInstance().getMessagesPerPersonDate());
		
	}
	
	public static int getMonthInt(String s){
		String[] months = new String[]{"jan","feb","mar","apr","may","jun","jul","aug","sep","okt","nov","dec"};
		List<String> list = Arrays.asList(months);
		return list.indexOf(s);		
	}
	/*
	
	public static void main(String[] args){
		
		String regex = "\\s?(\\d{4})?\\s";
		String regex2 = "(\\d{1,2})\\s"+REGEX_MONTHS+"\\.\\s(\\d\\d:\\d\\d)\\s-\\s([\\w\\s]*):\\s((.|\\s)*)";
		
		System.out.println(" 2014 ".matches(regex));
		System.out.println("2014 ".matches(regex));
		System.out.println(" 2015 ".matches(regex));
		System.out.println("  ".matches(regex));
		System.out.println("".matches(regex));
		
		System.out.println("24 okt. 2014 02:08 - Shannon Cleijne: Ik wou nog doei zeggen".matches(REGEX_MSG_START));
		System.out.println("31 mrt. 12:36 - Jaap Gerrits: Derk".matches(REGEX_MSG_START));
		System.out.println("31 mrt. 12:36 - Jaap Gerrits: Derk".matches(regex2));
	}
	*/
}
