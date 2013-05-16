package ss.apiImpl.strategies;

import java.util.List;

import ss.api.Iteration;
import ss.api.Project;
import ss.api.ReasignationStrategy;
import ss.apiImpl.DistributionManager;

public class SwitchStrategy implements ReasignationStrategy {

	@Override
	public int reasing(Project to, List<Project> from, int idleProgrammers) {
		int projectIndex = from.indexOf(to);
		Iteration iteration = to.getCurrentIteration();
		int iterationProgrammers = iteration.getProgrammersWorking();
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
			for (int i = from.size() - 1; i <= projectIndex; i++) {
				Project other = from.get(i);
				Iteration otherIteration = other.getCurrentIteration();
				int programmersQty = otherIteration.getProgrammersWorking();

				if (programmersQty > 0) {
					programmersAvailable++;
					otherIteration.removeProgrammer();
					newProgrammers++;
					newEstimateProgrammers = iterationProgrammers
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
						iteration.addProgrammer(newEstimateProgrammers);
						iteration.setEstimate(newIterationEstimation);
						return 0;
					}
				}
			}

			if (programmersAvailable == 0 && newProgrammers > 0) {
				finished = true;
				iteration.addProgrammer(newEstimateProgrammers);
				iteration.setEstimate(newIterationEstimation);
			}
		}
		return 0;
	}
}
