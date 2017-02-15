
// 		 Name: 	Karthikeyan Thorali Krishnamurthy Ragunath
// Student id:  800936747
// 	  Uncc Id: 	kthorali@uncc.edu

// Graph.java
// Graph code, modified from code by Mark A Weiss.


import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.SortedSet;

// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException {

	public GraphException(String name) {
		super(name);
	}
}

// Represents a edge in the graph

class Edge implements Comparable {
	public Vertex dest; // Second vertex in Edge
	public double cost; // Edge cost
	public boolean up;

	public Edge(Vertex d, double c) {
		dest = d;
		cost = c;
		up = true;
	}

	public int compareTo(Object destt) {
		double otherCost = ((Edge) destt).cost;

		return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
	}
}

// Represents a vertex in the graph.
class Vertex implements Comparable<Vertex> {
	public String name; // Vertex name
	public List adj; // Adjacent vertices
	public Vertex prev; // Previous vertex on shortest path
	public double dist; // Distance of path
	public int visited;
	public boolean up;
	public int colour = 0;
	TreeSet<String> Reachable_vList;
	public int Heap_index;

	public Vertex(String nm) {
		name = nm;
		up = true;
		adj = new LinkedList<Vertex>();
		Reachable_vList = new TreeSet<String>();
		reset();
	}

	public void reset() {
		dist = graph.INFINITY;
		prev = null;
		visited = 0;
		colour = 0;
	}

	public int compareTo(Vertex other) {
		return Double.compare(dist, other.dist);
	}

}

// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w )
// --> Add additional edge
// void printPath( String w ) --> Print path after alg is run
// void unweighted( String s ) --> Single-source unweighted
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm. Exceptions are thrown if errors are detected.

public class graph {

	public static final int INFINITY = Integer.MAX_VALUE;
	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

	

	/**
	 * To extract the minimum element from the queue.
	 * 
	 * @param size
	 * @param vertexQueue
	 */

	public Vertex extractMin(Vertex[] vertexQueue, int totalVertices) {

		if (vertexQueue.length == 0) {
			throw new IllegalStateException("queue is EMPTY");
		} else if (vertexQueue.length == 1) {

			Vertex min = vertexQueue[0];
			vertexQueue[0] = vertexQueue[totalVertices - 1];
			return min;
		}
		Vertex Ver_min = vertexQueue[0];
		
		int V_q_length = vertexQueue.length;
		
		Vertex last_Vertex = vertexQueue[V_q_length - 1];
		
		if (last_Vertex != null) {
			vertexQueue[0] = last_Vertex;
			vertexQueue[0].Heap_index = 0;
			totalVertices--;
			minHeapify(vertexQueue, 0, totalVertices);
		}

		return Ver_min;
	}

	/*
	 *  To build the Heap logic. minimum elements to left and maximum elements to the right.
	 */
	private void buildHeapMinimum(Vertex[] vertexQueue, int totalVerticesize) {
		//int size1 = totalVerticesize / 2;
		for (int i = (totalVerticesize / 2) - 1; i >= 0; i--) {
			minHeapify(vertexQueue, i, totalVerticesize);
		}
	}
	
	/**
	 * To maintain the heap order.
	 * 
	 * @param vertexQueue
	 * @param heapIndexPosition
	 * @param key
	 * 
	 */

	public void Maintain_Heap_Order(Vertex[] vertexQueue, Vertex key, int heapIndexPosition) {
	
		vertexQueue[heapIndexPosition] = key;

		int parent = parent(heapIndexPosition);

		while (heapIndexPosition > 0 && vertexQueue[parent].dist > vertexQueue[heapIndexPosition].dist) {

			swap_least_parent(heapIndexPosition, parent, vertexQueue);
			heapIndexPosition = parent;
			parent = parent(parent);
		}
		
		
	}
	
	/**
	 * To Find the Heapify Minimum.
	 * Recursive call is made until heap order is maintained.
	 * 
	 * @param i
	 * @param size
	 * @param vertexQueue
	 */

