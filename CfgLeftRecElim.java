package csen1002.main.task5;

import java.util.ArrayList;
import java.util.Arrays;

public class CfgLeftRecElim {

	private ArrayList<String> variables;
	private ArrayList<String> terminals;
	private ArrayList<ArrayList<String>> rules;

	private ArrayList<String> modifiedStates;
	private ArrayList<String> newStates;
	private ArrayList<ArrayList<String>> newRules;

	public CfgLeftRecElim(String cfg) {
		variables = new ArrayList<>();
		terminals = new ArrayList<>();
		rules = new ArrayList<>();

		modifiedStates = new ArrayList<>();
		newStates = new ArrayList<>();
		newRules = new ArrayList<>();

		parseCfgString(cfg);
	}

	private void parseCfgString(String cfgString) {
		String[] parts = cfgString.split("#");
		String[] variableStrings = parts[0].split(";");
		String[] constantsStrings = parts[1].split(";");
		String[] ruleStrings = parts[2].split(";");

		variables.addAll(Arrays.asList(variableStrings));

		terminals.addAll(Arrays.asList(constantsStrings));

		System.out.println("Variables: " + variables);

		for (String rule : ruleStrings) {
			String[] ruleParts = rule.split("/");
			String[] rightHandSides = ruleParts[1].split(",");
			ArrayList<String> ruleList = new ArrayList<>(Arrays.asList(rightHandSides));
			rules.add(ruleList);

			System.out.println("Added rule: " + ruleList);

		}
	}



	public void eliminateLeftRecursion() {
		ArrayList<String> rhs;
		String lhs;
		for (int i = 0; i < variables.size(); i++) {
			lhs = variables.get(i);
			rhs = rules.get(i);
			updateAll(i);
			removeLR(lhs, rhs, i);
			modifiedStates.add(lhs);
			System.out.println("ModifiedStates: " + modifiedStates);
		}

		for (String s : newStates) {
			variables.add(s);
		}

		for (ArrayList<String> s : newRules) {
			rules.add(s);
		}

		System.out.println("All States " + variables + newStates + "\n" + "All rules: " + rules + newRules);
	}
	
	private void removeLR(String lhs, ArrayList<String> rhs, int x) {
		char c = lhs.charAt(0);
		String newState = c + "'";
		String temp;
		ArrayList<String> nR = new ArrayList<String>();
		ArrayList<String> updateR = new ArrayList<String>();

		for (String entry : rhs) {
			if (c == entry.charAt(0)) { 

				temp = entry.substring(1) + newState;
				nR.add(temp);

			} else {
				updateR.add(entry + newState);
				continue;
			}

			if (!newStates.contains(newState)) {
				newStates.add(newState);
			}
			rules.set(x, updateR);
			if (!nR.contains("e")) {
				nR.add("e");
			}
			if (!newRules.contains(nR)) {
				newRules.add(nR);
			}
			if (nR.contains("e")) {
				nR.remove("e");
				nR.add("e");
			}

		}
		System.out.println("Final new state: " + newState + "-> " + nR + "\n" + "Updated state: " + updateR);

	}

	private void updateAll(int x) {
		int index;
		String toChange;
		ArrayList<String> ruleChanging = new ArrayList<String>();
		ArrayList<String> rule = new ArrayList<String>();

		for (int i = x; i < rules.size(); i++) {
			rule = rules.get(i);
			for (int j = 0; j < rule.size(); j++) {
				boolean isChanged = false;
				String entry = rule.get(j);
				ArrayList<String> newR = new ArrayList<String>();
				if (modifiedStates.contains(String.valueOf(entry.charAt(0)))) {
					for (int z = 0; z < j; z++) {
						if (!newR.contains(rule.get(z))) {
							newR.add(rule.get(z));
						}
					}
					isChanged = true;
					index = variables.indexOf(String.valueOf(entry.charAt(0)));
					ruleChanging = rules.get(index);
					for (String s : ruleChanging) {
						toChange = s + entry.substring(1);
						newR.add(toChange);
					}
				} else {
					newR.add(entry);
				}
				for (int z = j + 1; z < rule.size(); z++) {
					if (!newR.contains(rule.get(z))) {
						newR.add(rule.get(z));
					}
				}
				if (isChanged) {
					rule.clear();
					rule.addAll(newR);
					j = -1;
				}
			}

		}

	}
	
	public static String formatArrayList(ArrayList<String> strings) {
		StringBuilder formattedString = new StringBuilder();

		for (String str : strings) {
			formattedString.append(str).append(";");
		}
		int lastIndex = formattedString.length() - 1;
		if (lastIndex >= 0) {
			formattedString.deleteCharAt(lastIndex);
		}
		return formattedString.toString();
	}

	private String concatRules() {
		String result = "";
		for (int i = 0; i < rules.size(); i++) {
			result += variables.get(i) + "/" + makeString(rules.get(i)) + ";";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	private String makeString(ArrayList<String> arrayList) {
		ArrayList<String> l = new ArrayList<String>();
		l = arrayList;
		String result = "";
		for (String s : l) {
			result += s + ",";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	@Override
	public String toString() {
		String result = formatArrayList(variables) + "#" + formatArrayList(terminals) + "#" + concatRules();
		System.out.println(result);
		return result;
	}

}
