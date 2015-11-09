
public class Action {
	
	public enum Move {
		R, RD, D, LD, L, LU, U, RU
	}

	private Move move;
	private int cost;
	
	public Action(Move move){
		this.setMove(move);
		this.cost = 1; // default
	}
	
	/**
	 * Computes the result of performing the action from the specified node
	 * The result is the resulting node from performing the move of the action 
	 * from the given node, e.g. moving RD from node (1,1) results in node (2,2)
	 * @param node
	 * @param map
	 * @return The resulting node
	 */
	public Node computeResult(Node node, Node[][] map){
		int row = node.getRow();
		int column = node.getColumn();
		
		switch(this.getMove()) {
		case D:
			this.cost = 2;
			row = row + 1;
			break;
		case L:
			this.cost = 2;
			column = column - 1;
			break;
		case LD:
			row = row + 1;
			column = column - 1;
			break;
		case LU:
			row = row - 1;
			column = column - 1;
			break;
		case R:
			this.cost = 2;
			column = column + 1;
			break;
		case RD:
			row = row + 1;
			column = column + 1;
			break;
		case RU:
			row = row - 1;
			column = column + 1;
			break;
		case U:
			this.cost = 2;
			row = row - 1;
			break;
		default:
			break;
		
		}

		// check for out of borders before returning resulting node
		if(row >= 0 && row < map.length && column >= 0 && column < map.length){
			return new Node(map[row][column].getType(), row, column);	
		} else return null;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return the move
	 */
	public Move getMove() {
		return move;
	}

	/**
	 * @param move the move to set
	 */
	public void setMove(Move move) {
		this.move = move;
	}
}
