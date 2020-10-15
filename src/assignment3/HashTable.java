package assignment3;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashTable <V>{

	//Class for the Nodes within the table table
	public class Node<V>{
		//name of website/word
		String key;
		//name of cached time
		V value;
		//frequency of word count
		int frequency = 1;
		//reference to next node
		Node<V> next;

		public Node(String key) {
			this.key = key;
			this.value = value;
		}

		public void increaseFrequency() {
			this.frequency++;
		}

		@Override
		public String toString() {
			return "[" + key + ", " + frequency+ "]";
		}
	}

	//stores chains
	private ArrayList<Node<V>> array; 
	//max size
	private int arrayMax; 
	//current size
	private int size; 

	public HashTable() { 
		array = new ArrayList<>(); 
		arrayMax = 128; 
		size = 0; 

		//initialization
		for (int i = 0; i < arrayMax; i++) 
			array.add(null); 
	} 
	
	public ArrayList<Node<V>> getNodes(){
		return this.array;
	}

	//returns the size of the arrayList
	public int getSize() { 
		return size; 
	} 

	//returns true or false whether the array is empty or not
	public boolean isEmpty() { 
		if(getSize() == 0)
			return true;
		else
			return false;
	} 

	/*
	 * @param:
	 * 	String key - is the key of the Node 
	 * @return:
	 *  int index - is the index of the key after determining the hash code for it
	 */
	private int getIndex(String key) { 
		int hashCode = key.hashCode(); 
		int index = hashCode % arrayMax; 
		return index; 
	} 

	/*
	 * @param:
	 * 	String key - is the key of the node that is being searched and removed
	 * @return 
	 * 	V - the value of the Node that was removed
	 */
	public V remove(String key) { 
		int index = getIndex(key); 

		// Get head of chain 
		Node<V> head = array.get(index); 

		// Search for key in its chain 
		Node<V> prev = null; 
		while (head != null) { 
			if (head.key.equals(key)) 
				break; 
			prev = head; 
			head = head.next; 
		} 

		//If key is not found, return null. Otherwise decrease size and remove
		if (head == null) 
			return null; 
		size--; 
		if (prev != null) 
			prev.next = head.next; 
		else
			array.set(index, head.next); 

		return head.value; 
	} 

	/*
	 * @param:
	 * 	String key - is the key of the Node that is being searched
	 * @return:
	 * 	V - the value of the Node that was searched
	 */
	public V getValue(String key) { 
		int index = getIndex(key); 
		Node<V> head = array.get(index); 

		while (head != null) { 
			if (head.key.equals(key)) 
				return head.value; 
			head = head.next; 
		} 
		return null; 
	} 

	/*
	 * @param:
	 * 	String key - is the key of the Node that is being searched
	 * @return:
	 * 	boolean - returns true or false whether the key was found
	 */
	public boolean contains(String key) {
		int index = getIndex(key); 
		if(index < 0)
			return false;
		Node<V> head = array.get(index); 

		while (head != null) { 
			if (head.key.equals(key)) 
				return true; 
			head = head.next; 
		} 
		return false;
	}

	/*
	 * @param:
	 * 	String key - is the key of the Node that is being searched
	 * @return:
	 * 	Node - the Node that was searched
	 */
	public Node getNode(String key) {
		int index = getIndex(key); 
		if(index < 0)
			return null;
		Node<V> head = array.get(index); 

		while (head != null) { 
			if (head.key.equals(key)) 
				return head; 
			head = head.next; 
		} 
		return null; 
	}

	/*
	 * Adds the key and value to a node into the hash table
	 * @param:
	 * 	String key - the key of the Node to be added 
	 * 	V value - the value of the Node to be added
	 */
	public void add(String key, V value) { 
		key = removeSymbols(key);
		/*
		 * This if statement is used for the word frequencies
		 *  - starts with checking whether the word is already in the hash table
		 *  - if the word is: increases the frequency and leaves the method
		 *  - otherwise, does the rest of the method
		 */
		if(contains(key)) {
			Node temp = getNode(key);
			temp.increaseFrequency();
			size++;
			return;
		}
		//gets the location of the key
		int index = getIndex(key); 
		if(index < 0)
			return;
		Node<V> head = array.get(index); 

		while (head != null) { 
			if (head.key.equals(key)) { 
				head.value = value; 
				return; 
			} 
			head = head.next;
		} 

		size++; 
		head = array.get(index); 
		Node<V> newNode = new Node<V>(key); 
		newNode.next = head; 
		array.set(index, newNode); 

		//Resize table if above threshold
		if ((1.0*size)/arrayMax >= 0.75) { 
			ArrayList<Node<V>> temp = array; 
			array = new ArrayList<>(); 
			arrayMax = 2 * arrayMax; 
			size = 0; 
			for (int i = 0; i < arrayMax; i++) 
				array.add(null); 

			for (Node<V> headNode : temp) { 
				while (headNode != null) { 
					add(headNode.key, headNode.value); 
					headNode = headNode.next; 
				} 
			} 
		} 
		//end of resizing
	}

	/*
	 * Creates a new file name removing all symbols from the url using pattern matching
	 * @param 
	 * 	String url - the url of the website
	 * @return 
	 * 	returns the string of the file name
	 */
	public String removeSymbols(String word) {
		Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
		Matcher match= pt.matcher(word);
		while(match.find()){
			String s= match.group();
			word=word.replaceAll("\\"+s, "");
		}
		return word;
	}

	/*
	 * prints the map of non-null nodes
	 */
	public void printMap() {
		for(int i = 0; i < size; i++) {
			for(Node n : this.array) {
				if(n != null)
					System.out.println(n);
			}
		}
	}

	/*
	 * Calculates a number based on how similar two hash tables are
	 * 1 Hash table is the table that was searched which is the current one
	 * @param:
	 * 	ArrayList<Node<V>> comparing - the other hash table to be compared to
	 * @return:
	 * 	the int of how similar the tables are: the higher the number the more similar they are
	 */
	public int compareTables(HashTable<Integer> comparing) {
		int similarity = 0;
		int tempFrequency = 0;
		double num = 0.00;
		for(int i = 0; i < size; i++) {
			for(Node n : this.array) {
				if(n != null) {
					if(comparing.contains(n.key)) {
						tempFrequency = comparing.getNode(n.key).frequency;
						if(n.key.length() > 4) {
							if(n.frequency > tempFrequency)
								similarity += 5*tempFrequency;
							else
								similarity += 5*n.frequency;
						} else {
							if(n.frequency > tempFrequency)
								similarity += (tempFrequency);
							else 
								similarity += n.frequency;
						}
					}
				}
			}
		}
		num = (similarity/size) * (similarity/comparing.getSize());
		num = (1/num);
		String s = String.valueOf(num);
		s = s.substring(0,3);
		num = Double.parseDouble(s);
		return (int)num;
	}
	
	public Cluster setCluster(String website, HashTable temp) {
		ArrayList<Cluster.Point> points =  new ArrayList<>();
		Cluster clust = new Cluster(website, points);
		for(int i = 0; i < size; i++) {
			for(Node n : this.array) {
				if(n != null)
					clust.addPoint((long)n.key.hashCode(), n.frequency);
			}
		}
		return clust;
	}
}
