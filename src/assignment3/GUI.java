package assignment3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class GUI extends JFrame implements ActionListener{
	private WebsiteWork ww;
	private FileWork fw;
	private Graph g;

	private JFrame frame;
	private JTextField url;

	private String[] websiteChoices;
	private JComboBox messageList, messageList2;
	private JLabel label = new JLabel();
	private JLabel label2 = new JLabel();

	private boolean website1 = false, website2 = false;
	private String start = "", end = "", shortestPath= "No valid path";

	private JButton addMoreWebsites;
	private String websiteURL;
	private JLabel moreWebsites;
	private JButton search, okay;

	private JLabel disjointSets = new JLabel();

	private KMedoid medoid;

	public GUI(WebsiteWork ww, FileWork fw, Graph g) {
		this.ww = ww;
		this.fw = fw;
		this.g = g;
		this.websiteChoices = new String[g.getNumOfNodes()];
	}

	public void setUpChoices() {
		int count = 0;
		for(Graph.Vertex v : this.g.getVertexes()) {
			this.websiteChoices[count] = v.getId();
			count++;
		}
		this.messageList = new JComboBox(this.websiteChoices);
		this.messageList2 = new JComboBox(this.websiteChoices);
	}

	public void startScreen() {
		setUpChoices();
		this.frame = new JFrame("Assignment3");
		this.frame.setLayout(new FlowLayout());
		this.frame.setSize(500,500);
		this.frame.setTitle("Assignment 3");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel startNode = new JLabel("start");
		startNode.setText("Click the starting website");
		this.frame.add(startNode);
		this.messageList.setPrototypeDisplayValue("bettttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		this.messageList.setSelectedIndex(0);
		this.messageList.addActionListener(this);
		this.frame.add(this.messageList);
		this.frame.add(this.label);

		JLabel endNode = new JLabel("end");
		endNode.setText("Click the ending website");
		this.frame.add(endNode);
		this.messageList2.setPrototypeDisplayValue("bettttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		this.messageList2.setSelectedIndex(0);
		this.messageList2.addActionListener(this);
		this.frame.add(this.messageList2);
		this.frame.add(this.label2);

		this.addMoreWebsites = new JButton("Add more websites");
		this.addMoreWebsites.addActionListener(this);
		this.addMoreWebsites.setPreferredSize(new Dimension(250,75));
		this.addMoreWebsites.setLocation(500,500);
		this.frame.getContentPane().add(this.addMoreWebsites);

		this.frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.messageList) {
			JComboBox cb = (JComboBox)e.getSource();
			this.start = (String)cb.getSelectedItem();
			this.label.setText(String.format("<html><div WIDTH=%d>%s</div></html>", 450, start));
			this.website1 = true;
		}

		if(e.getSource() == this.messageList2) {
			JComboBox cb = (JComboBox)e.getSource();
			this.end = (String)cb.getSelectedItem();
			this.label2.setText(String.format("<html><div WIDTH=%d>%s</div></html>", 450, end));
			this.website2 = true;
		}

		//Actually adding more websites
		if(e.getSource() == this.search) {
			String searching = this.url.getText();
			this.ww.getWebsiteData(searching, true);
			this.g = this.ww.getGraph();
			this.websiteChoices = new String[this.g.getNumOfNodes()];
			setUpChoices();
			this.frame.setVisible(false);
			startScreen();
		}

		//User decided to add more websites
		if(e.getSource() == this.addMoreWebsites) {
			this.frame.setVisible(false);
			addMoreWebsitesScreen();
		}

		if(e.getSource() == this.okay) {
			this.frame.setVisible(false);
			this.website1 = false;
			this.website2 = false;
			this.start = "";
			this.end = "";
			this.label = new JLabel();
			this.label2 = new JLabel();
			startScreen();
		}

		if(this.website1 && this.website2) {
			this.frame.setVisible(false);
			resultScreen(start, end);
		}
	}

	public void resultScreen(String start, String end) {
		this.shortestPath= "No valid path";
		DijkstraAlgorithm shortest = new DijkstraAlgorithm(this.g);
		shortest.execute(this.g.getVertexes().get(this.g.getNumber(start)));
		LinkedList<Graph.Vertex> path = shortest.getPath(this.g.getVertexes().get(this.g.getNumber(end)));
		int count = 0;
		if(start.equals(end))
			this.shortestPath = "It's the same website!";
		else if(path != null) {
			this.shortestPath = "";
			for (Graph.Vertex vertex : path) {
				if(count == 0) { 
					if(checkingClusters().isWebsiteInCluster(vertex.getId()))
						this.shortestPath = vertex.getId() + "(medoid)";
					else 
						this.shortestPath = vertex.getId() + "(not medoid)";
				} else {
					if(checkingClusters().isWebsiteInCluster(vertex.getId()))
						this.shortestPath =this.shortestPath + " --> " + vertex.getId()+ "(medoid)";
					else 
						this.shortestPath =this.shortestPath + " --> " + vertex.getId() +" (not medoid)";
				}
				count++;
			}
		} 
		this.frame = new JFrame("Assignment3");
		this.frame.setLayout(new FlowLayout());
		this.frame.setSize(500,500);
		this.frame.setTitle("Assignment 3");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.disjointSets = new JLabel("sets");
		this.disjointSets.setText(String.format("<html><div WIDTH=%d>%s</div></html>", 450, "Number of disjoint sets: "+this.g.getNumOfDisjointSets(this.g)));
		this.frame.add(disjointSets);

		JLabel startingNode = new JLabel ("start");
		startingNode.setText("Start: "+this.start);
		this.frame.add(startingNode);

		JLabel pathNodes = new JLabel("pathNodes");
		pathNodes.setText(String.format("<html><div WIDTH=%d>%s</div></html>", 450, this.shortestPath));
		this.frame.add(pathNodes);

		JLabel endingNode = new JLabel ("end");
		endingNode.setText("End: "+this.end);
		this.frame.add(endingNode);

		this.okay = new JButton("okay");
		this.okay.addActionListener(this);
		this.okay.setPreferredSize(new Dimension(75,75));
		this.frame.getContentPane().add(this.okay);

		this.frame.setVisible(true);
	}

	public void addMoreWebsitesScreen() {
		this.frame = new JFrame("Assignment3");
		this.frame.setLayout(new FlowLayout());
		this.frame.setSize(300,300);
		this.frame.setTitle("Assignment 3");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.moreWebsites = new JLabel("more websites");
		moreWebsites.setText("Enter a new website to search");
		this.frame.add(moreWebsites);

		this.url = new JTextField(25);
		this.frame.add(url);
		this.search = new JButton("search");
		this.search.addActionListener(this);
		this.search.setPreferredSize(new Dimension(75,75));
		frame.getContentPane().add(this.search);
		this.frame.setVisible(true);
	}

	public KMedoid checkingClusters() {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		HashTable tempTable = new HashTable();
		int count = 0;
		for(Graph.Vertex v : this.g.getVertexes()) {
			if(v != null) {
				if(count % 100 == 0){
					clusters.add(tempTable.setCluster(v.getId(), this.ww.getAWebsiteData(v.getId())));
				}
				count++;
			}
		}
		KMedoid temp = new KMedoid(clusters);
		return temp;
	}

}
