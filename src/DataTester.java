import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class DataTester {
  @Test
  void test_wget() {
    Assertions.assertEquals(Data.w[0], Data.wget(204));
    Assertions.assertEquals(Data.w[0], Data.wget(0));
    Assertions.assertEquals(Data.w[0], Data.wget(104));
    Assertions.assertEquals(Data.w[1], Data.wget(205));
    Assertions.assertEquals(Data.w[1], Data.wget(204 + 420));
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Data.wget(-1));

    int sumItems = 0;
    for (int r : Data.req) sumItems += r;

    final int s = sumItems;
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Data.wget(s));

    Assertions.assertEquals(Data.w[Data.w.length-1], Data.wget(s-1));
  }

  @Test
  void test_vget() {
    Assertions.assertEquals(Data.v[0], Data.vget(204));
    Assertions.assertEquals(Data.v[0], Data.vget(0));
    Assertions.assertEquals(Data.v[0], Data.vget(104));
    Assertions.assertEquals(Data.v[1], Data.vget(205));
    Assertions.assertEquals(Data.v[1], Data.vget(204 + 420));
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Data.vget(-1));

    int sumItems = 0;
    for (int r : Data.req) sumItems += r;

    final int s = sumItems;
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Data.vget(s));

    Assertions.assertEquals(Data.v[Data.v.length - 1], Data.vget(s - 1));
  }

  @Test
  void test_getItemIndex() {
    Assertions.assertEquals(0, Data.getItemIndex(204));
    Assertions.assertEquals(0, Data.getItemIndex(0));
    Assertions.assertEquals(0, Data.getItemIndex(104));
    Assertions.assertEquals(1, Data.getItemIndex(205));
    Assertions.assertEquals(1, Data.getItemIndex(204 + 420));
  }

  @Test
  void test_numItems() {

    Assertions.assertEquals(3292, Data.numItems());
    Assertions.assertEquals(Data.w[Data.w.length-1], Data.wget(Data.numItems()-1));
    Assertions.assertEquals(Data.v[Data.v.length-1], Data.vget(Data.numItems()-1));

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Data.vget(Data.numItems()));
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> Data.wget(Data.numItems()));
  }

  @Test
  void test_sumValues() {
    Assertions.assertEquals(168680, Data.sumValue());
  }

  @Test
  void test_sumWeights() {
    Assertions.assertEquals(6731051, Data.sumWeight());
  }

  @Test
  void test_sumValuesHeuristic() {
    /*  The heuristic must underestimate the actual maximum achievable value
     *  and overestimate the actual maximum knapsack value.
     *  The latter of which is known to be around 50000 (by empirical measurement :-D ).
     */

    int sumH = Data.sumValuesHeuristic();
    int sum = Data.sumValue();

    Assertions.assertTrue(sumH < sum);
    Assertions.assertTrue(sumH > 50000);

  }

}
