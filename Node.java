import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Node implements Comparator<Node>{
	
	private char type; // R for normal, X for mountain, S for start, G for goal 
	private int row, column;
	private Action action;
	private List<Node> successors;
	private List<Edge> edges;
	private int g, h;
	private int timeOfGeneration;
	private List<Node> parents;
	
	public Node(char type, int row, int column) {
		this.setType(type);
		this.setRow(row);
		this.setColumn(column);
		this.parents = new ArrayList<Node>();
		this.successors = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}

	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(char type) {
		this.type = type;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
	/**
	 * Returns a string representation of the node
	 * @return the string
	 */
	public String toString(){
		return "(" + row + ", " + column + "): " + type;
		
	}

	/**
	 * @return the ancestors
	 */
	public List<Node> getParents() {
		return parents;
	}

	/**
	 * @param ancestors the ancestors to set
	 */
	public void setParents(List<Node> parents) {
		this.parents = parents;
	}

	/**
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public void addSuccessor(Node suc) {
		this.successors.add(suc);
	}

	/**
	 * @return the successors
	 */
	public List<Node> getSuccessors() {
		return successors;
	}

	/**
	 * @param successors the successors to set
	 */
	public void setSuccessors(List<Node> successors) {
		this.successors = successors;
	}

	/**
	 * Adds an edge to the list of edges for the node
	 * @param n
	 * @param suc
	 */
	public void addEdge(Node n, Node suc) {
		this.edges.add(new Edge(n, suc));
		
	}
	
	@Override
	public boolean equals(Object object){
		if(object != null && object instanceof Node){
			if(((Node)object).getColumn() == this.getColumn()
				&& ((Node)object).getRow() == this.getRow()){
					return true;
				}
		}
		return false;
	}

	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addParent(Node n) {
		this.parents.add(n);
		
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * @return the g
	 */
	public int getG() {
		return g;
	}

	/**
	 * Sets the cost. This depends on the algorithm: 
	 * For BFS and DFS the cost is not the path cost but the depth
	 * For UCS and A* the cost is the path cost
	 * Also sets the heuristic, if the algorithm is A*.
	 * 
	 * @param g the g to set
	 */
	public void setG(GraphSearch algorithm, Action a, int g) {
		if(algorithm instanceof BFS || algorithm instanceof DFS){
			this.g = 1 + g;
		} else {
			this.g = a.getCost() + g;
			if (algorithm instanceof ASTAR) {
				int h = ((ASTAR) algorithm)
						.computeHeuristic(this, algorithm.getGoal());
				this.setH(h);
			}
		}
	}

	/**
	 * @return the h
	 */
	public int getH() {
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(int h) {
		this.h = h;
	}

	/**
	 * @return the f
	 */
	public int getF() {
		return g + h;
	}

	/**
	 * @return the timeOfGeneration
	 */
	public int getTimeOfGeneration() {
		return timeOfGeneration;
	}

	/**
	 * @param timeOfGeneration the timeOfGeneration to set
	 */
	public void setTimeOfGeneration(int timeOfGeneration) {
		this.timeOfGeneration = timeOfGeneration;
	}

	/**
	 * @return the ancestors of the node
	 */
	public ArrayList<Node> getAncestors() {
		ArrayList<Node> ancestors = new ArrayList<Node>();
		Node tempnode = this;
		while (tempnode != null && !tempnode.getEdges().isEmpty()) {
			for (Edge e : tempnode.getEdges()) {
				if (tempnode.getParents().contains(e.getStart())) {
					ancestors.add(e.getStart());
					tempnode = e.getStart();
				}
			}
		}
		return ancestors;
	}


	
}
