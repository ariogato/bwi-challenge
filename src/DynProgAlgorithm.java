import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
   * GENERAL CHARACTERIZATION
   *  Attempts to solve the challenge using a dynamic programming approach.
   *  The array will be dp with dimensions:
   *      - Data.numItems()+1 columns
   *      - Data.sumValue() rows
   *
   *  dp[V][i] is the weight of
   *      - items >= i
   *      - values >= V
   *  the weight m1 may be exceeded.
   *
   *
   * DP CHARACTERIZATION
   *  dp[V][i] = min(
   *      dp[V][i+1],
   *      w[i] + dp[V - v[i]][i+1]
   *  )
   *  which is the minimum of
   *      1. the identically valued knapsack without item i (i.e. item i not taken)
   *      2. the knapsack that is v[i] less valuable than the current one + the weight of item i (i.e. item i taken)
   *
   *
   *  start-condition at dp[...][Data.numItems()]:
   *      - overfull knapsack: max weight possible (i.e. all items)
   *
   *  topological sort:
   *      - i in Data.numItems()..0
   *      - V in 0..Data.sumValues()
   *
   *
   * RESULT
   *  At the end of the calculation dp[V][i] will represent the minimum weight needed to
   *  achieve a value >= V with items >= i.
   *
   *
   * CONCURRENCY
   *  This implementation makes use of threads to calculate the item columns i.
   *  The columns are divided into NUM_THREADS chunks of same size. Since these chunks are not interdependent
   *  by construction of the algorithm, there is no need for any producer/consumer pattern.
   *  Still rows are always computed one at a time.
   */
  void dynProg() throws InterruptedException {
    int sumValueHeuristic = Data.sumValuesHeuristic();            //  Heuristic of maximum achievable value
    final int NUM_THREADS =
            Runtime.getRuntime().availableProcessors();           //  Constant for number of threads
    ThreadPoolExecutor executor =
            (ThreadPoolExecutor)
                    Executors.newFixedThreadPool(NUM_THREADS);    //  Executor for workers
    List<Callable<Object>> callables =
            new ArrayList<>(NUM_THREADS);                         //  List for gathering all workers for invocation
    List<Worker> runnables =
            new ArrayList<>(NUM_THREADS);                         //  List for accessing all workers inside the loop

    //  Establish the starting condition
    Arrays.fill(dp[dp.length - 1], Data.sumWeight());

    for (int i = 0; i < NUM_THREADS; i++) {
      //  Generate new worker
      Worker r;
      if (i != NUM_THREADS-1)
        r = new Worker(i * sumValueHeuristic/3 + i, (i+1) * sumValueHeuristic/3 + i, 0);
      else
        r = new Worker(i * sumValueHeuristic/3 + i, sumValueHeuristic-1, 0);

      //  Save as callable & for referencing
      callables.add(Executors.callable((r)));
      runnables.add(r);
    }

    //  Main computation loop
    for (int i = Data.numItems() - 1; i >= 0; i--) {
      for (Worker r : runnables) r.row = i;           //  Set current row
      executor.invokeAll(callables);                  //  Invoke all (waits until all threads terminate)
    }

    executor.shutdown();
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

  /**
   * Runnable to fill out one row concurrently.
   * This is more efficient, if the machine running the code has >= 3 cores.
   */
  class Worker implements Runnable {

    int startInd, endInd, row;

    Worker(int startInd, int endInd, int row) {
      this.startInd = startInd;
      this.endInd = endInd;
      this.row = row;
    }

    @Override
    public void run() {
      for (int V = startInd; V <= endInd; V++) {

        //  Skip if the value doesn't fit
        if (V < Data.vget(row)) {
          dp[row][V] = 0;
          added[row][V] = false;
          continue;
        }

        //  Set the weight for the current field
        int wAdded = Data.wget(row) + dp[row+1][V - Data.vget(row)];      //  New weight in case item i is added
        int wNotAdded = dp[row+1][V];                                     //  New weight in case item i is not added

        if (wNotAdded <= wAdded) {
          dp[row][V] = wNotAdded;
          added[row][V] = false;
        }
        else {
          dp[row][V] = wAdded;
          added[row][V] = true;
        }

      }
    }
  }
}
