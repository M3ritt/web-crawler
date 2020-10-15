package assignment3;

import java.util.LinkedList;
import java.util.List;

public class Graph {


	public class Vertex {
		//Global variable for vertex
		final private String id;
		final private long idCode;

		public Vertex(String id) {
			this.id = id;
			this.idCode = id.hashCode();
		}

		//gets the Id on the vertex
		public String getId() {
			return id;
		}

		//gets the hashCode of the id
		public long getidCode() {
			return this.idCode;
		}

		//Creates the hashcode
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		//equals function to see if the object is already used
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		//prints the id
		@Override
		public String toString() {
			return id + " with the code: "+this.idCode;
		}
	}

	public class Edge  {
		private final Vertex source;
		private final Vertex destination;
		private final int weight; 

		public Edge(Vertex source, Vertex destination, int weight) {
			this.source = source;
			this.destination = destination;
			this.weight = weight;
		}

		//gets the destination node
		public Vertex getDestination() {
			return destination;
		}

		//gets the source node
		public Vertex getSource() {
			return source;
		}

		//gets the weight/time of the node
		public int getWeight() {
			return weight;
		}

		@Override
		public String toString() {
			return source + " " + destination;
		}
	}

	//Global variables for the Graph
	private final List<Vertex> vertexes;
	private final List<Edge> edges;
	private int numOfNodes = 0;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public int getNumOfNodes() {
		return this.numOfNodes;
	}

	//returns the list of vertexes
	public List<Vertex> getVertexes() {
		return vertexes;
	}

	//returns the list of Edges
	public List<Edge> getEdges() {
		return edges;
	}

	/*
	 * Adds the vertex to the list of Nodes
	 * @param:
	 * 	String website - the website that is turned into a vertex
	 */
	public boolean addNodes(String website) {
		if(getNumber(website) == -1) {
			Vertex v = new Vertex(website);
			this.vertexes.add(v);
			this.numOfNodes++;
			return true;
		} else 
			return false;
	}

	/*
	 * Creates a lane from one Node to another
	 * @param: 
	 * 	int sourceLocNo - the source node number
	 * 	int destLocNo - the destination noce number
	 * 	int duration - the time/weight it takes from source to destination
	 */
	public void addLane(int sourceLocNo, int destLocNo, int duration) {
		Graph.Edge lane = new Edge(vertexes.get(sourceLocNo), vertexes.get(destLocNo), duration);
		edges.add(lane);
	}

	/*
	 * Gets the number of where the website is in the list
	 * @param:
	 * 	String website - the name of the website
	 * @return:
	 * 	int - the number or -1 if not in the list
	 */
	public int getNumber(String website) {
		int pos = 0;
		for(Vertex v : this.vertexes) {
			if(v.getId().equals(website))
				return pos;
			pos++;
		}

		return -1;
	}
	

	/*
	 * gets the number of disjoint sets
	 * @param:
	 * 	Graph g - the graph to check for number of disjoint sets
	 * return:
	 * 	int - the number
	 */
	public int getNumOfDisjointSets(Graph g) {
		int count = 0;
		DijkstraAlgorithm shortest = new DijkstraAlgorithm(g);
		LinkedList<Graph.Vertex> path = null;
		String website = "https://en.wikipedia.org/wiki/Hash_table";
		//Iterates through the graph and checks if there is a path from the node to the current vertex
		for(Vertex v : this.vertexes) {
			shortest.execute(g.getVertexes().get(g.getNumber(website)));
			path = shortest.getPath(v);
			if(path == null)
				count++;
		}
		return count;
	}
}
