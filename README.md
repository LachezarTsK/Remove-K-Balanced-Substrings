# Remove-K-Balanced-Substrings
Challenge at LeetCode.com. Tags: Stack, String, Design.

------------------------------------------------------------------------------------------------------------------

The names of the methods, variables, fields, etc. are self-explanatory.<br/>

Data structure brackets: contains all encountered brackets as characters and what remains after removals.<br/>
Data structure countConsecutiveOpen: contains the count of consecutive opening brackets.<br/>
Data structure countConsecutiveClose: contains the count of consecutive closing brackets.<br/>

Information about field PLACEHOLDER_VALUE, its rationality and implementation logic.<br/>

Consecutive opening or closing brackets are assigned to valid consecutive numbers (1, 2, 3, 4, etc.) only if these,<br/> at a later stage, could form valid opening/closing bracket pairs, as per the problem statement, with size equal to targetSizeBalancedPairs. 

All opening brackets at the time of their encounter potentially could form such pairs, therefore, PLACEHOLDER_VALUE is assigned only to closing brackets. And more specifically, to those closing brackets, which at the time of their encounter, it can be said with certainty that they can never form pairs. There are four such cases:

1.	 The closing bracket is at the beginning of the string.<br/>
 Example: “)”

2.	 The closing bracket follows a chain of closing brackets, where the first closing bracket is at the beginning of the string.<br/>
 Example: “)))))”

3.	The closing bracket follows a chain of opening brackets, and this chain of opening brackets has size less than targetSizeBalancedPairs.<br/>
Example: “(()”, targetSizeBalancedPairs = 4, opening brackets preceding current closing bracket = 2

4.	The closing bracket follows a chain of closing brackets, where the first closing bracket is at the beginning of a chain of opening brackets, which has size less than targetSizeBalancedPairs.<br/>
Example: “(()))))”, targetSizeBalancedPairs = 4, opening brackets chain size that precedes current closing bracket chain = 2


By using PLACEHOLDER_VALUE in this way, the algorithm ensures that after each update, the following equation is always true:
brackets.size = countConsecutiveOpen.size + countConsecutiveClose.size.

This helps to reduce the if-else statements and to streamline the code, making it more readable and cleaner.<br/> The algorithm guarantees that when countConsecutiveClose.peek() == targetSizeBalancedPairs is true, there are valid pairs, no other checks are necessary.

The problem could be solved with less lines of code, but also far less efficiently, by applying brute force.<br/> Due to the size of the input for this problem, namely pow(10, 5), such a solution will either time out, or have time that is more than 10x the time of this solution or other efficient solutions. And of course, it is possible to solve<br/> the problem efficiently with less lines of code, at the expense of cleanliness and readability.

The solution with Golang implements custom-made generic stack.

