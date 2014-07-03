package optimal.ghost.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	private final static String DEFAULT_FILE_NAME = "word.lst";

	private static class SingletonHolder {
		final static FileUtils uniqueInstance = new FileUtils();
	}

	public static FileUtils getInstance() {
		return SingletonHolder.uniqueInstance;
	}

	private List<String> wordList;
	private List<String> wordsStartingWithNList;
	
	private FileUtils() {
		readWordList();
		readWordsStartingWithNList();  
	}

	public List<String> getWordList() {
		return wordList;
	}

	public List<String> getWordList(Integer maxNumberOfWords) {
		List<String> wordList = new ArrayList<String>();

		for (int i = 0; i < maxNumberOfWords; i++) {
			wordList.add(getWordList().get(i));
		}

		return wordList;
	}

	public List<String> getWordsStartingWithNList() {
		return wordsStartingWithNList;
	}

	// Not to have these lists around in memory when not needed anymore...
	public void deleteWordLists() {
		
		wordList.clear();
		wordList = null;
		
		wordsStartingWithNList.clear();
		wordsStartingWithNList = null;
	}

	protected  List<String> readWordsStartingWithNList() {
		List<String> wordList = new ArrayList<String>();

		if (getWordList()==null) {
			readWordList();
		}
		
		for (String word : getWordList()) {
			if (word.startsWith("n")) {
				wordList.add(word);
			}
		}

		wordsStartingWithNList = wordList;
		
		return wordsStartingWithNList;
	}

	protected List<String> readWordList() {
		wordList = readWordList(DEFAULT_FILE_NAME);
		return wordList;
	}

	private List<String> readWordList(String fileName) {
		List<String> wordList = new ArrayList<String>();

		try {
			BufferedReader bufferedReader =
					new BufferedReader(new InputStreamReader(FileUtils.class
							.getClassLoader().getResourceAsStream(fileName)));
			try {
				String word;
				while ((word = bufferedReader.readLine()) != null) {
					wordList.add(word.trim());
				}

			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		} catch (Exception exception) {
			throw new RuntimeException("Failed to read [" + fileName + "]...",
					exception);
		}

		return wordList;
	}

}
