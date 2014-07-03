package optimal.ghost.tree;

import optimal.ghost.tree.TreeManager.Dictionary;

import org.junit.Test;

public class TreeTest {

	@Test
	public void buildACompleteTree() {
		TreeManager.getInstance().getTree(Dictionary.DEFAULT);
	}

}
