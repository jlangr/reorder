package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static java.util.Collections.EMPTY_LIST;
import org.junit.Test;
import static java.util.Arrays.asList;

public class RangeTest {
   @Test
   public void removeFromListMatchingSizeReturnsEmptyList() {
      assertThat(new Range(0, 1).removeFrom(asList(10, 20)), equalTo(EMPTY_LIST));
   }

   @Test
   public void removeSomeFromList() {
      assertThat(new Range(1, 2).removeFrom(asList(10, 20, 30, 40)), equalTo(asList(10, 40)));
   }
}
