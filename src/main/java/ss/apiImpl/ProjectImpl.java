package ss.apiImpl;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import ss.api.Iteration;
import ss.api.Project;

public class ProjectImpl implements Project {

	private Deque<Iteration> iterations = new LinkedBlockingDeque<>();

	private Iteration currentIteration = null;

	private Integer maxCost;

	private Integer duration = 0;

	private boolean finished = false;

	public ProjectImpl(Deque<Iteration> iterations, Integer maxCost) {
		super();
		this.iterations = iterations;
		this.maxCost = maxCost;
		setDuration();
		currentIteration = iterations.pop();

	}

	private void setDuration() {
		for (Iteration iteration : iterations) {
			duration += iteration.getDuration();
		}
	}

	public Integer getDuration() {
		return duration;
	}

	public Integer getMaxCost() {
		return maxCost;
	}

	public void addIteration(Iteration iteration) {
		this.iterations.add(iteration);
	}

	public void nextIteration() {
		try {
			currentIteration = iterations.pop();
		} catch (NoSuchElementException e) {
			finished = true;
		}
	}

	public Iteration getCurrentIteration() {
		if (currentIteration == null) {
			currentIteration = iterations.pop();
		}
		return currentIteration;
	}

	@Override
	public void decreaseCost(int qty) {
		maxCost -= qty;
	}

	@Override
	public boolean finished() {
		return finished;
	}

	@Override
	public String toString() {
		return "Project It: " + iterations.size() + " maxCost: " + maxCost
				+ " duration: " + getDuration() + " It:["+currentIteration+"]";
	}

}