	private void minHeapify(Vertex[] vertexQueue, int i, int size) {
		int left = left(i);
		int right = right(i);
		int least_element;

		if (left <= vertexQueue.length - 1 && vertexQueue[left].dist < vertexQueue[i].dist)
			least_element = left;
		else
			least_element = i;

		if (right <= vertexQueue.length - 1 && vertexQueue[right].dist < vertexQueue[least_element].dist)
			least_element = right;

		if (least_element != i) {

			swap_least_parent(i, least_element, vertexQueue);
			
			minHeapify(vertexQueue, least_element, size);
		}
	}
	
	/**
	 * To exchange the least element and the parent node.
	 * 
	 * @param index
	 * @param parent
	 * @param vertexQueue
	 */

	private void swap_least_parent(int index, int parent, Vertex[] vertexQueue) {

		Vertex temp = vertexQueue[parent];
		vertexQueue[parent] = vertexQueue[index];
		
		vertexQueue[index].Heap_index = index;
		temp.Heap_index = parent;
		
		vertexQueue[index] = temp;

	}

	//To get the parent node value.
	public int parent(int index) {

		if (index % 2 == 1) {
			return index / 2;
		}
		return (index - 1) / 2;
	}

	//To get the right node value.
	public int right(int index) {
		return 2 * index + 2;
	}
	
	//To get the left node value.
	private int left(int index) {
		return 2 * index + 1;
	}

	

	/**
	 * Up / Down a Vertex from the graph.
	 * 
	 * @param sourceName
	 * @param status
	 */

	public void updownVertex(String VertexName, boolean status) {

		Vertex v = vertexMap.get(VertexName);

		if (v != null) {
			if (status == false)
				v.up = false;
			else
				v.up = true;	
		}
	}


	/**
	 * Up / Down a edge from the graph.
	 * 
	 * @param sourceName
	 * @param destName
	 * @param status
	 */

	public void updownEdge(String sourceName, String destName, boolean status) {

		if (vertexMap.get(sourceName) != null && vertexMap.get(destName) != null) {

			Vertex v = getVertex(sourceName);

			for (Iterator itr = v.adj.iterator(); itr.hasNext();) {
				Edge e = (Edge) itr.next();
				Vertex w = e.dest;

				if (w.name.equals(vertexMap.get(destName).name)) {
					if (status == false)
						e.up = false;
					else
						e.up = true;
				}
			}
		} else
			System.out.println("Edge Not Present");

	}

	/**
	 * delete a edge from the graph.
	 * 
	 * @param sourceName
	 * @param destName
	 */

	public void deleteEdge(String sourceName, String destName) {

		if (vertexMap.get(sourceName) != null && vertexMap.get(destName) != null) {

			Vertex v = getVertex(sourceName);

			Vertex w = getVertex(destName);

			for (Iterator next_Edge = v.adj.iterator(); next_Edge.hasNext();) {
				Edge e = (Edge) next_Edge.next();

				if (w.name.equals(e.dest.name)) {
					next_Edge.remove();
					
				}
			}
		}
	}

	/**
	 * Add a new edge to the graph.
	 * 
	 * @param sourceName
	 * @param destName
	 * @param cost
	 * @param i
	 */
	public void addEdge(String sourceName, String destName, double cost, int i) {

		if (i == 0) {
			Vertex v = getVertex(sourceName);
			Vertex w = getVertex(destName);
			v.adj.add(new Edge(w, cost));
		}
		if (i == 1) {
			if (vertexMap.get(sourceName) != null && vertexMap.get(destName) != null) {

				Vertex v = getVertex(sourceName);
				Vertex w = getVertex(destName);
				Iterator it = v.adj.listIterator();
				while (it.hasNext()) {
					Edge edge = (Edge) it.next();
					if (destName.equals(edge.dest.name)) {
						it.remove();
					}
				}
				
			} else {
				Vertex v = getVertex(sourceName);
				Vertex w = getVertex(destName);
				v.adj.add(new Edge(w, cost));
			}
		}

	}

	/**
	 * Driver routine to print total distance. It calls recursive routine to
	 * print shortest path to destNode after a shortest path algorithm has run.
	 */
	public void printPath(String destName) {
		Vertex w = (Vertex) vertexMap.get(destName);

		if (w == null)
			throw new NoSuchElementException("Destination vertex not found");
		else if (w.dist == INFINITY)
			System.out.println(destName + " is unreachable");
		else {

			printPath(w);
			System.out.printf("%.2f \n", (double) w.dist);

		}
	}

