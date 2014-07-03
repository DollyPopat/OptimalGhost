package optimal.ghost.tree;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import optimal.ghost.game.Player;
import org.apache.commons.collections.CollectionUtils;

public class Node {

	private final String string;
	private final boolean completeWord;
	private List<Node> children;
	// We can try to mark each node with the players whom this node is a goal
	// for ...
	private EnumSet<Player> goalForPlayers;

	public Node() {
		this("", false, null);
	}

	public Node(String string, boolean completeWord, Node parent) {
		this.string = string;
		this.completeWord = completeWord;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public String getString() {
		return string;
	}

	public boolean isCompleteWord() {
		return completeWord;
	}

	public List<Node> getChildren() {
		if (children == null) {
			children = new ArrayList<Node>();
		}

		return children;
	}

	public List<Node> getWinnerChildren(Player player) {
		List<Node> winnerChildren = new ArrayList<Node>();

		for (Node child : getChildren()) {
			if (child.getGoalForPlayers().contains(player)) {
				winnerChildren.add(child);
			}
		}

		return winnerChildren;
	}

	public Player whoPlaysThisNode() {
		if (getString() == null || getString().length() == 0) {
			return null;
		}
		
		return Player.getFromOrdinal((getString().length() - 1)
				% Player.values().length);
	}

	public Node travelToNext(Character play) {
		for (Node childNode : getChildren()) {
			if (play == childNode.getLetter()) {
				return childNode;
			}
		}
		
		return null;
	}

	public Character getLetter() {
		return getString().charAt(getString().length() - 1);
	}

	public EnumSet<Player> getGoalForPlayers() {
		if (goalForPlayers == null) {
			goalForPlayers = EnumSet.noneOf(Player.class);
		}
		return goalForPlayers;
	}

	public boolean isLeaf() {
		return CollectionUtils.isEmpty(getChildren());
	}

	public void postOrder(Visitor visitor) {
		postOrder(this, visitor);
	}

	public void preOrder(Visitor visitor) {
		preOrder(this, visitor);
	}

	@Override
	public String toString() {
		return "Node [string=" + string + ", completeWord=" + completeWord
				+ ", goalForPlayers=" + goalForPlayers + "]";
	}

	public Node getShortestPathToDefeat(Player player) {
		ShortestPathToDefeatVisitor visitor = new ShortestPathToDefeatVisitor(player);
		postOrder(visitor);
		return visitor.getDefeatFinalNode();
	}

	protected void setGoalForPlayers(EnumSet<Player> goalForPlayers) {
		this.goalForPlayers = goalForPlayers;
	}

	private void postOrder(Node node, Visitor visitor) {

		for (Node child : node.getChildren()) {
			postOrder(child, visitor);
		}

		visitor.visit(node);
	}

	private void preOrder(Node node, Visitor visitor) {

		visitor.visit(node);

		for (Node child : node.getChildren()) {
			preOrder(child, visitor);
		}
	}

	private void addChild(Node childNode) {
		getChildren().add(validChildNode(childNode));
	}

	private Node validChildNode(Node childNode) {
		if (childNode.getNodeLevel() != getNodeLevel() + 1) {
			throw new IllegalArgumentException(
					MessageFormat
							.format("The child word {0} must be one letter longer than its parent {1} ...",
									childNode, this));
		}

		return childNode;
	}

	private int getNodeLevel() {
		return string.length();
	}

}
