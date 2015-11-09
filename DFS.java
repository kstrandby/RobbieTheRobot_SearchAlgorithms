import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;


public class DFS extends GraphSearch {

	private Stack<Node> OPEN;
	
	public DFS(ArrayList<String> inputArray, String inputFile) {
		super(inputArray, inputFile);
		OPEN = new Stack<Node>();
	}

	@Override
	Node getNodeFromOpen() {
		return OPEN.pop();
	}

	@Override
	void putNodeInOpen(Node node) {
		OPEN.push(node);
	}

	@Override
	void removeNodeFromOpen(Node node) {
		OPEN.remove(node);
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
	Stack<Node> getOpenList() {
		Stack<Node> clone = new Stack<Node>();
		clone.addAll(OPEN);
		return clone;
	}

	@Override
	void putNodesInOpen(List<Node> nodes) {
		Comparator<Node> comparator = new SimpleNodeComparator();
		PriorityQueue<Node> sorted = new PriorityQueue<Node>(100, comparator);
		sorted.addAll(nodes);
		
		Stack<Node> reversed = new Stack<Node>();
		while(!sorted.isEmpty()){
			Node n = sorted.poll();
			reversed.push(n);
		}
		while(!reversed.isEmpty()){
			OPEN.push(reversed.pop());
		}
		
	}

}
