package optimal.ghost.tree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import optimal.ghost.game.Player;

public class PruneVisitor implements Visitor {

	@Override
	public void visit(Node node) {
		
		if (Player.COMPUTER == Player.getNextPlayer(node.whoPlaysThisNode())) {
			// The next one to play is the COMPUTER: we can expect from him only
			// logical moves, in the contrary, HUMANs can make any kind of crazy
			// decisions ;-) and we can rule NONE of them out...
			List<Node> winnerChildren = node.getWinnerChildren(Player.COMPUTER);
			if (CollectionUtils.isNotEmpty(winnerChildren)) {
				// We will consider only winner children for COMPUTER
				node.getChildren().clear();
				node.getChildren().addAll(winnerChildren);
			}
		}
	}
}
