package assignment3;

import java.util.ArrayList;

public class Cluster {
	private int numOfCentroids = 5;
	//arraylist of points in the cluster
	private ArrayList<Point> points;
	private Point centroids[] = new Point[numOfCentroids];
	long minX, maxX;
	//name of the website
	private String website;
	private int numOfWords = 0;
	
	public class Point {
		//Hashcode for word
		private long x;
		//frequency
		private int y;

		public Point(long x, int y) {
			this.x = x; 
			this.y = y;
		}

		//gets x pos
		public long getX() {
			return this.x;
		}

		//gets y pos
		public int getY() {
			return this.y;
		}

		//gets the point
		public Point getPoint() {
			return new Point(this.x, this.y);
		}

		@Override
		public String toString() {
			return "["+this.x + " , " + this.y + "]";
		}
	}


	//constructor
	public Cluster(String website, ArrayList<Point> points) {
		this.website = website;
		this.points = points;
	}
	
	public int getNumOfCentroids() {
		return this.numOfCentroids;
	}

	//adds a point to the arraylist
	public void addPoint(long x, int y) {
		this.points.add(new Point(x, y));
		if(this.minX > x)
			this.minX = x;
		else if(this.maxX < x)
			this.maxX = x;
		this.numOfWords += y;
	}
	
	//returns the number of words in the cluster
	public int getNumOfWords() {
		return this.numOfWords;
	}
	

	//returns all the points
	public ArrayList<Point> getPoints(){
		return this.points;
	}

	//returns the website
	public String getWebsite() {
		return this.website;
	}
	
	public void makeUselessCluster() {
		this.website = "";
		for(int i = 0; i < 5; i++)
			addPoint(0,0);
	}

	//gets the mean value of all of the points
	public Point getMean() {
		int XMean = 0;
		int YMean = 0;
		for(Point p : this.points) {
			XMean += p.getX();
			YMean += p.getY();
		}
		return new Point(XMean, YMean);
	}
	
	/*
	 * Gets the average Point on a certain interval
	 * @param:
	 * 	long start - the start point on the x-axis
	 * 	long end - the end point on the x-axis
	 * @return:
	 * 	Point - Point of all averaged x and y values on the given interval
	 */
	public Point getAveragePoint(long start, long end) {
		int numOfPoints = 0, yValue = 0;
		long xValue = 0;
		for(Point p : this.points) {
			if(p.getX() > start && p.getX() < end) {
				numOfPoints++;
				yValue += p.getY();
				xValue += p.getX();
				
			}
		}
		if(numOfPoints != 0)
			return new Point(xValue/numOfPoints, yValue/numOfPoints);
		return getMean();
	}
	
	/*
	 * sets all 'centroids'
	 * Partitions each cluster by 'this.numOfCentroids' to create different intervals
	 * Determines the average point of each partition which is used to compare the cluster
	 * @return 
	 * 	Point[] - the average points of each partition 
	 */
	public Point[] setCentroids() {
		long tempMin = this.minX;
		long partitionNumber; 
		if(this.minX < 0 && this.maxX > 0)
			partitionNumber = (Math.abs(this.minX) + this.maxX)/this.numOfCentroids;
		else
			partitionNumber = (this.minX + this.maxX)/this.numOfCentroids;
		
		for(int i = 0; i < this.numOfCentroids; i ++) {
			this.centroids[i] = getAveragePoint(tempMin, tempMin+partitionNumber);
			tempMin += partitionNumber;
		}
		return this.centroids;
	}
}
