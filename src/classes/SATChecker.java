package classes;

import java.util.*;

public class SATChecker {

    public ArrayList<String> solution = new ArrayList<>();
    private Map<Character, Boolean> termDictionary = new HashMap<>();
    private Stack<Character> charStack = new Stack<>();
    private ArrayList<Character> terms = new ArrayList<>();
    private ArrayList<Formula> formulaList = new ArrayList<>();
    boolean isSetSatisfiable = true;

    /*
    Verify whether the equation or set of equations is satisfiable
     */
    public boolean isSatisfiable(ArrayList<String> formulas) {
        // gather all terms
        for (String formula : formulas) { // for each formula
            Formula formulaObject = new Formula(formula);
            String[] splitFormula = formula.split(" "); // split the formula up by space
            for (String term : splitFormula) { // for each of these parts
                char[] chars = term.toCharArray(); // turn each term into a character array
                for (char character : chars) { // for each character, terms should either be a single letter or a letter with !
                    if (Character.isLetter(character)) { //if it's a letter
                        if (termDictionary.get(character) == null) { // add it to our dictionary and list of terms if it's not already there
                            termDictionary.put(character, false);
                            terms.add(character);
                            formulaObject.setTerms(character);
                        }
                    }
                }
            }
            formulaList.add(formulaObject);
        }

        // generate number or rows in our truth table
        int numRows = (int) Math.pow(2, terms.size());

        // map for translating ints to booleans
        Map<Integer, Boolean> booleanMap = new HashMap<>();
        booleanMap.put(0, false);
        booleanMap.put(1, true);

        boolean isSetSatisfiable = false;

        /*for (int i = 0; i < terms.size(); i++) {
            for (int j = 0; j < 2; j++) {
                for (String formula : formulas) {
                
                }
                termDictionary.replace(terms.get(i), booleanMap.get(j)); //set the row values for our terms
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <terms.size() ; j++) {
                termDictionary.replace(terms.get(j), booleanMap.get(i)); //set the row values for our terms
            }
        }*/

        Node firstNode = new Node();

        for (Character term : terms) {
            termDictionary.replace(term, null);
            firstNode.states.add(null);
        }

        traverseTree(firstNode);

        boolean isTreeClosed = false;
        int i = 0;

        while (!isTreeClosed) {
            Node root = new Node(terms.get(i));

            if (i == 0) {
                root.isRoot = true;
            }

            charStack.push(terms.get(i));

            for (Formula formula : formulaList) {
                boolean isFormulaSatisfiable = true;
                boolean matches = true;
                if (formula.getTerms().size() == charStack.size()) {
                    Stack<Character> tempStack = charStack;
                    for (int j = 0; j < tempStack.size(); j++) {
                        boolean termMatches = false;
                        for (int k = 0; k < formula.getTerms().size(); k++) {
                            if (formula.getTerms().get(k) == tempStack.peek()) {
                                termMatches = true;
                                break;
                            }
                        }
                        if (!termMatches) {
                            matches = false;
                            break;
                        }
                    }
                }

                if (matches) {
                    String[] splitFormula = breakUpTerms(formula); // separate out the terms first
                    isFormulaSatisfiable = isFormulaSatisfiable && evaluateFormula(splitFormula); // update whether the solution is correct for all formulas
                    if (isFormulaSatisfiable) {

                    } else {
                        break;
                    }
                } else {
                    i++;
                    if (root.getLeftNode() == null) {
                        Node nextNode = new Node(terms.get(i), false);
                        root.setLeftNode(nextNode);
                        nextNode.setParentNode(root);
                    } else {
                        Node nextNode = new Node(terms.get(i), true);
                        root.setRightNode(nextNode);
                        nextNode.setParentNode(root);
                    }
                }
            }


        }
        return false;

        //traverseTree(root);

        /*for (int i = 0; i < numRows; i++) {
            boolean isFormulaSatisfiable = true;

            for (int j = 0; j < terms.size(); j++) {
                termDictionary.replace(terms.get(j), booleanMap.get((i / (int) Math.pow(2, j)) % 2)); //set the row values for our terms
            }

            //do equations with current terms
            for (String formula : formulas) {
                String[] splitFormula = breakUpTerms(formula); // separate out the terms first
                isFormulaSatisfiable = isFormulaSatisfiable && evaluateFormula(splitFormula); // update whether the solution is correct for all formulas
            }

            isSetSatisfiable = isFormulaSatisfiable || isSetSatisfiable; // update whether the set is satisfiable
            if (isFormulaSatisfiable) {
                solution.add(termDictionary.toString()); // add this to our solution if it works
            }
        }*/

        return isSetSatisfiable;
    }

