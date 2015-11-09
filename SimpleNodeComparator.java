import java.util.Comparator;

public class SimpleNodeComparator implements Comparator<Node>{
	
	@Override
	public int compare(Node o1, Node o2) {
		return o1.getAction().getMove().compareTo(o2.getAction().getMove());
	}
}