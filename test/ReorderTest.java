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
}
