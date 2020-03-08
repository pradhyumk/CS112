package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr   The expression
	 * @param vars   The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/

		StringTokenizer st = new StringTokenizer(expr, delims);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if ((expr.indexOf(token) + token.length()) < expr.length()
					&& expr.indexOf(token+"[")!=-1 && !arrays.contains(new Array(token))) {
				arrays.add(new Array(token));
			} else if (!Character.isDigit(token.charAt(0)) && !vars.contains(new Variable(token))) {
				vars.add(new Variable(token));
			}
		}
		
		System.out.println(vars +"\n" + arrays);
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input
	 * @param vars   The variables array list, previously populated by
	 *               makeVariableLists
	 * @param arrays The arrays array list - previously populated by
	 *               makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars   The variables array list, with values for all variables in the
	 *               expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation

		Stack<Float> operands = new Stack<Float>();
		Stack<Character> operators = new Stack<Character>();

		expr = expr.replaceAll("\\s", ""); // Removes all Spaces

		String tmp = ""; // Creates a temporary string holding the term's name

		for (int k = 0; k < expr.length(); k++) {

			char cur = expr.charAt(k);

			if (Character.isLetter(cur) || Character.isDigit(cur)) {
				tmp += cur;
			} else if (cur == '[') {
				int oc = 1;
				String inside = "[";

				while (oc != 0) {

					if (k < expr.length()) {
						k++;
						cur = expr.charAt(k);
					}
					if (cur == '[' || cur == '(') {
						oc += 1;
					} else if (cur == ']' || cur == ')') {
						oc -= 1;
					}

					inside += cur;
					System.out.println(inside + " " + k);

				}
				System.out
						.println("inside bracket: " + inside.substring(1, inside.length() - 1) + " Entering Recursion");

				int in = (int) evaluate(inside.substring(1, inside.length() - 1), vars, arrays);

				System.out.println("Exited Recursion (Val: " + in + ")");

				int[] val = arrays.get(arrays.indexOf(new Array(tmp))).values;

				tmp = val[in] + "";

			} else if (cur == '(') {
				int oc = 1;
				String inside = "(";
				while (oc != 0 && k < expr.length()) {
					if (k < expr.length()) {
						k++;
						cur = expr.charAt(k);
					}
					if (cur == '[' || cur == '(') {
						oc += 1;
					} else if (cur == ']' || cur == ')') {
						oc -= 1;
					}

					inside += cur;
					System.out.println(inside + " " + k);

				}

				System.out.println(
						"Inside Parentheses: " + inside.substring(1, inside.length() - 1) + " Entering Recursion");
				Float in = evaluate(inside.substring(1, inside.length() - 1), vars, arrays);
				System.out.println("Exited Recursion (Val: " + in + ")");
				tmp = in + "";
				System.out.println("tmp (in): " + tmp);
			} else if (cur == '/' || cur == '*' || cur == '+' || cur == '-') {

				if (isNumeric(tmp) == true) {
					operands.push(Float.parseFloat(tmp));
					System.out.println(operands.peek());
					tmp = "";
				} else if (isNumeric(tmp) == false) {
					System.out.println("tmp (isNumeric-false) : " + tmp);
					operands.push((float) vars.get(vars.indexOf(new Variable(tmp))).value);
					System.out.println(operands.peek());
					tmp = "";
				}
				System.out.println("Operands Size: " + operands.size() + " | Operators Peek: ");

				if (operators.size() > 0)
					System.out.print(operators.peek());
				else
					System.out.print("Empty Stack \n");

				if (operands.size() > 1 && operators.size() > 0) {

					if (operators.peek() == '*') {
						float second = operands.pop();
						float first = operands.pop();

						operands.push(first * second);
						System.out.println("multiplied: " + first + " and " + second + ": pushed: " + operands.peek());
						operators.pop();
					} else if (operators.peek() == '/') {
						float second = operands.pop();
						float first = operands.pop();

						operands.push(first / second);
						System.out.println("divided: " + first + " and " + second + ": pushed: " + operands.peek());
						operators.pop();
					}
				}

				operators.push(cur);

			}
			System.out.println("k: " + k);
			System.out.println("tmp: " + tmp + "\n");

		}

		if (isNumeric(tmp) == true) {
			operands.push(Float.parseFloat(tmp));
			tmp = "";
		} else if (isNumeric(tmp) == false) {
			System.out.println("tmp (isNumeric-false) : " + tmp);
			operands.push((float) vars.get(vars.indexOf(new Variable(tmp))).value);
			System.out.println(operands.peek());
			tmp = "";
		}

		if (operands.size() > 1 && operators.size() > 0) {

			if (operators.peek() == '*') {
				float second = operands.pop();
				float first = operands.pop();

				operands.push(first * second);
				System.out.println("multiplied: " + first + " and " + second + ": pushed: " + operands.peek());
				operators.pop();
			} else if (operators.peek() == '/') {
				float second = operands.pop();
				float first = operands.pop();

				operands.push(first / second);
				System.out.println("divided: " + first + " and " + second + ": pushed: " + operands.peek());
				operators.pop();
			}
		}

		Stack<Float> operandsR = new Stack<Float>();
		Stack<Character> operatorsR = new Stack<Character>();

		int max = operators.size();
		System.out.println("Operators Size: " + max);
		for (int k = 0; k < max; k++) {
			operatorsR.push(operators.pop());
			System.out.println("OperatorsR Pushed: " + operatorsR.peek());
		}
		System.out.println("Operands Size : " + operands.size());

		max = operands.size();

		for (int k = 0; k < max; k++) {
			operandsR.push(operands.pop());
			System.out.println("OperandsR Pushed: " + operandsR.peek());
		}

		while (operatorsR.size() > 0) {

			float pop1 = operandsR.pop();
			float pop2 = operandsR.pop();

			if (operatorsR.peek() == '+') {
				operandsR.push(pop1 + pop2);
				operatorsR.pop();
			} else if (operatorsR.peek() == '-') {
				operandsR.push(pop1 - pop2);
				operatorsR.pop();
			}
		}

		return operandsR.peek();
	}

	private static boolean isNumeric(String expr) { // Returns if the provided string can be parsed as a float or not

		if (expr == null)
			return false;

		try {
			Float.parseFloat(expr);
		} catch (NumberFormatException exception) {
			return false;
		}

		return true;
	}

}
