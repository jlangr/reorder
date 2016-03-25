import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import static java.util.Arrays.asList;

public class ReorderTest {
   @Test
   public void moveToSamePositionDoesNothing() {
      assertThat(Reorder.move(asList(1, 2), new Range(0, 0), 0), equalTo(asList(1, 2)));
   }

   @Test
   public void moveOneToPriorPositionSwapsElements() {
      assertThat(Reorder.move(asList(1, 2), new Range(1, 1), 0), equalTo(asList(2, 1)));
   }

   @Test
   public void moveOneToEarlierPosition() {
      assertThat(Reorder.move(asList(10, 20, 30, 40), new Range(3, 3), 1), equalTo(asList(10, 40, 20, 30)));
   }

   @Test
   public void moveOneToLaterPosition() {
      assertThat(Reorder.move(asList(10, 20, 30, 40), new Range(1, 1), 3), equalTo(asList(10, 30, 20, 40)));
   }
}