    /*
    Breaks up the formula into individual terms and operations by spaces and by brackets, so the following formula
    "((p || q) && r) ? t"
    will be turned into
    "((p || q) && r)" + "?" + "t"
     */
    private String[] breakUpTerms(Formula formulaObject) {
        String formula = formulaObject.getFormulaString();
        if (formula.contains("(") && formula.contains(")")) {
            ArrayList<Integer> startSeparations = new ArrayList<>();
            ArrayList<Integer> endSeparations = new ArrayList<>();
            int bracketCount = 0; // keeps track of how many open brackets we've got
            for (int i = 0; i < formula.length(); i++) { // go through the formula char by char
                if (formula.charAt(i) == '(') {
                    if (bracketCount == 0) { // if this is the first left bracket
                        if (i > 0) {
                            if (formula.charAt(i - 1) == '!') { // check if there's a ! before the bracket
                                startSeparations.add(i - 1); // add start separator at the !
                            } else {
                                startSeparations.add(i); // add start separator at bracket
                            }
                        } else {
                            startSeparations.add(i); // add start separator at bracket
                        }
                    }
                    bracketCount++; // increment to indicate we have one more open bracket
                } else if (formula.charAt(i) == ')') {
                    bracketCount--; // decrement to indicate we've closed the bracket
                    if (bracketCount == 0) { // if we've closed all the brackets
                        endSeparations.add(i); // add end separator at bracket
                    }
                }
            }

            // Below we use the separators to create our arrayList of formulas
            ArrayList<String> formulaArray = new ArrayList<>();
            for (int i = 0; i < startSeparations.size(); i++) {
                if (i == 0 && startSeparations.get(i) != 0) { // brackets not at the beginning
                    formulaArray.addAll(Arrays.asList(formula.substring(0, startSeparations.get(i) - 1).split(" "))); // separate and add first non-bracketed section
                }
                formulaArray.add(formula.substring(startSeparations.get(i), endSeparations.get(i) + 1)); // separate and add bracketed sections
                if (i + 1 < startSeparations.size()) { // if there are non bracketed sections after, separate and add them
                    formulaArray.addAll(Arrays.asList(formula.substring(endSeparations.get(i) + 2, startSeparations.get(i + 1) - 1).split(" ")));
                }
                if (i == startSeparations.size() - 1 && endSeparations.get(i) != formula.length() - 1) { // add last non-bracketed terms if there are any
                    formulaArray.addAll(Arrays.asList(formula.substring(endSeparations.get(i) + 2).split(" ")));
                }
            }

            String[] splitFormula = new String[formulaArray.size()];
            formulaArray.toArray(splitFormula); // copy array list to array

            return splitFormula;
        } else {
            return formula.split(" "); // if no brackets, just split it based on spaces
        }
    }

