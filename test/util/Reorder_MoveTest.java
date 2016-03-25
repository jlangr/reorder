package util;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static util.Reorder.move;

public class Reorder_MoveTest {
   @Test
   public void moveToSamePositionDoesNothing() {
      assertThat(move(asList(1, 2), new Range(0, 0), 0), equalTo(asList(1, 2)));
   }

   @Test
   public void moveOneToPriorPositionSwapsElements() {
      assertThat(move(asList(1, 2), new Range(1, 1), 0), equalTo(asList(2, 1)));
   }

   @Test
   public void moveOneToEarlierPosition() {
      assertThat(move(asList(10, 20, 30, 40), new Range(3, 3), 1), equalTo(asList(10, 40, 20, 30)));
   }

   @Test
   public void moveOneToLaterPosition() {
      assertThat(move(asList(10, 20, 30, 40), new Range(1, 1), 3), equalTo(asList(10, 30, 20, 40)));
   }

   @Test
   public void moveSomeToEarlierPosition() {
      assertThat(move(asList(10, 20, 30, 40), new Range(2, 3), 0), equalTo(asList(30, 40, 10, 20)));
   }

   @Test
   public void moveSomeToLaterPosition() {
      assertThat(move(asList(10, 20, 30, 40), new Range(0, 1), 3), equalTo(asList(30, 10, 20, 40)));
   }

   @Test(expected=IllegalArgumentException.class)
   public void throwWhenRangeEndsPastListSize() {
      move(asList(10), new Range(0, 1), 0);
   }

   @Test
   public void canInsertAfterLastPosition() {
      assertThat(move(asList(10, 20, 30), new Range(0, 1), 3), equalTo(asList(30, 10, 20)));
   }

   @Test(expected=IllegalArgumentException.class)
   public void throwWhenPositionPastListSize() {
      move(asList(10, 20, 30), new Range(0, 1), 4);
   }
}