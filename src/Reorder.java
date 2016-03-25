import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (range.start == position) return input;

      List<Integer> results = new ArrayList<>(input);
      if (position < range.start) {
         int element = results.remove(range.start);
         results.add(position, element);
      }
      else {
         results.add(position, results.get(range.start));
         results.remove(range.start);
      }
      return results;
   }

}
