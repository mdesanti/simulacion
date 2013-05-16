package ss.apiImpl.strategies;

import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.apiImpl.DistributionManager;

public class FreelanceStrategy implements ReasignationStrategy {
	@Override
	public int reasing(Project to, List<Project> from, int idleProgrammers) {
		int maxCost = to.getMaxCost();
		boolean delayed = true;
		int newProgrammers = 0;
		Iteration iteration = to.getCurrentIteration();
		int iterationProgrammers = iteration.getProgrammersWorking();
		while (newProgrammers < maxCost & delayed) {
			newProgrammers++;
			int newEstimateProgrammers = iterationProgrammers + newProgrammers;
			int newBackEstimation = DistributionManager.getInstance()
					.getLastingDaysForBackendIssue(newEstimateProgrammers);
			int newFrontEstimation = DistributionManager.getInstance()
					.getLastingDaysForFrontendIssue(newEstimateProgrammers);
			int newIterationEstimation = newBackEstimation + newFrontEstimation;

			if (!iteration.isDelayedWith(newIterationEstimation)
					|| newProgrammers == idleProgrammers) {
				delayed = false;
				iteration.addProgrammer(newEstimateProgrammers);
				iteration.setEstimate(newIterationEstimation);
				to.decreaseCost(newProgrammers);
				return 0;
			}
		}
		return 0;
	}
}
