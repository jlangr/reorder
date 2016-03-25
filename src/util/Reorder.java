package util;

import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      List<Integer> rangeToMove = input.subList(range.start, range.end + 1);

      List<Integer> results = new ArrayList<>(input);
      if (position < range.start) {
         results = remove(results, range);
         results.addAll(position, rangeToMove);
      } else {
         results.addAll(position, rangeToMove);
         results = remove(results, range);
      }
      return results;
   }

   private static List<Integer> remove(List<Integer> list, Range range) {
      List<Integer> newList = new ArrayList<>();
      newList.addAll(list.subList(0, range.start));
      newList.addAll(list.subList(range.end + 1, list.size()));
      return newList;
   }
}
