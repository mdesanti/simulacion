package ss.api;

import java.util.List;

public interface ReasignationStrategy {

	/**
	 * Reasigns programmers and returns the quantity of idle programmers consumed
	 * @param to
	 * @param from
	 * @param idleProgrammers
	 * @return Quantity of idle programmers used.
	 */
	int reasing(Project to, List<Project> from, int idleProgrammers);
	
	boolean isSwitchStrategy();
	
	boolean isFreelanceStrategy();
	
	boolean isIdleStrategy();
	
	String getStrategy();
	public int getStrategyID();

}
