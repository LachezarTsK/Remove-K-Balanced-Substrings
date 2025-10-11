
#include <vector>
#include <string>
#include <string_view>
using namespace std;

class Solution {

    static const char OPEN = '(';
    static const char CLOSE = ')';
    static const int PLACEHOLDER_VALUE = 0;

    int targetSizeBalancedPairs;
    vector<int> countConsecutiveOpen;
    vector<int> countConsecutiveClose;

public:
    string removeSubstring(const string& input, int targetSizeBalancedPairs) {
        countConsecutiveOpen.clear();
        countConsecutiveClose.clear();
        string brackets;
        this->targetSizeBalancedPairs = targetSizeBalancedPairs;

        for (const auto& bracket : input) {
            if (bracket == OPEN) {
                int consecutiveOpenCount = getNextNumberOfConsecutiveOpenBrackets(brackets);
                countConsecutiveOpen.push_back(consecutiveOpenCount);
            }
            else if (bracket == CLOSE) {
                int consecutiveCloseCount = getNextNumberOfConsecutiveCloseBrackets(brackets);
                countConsecutiveClose.push_back(consecutiveCloseCount);
            }

            brackets.push_back(bracket);
            removeBalancedPairsWithTargetSize(brackets);
        }
        return brackets;
    }

private:

    /*
     A valid number is assigned to all opening brackets since each opening bracket could later form pairs
     of size equal to targetSizeBalancedPairs. In other words, there are no opening brackets with PLACEHOLDER_VALUE.
     */
    int getNextNumberOfConsecutiveOpenBrackets(string_view brackets) const {
        int consecutiveOpenCount = 1;
        if (!brackets.empty() && brackets.back() == OPEN) {
            consecutiveOpenCount += countConsecutiveOpen.back();
        }
        return consecutiveOpenCount;
    }

    /*
     A valid number is assigned only to those closing brackets, which could later form pairs of size equal
     to targetSizeBalancedPairs. Otherwise, the closing brackets are assigned PLACEHOLDER_VALUE.
    */
    int getNextNumberOfConsecutiveCloseBrackets(string_view brackets) const {
        int consecutiveCloseCount = 1;
        if (!brackets.empty()
            && brackets.back() == CLOSE
            && countConsecutiveClose.back() != PLACEHOLDER_VALUE) {
            consecutiveCloseCount += countConsecutiveClose.back();
        }
        else if (brackets.empty()
            || (brackets.back() == CLOSE && countConsecutiveClose.back() == PLACEHOLDER_VALUE)
            || (brackets.back() == OPEN && countConsecutiveOpen.back() < targetSizeBalancedPairs)) {
            consecutiveCloseCount = PLACEHOLDER_VALUE;
        }
        return consecutiveCloseCount;
    }

    void removeBalancedPairsWithTargetSize(string& brackets) {
        if (countConsecutiveClose.empty() || countConsecutiveClose.back() < targetSizeBalancedPairs) {
            return;
        }
        brackets.resize(brackets.size() - 2 * targetSizeBalancedPairs);
        countConsecutiveOpen.resize(countConsecutiveOpen.size() - targetSizeBalancedPairs);
        countConsecutiveClose.resize(countConsecutiveClose.size() - targetSizeBalancedPairs);
    }
};
