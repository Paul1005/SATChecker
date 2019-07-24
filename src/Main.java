import classes.SATChecker;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> formulas = new ArrayList<>();
        formulas.add("p ?? q");
        //formulas.add("p || r");
        SATChecker satChecker = new SATChecker();
        satChecker.isSatisfiable(formulas);
    }
}
