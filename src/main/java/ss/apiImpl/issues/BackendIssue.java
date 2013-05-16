package ss.apiImpl.issues;

import ss.api.Issue;

public class BackendIssue implements Issue {
	
	public boolean isBackend() {
		return true;
	}
	
	public boolean isFrontend() {
		return false;
	}

	@Override
	public long getLastingDays() {
		// TODO Auto-generated method stub
		return 0;
	}
}
