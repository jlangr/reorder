import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (position == range.start) return input;

      int toMove = input.get(range.start);

      List<Integer> results = new ArrayList<>(input);
      if (position < range.start) {
         results.remove(range.start);
         results.add(position, toMove);
      }
      else {
         results.add(position, toMove);
         results.remove(range.start);
      }
      return results;
   }

}
