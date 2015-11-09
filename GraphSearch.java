import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public abstract class GraphSearch {

	private Node[][] map;
	private ArrayList<Node> CLOSED;
	private Node start;
	private Node goal;
	private ArrayList<Action> actions;
	private int numberOfDiagInfoLines;
	private String output;
	private String outputFile;

	/**
	 * Constructor for GraphSearch
	 * 
	 * @param inputArray
	 *            , an array of strings on the form it is created in DriverBeta
	 * @param inputFile
	 *            , the name of the input file
	 */
	public GraphSearch(ArrayList<String> inputArray, String inputFile) {
		createMap(inputArray);
		createActionList();
		this.numberOfDiagInfoLines = Integer.parseInt(inputArray.get(1));
		this.CLOSED = new ArrayList<Node>();
		this.output = "";
		this.outputFile = inputFile.replace("input", "output");
	}

	/**
	 * Creates a list over all possible actions of the search problem
	 */
	private void createActionList() {
		this.actions = new ArrayList<Action>();
		this.actions.add(new Action(Action.Move.R));
		this.actions.add(new Action(Action.Move.RD));
		this.actions.add(new Action(Action.Move.D));
		this.actions.add(new Action(Action.Move.LD));
		this.actions.add(new Action(Action.Move.L));
		this.actions.add(new Action(Action.Move.LU));
		this.actions.add(new Action(Action.Move.U));
		this.actions.add(new Action(Action.Move.RU));
	}

	/**
	 * Sets up configurations and runs the algorithm.
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void run() throws FileNotFoundException,
			UnsupportedEncodingException {
		String solution = search();
		if (solution == null)
			solution = "NO PATH\n";
		String temp = output;
		output = solution + temp;

		File file = new File(
				"/home/kristine/workspace/FIT5047_Assignment1/output/"
						+ outputFile);
		file.getParentFile().mkdirs();

		PrintWriter writer = new PrintWriter(file);
		writer.write(output);
		writer.close();
	}

	/**
	 * Searches for a solution to the given input file. The method runs the main
	 * graph search algorithm and distinguishes between the different search
	 * algorithms by calling the abstract methods getNodeFromOpen and
	 * putNodesInOpen, which is implemented differently in each algorithm
	 * subclass according to the requirements of the algorithms.
	 * 
	 * @return The solution found by the algorithm.
	 */
	public String search() {

		putNodeInOpen(start);
		int counter = 0; // for diagnosis information control

		// main loop
		while (true) {
			if (openIsEmpty()){
				System.out.println("Number of iterations: " + counter);
				return null;
			}

			Node n = getNodeFromOpen();
			removeNodeFromOpen(n);
			CLOSED.add(n);

			// check for goal state
			if (n.getType() == 'G') {
				System.out.println("Number of iterations: " + counter);
				return constructPath(start, n);
			}

			List<Node> successors = expand(n);

			List<Node> nodesToAdd = new ArrayList<Node>();

			// only put non-ancestors as successors of n
			for (Node suc : successors) {
				if (!n.getParents().contains(suc)) {
					n.addSuccessor(suc);
					suc.addParent(n);

					suc.addEdge(n, suc);
					suc.setTimeOfGeneration(counter);

					if(!CLOSED.contains(suc)){
						nodesToAdd.add(suc);
					}
				}
			}
			putNodesInOpen(nodesToAdd);

			if (counter < numberOfDiagInfoLines)
				print_diag_inf(n, CLOSED, getOpenList());
			counter++;
		}
	}

	/**
	 * Expands the given node by applying all actions to the node. The method
	 * also checks for mountains on the resulting node and mountains surrounding
	 * the resulting node, in order to ensure only valid nodes are returned.
	 * 
	 * @param n, the node to be expanded
	 * @return a list of nodes resulting from the expansion (the child nodes)
	 */
	private List<Node> expand(Node n) {

		// apply all possible actions
		List<Node> successors = new ArrayList<Node>();
		for (Action a : actions) {
			Node successor = a.computeResult(n, map);

			if (successor != null) {
				successor.setAction(a);
				successor.setG(this, a, n.getG());
				
				int suc_c = successor.getColumn();
				int suc_r = successor.getRow();

				// check for mountains on new node
				if (successor.getType() != 'X') {
					
					// check for mountains surrounding new node
					switch (a.getMove()) {
					case RD:
						if (suc_c < map.length
								&& map[suc_r][suc_c - 1].getType() != 'X'
								&& suc_r < map.length
								&& map[suc_r - 1][suc_c].getType() != 'X') {
							successors.add(successor);
						}
						break;
					case LD:
						if (suc_c >= 0
								&& map[suc_r][suc_c + 1].getType() != 'X'
								&& suc_r < map.length
								&& map[suc_r - 1][suc_c].getType() != 'X') {

							successors.add(successor);
						}
						break;
					case LU:
						if (suc_c >= 0
								&& map[suc_r][suc_c + 1].getType() != 'X'
								&& suc_r >= 0
								&& map[suc_r + 1][suc_c].getType() != 'X') {
							successors.add(successor);
						}
						break;
					case RU:
						if (suc_c < map.length
								&& map[suc_r][suc_c - 1].getType() != 'X'
								&& suc_r >= 0
								&& map[suc_r + 1][suc_c].getType() != 'X') {
							successors.add(successor);
						}
						break;
					default: // moving straight, i.e. not having to worry about
								// surrounding mountains
						successors.add(successor);
						break;
					}
				}
			}
		}
		return successors;
	}

	/**
	 * Constructs a string representing the resulting solution found, i.e. a
	 * path from the start node to the goal node
	 * 
	 * @param graph
	 * @param start
	 * @param goal
	 * @return
	 */
	private String constructPath(Node start, Node goal) {
		String solution = "" + goal.getType();
		Node tempnode = goal;
		int cost = 0;
		while (tempnode != null && !tempnode.getEdges().isEmpty()) {
			for (Edge e : tempnode.getEdges()) {
				if (tempnode.getParents().contains(e.getStart())) {
					String temp = solution;
					solution = tempnode.getAction().getMove() + "-" + temp;
					cost += tempnode.getAction().getCost();
					tempnode = e.getStart();
				}
			}
		}
		String temp = solution;
		solution = tempnode.getType() + "-" + temp + " " + cost + "\n";

		return solution;
	}

	/**
	 * Parses the map given in the input file to a 2D array
	 * 
	 * @param inputArray
	 */
	private void createMap(ArrayList<String> inputArray) {

		// initialize 2d map array to correct size
		int size = Integer.parseInt(inputArray.get(2));
		map = new Node[size][size];

		// parse the map
		for (int i = 0; i < size; i++) {
			String row = inputArray.get(3 + i);
			for (int j = 0; j < size; j++) {
				char type = row.charAt(j);
				map[i][j] = new Node(type, i, j);
				if (type == 'S')
					this.start = map[i][j];
				if (type == 'G')
					this.goal = map[i][j];
			}
		}
		printMap(map);
	}

	/**
	 * Prints the parsed map. (for debugging purposes)
	 * 
	 * @param map
	 *            the map to print
	 * @return a string representation of the map
	 */
	public String printMap(Node[][] map) {
		String mapstr = "";
		String row = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				row = row + map[i][j].getType() + " ";
			}
			mapstr = mapstr + row + "\n";
			row = "";
		}
		return mapstr;
	}

	/**
	 * Appends diagnosis information for one iteration of the search algorithm to the string "output".
	 * This information consists of 3 lines:
	 * The first line contains the path to the node being expanded in this iteration along with the values of g, h and f.
	 * The second line contains nodes (and their paths) in the OPEN list.
	 * The third line contains nodes (and their paths) in the CLOSED list.
	 * 
	 * @param node, the node being expanded
	 * @param CLOSED, the CLOSED list
	 * @param OPEN, the OPEN list
	 */
	private void print_diag_inf(Node node, ArrayList<Node> CLOSED,
			Collection<Node> OPEN) {

		/********** First line - expansion **********/
		String line1 = "";
		Node tempnode = node;
		int h = node.getH();
		int g = node.getG();
		int f = node.getF();
		String nstr = "";
		String temp = "";
		while (tempnode != null && !tempnode.getEdges().isEmpty()) {
			for (Edge e : tempnode.getEdges()) {
				if (tempnode.getParents().contains(e.getStart())) {
					temp = nstr;
					if (temp.length() == 0)
						nstr = tempnode.getAction().getMove().toString();
					else
						nstr = tempnode.getAction().getMove().toString() + "-"
								+ temp;
					tempnode = e.getStart();
				}
			}
		}
		if (nstr.length() == 0)
			temp = "" + tempnode.getType();
		else
			temp = tempnode.getType() + "-" + nstr;
		line1 = temp;

		temp = output;
		output = temp + line1 + " " + g + " " + h + " " + f + "\n";

		/********** Second line - open list **********/
		String line2 = "";
		while (!OPEN.isEmpty()) {
			// BFS
			if (OPEN instanceof LinkedList<?>)
				tempnode = (Node) ((LinkedList<Node>) OPEN).poll();
			// DFS
			else if (OPEN instanceof Stack<?>)
				tempnode = (Node) ((Stack<Node>) OPEN).pop();
			// UCS
			else if (OPEN instanceof PriorityQueue<?>)
				tempnode = (Node) ((PriorityQueue<Node>) OPEN).poll();
			nstr = "";
			temp = "";
			while (tempnode != null && !tempnode.getEdges().isEmpty()) {
				for (Edge e : tempnode.getEdges()) {
					if (tempnode.getParents().contains(e.getStart())) {
						temp = nstr;
						if (temp.length() == 0)
							nstr = tempnode.getAction().getMove().toString();
						else
							nstr = tempnode.getAction().getMove().toString()
									+ "-" + temp;
						tempnode = e.getStart();
					}
				}
			}
			if (nstr.length() == 0)
				temp = "" + tempnode.getType();
			else
				temp = tempnode.getType() + "-" + nstr;
			String temp2 = line2;
			line2 = temp2 + " " + temp;

		}

		temp = output;
		output = temp + "OPEN" + line2 + "\n";

		/********** Third line - closed list **********/
		String line3 = "";
		for (Node n : CLOSED) {
			tempnode = n;
			nstr = "";
			temp = "";
			while (tempnode != null && !tempnode.getEdges().isEmpty()) {
				for (Edge e : tempnode.getEdges()) {
					if (tempnode.getParents().contains(e.getStart())) {
						temp = nstr;
						if (temp.length() == 0)
							nstr = tempnode.getAction().getMove().toString();
						else
							nstr = tempnode.getAction().getMove().toString()
									+ "-" + temp;
						tempnode = e.getStart();
					}
				}
			}
			if (nstr.length() == 0)
				temp = "" + tempnode.getType();
			else
				temp = tempnode.getType() + "-" + nstr;
			String temp2 = line3;
			line3 = temp2 + " " + temp;

		}

		temp = output;
		output = temp + "CLOSED" + line3 + "\n";

	}
	
	/**
	 * @return the goal of the search
	 */
	public Node getGoal() {
		return this.goal;
	}


	/********************* abstract methods to be implemented by each subclass (BFS, DFS, UCS and A*) *********************/
	
	/**
	 * Retrieves a node from the OPEN list
	 * @return the resulting node
	 */
	abstract Node getNodeFromOpen();

	/**
	 * Adds a node to the OPEN list
	 * @param the node to be added
	 */
	abstract void putNodeInOpen(Node node);

	/**
	 * Removes a node from the OPEN list
	 * @param the node to be removed
	 */
	abstract void removeNodeFromOpen(Node node);

	/**
	 * Checks if OPEN list is empty
	 * @return true (is empty) or false (not empty)
	 */
	abstract boolean openIsEmpty();

	/**
	 * Checks if OPEN list contains a specific node
	 * @param n, the node to check for
	 * @return true (OPEN list contains the node) or false (OPEN list does not contain the node)
	 */
	abstract boolean openContains(Node n);

	/**
	 * Retrieves the OPEN list
	 * @return the OPEN list (can be any collection)
	 */
	abstract Collection<Node> getOpenList();

	/**
	 * Adds nodes to the OPEN list
	 * @param nodes, a list of nodes to be added
	 */
	abstract void putNodesInOpen(List<Node> nodes);

}