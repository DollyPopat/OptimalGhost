package optimal.ghost.game;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import optimal.ghost.tree.Node;
import optimal.ghost.tree.TreeManager;
import optimal.ghost.tree.TreeManager.Dictionary;
import optimal.ghost.tree.TreeManager.Tree;

import org.apache.commons.collections.CollectionUtils;

public class GhostGame {

	private final Tree tree;
	private Node currentNode;

	private final Random random = new Random(System.currentTimeMillis());

	public GhostGame(Dictionary dictionary) {
		tree = TreeManager.getInstance().getTree(dictionary);
		currentNode = tree.getRoot();
	}

	public void reset() {
		currentNode = tree.getRoot();
	}

	public boolean gameOver() {
		return currentNode.isLeaf();
	}

	public Node playAutomatically() {
		while (!gameOver()) {
			validPlayerPlayBest(getValidPlayer());
		}

		return currentNode;
	}

	public Node playAgainstComputer(Character play) {
		if (play != null) {
			play(Player.HUMAN, play);
			// ... now processing response play
			playBest(Player.COMPUTER);
		}

		return currentNode;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	private void play(Player player, Character play) {
		if (gameOver()) {
			return;
		}

		// We need first to validate if this is the right player...
		validPlayerPlay(getValidPlayer(player), play);
	}

	private void playBest(Player player) {
		// We need first to validate if this is the right player...
		validPlayerPlayBest(getValidPlayer(player));
	}

	private void validPlayerPlayBest(Player validPlayer) {
		if (gameOver()) {
			return;
		}

		validPlayerPlay(validPlayer, chooseBestPlay(validPlayer));
	}

	private void validPlayerPlay(Player player, Character play) {

		Node nextNode = currentNode.travelToNext(play);
		if (nextNode == null) {
			throw new InvalidPlay(
					MessageFormat
							.format(
							"Invalid play [{0}] for player [{1}]. The resulting word [{2}] "+
							"does not exist in the dictionary and cannot be extended into a word ...",
									play, player, currentNode.getString() + play));
		}

		currentNode = nextNode;
	}

	private Player getValidPlayer(Player player) {
		Player validPlayer = getValidPlayer();
		if (player != validPlayer) {
			throw new IllegalArgumentException(MessageFormat.format(
					"Player should be [{0}] but is [{1}])...", validPlayer,
					player));
		}

		return validPlayer;
	}

	private Player getValidPlayer() {
		if (currentNode == tree.getRoot()) {
			return Player.getFirstPlayer();
		}

		return Player.getNextPlayer(currentNode.whoPlaysThisNode());
	}

	private Character chooseBestPlay(Player validPlayer) {

		List<Node> winnerChildren = currentNode.getWinnerChildren(validPlayer);
		if (CollectionUtils.isNotEmpty(winnerChildren)) {
			// If the computer thinks it will win, it should play randomly among
			// all its winning moves
			return winnerChildren.get(random.nextInt(winnerChildren.size()))
					.getLetter();
		}

		@SuppressWarnings("unchecked")
		List<Node> loserNodes =
				(List<Node>) CollectionUtils.subtract(
						currentNode.getChildren(), winnerChildren);

		return choosePathToDefeat(loserNodes, validPlayer).getLetter();
	}

	// "if the computer thinks it will lose, it should play so as to extend
	// the game as long as possible (choosing randomly among choices that
	// force the maximal game length)."
	private Node choosePathToDefeat(List<Node> loserNodes, Player player) {
		List<Node> nodesWithMaxDepth = new ArrayList<Node>();

		int maxDepth = 0;

		// At any moment the player has to chose the option with the longest
		// minimum path to defeat...
		for (Node loserNode : loserNodes) {
			Node defeatFinalNode = loserNode.getShortestPathToDefeat(player);
			
			if (defeatFinalNode != null) {
				int defeatFinalNodeDepth = defeatFinalNode.getString().length();

				if (defeatFinalNodeDepth > maxDepth) {
					maxDepth = defeatFinalNodeDepth;
					nodesWithMaxDepth.clear();
				}
				if (defeatFinalNodeDepth == maxDepth) {
					nodesWithMaxDepth.add(loserNode);
				}
			}
		}

		return nodesWithMaxDepth.get(random.nextInt(nodesWithMaxDepth.size()));
	}

	public static class InvalidPlay extends IllegalArgumentException {
		private static final long serialVersionUID = 1126590095558370837L;

		public InvalidPlay(String s) {
			super(s);
		}
	}
}
