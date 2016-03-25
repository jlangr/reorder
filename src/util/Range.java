package util;

import java.util.*;

public class Range {
   public final int start;
   public final int end;

   public Range(int start, int end) {
      this.start = start;
      this.end = end;
   }

   public List<Integer> removeFrom(List<Integer> list) {
      List<Integer> newList = new ArrayList<>();
      newList.addAll(list.subList(0, start));
      newList.addAll(list.subList(end + 1, list.size()));
      return newList;
   }
}
