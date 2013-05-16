package ss.gui.in;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ss.apiImpl.IssueFactory;

@Root
public class Iteration {

	@Attribute
	private Integer number;
	
	@Element
	private Integer duration;
	
	public Integer getDuration() {
		return duration;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public ss.api.Iteration buildIteration() {
		return new ss.apiImpl.IterationImpl(IssueFactory.createBackendIssue(), IssueFactory.createFrontEndIssue(), duration);
	}
}
