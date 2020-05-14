package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION

		TrieNode ret = new TrieNode(null, null, null);
		TrieNode firstWord = new TrieNode(new Indexes(0, (short) 0, (short) (allWords[0].length() - 1)), null, null);
		ret.firstChild = firstWord;

		TrieNode ptr = ret.firstChild;

		short cfirst, clast, add = 0;

		for (int k = 1; k < allWords.length; k++) {
			cfirst = 0; // First position of current word
			clast = (short) (allWords[k].length() - 1); // Last Position of Current Word
			ptr = ret.firstChild;
			add = 0;
			while (ptr != null) {
				int endP = endPrefix(
						allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1),
						allWords[k].substring(cfirst, clast));
				if (endP > -1)
					endP += add;

				if (ptr.substr.endIndex == endP) {
					add += (short) (ptr.substr.endIndex - ptr.substr.startIndex + 1);
					ptr = ptr.firstChild;
					cfirst = (short) (endP + 1);

				} else if (ptr.substr.endIndex > endP && endP != -1) {
					TrieNode child = new TrieNode(
							new Indexes(ptr.substr.wordIndex, (short) (endP + 1), ptr.substr.endIndex), null, null);
					child.firstChild = ptr.firstChild;
					ptr.substr.endIndex = (short) endP;
					ptr.firstChild = child;
					ptr = ptr.firstChild;
					cfirst = (short) (endP + 1);
				} else if (endP == -1 && ptr.sibling != null) {
					ptr = ptr.sibling;
				} else if (endP == -1 && ptr.sibling == null) {
					break;
				}
			}
			ptr.sibling = new TrieNode(new Indexes(k, (short) (cfirst), clast), null, null);
		}
		return ret;
	}

	// Returns the index number of the ending character of the prefix in the two
	// strings provided
	private static int endPrefix(String w1, String w2) {
		int k = 0;
		int end = -1;

		while ((k < w1.length()) && (k < w2.length()) && w1.charAt(k) == w2.charAt(k)) {
			end++;
			k++;
		}

		return end;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf
	 * nodes in the trie whose words start with this prefix. For instance, if the
	 * trie had the words "bear", "bull", "stock", and "bell", the completion list
	 * for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and
	 * "bell", and for prefix "bell", completion would be the leaf node that holds
	 * "bell". (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be", the
	 * returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root     Root of Trie that stores all words to search on for
	 *                 completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix   Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		TrieNode ptr = root;
		ArrayList<TrieNode> ret = new ArrayList<TrieNode>();
		boolean trigger = false;
		for (ptr = (root.substr == null) ? ptr.firstChild : root; ptr != null; ptr = ptr.sibling) {
			System.out.println(ptr.toString());
			if (trigger) {
				break;
			}
			if (prefix.startsWith(allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex + 1))
					|| allWords[ptr.substr.wordIndex].startsWith(prefix)) {
				if (ptr.firstChild == null && allWords[ptr.substr.wordIndex].startsWith(prefix)) {
					ret.add(ptr);
					trigger = true;
				}
				else if (ptr.firstChild != null)
					ret.addAll(completionList(ptr.firstChild, allWords, prefix));
			}
			
		}
		return (ret.isEmpty()) ? null : ret;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
