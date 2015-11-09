
public class Edge {
	
	private Node start, end;
	
	public Edge(Node start, Node end) {
		this.setStart(start);
		this.setEnd(end);
	}

	/**
	 * @return the n1
	 */
	public Node getStart() {
		return this.start;
	}

	/**
	 * @param n1 the n1 to set
	 */
	public void setEnd(Node end) {
		this.end = end;
	}

	/**
	 * @return the end
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Node start) {
		this.start = start;
	}
}
