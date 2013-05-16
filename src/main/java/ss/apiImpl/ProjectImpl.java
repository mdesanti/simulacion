package ss.apiImpl;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import ss.api.Issue;
import ss.api.Iteration;
import ss.api.Project;

public class ProjectImpl implements Project {
	
	private Deque<Iteration> iterations = new LinkedBlockingDeque<>();
	
	private Iteration currentIteration = null;
	
	private Integer maxCost;
	
	private Integer duration = -1;
	
	private Integer programmersWorking;
	
	public ProjectImpl(Deque<Iteration> iterations,
			Integer maxCost) {
		super();
		this.iterations = iterations;
		this.maxCost = maxCost;
		currentIteration = iterations.pop();
		this.programmersWorking = 0;
	}
	
	public Integer getProgrammersWorking() {
		return programmersWorking;
	}
	
	public Integer getDuration() {
		if(duration == -1) {
			for(Iteration iteration: iterations) {
				duration += iteration.getDuration();
			}
		}
		return duration;
	}

	public Integer getMaxCost() {
		return maxCost;
	}
	
	public void addIteration(Iteration iteration) {
		this.iterations.add(iteration);
	}

	public Issue getNextIssue() {
		return currentIteration.getNextIssue(0);
	}

	public Iteration getCurrentIteration() {
		if(currentIteration == null) {
			currentIteration = iterations.pop();
		}
		return currentIteration;
	}

}
