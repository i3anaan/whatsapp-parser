import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

	private static Pattern PATTERN_MSG_START = Pattern
			.compile(Parser.REGEX_MSG_START);
	private DataStore dataStore;

	private String fullMessage;

	public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
	
	public String content;
	public String day;
	public String month;
	public String year;
	public String time;
	public String person;

	private boolean dirty;
	private Matcher matcher;
	private Calendar calendar;

	public Message() {
		clear();
		dataStore = DataStore.getInstance();
		calendar = Calendar.getInstance();
	}

	public void storeMessage() {
		this.parseMessageContent();
		if (person != null) {
			dataStore.storeMessage(this);
		}
	}

	public void clear() {
		fullMessage = "";
		dirty = true;
	}

	public void append(String s) {
		fullMessage += s;
		dirty = true;
	}

	public String toString() {
		if (!fullMessage.isEmpty()) {
			parseMessageContent();
			return String.format("%s %s %s %s %s: %s", year, month, day, time,
					person, content);
		} else {
			return "[Empty Message]";
		}
	}

	public String getDateString() {
		return year + month + day;
	}

	public Date getDate() {
		parseMessageContent();
		if (person != null) {
			if(year==null){
				year = CURRENT_YEAR+"";
			}
			calendar.set(Integer.parseInt(year), Parser.getMonthInt(month),
					Integer.parseInt(day));
			return calendar.getTime();
		} else {
			return null;
		}
	}

	private void parseMessageContent() {
		if (dirty) {
			// Avoid stackoverflowexception in regexes from too long messages;
			String[] stringParts = fullMessage.split(":");
			//System.out.println(fullMessage);
			if(stringParts.length>=2){
				String msgInfo = stringParts[0] + ":" + stringParts[1] + ": ";
				String msgContent = "";
				for (int i = 2; i < stringParts.length; i++) {
					msgContent += stringParts[i];
				}
	
				matcher = PATTERN_MSG_START.matcher(msgInfo);
				if (matcher.find()) {
					day = matcher.group(1);
					month = matcher.group(2);
					year = matcher.group(3);
					time = matcher.group(4);
					person = matcher.group(5);
				}
				content = msgContent;
			}
		}
		dirty = false;
	}
}
