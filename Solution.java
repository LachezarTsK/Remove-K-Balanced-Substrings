
import java.util.ArrayDeque;
import java.util.Deque;

public class Solution {

    private static final char OPEN = '(';
    private static final char CLOSE = ')';
    private static final int PLACEHOLDER_VALUE = 0;

    private int targetSizeBalancedPairs;
    private Deque<Integer> countConsecutiveOpen;
    private Deque<Integer> countConsecutiveClose;

    public String removeSubstring(String input, int targetSizeBalancedPairs) {
        countConsecutiveOpen = new ArrayDeque<>();
        countConsecutiveClose = new ArrayDeque<>();
        StringBuilder brackets = new StringBuilder();
        this.targetSizeBalancedPairs = targetSizeBalancedPairs;

        for (char bracket : input.toCharArray()) {
            if (bracket == OPEN) {
                int consecutiveOpenCount = getNextNumberOfConsecutiveOpenBrackets(brackets);
                countConsecutiveOpen.addLast(consecutiveOpenCount);
            } else if (bracket == CLOSE) {
                int consecutiveCloseCount = getNextNumberOfConsecutiveCloseBrackets(brackets);
                countConsecutiveClose.addLast(consecutiveCloseCount);
            }

            brackets.append(bracket);
            removeBalancedPairsWithTargetSize(brackets);
        }
        
        return brackets.toString();
    }

    /*
    A valid number is assigned to all opening brackets since each opening bracket could later form pairs 
    of size equal to targetSizeBalancedPairs. In other words, there are no opening brackets with PLACEHOLDER_VALUE.
     */
    private int getNextNumberOfConsecutiveOpenBrackets(StringBuilder brackets) {
        int consecutiveOpenCount = 1;
        if (!brackets.isEmpty() && brackets.charAt(brackets.length() - 1) == OPEN) {
            consecutiveOpenCount += countConsecutiveOpen.peekLast();
        }
        return consecutiveOpenCount;
    }

    /*
    A valid number is assigned only to those closing brackets, which could later form pairs of size equal 
    to targetSizeBalancedPairs. Otherwise, the closing brackets are assigned PLACEHOLDER_VALUE.
     */
    private int getNextNumberOfConsecutiveCloseBrackets(StringBuilder brackets) {
        int consecutiveCloseCount = 1;
        if (!brackets.isEmpty()
                && brackets.charAt(brackets.length() - 1) == CLOSE
                && countConsecutiveClose.peekLast() != PLACEHOLDER_VALUE) {
            consecutiveCloseCount += countConsecutiveClose.peekLast();
        } else if (brackets.isEmpty()
                || (brackets.charAt(brackets.length() - 1) == CLOSE && countConsecutiveClose.peekLast() == PLACEHOLDER_VALUE)
                || (brackets.charAt(brackets.length() - 1) == OPEN && countConsecutiveOpen.peekLast() < targetSizeBalancedPairs)) {
            consecutiveCloseCount = PLACEHOLDER_VALUE;
        }
        return consecutiveCloseCount;
    }

    private void removeBalancedPairsWithTargetSize(StringBuilder brackets) {
        if (countConsecutiveClose.isEmpty() || countConsecutiveClose.peekLast() < targetSizeBalancedPairs) {
            return;
        }

        brackets.delete(brackets.length() - 2 * targetSizeBalancedPairs, brackets.length());
        for (int i = 0; i < targetSizeBalancedPairs; ++i) {
            countConsecutiveOpen.pollLast();
            countConsecutiveClose.pollLast();
        }
    }
}
