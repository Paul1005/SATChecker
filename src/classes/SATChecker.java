package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SATChecker {

    private Map<Character, Boolean> termDictionary = new HashMap<>();
    private ArrayList<Character> terms = new ArrayList<>();

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

        boolean isSetSatisfiable = false;
        for (int i = 0; i < numRows; i++) {
            boolean isSatisfiable = true;
            for (int j = 0; j < terms.size(); j++) {
                termDictionary.replace(terms.get(j), booleanMap.get((i / (int) Math.pow(2, j)) % 2));
            }

            //do equations with current terms
            for (String formula : formulas) {
                String[] splitFormula = breakUpTerms(formula);
                isSatisfiable = isSatisfiable && evaluateFormula(splitFormula);
            }

            isSetSatisfiable = isSatisfiable || isSetSatisfiable;
            System.out.println(termDictionary.toString());
            System.out.println(isSatisfiable);
        }
        System.out.println(isSetSatisfiable);
        return isSetSatisfiable;
    }

    private String[] breakUpTerms(String formula) {
        if(formula.contains("(") && formula.contains(")")){
            ArrayList<Integer> startSeparations = new ArrayList<>();
            ArrayList<Integer> endSeparations = new ArrayList<>();
            int bracketCount = 0;
            for(int i = 0; i<formula.length(); i++){
                if(formula.charAt(i) == '('){
                    bracketCount++;
                    if(bracketCount == 0){
                        startSeparations.add(i);
                    }
                } else if(formula.charAt(i) == ')'){
                    bracketCount--;
                    if(bracketCount == 0){
                        endSeparations.add(i);
                    }
                }
            }

            ArrayList<String> formulaArray = new ArrayList<>();
            for(int i = 0; i < startSeparations.size(); i++) {
                if(i == 0 && startSeparations.get(i) != 0){
                    formulaArray.addAll(Arrays.asList(formula.substring(0, startSeparations.get(i)).split(" ")));
                } else {
                    formulaArray.add(formula.substring(startSeparations.get(i) + 1, endSeparations.get(i)));
                    if(i+1 < startSeparations.size()) {
                        formulaArray.addAll(Arrays.asList(formula.substring(endSeparations.get(i) + 1, startSeparations.get(i + 1)).split(" ")));
                    }
                }
            }

            String[] splitFormula = new String[formulaArray.size()];
            formulaArray.toArray(splitFormula);

            return splitFormula;
        } else {
            return formula.split(" ");
        }
    }

    private boolean evaluateFormula(String[] splitFormula) {
        boolean term1;
        boolean term2;

        // see if first term has already been set
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

        // get result of second term
        if (splitFormula[2].toCharArray()[0] == '!') {
            term2 = !termDictionary.get(splitFormula[2].toCharArray()[1]);
        } else {
            term2 = termDictionary.get(splitFormula[2].toCharArray()[0]);
        }

        boolean answer = false;

        // set result of first part of equation
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

        // if there are more terms, function calls itself
        if (splitFormula.length > 3) {
            String[] newSplitFormula = new String[splitFormula.length-2];

            // set first part of equation to true or false
            if(answer){
                newSplitFormula[0] = "true";
            } else {
                newSplitFormula[0] = "false";
            }

            // copy remainder of old formula to new one
            System.arraycopy(splitFormula, 3, newSplitFormula, 1, splitFormula.length - 3);

            return evaluateFormula(newSplitFormula);
        } else {
            return answer;
        }
    }
}
