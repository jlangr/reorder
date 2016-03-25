package util;

import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      List<Integer> rangeToMove = input.subList(range.start, range.end + 1);

      List<Integer> results = new ArrayList<>(input);
      if (position < range.start) {
         results = range.removeFrom(results);
         results.addAll(position, rangeToMove);
      } else {
         results.addAll(position, rangeToMove);
         results = range.removeFrom(results);
      }
      return results;
   }
}
