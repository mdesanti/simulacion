package ss;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Start {

	public static void main(String[] args) {
		try {
			Properties simulatorProperties = new Properties();
			FileInputStream in = new FileInputStream("simulator.properties");
			simulatorProperties.load(in);
			in.close();
			System.out.println(simulatorProperties.getProperty("a"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
