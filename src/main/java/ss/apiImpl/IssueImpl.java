package ss.apiImpl;

import ss.api.Issue;
import ss.util.RandomGenerator;

public class IssueImpl implements Issue {

	private int a;
	private int b;
	private int c;
	
	private long lastingDays;
	
	public IssueImpl(int a, int b, int c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		lastingDays = RandomGenerator.triangular(a, b, c);
	}



	public long getLastingDays() {
		return lastingDays;
	}

}
