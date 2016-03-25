import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import static java.util.Arrays.asList;

public class ReorderTest {
   @Test
   public void moveToSamePositionDoesNothing() {
      assertThat(Reorder.move(asList(1, 2), 0, 0, 0), equalTo(asList(1, 2)));
   }
}
