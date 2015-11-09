import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class ASTAR extends GraphSearch {
	
	private Queue<Node> OPEN;

	public ASTAR(ArrayList<String> inputArray, String inputFile) {
		super(inputArray, inputFile);
		Comparator<Node> comparator = new PathCostNodeComparator();
		OPEN = new PriorityQueue<Node>(100, comparator);
	}

	@Override
	Node getNodeFromOpen() {
		return OPEN.poll();
	}

	@Override
	void putNodeInOpen(Node node) {
		OPEN.add(node);
	}

	@Override
	void removeNodeFromOpen(Node node) {
		if(OPEN.contains(node)) OPEN.remove(node);
	}

	@Override
	boolean openIsEmpty() {
		return OPEN.isEmpty();
	}

	@Override
	boolean openContains(Node n) {
		return OPEN.contains(n);
	}

	@Override
	Collection<Node> getOpenList() {
		Comparator<Node> comparator = new PathCostNodeComparator();
		PriorityQueue<Node> clone = new PriorityQueue<Node>(100, comparator);
		clone.addAll(OPEN);
		return clone;
	}

	@Override
	void putNodesInOpen(List<Node> nodes) {
		for(Node n : nodes){
			OPEN.add(n);
		}
		
	}
	
	/**
	 * Computes the heuristic from a given node to a given goal node.
	 * The heuristic function is the Chebychev distance.
	 * 
	 * @param node
	 * @param goal
	 * @return the calculated value of h
	 */
	int computeHeuristic(Node node, Node goal) {
		return Math.max(Math.abs(goal.getRow() - node.getRow()), 
				Math.abs(goal.getColumn() - node.getColumn()));
		
	}
	
}
