package classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SATChecker {

    public boolean isSatisfiable(ArrayList<String> formulas){

        //gather all terms
        Map<Character, Boolean> termDictionary = new HashMap<>();
        ArrayList<Character> terms = new ArrayList<>();
        //int numTerms = 0;

        for (String formula :formulas) {
            String[] splitFormula = formula.split(" ");
            for(String term : splitFormula){
                char[] chars = term.toCharArray();
                for(char character : chars){
                    if(Character.isLetter(character)){
                        if(termDictionary.get(character) == null) {
                            //numTerms++;
                            termDictionary.put(character, false);
                            terms.add(character);
                        }
                    }
                }
            }
        }

        // generate truth table
        int numRows = (int)Math.pow(2, terms.size());
        Map<Integer, Boolean> booleanMap = new HashMap<>();
        booleanMap.put(0, false);
        booleanMap.put(1, true);


        for (int i = 0; i< numRows; i++) {
            for (int j = 0; j < terms.size(); j++) {
                termDictionary.replace(terms.get(j), booleanMap.get((i/(int) Math.pow(2, j))%2));
            }
            System.out.println(termDictionary.toString());
        }

        return false;
    }
}
