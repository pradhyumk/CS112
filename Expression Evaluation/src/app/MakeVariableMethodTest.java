package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MakeVariableMethodTest {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("\nEnter the expression, or hit return to quit => ");
			String expr = sc.nextLine();
			if (expr.length() == 0) {
				break;
			}
			ArrayList<Variable> vars = new ArrayList<>();
			ArrayList<Array> arrays = new ArrayList<>();
			Expression.makeVariableLists(expr, vars, arrays);
			System.out.println("vars: " + vars + "\narrays: " + arrays);
		}
		sc.close();
	}
}