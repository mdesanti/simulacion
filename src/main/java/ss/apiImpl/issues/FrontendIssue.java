package ss.apiImpl.issues;

import ss.api.Issue;

public class FrontendIssue implements Issue {

	public boolean isBackend() {
		return false;
	}

	public boolean isFrontend() {
		return true;
	}
	
	@Override
	public long getLastingDays() {
		// TODO Auto-generated method stub
		return 0;
	}

}
