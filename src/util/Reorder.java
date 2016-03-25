package util;

import java.util.*;

public class Reorder {
   public static void throwIfInvalidInput(List<Integer> input, Range range, int position) {
      if (range.end + 1 > input.size() || position > input.size())
         throw new IllegalArgumentException();
   }

   public static List<Integer> move(List<Integer> input, Range range, int position) {
      throwIfInvalidInput(input, range, position);

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
