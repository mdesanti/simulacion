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

	private Integer totalCost = 0;

	private Integer duration = 0;

	private boolean finished = false;

	private Integer programmersWorking;

	private Integer id;

	public ProjectImpl(Deque<Iteration> iterations, Integer maxCost, Integer id) {
		super();
		this.id = id;
		this.iterations = iterations;
		this.maxCost = maxCost;
		setDuration();
		currentIteration = iterations.pop();
		this.programmersWorking = 0;
	}

	@Override
	public int getId() {
		return id;
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
		totalCost += qty;
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
	public void addProgrammers(int qty) {
		programmersWorking += qty;
	}

	@Override
	public void removeProgrammer() {
		programmersWorking--;
		if (programmersWorking == 0) {
			currentIteration.setEstimate(Integer.MAX_VALUE);
			return;
		}
		int newBackEstimation = DistributionManager.getInstance()
				.getLastingDaysForBackendIssue(programmersWorking);
		int newFrontEstimation = DistributionManager.getInstance()
				.getLastingDaysForFrontendIssue(programmersWorking);
		int newIterationEstimation = newBackEstimation + newFrontEstimation;
		currentIteration.setEstimate(newIterationEstimation);
	}

	@Override
	public int removeProgrammers() {
		int old = programmersWorking;
		programmersWorking = 0;
		return old;
	}

	@Override
	public int getTotalCost() {
		return totalCost;
	}

	@Override
	public String toString() {
		return "Project It: " + iterations.size() + " maxCost: " + maxCost
				+ " duration: " + getDuration() + " progQty: "
				+ programmersWorking + " It:[" + currentIteration + "]";
	}

}
