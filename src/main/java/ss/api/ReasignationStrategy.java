package ss.api;

import java.util.List;

public interface ReasignationStrategy {

	/**
	 * Reasigns programmers
	 * @param to
	 * @param from
	 * @param idleProgrammers
	 * @return Quantity of idle programmers used.
	 */
	int reasing(Project to, List<Project> from, int idleProgrammers);

}
