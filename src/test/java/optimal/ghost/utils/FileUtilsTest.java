package optimal.ghost.utils;

import java.util.List;

import optimal.ghost.tree.TreeManager;
import optimal.ghost.tree.TreeManager.Dictionary;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsTest {

	private static final int TOTAL_NUMBER_OF_WORDS_IN_FILE = 173528;
	private static final int NUMBER_OF_WORDS_STARTING_WITH_N = 4413;

	@Test
	public void readWordList() {
		List<String> wordList = FileUtils.getInstance().readWordList();
		Assert.assertEquals(TOTAL_NUMBER_OF_WORDS_IN_FILE, wordList.size());
	}

	@Test
	public void buildALittleTree() {
		// I create here the Tree as a warming up for the test executed after 
		TreeManager.getInstance().getTree(Dictionary.WORDS_STARTING_WITH_N);
		List<String> wordList = FileUtils.getInstance().readWordsStartingWithNList();
		Assert.assertEquals(NUMBER_OF_WORDS_STARTING_WITH_N, wordList.size());
	}
}
