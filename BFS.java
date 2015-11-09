import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class BFS extends GraphSearch {

	private Queue<Node> OPEN;
	
	
	public BFS(ArrayList<String> inputArray, String inputFile) {
		super(inputArray, inputFile);
		OPEN = new LinkedList<Node>();
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
	Queue<Node> getOpenList() {
		Queue<Node> clone = new LinkedList<Node>();
		clone.addAll(OPEN);
		return clone;
	}
	
	@Override
	void putNodesInOpen(List<Node> nodesToAdd) {
		Comparator<Node> comparator = new SimpleNodeComparator();
		PriorityQueue<Node> sorted = new PriorityQueue<Node>(100, comparator);
		sorted.addAll(nodesToAdd);
		OPEN.addAll(sorted);
	}
}
