package ss.apiImpl.strategies;

import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.apiImpl.DistributionManager;
import ss.gui.out.SimulationListener;

public class ReasignationStrategyImpl implements ReasignationStrategy {

	private boolean idleStrategy;
	private boolean switchStrategy;
	private boolean freelanceStrategy;
	private SimulationListener listener;

	public ReasignationStrategyImpl(boolean idleStrategy,
			boolean switchStrategy, boolean freelanceStrategy, SimulationListener listener) {
		this.idleStrategy = idleStrategy;
		this.switchStrategy = switchStrategy;
		this.freelanceStrategy = freelanceStrategy;
		this.listener = listener;
	}

	@Override
	public int reasing(Project to, List<Project> from, int idleProgrammers) {
		int ret = 0;
		if (idleStrategy) {
			ret = idleStrategyReasign(to, idleProgrammers);
		}
		if (switchStrategy) {
			switchStrategyReasign(to, from);
		}
		if (freelanceStrategy) {
			freelanceStrategyReasign(to);
		}
		return ret;
	}

	private int idleStrategyReasign(Project to, int idleProgrammers) {
		boolean delayed = true;
		int newProgrammers = 0;
		Iteration iteration = to.getCurrentIteration();
		int projectProgrammers = to.getProgrammersWorking();
		while (newProgrammers < idleProgrammers & delayed) {
			newProgrammers++;
			int newEstimateProgrammers = projectProgrammers + newProgrammers;
			int newBackEstimation = DistributionManager.getInstance()
					.getLastingDaysForBackendIssue(newEstimateProgrammers);
			int newFrontEstimation = DistributionManager.getInstance()
					.getLastingDaysForFrontendIssue(newEstimateProgrammers);
			int newIterationEstimation = newBackEstimation + newFrontEstimation;

			if (!iteration.isDelayedWith(newIterationEstimation)
					|| newProgrammers == idleProgrammers) {
				delayed = false;
				to.addProgrammers(newEstimateProgrammers);
				iteration.setEstimate(newIterationEstimation);
				return newProgrammers;
			}
		}
		return 0;
	}

	private void switchStrategyReasign(Project to, List<Project> from) {
		int projectIndex = from.indexOf(to);

		// Last proyect cant switch programmers with another project.
		if (projectIndex == from.size() - 1) {
			return;
		}
		Iteration iteration = to.getCurrentIteration();
		int projectProgrammers = to.getProgrammersWorking();
		int newProgrammers = 0;
		boolean delayed = true;
		int programmersAvailable = 0;
		boolean finished = false;
		int newEstimateProgrammers = 0;
		int newBackEstimation = 0;
		int newFrontEstimation = 0;
		int newIterationEstimation = 0;

		// Iterates from minor priority to mayor
		while (delayed && !finished) {
			programmersAvailable = 0;
			for (int i = from.size() - 1; i >= projectIndex; i--) {
				Project other = from.get(i);
				int programmersQty = other.getProgrammersWorking();

				if (programmersQty > 0) {
					programmersAvailable++;
					other.removeProgrammer();
					newProgrammers++;
					newEstimateProgrammers = projectProgrammers
							+ newProgrammers;
					newBackEstimation = DistributionManager.getInstance()
							.getLastingDaysForBackendIssue(
									newEstimateProgrammers);
					newFrontEstimation = DistributionManager.getInstance()
							.getLastingDaysForFrontendIssue(
									newEstimateProgrammers);
					newIterationEstimation = newBackEstimation
							+ newFrontEstimation;

					if (!iteration.isDelayedWith(newIterationEstimation)) {
						delayed = false;
						to.addProgrammers(newEstimateProgrammers);
						iteration.setEstimate(newIterationEstimation);
						return;
					}
				}
			}

			if (programmersAvailable == 0) {
				finished = true;
				if (newProgrammers > 0) {
					to.addProgrammers(newEstimateProgrammers);
					iteration.setEstimate(newIterationEstimation);
				}
			}
		}
	}

	private void freelanceStrategyReasign(Project to) {
		int maxCost = to.getMaxCost();
		boolean delayed = true;
		int newProgrammers = 0;
		Iteration iteration = to.getCurrentIteration();
		int projectProgrammers = to.getProgrammersWorking();
		while (newProgrammers < maxCost & delayed) {
			newProgrammers++;
			int newEstimateProgrammers = projectProgrammers + newProgrammers;
			int newBackEstimation = DistributionManager.getInstance()
					.getLastingDaysForBackendIssue(newEstimateProgrammers);
			int newFrontEstimation = DistributionManager.getInstance()
					.getLastingDaysForFrontendIssue(newEstimateProgrammers);
			int newIterationEstimation = newBackEstimation + newFrontEstimation;

			if (!iteration.isDelayedWith(newIterationEstimation)
					|| newProgrammers == maxCost) {
				delayed = false;
				to.addProgrammers(newEstimateProgrammers);
				iteration.setEstimate(newIterationEstimation);
				to.decreaseCost(newProgrammers);
				listener.updateCost(to);
				return;
			}
		}
		return;
	}

	@Override
	public boolean isSwitchStrategyOnly() {
		return switchStrategy && !(idleStrategy || freelanceStrategy);
	}
}
