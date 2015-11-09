import java.util.Comparator;

class PathCostNodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node o1, Node o2) {
		if(o1.getF() < o2.getF()){
			return -1;
		} else if(o1.getF() > o2.getF()){
			return 1;
		} else { // have equal cost, order after time of generation
			if(o1.getTimeOfGeneration() < o2.getTimeOfGeneration()) return -1;
			else if(o1.getTimeOfGeneration() > o2.getTimeOfGeneration()) return 1;
			else return o1.getAction().getMove().compareTo(o2.getAction().getMove());
		}	
	}	
}