	/**
	 * If vertexName is not present, add it to vertexMap. In either case, return
	 * the Vertex.
	 */
	private Vertex getVertex(String vertexName) {
		Vertex v = vertexMap.get(vertexName);
		if (v == null) {
			v = new Vertex(vertexName);
			vertexMap.put(vertexName, v);
		}
		return v;
	}

	/**
	 * Recursive routine to print shortest path to dest after running shortest
	 * path algorithm. The path is known to exist.
	 */
	private void printPath(Vertex dest) {
		if (dest.prev != null) {
			printPath(dest.prev);

		}
		System.out.print(dest.name + " ");
	}

	/**
	 * Initializes the vertex output info prior to running any shortest path
	 * algorithm.
	 */
	private void clearAll() {
		for (Vertex v : vertexMap.values())
			v.reset();
	}

	/**
	 * dijkstra algorithm.
	 * 
	 * @param string
	 */
	
	private void dijkstra_HeapOrder(String source) {

		clearAll();
		int UpVertices = 0;

		for (Vertex v : vertexMap.values()) {
			if (v.up)
				UpVertices++;
			v.Heap_index = 0;
		}
		Vertex[] vertexQueue = new Vertex[UpVertices];
		int index = 0;
		for (Vertex v : vertexMap.values()) {
			if (v.up) {
				vertexQueue[index++] = v;
				v.Heap_index = index;
			}
		}

		Vertex start = vertexMap.get(source);
		start.dist = 0;
		start.prev = null;

		int vertexQueueSize = UpVertices;
		
		// Time complexity is O(V)
		buildHeapMinimum(vertexQueue, vertexQueueSize);

		while (vertexQueueSize != 0) { //Time complexity is O(V)
			
			//Time complexity is O(log V)
			Vertex Ver_min = extractMin(vertexQueue, vertexQueueSize);
						
			////Time complexity is O(E)
			for (Iterator Next_Vertex = Ver_min.adj.iterator(); Next_Vertex.hasNext();) {
			
				Edge Next_Edge = (Edge) Next_Vertex.next();
			
				if (Next_Edge.up ) {
					
					Vertex w = vertexMap.get(Next_Edge.dest.name);
					
					if (Next_Edge.dest.up && w.dist > (Ver_min.dist + Next_Edge.cost)) {
						
						w.dist = (Ver_min.dist + Next_Edge.cost);
						w.prev = Ver_min;
						
						int heapIndexPosition = w.Heap_index;
						//Decrease Key is Used to maintain Heap order.
						//Time complexity is O(log V)
						Maintain_Heap_Order(vertexQueue,w, heapIndexPosition);
					}
				}
			}
			
			vertexQueueSize--;
		}

	}

		
	/*
	 * Finding the dijkstra path and print the path and the distance.
	 */
	
	public static void pathDijkstra(graph g, String source_new, String dest_new) {

		g.dijkstra_HeapOrder(source_new);

		g.printPath(dest_new);
	}

	/*
	 * Finding the Reachable Vertices
	 */

	public void Reachable(graph g) {
		SortedSet<String> keys = new TreeSet<String>(g.vertexMap.keySet());

		
			//Time complexity is V*(|V|)+|E|). Time complexity  for bfs is (|V|)+|E|) for each vertex.
		    //Time complexity to run bfs for v vertex is v times time complexity of BFS_Reachable.
		    //so the Time complexity is O(V *(|V|)+|E|))
		
		for (String key : keys) {

			Vertex start = (Vertex) g.vertexMap.get(key);

			if (start.up == true) {
				BFS_Reachable(start);
			}
		}
	}

	//bfs algorithm to calculate all reachable vertice. Time complexity is O(|V|)+|E|)
	public void BFS_Reachable(Vertex Source) {

		//Time complexity is O(V)
		for (Vertex v : vertexMap.values()) {
			v.colour = 0; // making the colour to white.
		}

		Vertex start = vertexMap.get(Source.name);

		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		PriorityQueue<Vertex> q = new PriorityQueue<Vertex>();

		start.dist = 0;
		start.colour = 1; //making the colour to gray.

		q.add(start);

		//Time complexity is O(|V|+|E|))
		while (!q.isEmpty()) {
			Vertex v = (Vertex) q.remove();

			for (Iterator itr = v.adj.iterator(); itr.hasNext();) {
				Edge e = (Edge) itr.next();
				Vertex w = e.dest;

				if (!(w.name.equals(Source.name)) && w.up == true && e.up == true) {

					if (w.colour == 0) {

						w.colour = 1;
						start.Reachable_vList.add(w.name.toString());
						q.add(w);
					}
				}
			}

			start.colour = 2; //Making the colour to Black.
		}

		System.out.println();
		System.out.print(start.name);
		
		// printing all valid reachable vertex. 
		for (String reachableVlist : start.Reachable_vList) {
			System.out.println(" ");
			System.out.println("  " + reachableVlist);
		}

	}

