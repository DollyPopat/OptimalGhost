package optimal.ghost.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import optimal.ghost.utils.FileUtils;

public class TreeManager {

	// I need more than one dictionary because I'm using them on tests.
	public enum Dictionary {
		DEFAULT, FIRST_N_WORDS, WORDS_STARTING_WITH_N;
	}

	// Intended just for tests...
	public static final int FIRST_N_WORDS_NUMBER = 10;

	private static class SingletonHolder {
		final static TreeManager uniqueInstance = new TreeManager();
	}

	public static TreeManager getInstance() {
		return SingletonHolder.uniqueInstance;
	}

	public Tree getTree(Dictionary dictionary) {
		return map.get(dictionary);
	}

	private final Map<Dictionary, Tree> map;

	private TreeManager() {
		map = new HashMap<Dictionary, Tree>();

		// Setting the collection of dictionaries we're using...
		map.put(Dictionary.DEFAULT, new Tree(FileUtils.getInstance().getWordList()));
		map.put(Dictionary.FIRST_N_WORDS, new Tree(FileUtils.getInstance().getWordList(FIRST_N_WORDS_NUMBER)));
		map.put(Dictionary.WORDS_STARTING_WITH_N, new Tree(FileUtils.getInstance().getWordsStartingWithNList()));
		
		// We don't want the already loaded lists to be using up memory space...
		FileUtils.getInstance().deleteWordLists();
	}

	// Enclosed here not to be instantiated by anyone else...
	public static class Tree {

		// We could read this from a properties file for instance...
		private static final int MINIMUM_NUMBER_OF_LETTERS_IN_VALID_WORD = 4;

		private final Node root;

		public Node getRoot() {
			return root;
		}

		private Tree(List<String> wordList) {
			root = new Node();
			
			// Constructing the tree from word list...
			for (String word : wordList) {
				addWord(word);
			}
			
			// "Solving" the game tree through post-order traversal...
			root.postOrder(new GoalVisitor());
			
			// "Pruning" the tree through preorder traversal  
			root.preOrder(new PruneVisitor());
		}

		private void addWord(String word) {
			if (word == null || word.length() < MINIMUM_NUMBER_OF_LETTERS_IN_VALID_WORD) {
				return;
			}

			// TODO: Maybe we shouldn't start always at the root node because
			// the provided list of words is sorted and we could probably make
			// use of that fact, but right now it's quick enough and I don't have
			// time for this...
			Node node = getRoot();
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < word.length(); i++) {
				String fragment = stringBuffer.append(word.charAt(i)).toString();
				boolean completeWord = (i == (word.length() - 1) ? true : false);
				node = getChildNode(node, fragment, completeWord);
				if (node == null) {
					return;
				}
			}
		}

		private Node getChildNode(Node parentNode, String fragment,
				boolean completeWord) {

			if (parentNode.isCompleteWord()	&& parentNode.getString().length() >= MINIMUM_NUMBER_OF_LETTERS_IN_VALID_WORD) {
				// We don't need to add more nodes because this already
				// completed word is preventing us from going any further
				return null;
			}

			Node childNode = parentNode.travelToNext(fragment.charAt(fragment.length() - 1));
			if (childNode != null) {
				return childNode;
			}

			return new Node(fragment, completeWord, parentNode);
		}
	}

}
