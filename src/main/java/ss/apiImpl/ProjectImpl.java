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

	private Integer programmersWorking;

	public ProjectImpl(Deque<Iteration> iterations, Integer maxCost) {
		super();
		this.iterations = iterations;
		this.maxCost = maxCost;
		setDuration();
		currentIteration = iterations.pop();
		this.programmersWorking = 0;
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
	public Integer getProgrammersWorking() {
		return programmersWorking;
	}

	@Override
	public void addProgrammer(int qty) {
		programmersWorking += qty;
	}

	@Override
	public void removeProgrammer() {
		programmersWorking--;
	}

	@Override
	public int removeProgrammers() {
		int old = programmersWorking;
		programmersWorking = 0;
		return old;
	}

	@Override
	public String toString() {
		return "Project It: " + iterations.size() + " maxCost: " + maxCost
				+ " duration: " + getDuration() + " progQty: "
				+ programmersWorking + " It:[" + currentIteration + "]";
	}

}
