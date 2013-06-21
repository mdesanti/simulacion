package ss.apiImpl;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Random;
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

	private Integer iterationsQty;

	private double finalIterationProbability;

	public ProjectImpl(Deque<Iteration> iterations, Integer maxCost, Integer id) {
		super();
		this.id = id;
		this.iterations = iterations;
		this.iterationsQty = iterations.size();
		this.maxCost = maxCost;
		setDuration();
		currentIteration = iterations.pop();
		this.programmersWorking = 0;
		finalIterationProbability = 0.2;
	}

	@Override
	public int getIterationsQty() {
		return this.iterationsQty;
	}

	@Override
	public int getIterationsLeft() {
		return this.iterationsQty - iterations.size();
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

	public boolean nextIteration(int extraTime) {
		try {
			currentIteration = iterations.pop();
			currentIteration.setDuration(currentIteration.getDuration()
					+ extraTime);
		} catch (NoSuchElementException e) {
			if (extraTime > 0) {
				currentIteration = new IterationImpl(
						IssueFactory.createBackendIssue(),
						IssueFactory.createFrontEndIssue(), extraTime);
			} else {
				Random r = new Random(45);
				double random = r.nextDouble();
				if (random < finalIterationProbability) {
					finalIterationProbability /= 1.3;
					return false;
				}
				finished = true;

			}
		}

		return finished;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectImpl other = (ProjectImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
