package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage
	 * format of the polynomial is:
	 * 
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * 
	 * with the guarantee that degrees will be in descending order. For example:
	 * 
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * 
	 * which represents the polynomial:
	 * 
	 * <pre>
	 * 4 * x ^ 5 - 2 * x ^ 3 + 2 * x + 3
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients
	 *         and degrees read from scanner
	 */
	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

		if(poly1 == null && poly2 == null) {
			return null;
		}
		
		if (poly1 == null) {
			Node pointer = poly2.next;
			Node p2 = new Node(poly2.term.coeff, poly2.term.degree, null);
			Node pointer1 = p2;
			while (pointer != null) {
				pointer1.next = new Node(pointer.term.coeff, pointer.term.degree, null);
				pointer1 = pointer1.next;
				pointer = pointer.next;
			}
			return p2;
		}

		if (poly2 == null) {
			Node pointer = poly1.next;
			Node p2 = new Node(poly1.term.coeff, poly1.term.degree, null);
			Node pointer1 = p2;
			while (pointer != null) {
				pointer1.next = new Node(pointer.term.coeff, pointer.term.degree, null);
				pointer1 = pointer1.next;
				pointer = pointer.next;
			}
			return p2;
		}

		Node unsortedResult = null;
		boolean added = false;
		for (Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next) {
			added = false;
			for (Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next) {
				if (ptr1.term.degree == ptr2.term.degree) {
					if (unsortedResult == null) {

						if (ptr1.term.coeff + ptr2.term.coeff == 0) {
							added = true;
							break;
						}
						unsortedResult = new Node((ptr1.term.coeff + ptr2.term.coeff), (ptr1.term.degree), null);
						added = true;
					} else {
						if (ptr1.term.coeff + ptr2.term.coeff == 0) {
							added = true;
							break;
						}
						Node ptr = unsortedResult;
						while (ptr.next != null)
							ptr = ptr.next;
						ptr.next = new Node((ptr1.term.coeff + ptr2.term.coeff), (ptr1.term.degree), null);
						added = true;
					}

				}
			}

			if (added != true) {
				if (unsortedResult == null) {

					unsortedResult = new Node((ptr1.term.coeff), (ptr1.term.degree), null);
					added = true;
				} else {

					Node ptr = unsortedResult;

					while (ptr.next != null)
						ptr = ptr.next;
					ptr.next = new Node((ptr1.term.coeff), (ptr1.term.degree), null);
					added = true;
				}
			}

		}

		for (Node ptr1 = poly2; ptr1 != null; ptr1 = ptr1.next) {
			added = false;
			for (Node ptr2 = poly1; ptr2 != null; ptr2 = ptr2.next) {
				if (ptr2.term.degree == ptr1.term.degree) {
					added = true;
				}
			}
			if (added != true) {
				if (unsortedResult == null) {

					unsortedResult = new Node((ptr1.term.coeff), (ptr1.term.degree), null);
					added = true;
				} else {

					Node ptr = unsortedResult;

					while (ptr.next != null)
						ptr = ptr.next;
					ptr.next = new Node((ptr1.term.coeff), (ptr1.term.degree), null);
					added = true;
				}
			}
		}
		return sort(unsortedResult);

	}

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

		if (poly1 == null || poly2 == null)
			return null;

		Node result = null;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node temp;

		while (ptr1 != null) {
			ptr2 = poly2;
			while (ptr2 != null) {
				temp = new Node((ptr1.term.coeff * ptr2.term.coeff), (ptr1.term.degree + ptr2.term.degree), null);
				result = add(result, temp);
				ptr2 = ptr2.next;
			}
			ptr1 = ptr1.next;
		}
		return result;
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x    Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		if (poly == null)
			return 0;
		float sum = 0;
		Node ptr = poly;
		while (ptr.next != null) {
			sum += ptr.term.coeff * Math.pow(x, ptr.term.degree);
			ptr = ptr.next;
		}
		sum += ptr.term.coeff * Math.pow(x, ptr.term.degree);
		return sum;
	}

	/**
	 * Returns the head of the sorted linked list with degrees in ascending order
	 * 
	 * @param head Polynomial (front of linked list) to be sorted
	 * @return Head of sorted Linked List
	 */
	private static Node sort(Node head) {
		// This helper method sorts the linked list with the degrees in ascending order

		Node ptr = head;
		Node ptr2 = null;
		float t1;
		int t2;
		while (ptr != null) {
			ptr2 = ptr.next;
			while (ptr2 != null) {
				if (ptr.term.degree > ptr2.term.degree) {
					t1 = ptr.term.coeff;
					t2 = ptr.term.degree;

					ptr.term.coeff = ptr2.term.coeff;
					ptr.term.degree = ptr2.term.degree;

					ptr2.term.coeff = t1;
					ptr2.term.degree = t2;
				}
				ptr2 = ptr2.next;
			}
			ptr = ptr.next;
		}
		return head;
	}

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next; current != null; current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}
