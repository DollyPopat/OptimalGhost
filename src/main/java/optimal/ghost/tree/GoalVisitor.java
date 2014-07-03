package optimal.ghost.tree;

import optimal.ghost.game.Player;

/**
 * This class will try to decide the players for whom certain node is a goal...
 * 
 */
public class GoalVisitor implements Visitor {

	@Override
	public void visit(Node node) {

		if (node.isLeaf()) {
			// If this node is a leaf node, the player who plays it loses and
			// then this node becomes a goal for the rest of the players
			node.setGoalForPlayers(Player.allPlayerExceptOne(node.whoPlaysThisNode()));
			return;
		}

		// If node is not a leaf we have to set the players it is a goal for from
		// their children...
		for (Player player : Player.values()) {
			if (isAGoal(node, player)) {
				node.getGoalForPlayers().add(player);
			}
		}
	}

	private boolean isAGoal(Node node, Player player) {

		boolean allNodesInChildrenAreGoals = true;

		for (Node child : node.getChildren()) {
			if (child.getGoalForPlayers().contains(player) == true) {
				if (player == child.whoPlaysThisNode()) {
					// if any of the child nodes is a goal for the Player
					// and the player can choose it because it's going to be
					// played in his turn the current node is also a goal for
					// the player
					return true;
				}
			} else {
				allNodesInChildrenAreGoals = false;
			}
		}

		// If all nodes beneath this one are goals for the Player
		// the current node is a goal for that player...
		return allNodesInChildrenAreGoals;
	}

}
