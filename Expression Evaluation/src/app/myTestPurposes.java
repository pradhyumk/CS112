package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class myTestPurposes {

	public static void main(String[] args) throws IOException {

		
		
		/*
		StringTokenizer st = new StringTokenizer(expr," \t*+-/()[]");
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();

			if (expr.charAt(expr.indexOf(token) + token.length() + 1) == '(') {
				//String paren = expr.substring((expr.indexOf(token) + token.length() + 2), expr.indexOf(")",(expr.indexOf(token) + token.length() + 2) ));
				//System.out.println(paren);
				//expr = expr.substring(expr.indexOf(")",(expr.indexOf(token) + token.length() + 2) ));
				//System.out.println("Expr: "+expr);
				
			System.out.println(st.nextToken("()"));
			} else {
				System.out.println(st.nextToken(" \t*+-/()[]"));
			}
		
		}		
		*/
		
		Scanner sc = new Scanner(System.in);

		ArrayList<Variable> vars = new ArrayList<>(); 
		ArrayList<Array> arrays = new ArrayList<>();
		Expression.makeVariableLists("d*(c-B[a+CARR[0]-A[1]+b])", vars, arrays);

		System.out.print("Enter variable values file name, or hit return if no variables => ");
		String fname = sc.nextLine();
		if (fname.length() != 0) {
			Scanner scfile = new Scanner(new File(fname));
			Expression.loadVariableValues(scfile, vars, arrays);
		}
		
		System.out.println(vars.get(vars.indexOf(new Variable("a"))).value);
		
		
		
		System.out.println(vars+"\n"+arrays);
	
		
		/*
		StringTokenizer st = new StringTokenizer("d*(c-B[a+CARR[0]-A[1]+b])"," \t*+-/()[]");
		
		while(st.hasMoreTokens()) {
			System.out.println(st.nextToken());
		}
		
		 * [d=0, c=0, a=0, A=0, b=0]
		 * [B=[ ], CARR=[ ]]
		 */

	}

}
