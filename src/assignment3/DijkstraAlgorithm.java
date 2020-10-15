package assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {

	private final List<Graph.Vertex> nodes;
	private final List<Graph.Edge> edges;
	private Set<Graph.Vertex> settledNodes;
	private Set<Graph.Vertex> unSettledNodes;
	private Map<Graph.Vertex, Graph.Vertex> predecessors;
	private Map<Graph.Vertex, Integer> distance;

	//Constructor for nodes and edges
	public DijkstraAlgorithm(Graph graph) {
		this.nodes = new ArrayList<Graph.Vertex>(graph.getVertexes());
		this.edges = new ArrayList<Graph.Edge>(graph.getEdges());
	}
	
	/*
	 * main runner method for the algorithm
	 * @param:
	 * 	Graph.Vertex source - the start node
	 */
	public void execute(Graph.Vertex source) {
		settledNodes = new HashSet<Graph.Vertex>();
		unSettledNodes = new HashSet<Graph.Vertex>();
		distance = new HashMap<Graph.Vertex, Integer>();
		predecessors = new HashMap<Graph.Vertex, Graph.Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Graph.Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}
	
	/*
	 * Sets the distance, predecessors and unsettled nodes
	 * @param:
	 * 	Graph.Vertex node - the starting node
	 */
	private void findMinimalDistances(Graph.Vertex node) {
		List<Graph.Vertex> adjacentNodes = getNeighbors(node);
		for (Graph.Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)+ getDistance(node, target)) {
				distance.put(target, getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
	}
	
	/*
	 * gets the distance between two vertexes
	 * @param:
	 * 	Graph.vertex node - the starting node
	 * 	Graph.Vertex target - destination node
	 * @return:
	 * 	int - the weighted distance between the nodes
	 */
	private int getDistance(Graph.Vertex node, Graph.Vertex target) {
		for (Graph.Edge edge : edges) {
			if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("");
	}

	/*
	 * returns a list of neighors
	 * @param:
	 * 	Graph.Vertex node - the node that is being looked at
	 * @return:
	 * 	List<Graph.Vertex> - the list of neighbors
	 */
	private List<Graph.Vertex> getNeighbors(Graph.Vertex node) {
		List<Graph.Vertex> neighbors = new ArrayList<Graph.Vertex>();
		for (Graph.Edge edge : edges) {
			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	/*
	 * returns the vertex that is closer
	 * @param:
	 * 	Set<Graph.Vertex> vertexes - the set of vertexes
	 * @return:
	 * 	Graph.Vertex - the closer vertex
	 */
	private Graph.Vertex getMinimum(Set<Graph.Vertex> vertexes) {
		Graph.Vertex minimum = null;
		for (Graph.Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}
	
	/*
	 * checks whether the vertex has been visited
	 * @param: 
	 * 	Graph.Vertex vertex - the vertex
	 * @return 
	 * 	boolean true if the vertex has been visited, false otherwise
	 */
	private boolean isSettled(Graph.Vertex vertex) {
		return settledNodes.contains(vertex);
	}
	
	//gets the distance of one vertex to another
	private int getShortestDistance(Graph.Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * gets the path given by the target vertex
	 * @param:
	 * 	Graph.Vertex target - the target vertex
	 * @return:
	 * 	LinkedList<Graph.Vertex> - the path
	 */
	public LinkedList<Graph.Vertex> getPath(Graph.Vertex target) {
		LinkedList<Graph.Vertex> path = new LinkedList<Graph.Vertex>();
		Graph.Vertex step = target;
		// Check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

}
