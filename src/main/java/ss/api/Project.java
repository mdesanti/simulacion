package ss.api;


public interface Project {

	/**
	 * Project moves to next iteration available. Returns true if the project finished.
	 */
	boolean nextIteration(int extraTime);

	/**
	 * Gets the iteration original duration
	 */
	Iteration getCurrentIteration();

	void addIteration(Iteration iteration);

	Integer getMaxInvestment();

	Integer getDuration();

	void decreaseInvestment();

	boolean finished();

	Integer getProgrammersWorking();

	void addProgrammers(int qty);

	void removeProgrammer();

	int removeProgrammers();

	int getTotalCost();

	int getId();
	
	int getIterationsQty();
	
	int getIterationNumber();
	
	void removeFreelanceProgrammers();
	
	void addFreelanceProgrammers(int qty);
	
	int getFreelanceProgrammersWorking();
	
}