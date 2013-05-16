package ss.api;

public interface Project {

	/**
	 * Project moves to next iteration available
	 */
	void nextIteration();

	/**
	 * Gets the iteration original duration
	 */
	Iteration getCurrentIteration();

	void addIteration(Iteration iteration);

	Integer getMaxCost();

	Integer getDuration();

	void decreaseCost(int qty);

	boolean finished();

}