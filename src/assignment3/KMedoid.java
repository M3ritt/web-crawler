package assignment3;

import java.util.ArrayList;

public class KMedoid {

	private ArrayList<Cluster> clusters;

	public KMedoid(ArrayList<Cluster> clusters){
		this.clusters = clusters;
	}
	
	/*
	 * adds a cluster to the array list
	 * @param:
	 * 	Cluster c - the cluster to be added
	 */
	public void addCluster(Cluster c) {
		if(!this.clusters.contains(c))
			this.clusters.add(c);
	}
	
	/*
	 * checks if the current website is part of the cluster
	 * @param:
	 * 	String website - the website to be searched
	 * @return:
	 * 	boolean - true or false whether it is or not
	 */
	public boolean isWebsiteInCluster(String website) {
		Cluster temp = getACluster(website);
		if(temp == null) {
			return false;
		}
		for(Cluster c : this.clusters) {
			for(Cluster.Point point : c.getPoints()) {
				for(Cluster.Point tempPoint : temp.getPoints()) {
					if(point == tempPoint)
						return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * gets a cluster
	 * @param:
	 * 	String website - the website that is being looked for
	 * @return:
	 * 	Cluster - the cluster of the searched for website
	 */
	public Cluster getACluster(String website) {
		for(Cluster c : this.clusters) {
			if(c.getWebsite().equals(website))
				return c;
		}
		return null;
	}
}
