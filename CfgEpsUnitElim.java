package csen1002.main.task4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CfgEpsUnitElim {

	private ArrayList<String> variables;
	private ArrayList<String> terminals;
	private ArrayList<ArrayList<String>> rules;
	private ArrayList<String> epPrevAdded;
	boolean substituted = false;

	public CfgEpsUnitElim(String cfg) {
		variables = new ArrayList<>();
		terminals = new ArrayList<>();
		rules = new ArrayList<>();
		epPrevAdded = new ArrayList<>();

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

	@Override
	public String toString() {		
		return formatArrayList(variables) + "#" + formatArrayList(terminals) + "#" + concatRules();
	}

    private String concatRules() {
    	String result = "";
		for(int i =  0; i < rules.size(); i++) {
			result += variables.get(i) + "/" + makeString(rules.get(i)) + ";";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	private String makeString(ArrayList<String> arrayList) {
		ArrayList<String> l = new ArrayList<String>();
		l = arrayList;
		String result = "";
		for(String s : l) {
			result += s + ",";
		}
		result = result.substring(0, result.length() - 1);
		return result;
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
    
	public void eliminateEpsilonRules() {
		ArrayList<String> l = new ArrayList<>();
		String temp;
		String changeVar;
		for (int i = 0; i < rules.size(); i++) {
			l = rules.get(i);
			for (int y = 0; y < l.size(); y++) {
				temp = (String) l.get(y);
				if (temp.equals("e")) {
					l.remove(y);
					changeVar = variables.get(i);
					epPrevAdded.add(changeVar);
					modify(changeVar);
				}
			}
		}

		for (int y = 0; y < rules.size(); y++) {
			l = rules.get(y);
			for (int z = 0; z < l.size(); z++) {
				l.remove("");
				Collections.sort(l);
			}
			System.out.println(rules.get(y));
		}

	}

	private void modify(String changeVar) {
		ArrayList<String> l = new ArrayList<>();
		String temp = null;
		String newInp;
		char[] ch;
		for (int i = 0; i < rules.size(); i++) {
			l = rules.get(i);
			for (int y = 0; y < l.size(); y++) {
				temp = (String) l.get(y);
				ch = temp.toCharArray();
				if (temp.equals(changeVar) && !l.contains("e") && !epPrevAdded.contains(variables.get(i))) {
					rules.get(i).add("e");
				} else {
					for (int z = 0; z < ch.length; z++) {
						char c = ch[z];
						if (c == changeVar.charAt(0)) {
							newInp = charToString(ch, z);
							if (!l.contains(newInp))
								rules.get(i).add(newInp); 

						}
					}
				}
			}

		}

		eliminateEpsilonRules();

	}

	private String charToString(char[] letters, int c) {
		StringBuilder x = new StringBuilder();
		for (int i = 0; i < letters.length; i++) {
			if (i == c) {
				continue;
			} else {
				x.append(letters[i]);
			}
		}
		return x.toString();

	}


	public void eliminateUnitRules() {
	    ArrayList<String> l;
	    String temp;
	    ArrayList<String> changed;
	    boolean isDup = false;

	    for (int i = 0; i < rules.size(); i++) {
	        substituted = false;
	        l = rules.get(i);
	        for (int y = 0; y < l.size(); y++) {
	            temp = l.get(y);
	            if (variables.contains(temp)) {
	                int index = find(temp);
	                if (variables.get(i).equals(temp)) {
	                    l.remove(y);
	                    y--;
	                } else {
	                    changed = modifyRule(index, temp);
	                    for (String c : changed) {
	                        isDup = false;
	                        for (String lx : l) {
	                            if (lx.equals(c)) {
	                                isDup = true;
	                                break;
	                            }
	                        }
	                        if (!isDup && !variables.contains(c)) {
	                            l.add(c);
	                            substituted = true;
	                        }
	                    }
	                }
	                Collections.sort(l);
	            }
	        }
	        if (substituted) {
	            i = -1;
	        }
	    }
	    
	    for(ArrayList<String> bs : rules) {
	        for (int i = 0; i < bs.size(); i++) {
	            temp = bs.get(i);
	            if (variables.contains(temp)) {
	                bs.remove(i);
	                i--; 
	            }
	        }
	    }
	}

	private ArrayList<String> modifyRule(int index, String temp) {
	    ArrayList<String> mod = new ArrayList<>(rules.get(index));
	    mod.remove(temp);
	    return mod;
	}

	private int find(String temp) {
		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).equals(temp)) {
				System.out.println("The variable searched for is " + temp + " the founded is " + variables.get(i)
						+ " at index " + i);
				return i;
			}
		}

		return 0;

	}
}
