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
	Issue getCurrentIssue();
}
