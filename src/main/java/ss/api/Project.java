package ss.api;

import java.util.Deque;

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

	Integer getProgrammersWorking();

	void addProgrammers(int qty);

	void removeProgrammer();

	int removeProgrammers();

	int getTotalCost();

	int getId();
	
}