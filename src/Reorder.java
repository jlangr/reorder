import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, int start, int end, int position) {
      if (start == position)
         return input;

      List<Integer> results = new ArrayList<>();
      results.add(input.get(1));
      results.add(input.get(0));
      return results;
   }

}
