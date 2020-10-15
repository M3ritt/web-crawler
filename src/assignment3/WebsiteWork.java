package assignment3;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import assignment3.Graph.Edge;
import assignment3.Graph.Vertex;

public class WebsiteWork {

	private FileWork fw;
	private Graph g;
	private List<Vertex> vertexes;
	private List<Edge> edges;
	private int maxNum = 520;
	private int numOfWebsites = 0;
	private int count = 0;
	private ArrayList<String> websites;

	public WebsiteWork(FileWork fw, Graph g) {
		this.fw = fw;
		this.g = g;
		this.vertexes = g.getVertexes();
		this.edges = g.getEdges();
	}

	//gets the graph
	public Graph getGraph() {
		return this.g;
	}

	/*
	 * adds a website to the list of websites if less than maxNum of websites
	 * @param:
	 * 	String currWebsite - the current website
	 */
	public boolean addWebsite(String currWebsite) {
		if(this.numOfWebsites < maxNum) {
			if(this.g.addNodes(currWebsite)) {
				this.numOfWebsites++;
				return true;
			}
		}
		return false;
	}

	/*
	 * Adds the link with the weight to the destination
	 * @param:
	 * 	String currWebsite - the current website
	 * 	String nextWebsite - the destination website
	 * 	int weight - the time/weight it takes/has
	 */
	public void addLinks(String currWebsite, String nextWebsite, int weight) {
		int currWebsiteNum = this.g.getNumber(currWebsite);
		int nextWebsiteNum = this.g.getNumber(nextWebsite);
		this.g.addLane(currWebsiteNum, nextWebsiteNum, weight);
	}

	public void getLinks(String url, boolean override) {
		if(url.equalsIgnoreCase("exit"))
			return;
		System.out.println(url);
		if(override)
			this.maxNum += 50;
		Document doc;
		String currentLink = "";
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a");
			for(Element link : links) {
				currentLink = link.absUrl("href");
				if(this.numOfWebsites < this.maxNum && !currentLink.equals("") && skipWebsite(currentLink) == false) {
					getLinksHelper(url, currentLink, override);
				}
			}
		} catch (Exception e) {
			System.out.print("");
			//			e.printStackTrace();
		}
	}

	public void getLinksHelper(String url, String currentLink, boolean override) {
		String nextLink = "";
		if(this.count == 0)
			nextLink = currentLink;
		if(addWebsite(currentLink) == false)
			getLinks("exit", true);
		this.count++;
		if(this.count < 50) {
			try {
				HashTable words = getAWebsiteData(url);
				HashTable words2 = getAWebsiteData(currentLink);
				int num = -words.compareTables(words2);
				addLinks(url, currentLink, num);
				if(override) {
					updatingFiles(url, currentLink, num);
					override = false;
				}
			} catch (Exception e) {
				this.count--;
			}
		} 
		//else getLinks(nextLink, false);
	}

	public void updatingFiles(String url, String destination, int weight) {
		String data;
		this.g.addNodes(destination);
		data = destination+","+this.g.getVertexes().get(this.g.getNumber(destination)).getidCode();
		fw.updateVertex(data);
		addLinks(url, destination, weight);
		data = url+","+destination+","+weight;
		fw.updateEdge(data);
	}

	/*
	 * determines whether to skip the website or not
	 * @param:
	 * 	String url - the url of the website
	 * @return:
	 * 	true if the program should ignore the website
	 * 	false if the website is valid
	 */
	public boolean skipWebsite(String url) {
		if(url.contains("#") || url.contains("?") || url.contains("%") ||  url.contains(".png") || url.contains(".svg") || 
				url.substring(10).contains(":") || url.contains("wikipeida") || url.contains(".pdf"))
			return true;
		return false;
	}

	public void getWebsiteData(String url, boolean override) { 
		if(this.g.getNumber(url) != -1)
			return;
		String data = "";
		try {
			data  = Jsoup.connect(url).get().text();
			this.g.addNodes(url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		getLinks(url, override);
	}

	public HashTable getAWebsiteData(String url) {
		HashTable words = new HashTable();
		String data = "";
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

		try {
			data = Jsoup.connect(url).get().text();
		} catch (Exception e) {
			System.out.print("");
			//			e.printStackTrace();
		}
		Matcher m = p.matcher(data);
		List<String> temp = Arrays.asList(data.split(" "));
		for(String s : temp) {
			if(m.find() == false) {
				words.add(s, 1);
			}
		}
		return words;
	}
}
