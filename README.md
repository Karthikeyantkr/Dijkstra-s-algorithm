
-------------------------Graph Implementation using Dijsktra and BFS with added Graph Functions-------------------------------


About
-----

The Graph functions like addedge, vertexup etc is used. 
The Dijsktra algorithm and BFS are used to compute the shortest path and the reachable vertices.


Build / Compile the program
---------------------------

Before building the class file make sure java(i.e jdk1.8) is installed in the system.

	javac filename.java

Once the above command is executed, a class file named filename.class is created

Run the Program
---------------
A argument is passed to the class file as below.

Argument is 
1. InputfileName - The file which contains the Vertices and edges and their weight.

	java filename InputfileName

Once executed,
* Control awaits for your query.
* Different query like "path Belk Education", "Vertexup VertexName" should be specified.

* Giving the input "quit" or "Quit" will exit the program.

Program Descriptions
----------------------

Programming language used : JAVA

Compiler Version: 1.8

* The HasMap Datastructure is used to create the Vertices which will hold the Vertices and their respective names.
	
	Syntax:
	-------

	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

* The Treeset datastructure is used to maintain the list of vertices, this makes the vertices in sorted order.
	 
* extractMin is used to get the minimum element in the Heap; 
* Maintain_Heap_Order(decreaseKey) is used to maintain the heap order, taking the Current position of the Vertex and Vertex itself;
* minHeapify is used to maintain the HeapOrder after Extracting of minimum element;Recursive call is made until heap order is maintained.
* Class Edge contains destination vertex, weight and also maintains the status of the edge.
* Class Vertex contains source vertex, adjacent list of vertices, distance and also maintains the status of the vertex.	

	Graph.java
	------------
	
	1. The while loop in main function which takes query to be processed which reads the input query and redirect to appropriate method of execution.
	2. executePrint is a void method, which prints the entire vertex name of the Graph g 
	3. addEdge is a void method, which adds Source and Dest edge in this order and with the given weight.
	4. deleteEdge is a void method, which removes the given Source and Dest.
	5. dijkstra_HeapOrder, is the method for finding the shortest path using Dijstra's algorithm.
	6. BFS_Reachable, is the method to get the reachable vertices and print in alphabetical Order.
		This takes time complexity O(|V|)+|E|). Total time complexity to find reachable vertices is O(V *(|V|)+|E|)).


What works well?
----------------

	The Program works for all the inputs as specified in the project description which was given. 
	The program computes Dijsktra path using decreaseKey and maintains heap order.
	
