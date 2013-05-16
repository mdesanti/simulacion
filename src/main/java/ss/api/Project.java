package ss.api;

public interface Project {

	/**
	 * Gets the issue that will be developed next
	 * 
	 * @return
	 */
	Issue getNextIssue();

	/**
	 * Gets the iteration original duration
	 */
	Iteration getCurrentIteration();

	void addIteration(Iteration iteration);

	Integer getMaxCost();

	Integer getDuration();

	void decreaseCost(int qty);


}