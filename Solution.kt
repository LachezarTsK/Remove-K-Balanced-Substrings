
class Solution {

    private companion object {
        const val OPEN = '('
        const val CLOSE = ')'
        const val PLACEHOLDER_VALUE = 0
    }

    private var targetSizeBalancedPairs = 0
    private lateinit var countConsecutiveOpen: MutableList<Int>
    private lateinit var countConsecutiveClose: MutableList<Int>

    fun removeSubstring(input: String, targetSizeBalancedPairs: Int): String {
        countConsecutiveOpen = mutableListOf<Int>()
        countConsecutiveClose = mutableListOf<Int>()
        val brackets = StringBuilder()
        this.targetSizeBalancedPairs = targetSizeBalancedPairs

        for (bracket in input) {
            if (bracket == OPEN) {
                val consecutiveOpenCount = getNextNumberOfConsecutiveOpenBrackets(brackets)
                countConsecutiveOpen.add(consecutiveOpenCount)
            } else if (bracket == CLOSE) {
                val consecutiveCloseCount = getNextNumberOfConsecutiveCloseBrackets(brackets)
                countConsecutiveClose.add(consecutiveCloseCount)
            }

            brackets.append(bracket)
            removeBalancedPairsWithTargetSize(brackets)
        }

        return brackets.toString()
    }

    /*
     A valid number is assigned to all opening brackets since each opening bracket could later form pairs
     of size equal to targetSizeBalancedPairs. In other words, there are no opening brackets with PLACEHOLDER_VALUE.
    */
    private fun getNextNumberOfConsecutiveOpenBrackets(brackets: StringBuilder): Int {
        var consecutiveOpenCount = 1
        if (brackets.isNotEmpty() && brackets[brackets.length - 1] == OPEN) {
            consecutiveOpenCount += countConsecutiveOpen.last()
        }
        return consecutiveOpenCount
    }

    /*
     A valid number is assigned only to those closing brackets, which could later form pairs of size equal
     to targetSizeBalancedPairs. Otherwise, the closing brackets are assigned PLACEHOLDER_VALUE.
    */
    private fun getNextNumberOfConsecutiveCloseBrackets(brackets: StringBuilder): Int {
        var consecutiveCloseCount = 1
        if (brackets.isNotEmpty()
            && brackets[brackets.length - 1] == CLOSE
            && countConsecutiveClose.last() != PLACEHOLDER_VALUE
        ) {
            consecutiveCloseCount += countConsecutiveClose.last()
        } else if (brackets.isEmpty()
            || (brackets[brackets.length - 1] == CLOSE && countConsecutiveClose.last() == PLACEHOLDER_VALUE)
            || (brackets[brackets.length - 1] == OPEN && countConsecutiveOpen.last() < targetSizeBalancedPairs)
        ) {
            consecutiveCloseCount = PLACEHOLDER_VALUE
        }
        return consecutiveCloseCount
    }

    private fun removeBalancedPairsWithTargetSize(brackets: StringBuilder) {
        if (countConsecutiveClose.isEmpty() || countConsecutiveClose.last() < targetSizeBalancedPairs) {
            return
        }

        brackets.delete(brackets.length - 2 * targetSizeBalancedPairs, brackets.length)

        for (i in 0..<targetSizeBalancedPairs) {
            countConsecutiveOpen.removeLast()
            countConsecutiveClose.removeLast()
        }
    }
}
