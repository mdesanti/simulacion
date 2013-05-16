package ss.apiImpl.strategies;

import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.apiImpl.DistributionManager;

public class IdleStrategy implements ReasignationStrategy {

	@Override
	public int reasing(Project to, List<Project> from, int idleProgrammers) {
		boolean delayed = true;
		int newProgrammers = 0;
		Iteration iteration = to.getCurrentIteration();
		int iterationProgrammers = iteration.getProgrammersWorking();
		while (newProgrammers < idleProgrammers & delayed) {
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
				return idleProgrammers - newProgrammers;
			}
		}
		return 0;

	}
}
