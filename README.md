This is a run-through of the first part of the Reordering kata from [cyber-dojo.org](http://www.cyber-dojo.org/). Here are the instructions from that site, verbatim:

```
Given a set of integer numbers your task is to reorder them as follows:

Move a range of elements from one position to another, preserving their order and the order of the other elements.
   
   e.g. Given the set
   { 4, 2, 7, 5, 9, 8, 6, 4, 3, 2 }
        ^     ^              ^
        s     e              p
   
moving the range of elements starting at element 2 (s) and ending at element 4 (e) to the position before element 9 (p) will give
   
   { 4, 9, 8, 6, 4, 2, 7, 5, 3, 2 }
                    ^     ^
                    s     e
```

This implementation uses an ArrayList as the data structure, not a native Java array.

The implementation of each iteration (titled *v1*, *v2*, *v3*, etc.) is stored in a separate branch with the corresponding name.

### v1

We start with a simple degenerate case: A move to the same position should do nothing.

```
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
```

Getting this test to pass is as simple as returning the input list (but begs the question, which we'll leave unanswered for now and decide arbitrarily: Are we to change the list in place, or return a new list?).

```
import java.util.List;

public class Reorder {
   public static List<Integer> move(List<Integer> list, int start, int end, int position) {
      return list;
   }
}
```

Writing this test lets us get all the JUnit bits in place and figure out the interface that we want to use. We get our first passing test within a couple minutes.

### v2

For the second test: In a two-element list, move a single element to the prior position.

```
 @Test
 public void moveOneToPriorPositionSwapsElements() {
    assertThat(Reorder.move(asList(1, 2), 1, 1, 0), equalTo(asList(2, 1)));
 }
 ```

We can code the simple swap easily. In order to prevent the first test from breaking, we add a simple guard clause to return early if the start of the source range is the same as the target position.

```
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
```

We also decide to return a new list, as opposed to altering the input list.

### v3

Faced with this unfriendly line of code in the test:

`assertThat(Reorder.move(asList(1, 2), 1, 1, 0), equalTo(asList(2, 1)));`

... we realize that it's tough to read. We either have to remember that the 2nd and 3rd arguments (1 and 1) represent the start and end of a range. One way to increase clarity in the test is to introduce local variables with intention-revealing names:

```
int start = 1;
int end = 1;
assertThat(Reorder.move(asList(1, 2), start, end, 0), equalTo(asList(2, 1)));
```

However, that would bloat all of the tests with similar code. Also, it makes the code more disjoint: You have to read each assignment statement in turn, then correlate the variables referenced in the `act` statement to the local variables.

We instead recognize that we are missing an abstraction. Any time we see a `start` of a range, we're almost always also going to see an `end` for that range. We introduce a Range abstraction:

```
public class Range {
   public final int start;
   public final int end;

   public Range(int start, int end) {
      this.start = start;
      this.end = end;
   }
}
```

Public fields? They're final primitives, and can't be changed. For now, it will serve our needs.

In the tests, we change both calls to `move` to use the Range:

```
   @Test
   public void moveToSamePositionDoesNothing() {
      assertThat(Reorder.move(asList(1, 2), new Range(0, 0), 0), equalTo(asList(1, 2)));
   }

   @Test
   public void moveOneToPriorPositionSwapsElements() {
      assertThat(Reorder.move(asList(1, 2), new Range(1, 1), 0), equalTo(asList(2, 1)));
   }
```

... and correspondingly change the `move` method:

`public static List<Integer> move(List<Integer> input, Range range, int position) {`

### v4.

Our hardcoded swap of the first and second elements won't hold up if we write another test. We keep our change simple, and write a test that moves the last element in a 4-element input array:

```
   @Test
   public void moveOneToEarlierPosition() {
      assertThat(Reorder.move(asList(10, 20, 30, 40), new Range(3, 3), 1), equalTo(asList(10, 40, 20, 30)));
   }
```

The change forces us to generalize the `move` code:

```
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (range.start == position)
         return input;

      List<Integer> results = new ArrayList<>(input);
      int element = results.remove(range.start);
      results.add(position, element);
      return results;
   }
```

We use the `remove` method, which returns the element removed at a given position. We subsequently call the `add` method to insert it at the destination `position`.

### v5.

Our algorithm works fine for moving an element to an earlier position in the list. However, it doesn't hold up for moving an element to a later position:

```
   @Test
   public void moveOneToLaterPosition() {
      assertThat(Reorder.move(asList(10, 20, 30, 40), new Range(1, 1), 3), equalTo(asList(10, 30, 20, 40)));
   }
```

We realize that the algorithm in reverse should work. We just need to determine whether the insert position comes before the range to be moved (`position < range.start`) or after. For the new case where the insert position is later, we need to add the element to be moved first, and *then* remove it from its place earlier in the list.

```
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
```

### v6.

Our code is slightly asymmetric. In the `if` block, we store the element to be moved, while in the `else` block, we grab the element to be moved inline as part of the `add` method call. We decide to always extract the element to be moved first, which gives us very symmetric code:

```
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
```

(Note that we pay a slight performance hit for the `if` case. We have no reason to be concerned yet.)

### v7.

So far all our tests demonstrate moving a single element. We need to be able to move multiple elements:

```
   @Test
   public void moveSomeToEarlierPosition() {
      assertThat(Reorder.move(asList(10, 20, 30, 40), new Range(2, 3), 0), equalTo(asList(30, 40, 10, 20)));
   }
```

We worry only about supporting the specific test case, and thus change only the code in the `if` block:

```
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
```

Java doesn't provide an immediate, effective means of removing a range from a list, so we supply our own implementation in a helper method:

```
   private static List<Integer> remove(List<Integer> list, Range range) {
      List<Integer> newList = new ArrayList<>();
      newList.addAll(list.subList(0, range.start));
      newList.addAll(list.subList(range.end + 1, list.size()));
      return newList;
   }
```

In any case, our code is now a bit messy--the local variable `toMove` is used only in the `else` branch.

### v8.

Some restructuring first. We rename `ReorderTest` to `ReorderMoveTest`, and move all our classes into a new package `util`. The new package allows us to use an `import static` on `util.Reorder.move`, which lets us omit the class name from calls to `move`: 

```
package util;

// ...
import static util.Reorder.move;

public class Reorder_MoveTest {
   @Test
   public void moveToSamePositionDoesNothing() {
      assertThat(move(asList(1, 2), new Range(0, 0), 0), equalTo(asList(1, 2)));
   }
   // ...
}
```

### v9.

We add a new failing test:

```
   @Test
   public void moveSomeToLaterPosition() {
      assertThat(move(asList(10, 20, 30, 40), new Range(0, 1), 3), equalTo(asList(30, 10, 20, 40)));
   }

   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (position == range.start) return input;

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
```

Now both legs of the `if` statement require a `rangeToMove`, so we move that statement prior to the `if` statement. We're back to the symmetric solution that we had earlier, except that now both branches of the `if` statement support moving a range of elements.

### v10.

We wonder if that guard clause in `move`--which returns early if the position-to-move-to is the same as the start of the range to move--is really necessary. We try eliminating it and discover that indeed all our tests still pass (particularly `moveToSamePositionDoesNothing`).


### v11.

The beauty of creating abstractions like Range is that we find out often that code can start living there.

We wanted an easy way of removing a range from a List, but Java doesn't provide it directly. If we can't send the message `remove` to a list object, perhaps we can send the message `removeFrom` to a range object and pass a list as an argument--thus, "remove this range from a list."

```
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


public class Range {
   // ...
   public List<Integer> removeFrom(List<Integer> list) {
      List<Integer> newList = new ArrayList<>();
      newList.addAll(list.subList(0, start));
      newList.addAll(list.subList(end + 1, list.size()));
      return newList;
   }
}
```

### v12.

We've taken a private behavior and made it a public behavior defined on the Range class. In order for other developers to be able to rapidly understand and consume--reuse--our newly public ability, we add a couple tests to document its behavior:

```
package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static java.util.Collections.EMPTY_LIST;
import org.junit.Test;
import static java.util.Arrays.asList;

public class RangeTest {
   @Test
   public void removeFromListMatchingSizeReturnsEmptyList() {
      assertThat(new Range(0, 1).removeFrom(asList(10, 20)), equalTo(EMPTY_LIST));
   }

   @Test
   public void removeSomeFromList() {
      assertThat(new Range(1, 2).removeFrom(asList(10, 20, 30, 40)), equalTo(asList(10, 40)));
   }
}
```

### v13.

We want to add some tests that will document how the code behaves when there's a problem. We start with the case where the end of the range-to-move extends past the list size:

```
   @Test(expected=IllegalArgumentException.class)
   public void throwWhenRangeEndsPastListSize() {
      move(asList(10), new Range(0, 1), 0);
   }
```

Here we use @Test annotation argument, `expected`, which delcares that we expect an exception to be thown sometime during execution of the test method. If it executes to completion and no IllegalArgumentException gets thrown, the test fails.

Getting the test to pass:

```
   public static List<Integer> move(List<Integer> input, Range range, int position) {
      if (range.end + 1 > input.size())
         throw new IllegalArgumentException();
      // ...
   }
```

### v14.

We think of a test:

```
   @Test
   public void canInsertAfterLastPosition() {
      assertThat(move(asList(10, 20, 30), new Range(0, 1), 3), equalTo(asList(30, 10, 20)));
   }
```

It should work. It passes! What should we do with the test?

### v15.

We think of another error condition:

```
   @Test(expected=IllegalArgumentException.class)
   public void throwWhenPositionPastListSize() {
      move(asList(10, 20, 30), new Range(0, 1), 4);
   }
```

Adding exception-handling code directly to `move` with the level of detail needed makes for distracting code. We extract a helper method with a declarative name:

```
   public static void throwIfInvalidInput(List<Integer> input, Range range, int position) {
      if (range.end + 1 > input.size() || position > input.size())
         throw new IllegalArgumentException();
   }

   public static List<Integer> move(List<Integer> input, Range range, int position) {
      throwIfInvalidInput(input, range, position);

      // ...
   }
```

### v16.

We sometimes need to be able to check the state of things after an exception is thrown. We might also want to check the message that accompanies the exception object:

```
   @Test
   public void throwWhenRangeEndsPastListSize() {
      try {
         move(asList(10), new Range(0, 1), 0);
         fail();
      }
      catch (IllegalArgumentException expected) {
         assertThat(expected.getMessage(), equalTo("range end out of bounds"));
      }
   }
```

Later versions of JUnit 4.x provide a rule-based mechanism:

```
   @Rule
   public ExpectedException thrown = ExpectedException.none();
   // ...

   @Test
   public void throwWhenPositionPastListSize() {
      thrown.expect(IllegalArgumentException.class);
      thrown.expectMessage("position out of bounds");

      move(asList(10, 20, 30), new Range(0, 1), 4);
   }
```

We do need to modify the production code a little.

```
   public static void throwIfInvalidInput(List<Integer> input, Range range, int position) {
      if (range.end + 1 > input.size())
         throw new IllegalArgumentException("range end out of bounds");
      if (position > input.size())
         throw new IllegalArgumentException("position out of bounds");
   }
```

No doubt there are additional error conditions! And, not surprisingly, there are additional ways of checking for exceptions. The above three are among the more common mechanisms.
