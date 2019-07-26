import classes.SATChecker;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> formulas = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Please enter 1 or more formulas \n" +
                "Operand symbols are: \n" +
                "|| for OR \n" +
                "&& for AND \n" +
                "? for CONDITIONAL \n" +
                "?? for BICONDITIONAL \n" +
                "Use ! for negation \n" +
                "Put spaces between terms and operands but not between terms and negations or spaces");
        String formula;
        do {
            formula = scanner.nextLine();
            if(!formula.isEmpty()) {
                formulas.add(formula);
            }
        } while (!formula.isEmpty());

        SATChecker satChecker = new SATChecker();
        System.out.println(satChecker.isSatisfiable(formulas));
    }
}
