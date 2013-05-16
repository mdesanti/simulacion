package ss.apiImpl;

import ss.api.Issue;
import ss.apiImpl.issues.BackendIssue;
import ss.apiImpl.issues.FrontendIssue;

public class IssueFactory {
	


	public static Issue createFrontEndIssue() {
		return new FrontendIssue();
	}

	public static Issue createBackendIssue() {
		return new BackendIssue();
	}

}
