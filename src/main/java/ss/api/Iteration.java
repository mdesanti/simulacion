package ss.api;

public interface Iteration {

	Issue getBackendIssue();

	Issue getFrontendIssue();

	Issue getCurrentIssue();

	/**
	 * Returns the time when the actual programmers qty will finish
	 * 
	 * @return
	 */
	int getEstimate();

	void setEstimate(int time);

	Issue getNextIssue(int programmersQty);

	/**
	 * Returns the original iteration duration
	 * 
	 * @return
	 */
	int getDuration();

	int timeDelayed();

	boolean isDelayed();

	boolean isDelayedWith(int estimation);

	/**
	 * Returns true if the iterations finished.
	 * 
	 * @return
	 */
	boolean finished();
	
	void decreaseLastingDays();
	
	int getLastingDays();
	
	public void setDuration(int duration);

}
