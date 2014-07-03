package optimal.ghost.tree;

import optimal.ghost.game.Player;

public class ShortestPathToDefeatVisitor implements Visitor {

	private Node defeatFinalNode;
	private final Player player;

	public ShortestPathToDefeatVisitor(Player player) {
		this.player = player;
	}

	public Node getDefeatFinalNode() {
		return defeatFinalNode;
	}

	@Override
	public void visit(Node node) {
		if (node.isLeaf() && player == node.whoPlaysThisNode()) {
			// If the player is the one who plays this reachable solution, he is the LOSER..
			if (defeatFinalNode == null) {
				defeatFinalNode = node;
			}
			if (node.getString().length() < defeatFinalNode.getString().length()) {
				defeatFinalNode = node;
			}
		}
	}
}
