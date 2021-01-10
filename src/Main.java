import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    System.out.print("Initializing... ");
    DynProgAlgorithm d = new DynProgAlgorithm();
    System.out.println("done.");

    System.out.print("Asking magic code monkeys for solution... ");
    d.dynProg();
    int[] solTruck1 = d.evaluateDynProgSol(Data.m1);          //  Knapsack1
    Data.subtractFromReq(solTruck1);                          //  remove loaded items from required ones

    d = null;
    System.gc();                                              //  Free some memory for next calculation

    d = new DynProgAlgorithm();
    d.dynProg();
    int[] solTruck2 = d.evaluateDynProgSol(Data.m2);          //  Knapsack2

    System.out.println("done.\n");


    prettyPrintSolution(solTruck1, solTruck2);                //  Print solution
  }

  private static void prettyPrintSolution(int[] sol1, int[] sol2) {
    int solV = 0;                             //  Total value
    int solW = 0;                             //  Total weight

    for (int i = 0; i < sol1.length; i++) {
      solV += (sol1[i] + sol2[i]) * Data.v[i];
      solW += (sol1[i] + sol2[i]) * Data.w[i];
    }

    System.out.println("The solution has an accumulated value of " + solV + "\n\t at total weight of " + solW);

    String format = "%-30s: (%4d + %-4d)/%-4d%n";

    System.out.println("item distribution: ");
    for (int i = 0; i < sol1.length; i++) {
      System.out.format(format, "\t"+Data.names[i], sol1[i], sol2[i], Data.reqOrigin[i]);
    }
  }
}
