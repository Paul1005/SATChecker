package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SATChecker {

    Map<Character, Boolean> termDictionary = new HashMap<>();
    ArrayList<Character> terms = new ArrayList<>();

    public boolean isSatisfiable(ArrayList<String> formulas) {

        //gather all terms
        for (String formula : formulas) {
            String[] splitFormula = formula.split(" ");
            for (String term : splitFormula) {
                char[] chars = term.toCharArray();
                for (char character : chars) {
                    if (Character.isLetter(character)) {
                        if (termDictionary.get(character) == null) {
                            termDictionary.put(character, false);
                            terms.add(character);
                        }
                    }
                }
            }
        }

        // generate truth table
        int numRows = (int) Math.pow(2, terms.size());
        Map<Integer, Boolean> booleanMap = new HashMap<>();
        booleanMap.put(0, false);
        booleanMap.put(1, true);


        for (int i = 0; i < numRows; i++) {
            boolean isSatisfiable = true;
            for (int j = 0; j < terms.size(); j++) {
                termDictionary.replace(terms.get(j), booleanMap.get((i / (int) Math.pow(2, j)) % 2));
            }

            //do equations with current terms
            for (String formula : formulas) {
                String[] splitFormula = formula.split(" ");
                isSatisfiable = isSatisfiable && evaluateFormula(splitFormula);
            }
            System.out.println(termDictionary.toString());
            System.out.println(isSatisfiable);
        }

        return false;
    }

    private boolean evaluateFormula(String[] splitFormula) {
        boolean term1;
        boolean term2;

        switch (splitFormula[0]) {
            case "true":
                term1 = true;
                break;
            case "false":
                term1 = false;
                break;
            default:
                if (splitFormula[0].toCharArray()[0] == '!') {
                    term1 = !termDictionary.get(splitFormula[0].toCharArray()[1]);
                } else {
                    term1 = termDictionary.get(splitFormula[0].toCharArray()[0]);
                }
                break;
        }

        if (splitFormula[2].toCharArray()[0] == '!') {
            term2 = !termDictionary.get(splitFormula[2].toCharArray()[1]);
        } else {
            term2 = termDictionary.get(splitFormula[2].toCharArray()[0]);
        }

        boolean answer = false;


        switch (splitFormula[1]) {
            case "||":
                answer = term1 || term2;
                break;
            case "&&":
                answer = term1 && term2;
                break;
            case "?":
                answer = !(term1 && !term2);
                break;
            case "??":
                answer = term1 == term2;
                break;
        }

        if (splitFormula.length > 3) {
            String[] newSplitFormula = new String[splitFormula.length-2];

            if(answer){
                newSplitFormula[0] = "true";
            } else if (!answer){
                newSplitFormula[0] = "false";
            }

            System.arraycopy(splitFormula, 3, newSplitFormula, 1, splitFormula.length - 3);

            return evaluateFormula(newSplitFormula);
        } else {
            return answer;
        }
    }
}
