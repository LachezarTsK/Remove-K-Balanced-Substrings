
package main
import "errors"

const OPEN byte = '('
const CLOSE byte = ')'
const PLACEHOLDER_VALUE int = 0

var targetSizeBalancedPairs int
var countConsecutiveOpen Stack[int]
var countConsecutiveClose Stack[int]
var brackets Stack[byte]

func removeSubstring(input string, sizeBalancedPairs int) string {
    countConsecutiveOpen = NewStack(make([]int, len(input)))
    countConsecutiveClose = NewStack(make([]int, len(input)))
    brackets = NewStack(make([]byte, len(input)))
    targetSizeBalancedPairs = sizeBalancedPairs

    for i := range input {
        if input[i] == OPEN {
            consecutiveOpenCount := getNextNumberOfConsecutiveOpenBrackets()
            countConsecutiveOpen.Push(consecutiveOpenCount)
        } else if input[i] == CLOSE {
            consecutiveCloseCount := getNextNumberOfConsecutiveCloseBrackets()
            countConsecutiveClose.Push(consecutiveCloseCount)
        }

        brackets.Push(input[i])
        removeBalancedPairsWithTargetSize()
    }

    return string(brackets.container[:brackets.index])
}

/*
A valid number is assigned to all opening brackets since each opening bracket could later form pairs
of size equal to targetSizeBalancedPairs. In other words, there are no opening brackets with PLACEHOLDER_VALUE.
*/
func getNextNumberOfConsecutiveOpenBrackets() int {
    var consecutiveOpenCount = 1
    valueBrackets, errorBrackets := brackets.Peek()
    valueOpen, _ := countConsecutiveOpen.Peek()

    if errorBrackets == nil && valueBrackets == OPEN {
        consecutiveOpenCount += valueOpen
    }
    return consecutiveOpenCount
}

/*
A valid number is assigned only to those closing brackets, which could later form pairs of size equal
to targetSizeBalancedPairs. Otherwise, the closing brackets are assigned PLACEHOLDER_VALUE.
*/
func getNextNumberOfConsecutiveCloseBrackets() int {
    var consecutiveCloseCount = 1
    valueBrackets, errorBrackets := brackets.Peek()
    valueOpen, _ := countConsecutiveOpen.Peek()
    valueClose, _ := countConsecutiveClose.Peek()

    if errorBrackets == nil && valueBrackets == CLOSE && valueClose != PLACEHOLDER_VALUE {
        consecutiveCloseCount += valueClose
    } else if errorBrackets != nil ||
        (valueBrackets == CLOSE && valueClose == PLACEHOLDER_VALUE) ||
        (valueBrackets == OPEN && valueOpen < targetSizeBalancedPairs) {
        consecutiveCloseCount = PLACEHOLDER_VALUE
    }
    return consecutiveCloseCount
}

func removeBalancedPairsWithTargetSize() {
    valueClose, errorClose := countConsecutiveClose.Peek()
    if errorClose != nil || valueClose < targetSizeBalancedPairs {
        return
    }

    brackets.RemoveNumberOfElementsFromEnd(2 * targetSizeBalancedPairs)
    countConsecutiveOpen.RemoveNumberOfElementsFromEnd(targetSizeBalancedPairs)
    countConsecutiveClose.RemoveNumberOfElementsFromEnd(targetSizeBalancedPairs)
}

//////// Start Implementation of Stack \\\\\\\\

type Stack[T any] struct {
    container []T
    /*
    Index always points to the next element to be inserted.
    Therefore, when index = 0, the Stack is empty.
    */
    index     int
}

func NewStack[T any](container []T) Stack[T] {
    stack := Stack[T]{
        container: container,
    }
    return stack
}

func (this *Stack[T]) Push(element T) (any, error) {
    if this.index == len(this.container) {
        return false, errors.New("Stack is full.")
    }
    this.container[this.index] = element
    this.index++
    return true, nil
}

func (this *Stack[T]) Pop() (T, error) {
    if this.index == 0 {
        return this.container[this.index], errors.New("Stack is empty.")
    }

    this.index--
    element := this.container[this.index]
    return element, nil
}

func (this *Stack[T]) Peek() (T, error) {
    if this.index == 0 {
        return this.container[this.index], errors.New("Stack is empty.")
    }
    return this.container[this.index - 1], nil
}

func (this *Stack[T]) RemoveNumberOfElementsFromEnd(numberOfElementsToRemoveFromEnd int) (any, error) {
    if this.index - numberOfElementsToRemoveFromEnd < 0 {
        return false, errors.New("NumberOfElementsToRemoveFromEnd exceeds total elements.")
    }
    this.index -= numberOfElementsToRemoveFromEnd
    return true, nil
}

func (this *Stack[T]) Size() int {
    return this.index
}

func (this *Stack[T]) Capacity() int {
    return len(this.container)
}

func (this *Stack[T]) IsEmpty() bool {
    return this.index == 0
}

//////// End Implementation of Stack \\\\\\\\
