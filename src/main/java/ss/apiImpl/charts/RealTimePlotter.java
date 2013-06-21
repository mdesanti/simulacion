package ss.apiImpl.charts;

import java.util.List;

import ss.api.Project;

public interface RealTimePlotter {
	
	void setProjects(List<Project> projects);
	
	public void removeProject(Project p);
	
	void addProject(Project project);
	
	void updateTime();
	
	void restart(List<Project> projects);
	
	RealTimePlotter newInstance(List<Project> projects);

}
