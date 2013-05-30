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

	public int getLastingDaysForBackendIssue(int programmersQty) {
		int qty = (programmersQty <= 4) ? programmersQty : 4;
		if(qty == 0) {
			return Integer.MAX_VALUE;
		}
		String str = (String) simulatorProperties.get("back_" + qty);
		String[] parameters = str.split(",");
		return (int)RandomGenerator.triangular(Double.parseDouble(parameters[0]),
				Double.parseDouble(parameters[2]),
				Double.parseDouble(parameters[1]));
	}
	
	public int getLastingDaysForFrontendIssue(int programmersQty) {
		int qty = (programmersQty <= 4) ? programmersQty : 4;
		if(qty == 0) {
			return 0;
		}
		String str = (String) simulatorProperties.get("front_" + qty);
		String[] parameters = str.split(",");
		return (int)RandomGenerator.triangular(Double.parseDouble(parameters[0]),
				Double.parseDouble(parameters[2]),
				Double.parseDouble(parameters[1]));
	}
}
