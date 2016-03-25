import java.util.*;

public class Reorder {
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (position == range.start) return input;

      int toMove = input.get(range.start);

      List<Integer> results = new ArrayList<>(input);
      if (position < range.start) {
         List<Integer> rangeToMove = input.subList(range.start, range.end + 1);
         results = remove(results, range);
         results.addAll(position, rangeToMove);
      }
      else {
         results.add(position, toMove);
         results.remove(range.start);
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
