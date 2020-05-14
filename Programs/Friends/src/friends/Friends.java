package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2. Chain is returned as a
	 * sequence of names starting with p1, and ending with p2. Each pair (n1,n2) of
	 * consecutive names in the returned chain is an edge in the graph.
	 * 
	 * @param g  Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there
	 *         is no path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION

		if ((p1 == null && p2 == null) || (p1 == null || p2 == null))
			return null;

		ArrayList<String> ret = new ArrayList<String>();

		Person[] p = g.members; // Person Array
		boolean[] visited = new boolean[p.length];
		int[] pred = new int[p.length];
		Queue<Person> q = new Queue<Person>();

		q.enqueue(p[g.map.get(p1)]); // Inputs source Friend
		visited[g.map.get(p1)] = true; // Marks Source Friend as Visited
		pred[g.map.get(p1)] = -1;
		ret.add(p1);
		int k = 0;

		outerloop: while (!q.isEmpty()) {

			Person curr = q.dequeue();
			visited[g.map.get(curr.name)] = true;

			// Friend friend = curr.first;
			if (curr.first == null)
				return null;

			for (Friend friend = curr.first; friend != null; friend = friend.next) { // Traverses through all the
																						// friends

				if (!visited[friend.fnum]) { // Makes sure that only the not visited are looked at
					visited[friend.fnum] = true;
					pred[friend.fnum] = g.map.get(curr.name);
					q.enqueue(g.members[friend.fnum]);

					if (g.members[friend.fnum].name.equals(p2)) { // When the first target is found, breaks and the path
																	// is recorded from the predeccesors
						curr = g.members[friend.fnum];
						k = friend.fnum;
						break outerloop;
					}
				}
			}
		}

		while (pred[k] != -1) {
			ret.add(1, g.members[k].name);
			k = pred[k];
		}

		return ret;

	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g      Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there
	 *         is no student in the given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION

		if (g == null) {
			return null;
		}

		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		boolean[] v = new boolean[g.members.length];
		ArrayList<String> clique = new ArrayList<String>();

		int k = 0;
		while (k < g.members.length) {
			Person curr = g.members[k];
			clique.clear();

			if (g.members[k].school == null) {
				k++;
			} else if (curr.school.equals(school) && !v[g.map.get(curr.name)]) {
				Queue<Person> q = new Queue<Person>();
				q.enqueue(curr);

				// BFS Scan Starts Here...
				while (q.isEmpty() != true) {
					Person currBFS = q.dequeue();
					System.out.println(currBFS.name);
					v[g.map.get(currBFS.name)] = true;

					clique.add(currBFS.name);

					for (Friend friendBFS = currBFS.first; friendBFS != null; friendBFS = friendBFS.next) {
						if (!v[friendBFS.fnum] && g.members[friendBFS.fnum].student
								&& g.members[friendBFS.fnum].school.equals(school)) {
							v[friendBFS.fnum] = true;
							q.enqueue(g.members[friendBFS.fnum]);
						}
					}
				}
			}
			System.out.println(clique);
			if (clique.isEmpty() != true) {
				ArrayList<String> click = new ArrayList<>();
				for (int x = 0; x < clique.size(); x++) {
					click.add(clique.get(x));
				}
				ret.add(click);

				System.out.println("Added" + ret);
			}
			k++;
		}
		System.out.println(ret);
		return ret;

	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no
	 *         connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		if (g == null)
			return null;

		boolean[] visited = new boolean[g.members.length];
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];

		boolean[] vBack = new boolean[g.members.length];
		ArrayList<String> cameBack = new ArrayList<String>();

		ArrayList<String> ret = new ArrayList<String>();
		int[] pred = new int[g.members.length];

		int y = 1; // DFSNum Incrementor
		int k = 0; // Vertex / While Loop iterator

		while (k < g.members.length) { // Only scans using DFS if vertex has not been visited
			if (!visited[k])
				DFS(g, visited, dfsnum, back, vBack, ret, pred, k, y, k, cameBack);
			k++;
		}

		return ret;

	}

	private static void DFS(Graph g, boolean[] visited, int[] dfsnum, int[] back, boolean[] vBack,
			ArrayList<String> ret, int[] pred, int k, int y, int b, ArrayList<String> cameBack) {

		if (visited[k])
			return;

		visited[k] = true;
		dfsnum[k] = y;
		back[k] = dfsnum[k];
		System.out.println(y);
		y++;

		Friend friend = g.members[k].first;

		while (friend != null) {

			if (visited[friend.fnum] == true) {
				back[k] = (back[k] > dfsnum[friend.fnum]) ? dfsnum[friend.fnum] : back[k];
				friend = friend.next;
				continue;
			}

			// Goes inside vertex because it has not been visited yet.
			DFS(g, visited, dfsnum, back, vBack, ret, pred, friend.fnum, y, b, cameBack);

			if (dfsnum[k] <= back[friend.fnum] && !ret.contains(g.members[k].name)
					&& (cameBack.contains(g.members[k].name) || k != b)) {
				ret.add(g.members[k].name);
				// System.out.println(ret);
			}
			if (back[friend.fnum] < dfsnum[k]) {
				back[k] = (back[k] > back[friend.fnum]) ? back[friend.fnum] : back[k];
			}

			cameBack.add(g.members[k].name);
			friend = friend.next;
		}
	}
}
