
using System;
using System.Collections.Generic;
using System.Text;

public class Solution
{
    private static readonly char OPEN = '(';
    private static readonly char CLOSE = ')';
    private static readonly int PLACEHOLDER_VALUE = 0;

    private int targetSizeBalancedPairs;
    private Stack<int>? countConsecutiveOpen;
    private Stack<int>? countConsecutiveClose;

    public string RemoveSubstring(string input, int targetSizeBalancedPairs)
    {
        countConsecutiveOpen = new Stack<int>(input.Length);
        countConsecutiveClose = new Stack<int>(input.Length);
        StringBuilder brackets = new StringBuilder();
        this.targetSizeBalancedPairs = targetSizeBalancedPairs;

        foreach (char bracket in input)
        {
            if (bracket == OPEN)
            {
                int consecutiveOpenCount = GetNextNumberOfConsecutiveOpenBrackets(brackets);
                countConsecutiveOpen.Push(consecutiveOpenCount);
            }
            else if (bracket == CLOSE)
            {
                int consecutiveCloseCount = GetNextNumberOfConsecutiveCloseBrackets(brackets);
                countConsecutiveClose.Push(consecutiveCloseCount);
            }

            brackets.Append(bracket);
            RemoveBalancedPairsWithTargetSize(brackets);
        }

        return brackets.ToString();
    }

    /*
     A valid number is assigned to all opening brackets since each opening bracket could later form pairs 
     of size equal to targetSizeBalancedPairs. In other words, there are no opening brackets with PLACEHOLDER_VALUE.
    */
    private int GetNextNumberOfConsecutiveOpenBrackets(StringBuilder brackets)
    {
        int consecutiveOpenCount = 1;
        if (brackets.Length > 0 && brackets[brackets.Length - 1] == OPEN)
        {
            consecutiveOpenCount += countConsecutiveOpen!.Peek();
        }
        return consecutiveOpenCount;
    }

    /*
     A valid number is assigned only to those closing brackets, which could later form pairs of size equal 
     to targetSizeBalancedPairs. Otherwise, the closing brackets are assigned PLACEHOLDER_VALUE.
    */
    private int GetNextNumberOfConsecutiveCloseBrackets(StringBuilder brackets)
    {
        int consecutiveCloseCount = 1;
        if (brackets.Length > 0
                && brackets[brackets.Length - 1] == CLOSE
                && countConsecutiveClose!.Peek() != PLACEHOLDER_VALUE)
        {
            consecutiveCloseCount += countConsecutiveClose.Peek();
        }
        else if (brackets.Length == 0
                || (brackets[brackets.Length - 1] == CLOSE && countConsecutiveClose!.Peek() == PLACEHOLDER_VALUE)
                || (brackets[brackets.Length - 1] == OPEN && countConsecutiveOpen!.Peek() < targetSizeBalancedPairs))
        {
            consecutiveCloseCount = PLACEHOLDER_VALUE;
        }
        return consecutiveCloseCount;
    }

    private void RemoveBalancedPairsWithTargetSize(StringBuilder brackets)
    {
        if (countConsecutiveClose!.Count == 0 || countConsecutiveClose.Peek() < targetSizeBalancedPairs)
        {
            return;
        }

        brackets.Remove(brackets.Length - 2 * targetSizeBalancedPairs, 2 * targetSizeBalancedPairs);

        for (int i = 0; i < targetSizeBalancedPairs; ++i)
        {
            countConsecutiveOpen!.Pop();
            countConsecutiveClose!.Pop();
        }
    }
}
