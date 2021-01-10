public class Data {
  static final String[] names = {
          "Notebook Büro 13\"",
          "Notebook Büro 14\"",
          "Notebook outdoor",
          "Mobiltelefon Büro",
          "Mobiltelefon Outdoor",
          "Mobiltelefon Heavy Duty",
          "Tablet Büro klein",
          "Tablet Büro groß",
          "Tablet outdoor klein",
          "Tablet outdoor groß"
  };

  static final int[] w = {
          2451, 2978, 3625, 717, 988, 1220, 1405, 1455, 1690, 1980
  };

  static final int[] v = {
          40, 35, 80, 30, 60, 65, 40, 40, 45, 68
  };

  static int[] reqOrigin = {
          205, 420, 450, 60, 157, 220, 620, 250, 540, 370
  };

  static int[] req = {
          205, 420, 450, 60, 157, 220, 620, 250, 540, 370
  };

  static final int m1 = 1100000 - 72400;
  static final int m2 = 1100000 - 85700;

  /**
   * Access the weights accounting for the number of required units
   * @param index from 0 to 3291
   * @return weight of the item at the given index
   */
  static int wget(int index) {
    return get(w, index);
  }

  /**
   * Access the values accounting for the number of required units
   * @param index from 0 to 3291
   * @return value of the item at the given index
   */
  static int vget(int index) {
    return get(v, index);
  }

  /**
   * Accounts for the number of required units per item
   * @return sum of the array req, i.e. total number of items
   */
  static int numItems() {
    int i = 0;
    for (int r : req) {
      i += r;
    }

    return i;

    //return 3292;
  }

  /**
   * Calculates the sum of all values accounting for req, i.e. the maximum achievable value.
   * @return maximum achievable value
   */
  static int sumValue() {
    return sum(v);
  }

  /**
   * Calculates the sum of all weights accounting for req, i.e. the maximum achievable weight.
   * @return maximum achievable weight
   */
  static int sumWeight() {
    return sum(w);
  }

  /**
   * This function will calculate an upper bound to the maximum achievable value.
   * It generates a fictitious item with the weight of the lightest and the value of the most valuable item with
   * req[item] > 0.
   * The calculated heuristic value will be the value of a knapsack (max of m1, m2 in this case) filled with
   * only this item.
   * @return heuristic value hopefully less than sumValues()
   */
  static int sumValuesHeuristic() {
    int maxVal = 0;
    int minWeight = Integer.MAX_VALUE;
    int maxKnapsack = Integer.max(m1, m2);

    //  Find the maximum value & minimum weight
    for (int i = 0; i < w.length; i++) {
      if (req[i] > 0) {
        if (v[i] > maxVal) maxVal = v[i];
        if (w[i] < minWeight) minWeight = w[i];
      }
    }

    return (maxKnapsack / minWeight) * maxVal;
  }

  /**
   * @param ind index from 0 to 3291
   * @param arr array for item lookup
   * @return item of arr at the given index
   */

  private static int get(int[] arr, int ind) {
    if (ind < 0)
      throw new IndexOutOfBoundsException("index " + ind + " out of bounds");

    //  account for the fact that the first subtraction breaks the 0 indexed variable
    ind += 1;

    for (int i = 0; i < req.length; i++) {
      ind -= req[i];

      if (ind <= 0)
        return arr[i];
    }

    throw new IndexOutOfBoundsException("index " + ind + " out of bounds");
  }

  /**
   * This function will calculate the sum of the given array taking also into account the
   * multiplicity provided in req.
   * @param arr array for sum
   * @return sum of the array
   */
  private static int sum(int arr[]) {
    int s = 0;
    for (int i = 0; i < req.length; i++) {
      s += arr[i] * req[i];
    }

    return s;
  }

  /**
   * @param ind index from 0 to 3291
   * @return index of item considering the req array
   */
  static int getItemIndex(int ind) {
    if (ind < 0)
      throw new IndexOutOfBoundsException("index " + ind + " out of bounds");

    //  account for the fact that the first subtraction breaks the 0 indexed variable
    ind += 1;

    for (int i = 0; i < req.length; i++) {
      ind -= req[i];

      if (ind <= 0)
        return i;
    }

    throw new IndexOutOfBoundsException("index " + ind + " out of bounds");
  }

  /**
   * Subtracts the given array of number of required units from req.
   * @param arr array to subtract from req
   */
  static void subtractFromReq(int[] arr) {
    if (arr == null || arr.length != req.length)
      throw new RuntimeException("given array cannot be subtracted.");

    //  Subtract each item individually
    for (int i = 0; i < arr.length; i++) {
      req[i] -= arr[i];
    }
  }
}
