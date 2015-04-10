import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Controller {

	public static final int PRECISION_DAY = Calendar.HOUR * 24;
	public static final int PRECISION_HOUR = Calendar.HOUR;
	public static final int PRECISION_MINUTE = Calendar.MINUTE;

	public static int PRECISION;

	// null for dont care
	public static Date TIME_PERIOD_FROM = null;
	public static Date TIME_PERIOD_TO = null;

	public static String CURRENT_FILE = "";

	public static void main(String[] args) {
		PRECISION = PRECISION_HOUR;

		try {
			File dir = new File(System.getProperty("user.dir"));
			for (File file : dir.listFiles()) {
				if (file.isFile()
						&& file.getName().toLowerCase().startsWith("whatsapp")
						&& file.getName().endsWith(".txt")) {
					CURRENT_FILE = file.getName().substring(0, file.getName().length()-4);
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(file, "UTF-8")
							.useDelimiter("\\n");
					Parser.parse(scanner);
					scanner.close();
					DataStore.clear();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
