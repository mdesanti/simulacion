package ss.gui.in;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class Configuration {
	
	@ElementArray
	private Project[] projects;
	
	@Element
	private Integer programmersQty;
	
	@Element
	private String strategy;
	
	public String getStrategy() {
		return strategy;
	}
	
	public Integer getProgrammersQty() {
		return programmersQty;
	}
	
	
	public Project[] getProjects() {
		return projects;
	}
	
	public void setProjects(Project[] projects) {
		this.projects = projects;
	}
	
	
	public static Configuration fromXML(String path) {
		Serializer serializer = new Persister();
		File source = new File(path);

		try {
			return serializer.read(Configuration.class, source);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
