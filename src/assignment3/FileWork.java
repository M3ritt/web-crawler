package assignment3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileWork {
	private String edgeFilePath = "C://Users//Josh//eclipse-workspace//365-programs//assignment3//src//files//edge.txt";
	private String vertexFilePath = "C://Users//Josh//eclipse-workspace//365-programs//assignment3//src//files//vertex.txt";
	private Graph g;

	public FileWork(Graph g) {
		this.g = g;
	}
	
	//returns the graph
	public Graph getGraph() {
		return this.g;
	}
	
	//updates the current graph
	public void updateGraph(Graph g) {
		this.g = g;
	}

	/*
	 * Writes to vertex.txt in the format:
	 * 'website', 'hashcode'
	 */
	public void writeVertex() {
		BufferedWriter writer;
		for(Graph.Vertex v : this.g.getVertexes()) {
			String data = v.getId() + ","+v.getidCode();
			try {
				writer = new BufferedWriter(new FileWriter(this.vertexFilePath, true));
				writer.newLine();
				writer.write(data);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * updates vertex.txt if more websites were added
	 * @param:
	 * 	String data - the data that is being added to vertex.txt
	 */
	public void updateVertex(String data) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(this.vertexFilePath, true));
			writer.newLine();
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * writes to edge.txt in the format:
	 * 'source', 'destination', 'time'
	 */
	public void writeEdge() {
		BufferedWriter writer;
		for(Graph.Edge e : this.g.getEdges()) {
			String data = e.getSource().getId() + "," + e.getDestination().getId() + "," + e.getWeight();
			//			System.out.println(data);
			try {
				writer = new BufferedWriter(new FileWriter(this.edgeFilePath, true));
				writer.newLine();
				writer.write(data);
				writer.close();
			} catch (IOException er) {
				er.printStackTrace();
			}
		}
	}
	
	/*
	 * updates edge.txt if more websites were added
	 * @param:
	 * 	String data - the data that is being added to edge.txt
	 */
	public void updateEdge(String data) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(this.edgeFilePath, true));
			writer.newLine();
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//reads vertex.txt and creates nodes from each line in the file
	public void readVertex() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(this.vertexFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(sc.hasNextLine()) {
			Scanner cs = new Scanner(sc.nextLine());
			while(cs.hasNext()) {
				String word = cs.next();
				int pos = word.indexOf(",");
				this.g.addNodes(word.substring(0,pos));
			}
		}
	}
	
	//reads edge.txt and creates lanes for each line in the file
	public void readEdge() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(this.edgeFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(sc.hasNextLine()) {
			Scanner cs = new Scanner(sc.nextLine());
			while(cs.hasNext()) {
				String word = cs.next();
//				System.out.println(word);
				String source, destination, weight;
				int pos = word.indexOf(",");
				source = word.substring(0,pos);
				word = word.substring(pos+1);
				pos = word.indexOf(",");
				destination = word.substring(0,pos);
				weight = word.substring(pos+1);
				int sourceLoc = this.g.getNumber(source);
				int destLoc = this.g.getNumber(destination);
				this.g.addLane(sourceLoc, destLoc, Integer.parseInt(weight));
			}
		}
	}
}
