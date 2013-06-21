package ss;

public class BackupItem {
	private int finishedProjects;
	private int cost;

	public BackupItem(int finishedProjects, int cost) {
		this.finishedProjects = finishedProjects;
		this.cost = cost;
	}

	public int getFinishedProjects() {
		return finishedProjects;
	}

	public int getCost() {
		return cost;
	}

}
