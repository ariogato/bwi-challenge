
class DynProgAlgorithm {

  private boolean isComputed;                           //  Indication whether dp has been filled properly or not
  private int[][] dp;                                   //  Array for dynamic programming
  private boolean[][] added;                            //  Indicator whether an item has been added at a certain
                                                        //  computation step. This is needed for backtracking

  DynProgAlgorithm() {
    isComputed = false;
    dp = new int[Data.numItems()+1][Data.sumValuesHeuristic()];
    added = new boolean[dp.length][dp[0].length];
  }

  /**
   * Attempts to solve the challenge using a dynamic programming approach.
   * The array will be dp with dimensions:
   *    - Data.numItems()+1 columns
   *    - Data.sumValue() rows
   *
   * dp[V][i] is the weight of
   *    - items >= i
   *    - values >= V
   * the weight m1 may be exceeded.
   *
   * dp[V][i] = min(
   *    dp[V][i+1],
   *    w[i] + dp[V - v[i]][i+1]
   * )
   * which is the minimum of
   *    1. the identically valued knapsack without item i (i.e. item i not taken)
   *    2. the knapsack that is v[i] less valuable than the current one + the weight of item i (i.e. item i taken)
   *
   *
   * start-condition at dp[...][Data.numItems()]:
   *    - overfull knapsack: max weight possible (i.e. all items)
   *
   * topological sort:
   *    - i in Data.numItems()..0
   *    - V in 0..Data.sumValues()
   *
   *
   * At the end of the calculation dp[V][i] will represent the minimum weight needed to
   * achieve a value >= V with items >= i.
   *
   */
  void dynProg() {
    int sumValueHeuristic = Data.sumValuesHeuristic();            //  Heuristic of maximum achievable value

    //  Establish the starting condition
    for (int i = 0; i < dp[dp.length-1].length; i++) {
      dp[dp.length-1][i] = Data.sumWeight();
    }

    for (int i = Data.numItems() - 1; i >= 0; i--) {
      for (int V = 0; V < sumValueHeuristic; V++) {

        //  Skip if the value doesn't fit
        if (V < Data.vget(i)) {
          dp[i][V] = 0;
          added[i][V] = false;
          continue;
        }

        //  Set the weight for the current field
        int wAdded = Data.wget(i) + dp[i+1][V - Data.vget(i)];      //  New weight in case item i is added
        int wNotAdded = dp[i+1][V];                                 //  New weight in case item i is not added

        if (wNotAdded <= wAdded) {
          dp[i][V] = wNotAdded;
          added[i][V] = false;
        }
        else {
          dp[i][V] = wAdded;
          added[i][V] = true;
        }

      }
    }

    isComputed = true;
  }

  /**
   * Finds the greatest value computed which has a smaller weight than the given maximum weight
   * @param m maximum weight of knapsack
   * @return array of number of chosen units per item
   */
  int[] evaluateDynProgSol(int m) {
    if (!isComputed)
      throw new RuntimeException("Attempt to evaluate an uncomputed array. Call dynProg() first.");

    int[] addedItems = new int[Data.req.length];

    for (int i = dp[0].length-1; i >= 0; i--) {
      if (m >= dp[0][i]) {
        //  Backtrack the solution to find all added items
        addedItems = backtrack(i);
        break;
      }
    }

    return addedItems;

  }

  /**
   * This method will backtrack the chosen items and return an array accordingly
   * @param i value of the found solution
   * @return array of number of units per item added
   */
  private int[] backtrack(int i) {
    if (!isComputed)
      throw new RuntimeException("Attempt to backtrack an uncomputed array. Call dynProg() first.");

    int[] res = new int[Data.req.length];                     //  array of number of units per item added to fill

    //  Iterate over all item columns except for the last one which only exists for the start condition
    for (int it = 0; it < added.length - 1; it++) {
      if (added[it][i]) {
        //  Added the item to the resulting array and decrease the overall value by the items value
        res[Data.getItemIndex(it)]++;
        i -= Data.vget(it);
      }
    }

    return res;
  }
}
