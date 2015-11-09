import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class UCS extends GraphSearch {
	
	private Queue<Node> OPEN;

	public UCS(ArrayList<String> inputArray, String inputFile) {
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
	PriorityQueue<Node> getOpenList() {
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
}


