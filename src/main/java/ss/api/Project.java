package ss.api;

public interface Project {

	/**
	 * Gets the issue that will be developed next
	 * @return
	 */
	Issue getNextIssue();
	
	/**
	 * Gets the issue being developed
	 * @return
	 */
	Iteration getCurrentIteration();
	
	void addIteration(Iteration iteration);
	
	Integer getMaxCost();
	
	Integer getDuration();
	
	Integer getProgrammersWorking();
	
}