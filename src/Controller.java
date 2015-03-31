import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Controller {

	
	
	public static void main(String[] args){
		try {
			Scanner scanner = new Scanner(new File("whatsapp.txt"),"UTF-8").useDelimiter("\\n");
			Parser.parse(scanner);
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(DataStore.getInstance().getMessagePerDayToString());
	}
}
