package optimal.ghost.game;

import java.util.ArrayList;
import java.util.List;

import optimal.ghost.tree.Node;
import optimal.ghost.tree.TreeManager.Dictionary;

import org.junit.Assert;
import org.junit.Test;

public class GhostGameTest {

	private static final int NUMBER_OF_GAMES_TO_PLAY = 100;

	@Test
	public void playAutomaticallyFirstNWords() {
		GhostGame ghostGame = new GhostGame(Dictionary.FIRST_N_WORDS);

		Node result = ghostGame.playAutomatically();
		Assert.assertEquals("aardvark", result.getString());
		Assert.assertEquals(Player.COMPUTER, result.whoPlaysThisNode());
	}

	@Test
	public void playAutomaticallyWordsStartingWithN() {
		GhostGame ghostGameNWords = new GhostGame(Dictionary.WORDS_STARTING_WITH_N);

		// Playing sequentially...
		for (int i = 1; i < NUMBER_OF_GAMES_TO_PLAY; i++) {
			Node result = ghostGameNWords.playAutomatically();
			// System.out.println(result);
			Assert.assertEquals("nyctalopia", result.getString());
			Assert.assertEquals(Player.COMPUTER, result.whoPlaysThisNode());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void playFallibleHuman0AgainstComputer() {
		GhostGame ghostGameNWords = new GhostGame(Dictionary.WORDS_STARTING_WITH_N);
		ghostGameNWords.playAgainstComputer('s');
	}

	@Test
	public void playFallibleHuman1AgainstComputer() {
		playFallibleHumanAgainstComputer(new GhostGame(Dictionary.WORDS_STARTING_WITH_N));
	}

	@Test
	public void playAutomaticallyMultithreaded() {

		List<GhostGameThread> threadList = new ArrayList<GhostGameTest.GhostGameThread>();

		for (int i = 0; i < NUMBER_OF_GAMES_TO_PLAY; i++) {
			threadList.add(new GhostGameThread(new GhostGame(Dictionary.WORDS_STARTING_WITH_N)));
			threadList.get(i).start();
		}

		// Waiting for threads to stop...
		for (int i = 0; i < NUMBER_OF_GAMES_TO_PLAY; i++) {
			try {
				threadList.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Asserting results
		for (int i = 0; i < NUMBER_OF_GAMES_TO_PLAY; i++) {
			Assert.assertEquals("nyctalopia", threadList.get(i).getResult()
					.getString());
			Assert.assertEquals(Player.COMPUTER, threadList.get(i).getResult()
					.whoPlaysThisNode());
		}
	}

	@Test
	public void playDefaultDictionary() {
		playFallibleHumanAgainstComputer(new GhostGame(Dictionary.DEFAULT));
	}

	private void playFallibleHumanAgainstComputer(GhostGame ghostGameNWords) {
		Node response = ghostGameNWords.playAgainstComputer('n');
		Assert.assertEquals("ny", response.getString());
		
		// Wrong HUMAN movement: it brings my defeat. The right one would be 'c'
		// leading to 'nyctalopia'
		response = ghostGameNWords.playAgainstComputer('s');
		Assert.assertEquals("nyst", response.getString());
		response = ghostGameNWords.playAgainstComputer('a');
		Assert.assertEquals("nystag", response.getString());

		// From this moment on there are two possible solutions depending on what the computer
		// randomly chooses: "nystagmic" and "nystagmus"
		while (!ghostGameNWords.gameOver()) {
			Assert.assertEquals(1, response.getChildren().size());
			response = ghostGameNWords.playAgainstComputer(response.getChildren().get(0).getLetter());
		}

		Assert.assertTrue(response.getString().equals("nystagmus") || response.getString().equals("nystagmic"));
		Assert.assertEquals(Player.HUMAN, response.whoPlaysThisNode());
	}

	private static class GhostGameThread extends Thread {

		private final GhostGame ghostGame;
		private Node result;

		private GhostGameThread(GhostGame ghostGame) {
			this.ghostGame = ghostGame;
		}

		@Override
		public void run() {
			ghostGame.reset();
			result = ghostGame.playAutomatically();
		}

		private Node getResult() {
			if (!ghostGame.gameOver()) {
				return null;
			}

			return result;
		}
	}
}
