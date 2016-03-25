import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (range.start == position)
         return input;

      List<Integer> results = new ArrayList<>();
      results.add(input.get(1));
      results.add(input.get(0));
      return results;
   }

}
