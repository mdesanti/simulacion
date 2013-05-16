package ss.api;

public interface Iteration {
	
	Issue getBackendIssue();
	
	Issue getFrontendIssue();
	
	Issue getCurrentIssue();
	
	int getEstimate();
	
	void setEstimate(int time);
	
	Issue getNextIssue(int programmersQty);
	
	int getDuration();
	
	boolean isDelayed();

}