	/* Print the Graph */

	public static void executePrint(graph g) {

		int size = g.vertexMap.size();

		SortedSet<String> keys = new TreeSet<String>(g.vertexMap.keySet());

		for (String key : keys) {

			Vertex start = (Vertex) g.vertexMap.get(key);

			if (start.up == false) {
				System.out.println(start.name + " " + "DOWN");
			} else
				System.out.println(start.name);


			Collections.sort(start.adj, new Comparator<Edge>() {
				@Override
				public int compare(Edge e1, Edge e2) {
					return (e1.dest.name).compareTo(e2.dest.name);
				}
			});

			for (Iterator itr = start.adj.iterator(); itr.hasNext();) {
				Edge e = (Edge) itr.next();

				if (e.up == true) {

					System.out.println(" " + e.dest.name + " " + e.cost);
				} else {
					System.out.println(" " + e.dest.name + " " + e.cost + " " + "DOWN");
				}

			}

		}

	}

	/**
	 * A main routine that: 1. Reads a file containing edges (supplied as a
	 * command-line parameter); 2. Forms the graph; 3. Repeatedly prompts for
	 * two vertices and runs the shortest path algorithm. The data file is a
	 * sequence of lines of the format source destination
	 */
	public static void main(String[] args) {
		graph g = new graph();
		try {
			FileReader fin = new FileReader(args[0]);
			Scanner graphFile = new Scanner(fin);

			// Read the edges and insert
			String line;
			while (graphFile.hasNextLine()) {
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);

				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					String source = st.nextToken();
					String dest = st.nextToken();
					double cost = Double.parseDouble(st.nextToken());
					g.addEdge(source, dest, cost, 0);
					g.addEdge(dest, source, cost, 0);

				} catch (NumberFormatException e) {
					System.err.println("Skipping ill-formatted line " + line);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		System.out.println("File read...");
		System.out.println(g.vertexMap.size() + " vertices");

		Scanner in = new Scanner(System.in);

		String input1 = in.nextLine();

		while (!input1.equalsIgnoreCase("quit")) {

			if (!input1.isEmpty()) {

				StringTokenizer input = new StringTokenizer(input1);

				String input2 = input.nextToken();

				if (input2.equalsIgnoreCase("addedge")) {

					String source1 = input.nextToken();
					String dest1 = input.nextToken();
					double cost1 = Double.parseDouble(input.nextToken());
					g.addEdge(source1, dest1, cost1, 1);

				} else if (input2.equalsIgnoreCase("deleteedge")) {

					String source1 = input.nextToken();
					String dest1 = input.nextToken();

					g.deleteEdge(source1, dest1);

				} else if (input2.equalsIgnoreCase("edgedown")) {
					String source1 = input.nextToken();
					String dest1 = input.nextToken();

					g.updownEdge(source1, dest1, false);

				} else if (input2.equalsIgnoreCase("edgeup")) {

					String source1 = input.nextToken();
					String dest1 = input.nextToken();

					g.updownEdge(source1, dest1, true);

				} else if (input2.equalsIgnoreCase("vertexdown")) {

					String VertexName = input.nextToken();
					g.updownVertex(VertexName, false);

				} else if (input2.equalsIgnoreCase("vertexup")) {

					String VertexName = input.nextToken();

					g.updownVertex(VertexName, true);

				} else if (input2.equalsIgnoreCase("path")) {

					String source_new = input.nextToken();
					String dest_new = input.nextToken();

					pathDijkstra(g, source_new, dest_new);

				} else if (input2.equalsIgnoreCase("print")) {

					executePrint(g);

				} else if (input2.equalsIgnoreCase("reachable")) {

					g.Reachable(g);
					
				}
			}
			
			input1 = in.nextLine();
		}
	}
}