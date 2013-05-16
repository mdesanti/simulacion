package ss.apiImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import ss.util.RandomGenerator;

public class DistributionManager {

	private Properties simulatorProperties = new Properties();
	
	private static DistributionManager instance;

	private DistributionManager() {
		try {
			FileInputStream in = new FileInputStream("distributions.properties");
			simulatorProperties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static DistributionManager getInstance() {
		if(instance == null) {
			instance = new DistributionManager();
		}
		return instance;
	}

	public long getLastingDaysForBackendIssue(int programmersQty) {
		int qty = (programmersQty <= 3) ? programmersQty : 3;
		String str = (String) simulatorProperties.get("back_" + qty);
		String[] parameters = str.split(",");
		return RandomGenerator.triangular(Double.parseDouble(parameters[0]),
				Double.parseDouble(parameters[2]),
				Double.parseDouble(parameters[1]));
	}
	
	public long getLastingDaysForFrontendIssue(int programmersQty) {
		int qty = (programmersQty <= 3) ? programmersQty : 3;
		String str = (String) simulatorProperties.get("front_" + qty);
		String[] parameters = str.split(",");
		return RandomGenerator.triangular(Double.parseDouble(parameters[0]),
				Double.parseDouble(parameters[2]),
				Double.parseDouble(parameters[1]));
	}
}
