package csen1002.main.task8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Ahmed Soliman
 * @id 49-2711
 * @labNumber 23
 */

public class CfgLl1Parser {

	private ArrayList<String> LHS;
	private ArrayList<String> terminals;
	private ArrayList<ArrayList<String>> RHS;
	
	private ArrayList<ArrayList<String>> first;
	private ArrayList<ArrayList<String>> follow;

	String[][] table;


	public CfgLl1Parser(String input) {
		LHS = new ArrayList<>();
		terminals = new ArrayList<>();
		RHS = new ArrayList<>();
		
		first = new ArrayList<>();
		follow = new ArrayList<>();
		
		parser(input);
		System.out.println("First: " + first);
		System.out.println("Follow: " + follow);
		table();
	}
	
	public void parser(String cfgString) {
		String[] parts = cfgString.split("#");
		String[] LHStrings = parts[0].split(";");
		String[] constantsStrings = parts[1].split(";");
		String[] RHStrings = parts[2].split(";");
		String[] firstStrings = parts[3].split(";");
		String[] followStrings = parts[4].split(";");

		LHS.addAll(Arrays.asList(LHStrings));

		terminals.addAll(Arrays.asList(constantsStrings));
		terminals.add("$");
		
		System.out.println("Terminals: " + terminals);

		System.out.println("LHS: " + LHS);

		for (String rule : RHStrings) {
			String[] ruleParts = rule.split("/");
			String[] rightHandSides = ruleParts[1].split(",");
			ArrayList<String> ruleList = new ArrayList<>(Arrays.asList(rightHandSides));
			RHS.add(ruleList);

			System.out.println("Added rule: " + ruleList);

		}
		
		for (String rule : firstStrings) {
			String[] ruleParts = rule.split("/");
			String[] rightHandSides = ruleParts[1].split(",");
			ArrayList<String> ruleList = new ArrayList<>(Arrays.asList(rightHandSides));
			first.add(ruleList);

			System.out.println("Added First RHS: " + ruleList);

		}
		
		for (String rule : followStrings) {
			String[] ruleParts = rule.split("/");
			String[] rightHandSides = ruleParts[1].split(",");
			ArrayList<String> ruleList = new ArrayList<>(Arrays.asList(rightHandSides));
			follow.add(ruleList);

			System.out.println("Added Follow RHS: " + ruleList);

		}
	}

	public String parse(String input) {
		input+= "$";
		Stack<String> stack = new Stack<>();
		String derivation = "";
		char [] inputs = input.toCharArray();
		ArrayList<String> charString = new ArrayList<String>();

		for (int i = 0; i < inputs.length; i++) {
			charString.add(Character.toString(inputs[i]));
		}

		stack.push("$");
		stack.push(LHS.get(0));
		derivation = LHS.get(0) + ";";
		String temp = "";
		int varIndex;
		int termIndex;
		int length;
		String[] parts;
		String modify;
		String popped;
		int c = 0;
		String inp;
		while (!stack.isEmpty()) {
			inp = charString.get(c);
			if(stack.peek().equals("$")){
				stack.pop();
				break;
			}
			else if(stack.peek().equals(inp)){
				stack.pop();
				c++;
				continue;

			} else if(LHS.contains(stack.peek())){
				varIndex = LHS.indexOf(stack.peek());
				termIndex = terminals.indexOf(inp);
				temp = table[varIndex][termIndex];
				popped = stack.pop();

				if (temp == null) {
					derivation += "ERROR";
					System.out.println("Process null: " + derivation);
					break;
				} else if(temp.equals("e")) {
					parts = derivation.split(";");
					modify = parts[parts.length - 1];
		
					modify = modify.replaceFirst(popped, "");
		
					derivation += modify + ";";
					System.out.println("Process e: " + derivation);
				} else {
				inputs = temp.toCharArray();

				length = inputs.length - 1;
				for (int i = 0; i <= length; i++) {
					stack.push(Character.toString(inputs[length - i]));
				}
					parts = derivation.split(";");
					modify = parts[parts.length - 1];
		
					modify = modify.replaceFirst(popped, temp);
		
					derivation += modify + ";";
					System.out.println("Process normal: " + derivation);
				}
			}  else{
					derivation += "ERROR";
					System.out.println("Process non equal terminals: " + derivation);
					break;
			}
		}

		System.out.println("Result: " + derivation);
		if(String.valueOf(derivation.charAt(derivation.length()-1)).equals(";")) {
			derivation = (String) derivation.subSequence(0, derivation.length()-1);
		}	
		return derivation;
	}
	
	private void table() {
		table = new String[LHS.size()][terminals.size()];
		ArrayList<String> LHSfirst = new ArrayList<String>();
		ArrayList<String> LHSfollow = new ArrayList<String>();
		ArrayList<String> getRHS = new ArrayList<String>();
		char [] firstLetters;
		char[] followLetters; 
		for (int i = 0; i < LHS.size(); i++) {
			LHSfirst = first.get(i);
			LHSfollow = follow.get(i);
			getRHS = RHS.get(i);

			System.out.println("FOR VAR: " + LHS.get(i) + "/" + getRHS + " FIRST: " + LHSfirst );
		    for (int j = 0; j < terminals.size(); j++) {
		    	for(String firstEntry : LHSfirst) {
					firstLetters = firstEntry.toCharArray();
					ArrayList<String> letters = new ArrayList<String>();
					for (int k = 0; k < firstLetters.length; k++) {
						letters.add(Character.toString(firstLetters[k]));
					}

					for(String followEntry : LHSfollow) {
						followLetters = followEntry.toCharArray();
						ArrayList<String> fletters = new ArrayList<String>();
						for (int k = 0; k < followLetters.length; k++) {
							fletters.add(Character.toString(followLetters[k]));
						}

					for(String entr : getRHS){
						for( int z = 0 ; z < letters.size(); z++ ){
							String l = letters.get(z);
							if (entr.equals("e")) {
								for(String fl : fletters){
									table[i][terminals.indexOf(fl)] = "e";
								}
								
							} else if(entr.contains(l)){
								table[i][terminals.indexOf(l)] = entr;

								for(int a = z ; a < letters.size(); a++ ){
									int xn = terminals.indexOf(letters.get(a));
									table[i][xn] = entr;
								}
								break;
							} else{
								table[i][j] = null;
							}
						}
					}
		    	}
		    }
		}

	}
	

		System.out.print("  ");
		for (String terminal : terminals) {
		    System.out.print(terminal + "\t");
		}
		System.out.println(); 
		for (int i = 0; i < LHS.size(); i++) {
		    System.out.print(LHS.get(i) + " ");
		    for (int j = 0; j < terminals.size(); j++) {
		        System.out.print(table[i][j] + "\t");
		    }
		    System.out.println();
		}
		
	}
	
	public static void main(String[] args) {
		CfgLl1Parser ll1 = new CfgLl1Parser("S;H;M;U;J#g;i;k;r;v;w;x#S/iJrS,Jw;H/xMw,vHgU,e;M/gM,xSU,e;U/JgH,iJ,k,e;J/v,rSMw#S/i,rv;H/x,v,e;M/g,x,e;U/rv,i,k,e;J/v,r#S/$gikrvwx;H/gw;M/w;U/gw;J/grw");
		ll1.parse("rrriwwrk$");
	}

}
