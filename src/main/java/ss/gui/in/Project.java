package ss.gui.in;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

@Root
public class Project {

	@ElementArray
	private Iteration[] iterations;
	
	@Element
	private Integer maxCost;
	
	public Integer getMaxCost() {
		return maxCost;
	}
	
	public Iteration[] getIterations() {
		return iterations;
	}
	
	public void setIterations(Iteration[] iterations) {
		this.iterations = iterations;
	}
	
	public ss.api.Project buildProject(int id) {
		Deque<ss.api.Iteration> its = new LinkedBlockingDeque<>();
		for(Iteration iteration: iterations) {
			its.add(iteration.buildIteration());
		}
		return new ss.apiImpl.ProjectImpl(its, maxCost,id);
	}
	
}
