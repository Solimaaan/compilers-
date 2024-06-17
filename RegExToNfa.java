package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Ahmed Soliman
 * @id 49-2711
 * @labNumber 23
 */

public class RegExToNfa {
	
	static int stateNum = 0;
	static Stack<String> stack = new Stack<>();;
	static ArrayList<String> inp = new ArrayList<>();;
	static ArrayList<Integer> states = new ArrayList<>();;
	static ArrayList<String> transitions = new ArrayList<>();;
	static ArrayList<String> alphabet = new ArrayList<>();;
	static ArrayList<String> postFix = new ArrayList<>();;

	

	public RegExToNfa(String input) {
		stateNum = 0;
		stack = new Stack<>();
		inp = new ArrayList<>();
		states = new ArrayList<>();
		transitions = new ArrayList<>();
		alphabet = new ArrayList<>();
		postFix = new ArrayList<>();
		
		parser(input);
		seperateInp(inp);
		expandPostFix(postFix);
	}
	
	public static ArrayList<String> parser(String input){
		for(int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			inp.add(String.valueOf(c));
			System.out.println(c);
		}
		return inp;
	}
	
	public static void seperateInp(ArrayList<String> input) {
		boolean hash = false;
		String ch;
		char c;
		
		for(int i = 0; i < input.size(); i++) {
			ch = input.get(i);
			c = ch.charAt(0);
			if(input.get(i).equals("#")) {
				hash = true;
				continue;
			}
			if(input.get(i).equals(";")) {
				continue;
			}
			else if(!hash) {
				alphabet.add(input.get(i));
				System.out.println("alphabet: " + input.get(i));
			} else {
				if(input.get(i).equals("e")) {
					postFix.add("e");
				}
				if (!(alphabet.contains(input.get(i))) && Character.isLetter(c)) {
					System.out.println("can't do");
				} else {
					postFix.add(input.get(i));
					System.out.println("post Fix: " + input.get(i));
				}

			}
		}
	}
	
	public static void expandPostFix(ArrayList<String> pf) {
		//states.add(stateNum);
		String x;
		String y;
		String z;
		String a;
		int b;
		String temp;
		String temp2;
		for(int i = 0; i < pf.size(); i++) {
			switch (pf.get(i)) {
			case ".":
				x = stack.pop();
				y = stack.pop();
				temp = extractNumber(x, true);
				temp2 = extractNumber(y, false);
				x = x.replaceAll(temp, temp2);
				z = y + x;
				b = Integer.parseInt(temp);
				states.remove(Integer.valueOf(b));
				stack.push(z);
				System.out.println("Concat: " + z);
				break;
			case "|":
				states.add(stateNum);
			    x = stack.pop();
			    System.out.println("x: " + x);
			    temp = extractNumber(x, true);
			    z = stateNum + "," + "e" + "," + temp + ";";
			    transitions.add(z); // new start to old start x
			    z = z + x;
			    temp = extractNumber(x, false);
			    x = temp + "," + "e" + "," + (stateNum+1) + ";";
			    z = z + x;
			    transitions.add(x); // old finish x to new finish  
			    y = stack.pop();
			    System.out.println("y: " + y);
			    temp = extractNumber(y, true);
			    a = stateNum + "," + "e" + "," + temp + ";";
			    transitions.add(a); // new start to old start y
			    z = a + z + y;
			    temp = extractNumber(y, false);
			    stateNum++;
			    states.add(stateNum);
			    a = temp + "," + "e" + "," + stateNum + ";";
			    transitions.add(a); // old finish y to new finish 
			    z = z + a;
			    stack.push(z);
			    stateNum++;
			    System.out.println("stack final after union " + z);
			    break;
			case "*":
				states.add(stateNum);
				x = stack.pop();
				temp = extractNumber(x, true);
				y = stateNum + "," + "e" + "," + temp + ";"; //new start to old start
				transitions.add(y);
				stateNum++;
				z = y;
				y = "e" + "," + temp + ";"; 
				temp = extractNumber(x, false);
				y = temp + "," + y;
				transitions.add(y); //old finish to old start
				z = z + y;
				y = x + temp + "," + "e" + "," + stateNum + ";";
				transitions.add(y); //old finish to new finish
				z = z + y;
				y = (stateNum - 1) + "," + "e" + "," + stateNum + ";"; 
				transitions.add(y); //new start to new finish
				states.add(stateNum);
				stateNum++;
				z = z + y;
				stack.push(z);
				System.out.println("stack final after star " + z);
				
				break;
			default: 
				states.add(stateNum);
				z = stateNum + "," + pf.get(i) + ",";
				stateNum++;
				states.add(stateNum);
				z = z + stateNum + ";";
				stack.push(z);
				stateNum++; 
				transitions.add(z);
				System.out.println("transition " + z);
				//z = "";
				break;
			}
		}
	}
	
	public static String extractNumber(String input, boolean isFirst) {
        String delimiter = ",";
        String[] parts = input.split(";");
        String targetNumber;
        if (isFirst) {
            // Extract first number
            String[] firstPart = parts[0].split(delimiter);
            targetNumber = firstPart[0];
        } else {
            // Extract last number
            String[] lastPart = parts[parts.length - 1].split(delimiter);
            targetNumber = lastPart[2];
        }

        return targetNumber;
    }
	

	public static String sortTransitions(String input) {
        // Split the input string by the delimiter ;
        String[] transitions = input.split(";");
        
        // Sort the transitions based on their start states
        Arrays.sort(transitions, Comparator.comparingInt(t -> Integer.parseInt(t.split(",")[0])));
        
        // Concatenate the sorted transitions back into a single string
        StringBuilder sortedTransitions = new StringBuilder();
        for (String transition : transitions) {
            sortedTransitions.append(transition).append(";");
        }
        
        // Remove the trailing semicolon if present
        if (sortedTransitions.length() > 0) {
            sortedTransitions.setLength(sortedTransitions.length() - 1);
        }
        
        return sortedTransitions.toString();
    }
	
	public static String arrayListToString(ArrayList<String> list) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < list.size(); i++) {
	        builder.append(list.get(i));
	        if (i < list.size() - 1) {
	            builder.append(";");
	        }
	    }
	    return builder.toString();
    }
	
	public static String arrayListToStringg(ArrayList<Integer> list) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < list.size(); i++) {
	        builder.append(list.get(i));
	        if (i < list.size() - 1) {
	            builder.append(";");
	        }
	    }
	    return builder.toString();
	}
	
	@Override
	public String toString() {
		String s = stack.pop();
		String st = arrayListToStringg(states);
		String a = arrayListToString(alphabet);
		String transitions = sortTransitions(s);
		String startS = extractNumber(s, true);
		String finishS = extractNumber(s,false);
		System.out.println("Final to string:" + st + "#" + a + "#" + transitions + "#" + startS + "#" + finishS);
		return st + "#" + a + "#" + transitions + "#" + startS + "#" + finishS;
	}

}
