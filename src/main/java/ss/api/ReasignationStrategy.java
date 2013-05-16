package ss.api;

import java.util.List;

public interface ReasignationStrategy {
	
	int reasing(Project to, List<Project> from, int idleProgrammers);
	
}