    private boolean traverseTree(Node root) {
        if (root.terms.size() == 0) {
            if (root.getLeftNode() == null) {
                Node leftNode = new Node();
                leftNode.states.add(0, false);
                leftNode.terms.add(terms.get(0));
                root.setLeftNode(leftNode);
                leftNode.setParentNode(root);
                traverseTree(leftNode);
            } else if (root.getRightNode() != null) {
                return false;
            } else {
                Node rightNode = new Node();
                rightNode.states.add(0, true);
                rightNode.terms.add(terms.get(0));
                root.setRightNode(root);
                rightNode.setParentNode(root);
                traverseTree(rightNode);
            }
        } else {
            boolean isSetSatisfiable = true;
            boolean currentSolutionSatisfies = false;
            boolean areFormulasDoable = false;
            for (Formula formula : formulaList) {
                boolean isFormulaSatisfiable = true;
                boolean isFormulaDoable = false;
                if (formula.getTerms().size() == root.terms.size()) {
                    for (int j = 0; j < root.terms.size(); j++) {
                        boolean termMatches = formula.getTerms().contains(root.terms.get(j));
                        if (!termMatches) {
                            isFormulaDoable = false;
                            break;
                        } else {
                            isFormulaDoable = true;
                        }
                    }
                    areFormulasDoable = areFormulasDoable || isFormulaDoable;
                } else {

                }

                if (isFormulaDoable) {
                    String[] splitFormula = breakUpTerms(formula); // separate out the terms first
                    isFormulaSatisfiable = evaluateFormula(splitFormula); // update whether the solution is correct for all formulas
                    if(!isFormulaSatisfiable){
                        break;
                    } else {
                        currentSolutionSatisfies = true;
                    }
                }
            }


            if(areFormulasDoable && currentSolutionSatisfies){

            }

            if(currentSolutionSatisfies){

            }
        }
        return false;
    }

    /*
    Returns whether or not the formula resolves with the current inputs
     */
    private boolean evaluateFormula(String[] splitFormula) {
        boolean term1 = false;
        boolean term2 = false;

        // check if the first term is bracketed
        if (splitFormula[0].endsWith(")")) {
            if (splitFormula[0].startsWith("(")) {
                term1 = evaluateFormula(breakUpTerms(splitFormula[0].substring(1, splitFormula[0].length() - 1))); // function calls itself using only the first term with the brackets stripped out
            } else if (splitFormula[0].startsWith("!(")) {
                term1 = !evaluateFormula(breakUpTerms(splitFormula[0].substring(2, splitFormula[0].length() - 1))); // same as above but with an !
            }
        } else {
            // see if first term has already been set
            switch (splitFormula[0]) {
                case "true":
                    term1 = true;
                    break;
                case "false":
                    term1 = false;
                    break;
                default: // if it hasn't, assign it to the appropriate term in the dictionary
                    if (splitFormula[0].toCharArray()[0] == '!') {
                        term1 = !termDictionary.get(splitFormula[0].toCharArray()[1]);
                    } else {
                        term1 = termDictionary.get(splitFormula[0].toCharArray()[0]);
                    }
                    break;
            }
        }

        // check if the second term is bracketed
        if (splitFormula[2].endsWith(")")) {
            if (splitFormula[2].startsWith("(")) {
                term2 = evaluateFormula(breakUpTerms(splitFormula[2].substring(1, splitFormula[2].length() - 1))); // function calls itself using only the second term with the brackets stripped out
            } else if (splitFormula[2].startsWith("!(")) {
                term2 = !evaluateFormula(breakUpTerms(splitFormula[2].substring(2, splitFormula[2].length() - 1))); // same as above but with an !
            }
        } else { // if it isn't, assign it to the appropriate term in the dictionary
            if (splitFormula[2].toCharArray()[0] == '!') {
                term2 = !termDictionary.get(splitFormula[2].toCharArray()[1]);
            } else {
                term2 = termDictionary.get(splitFormula[2].toCharArray()[0]);
            }
        }

        boolean answer = false;

        // set result of first part of equation
        switch (splitFormula[1]) {
            case "||": // OR
                answer = term1 || term2;
                break;
            case "&&": // AND
                answer = term1 && term2;
                break;
            case "?": // CONDITIONAL
                answer = !(term1 && !term2);
                break;
            case "??": // BICONDITIONAL
                answer = term1 == term2;
                break;
        }

        // if there are more terms, function calls itself
        if (splitFormula.length > 3) {
            String[] newSplitFormula = new String[splitFormula.length - 2]; // create a new array whose length is 2 less than the original

            // set first part of equation to true or false depending on the outcome of the first 2 terms
            if (answer) {
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
