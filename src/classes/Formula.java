package classes;

import java.util.ArrayList;

public class Formula {
    private String formulaString;
    private ArrayList<Character> terms;

    public Formula(String formulaString) {
        this.formulaString = formulaString;
        terms = new ArrayList<>();
    }

    public ArrayList<Character> getTerms() {
        return terms;
    }

    public void setTerms(Character term) {
        this.terms.add(term);
    }

    public String getFormulaString() {
        return formulaString;
    }
}
