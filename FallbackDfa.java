package csen1002.main.task3;

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

public class FallbackDfa {

	String startState;
	ArrayList<String> finishState = new ArrayList<>();
	ArrayList<String> alphabet = new ArrayList<>();
	ArrayList<String> states = new ArrayList<>();
	ArrayList<String> transitions = new ArrayList<>();

	ArrayList<String> inputStack = new ArrayList<String>();
	Stack<String> stateStack = new Stack<String>();

	public FallbackDfa(String fdfa) {
		finishState = new ArrayList<>();
		alphabet = new ArrayList<>();
		states = new ArrayList<>();
		transitions = new ArrayList<>();

		parseInput(fdfa);
		//run("oroookoooshkkhs");

	}


	@SuppressWarnings("null")
	public String run(String input) {
		String temp;
		String destinationState = null;
		String finalPop = null;
		String res = null;
		StringBuilder subInp = new StringBuilder();
		int newInd;

		

		for (int i = 0; i < input.length(); i++) {
			inputStack.add(String.valueOf(input.charAt(i)));
			System.out.println("Stack char at " + i + ":" + inputStack.get(i));
		}

		String L = inputStack.get(0);
		for (int i = 0; i <= inputStack.size(); i++) {

			if (i == 0) {
				stateStack.add(startState);
				continue;
			}

			if (i == inputStack.size()) {
				for (String tr : transitions) {
					temp = stateStack.peek() + "," + L;

					if (tr.startsWith(temp)) {
						String[] parts = tr.split(",");
						destinationState = parts[2];
						System.out.println("Completion found: " + temp + "," + destinationState);
						stateStack.add(destinationState);
						break;
					}
				}

				finalPop = stateStack.peek();
				System.out.println("Last item in the stack: " + finalPop);
				break;
			}

			for (String tr : transitions) {
				temp = stateStack.peek() + "," + L;

				if (tr.startsWith(temp)) {
					String[] parts = tr.split(",");
					destinationState = parts[2];
					System.out.println("Completion found: " + temp + "," + destinationState);
					stateStack.add(destinationState);
					break;
				}
			}
			if (!(inputStack.get(i) == null)) {
				L = inputStack.get(i);
				finalPop = stateStack.peek();
				System.out.println("Last item in the stack: " + finalPop);
			}

		}

		int count;
		String check;
		count = inputStack.size() - 1;

		for (int i = 0; i <= inputStack.size(); i++) {

			if (count >= 0) {
				L = inputStack.get(count);
				count -= 1;
			}
			check = stateStack.pop();
			System.out.println("Poped Item: " + check + "and checking the state with input character: " + L);

			if (stateStack.isEmpty()) {

				System.out.println(arrayListToString(inputStack, inputStack.size()) + "," + finalPop);
				res = input + "," + finalPop;
				return res;
			}

			for (String f : finishState) {
				if (check.equals(f)) {
					count++;
					res = arrayListToString(inputStack, count) + "," + check;
					System.out.println(res);
					if (count + 1 < inputStack.size()) {
						L = inputStack.get(count + 1);
						newInd = count + 1;
					} else {
						break;
					}
					stateStack.clear();
					stateStack.push(startState);

					while (newInd < inputStack.size() && inputStack.get(newInd) != null) {
						subInp.append(inputStack.get(newInd));
						newInd++;
					}

					System.out.println("relooping on " + subInp);
					inputStack.clear();
					res = res + ";" + run(subInp.toString());
					System.out.println("Final result after relooping " + res);
					return res;
				}
			}

		}
		if(res == null) {
			return input + "," + finalPop;
		}
		return res;
	}


	public static String arrayListToString(ArrayList<String> list, int endIndex) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i <= endIndex && i < list.size(); i++) {
			stringBuilder.append(list.get(i));
		}

		return stringBuilder.toString();
	}

	public void parseInput(String input) {
		String[] parts = input.split("#");

		states = new ArrayList<>(Arrays.asList(parts[0].split(";")));
		alphabet = new ArrayList<>(Arrays.asList(parts[1].split(";")));

		String[] transitionStrings = parts[2].split(";");
		transitions = new ArrayList<>();

		for (String transitionString : transitionStrings) {
			String[] transitionParts = transitionString.split(",");
			String sourceState = transitionParts[0];
			String inputSymbol = transitionParts[1];
			String destinationState = transitionParts[2];
			transitions.add(sourceState + "," + inputSymbol + "," + destinationState);
		}

		startState = parts[3];
		String[] temp = parts[4].split(";");
		for (int i = 0; i < temp.length; i++) {
			finishState.add(temp[i]);
		}

		System.out.println("States: " + states);
		System.out.println("Alphabet: " + alphabet);
		System.out.println("Transitions: " + transitions);
		System.out.println("Start: " + startState);
		System.out.println("Finish: " + finishState);
	}

}
