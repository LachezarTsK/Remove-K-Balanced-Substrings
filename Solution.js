
/**
 * @param {string} input
 * @param {number} targetSizeBalancedPairs
 * @return {string}
 */
var removeSubstring = function (input, targetSizeBalancedPairs) {
    const util = new Util(targetSizeBalancedPairs);
    const brackets = new Array();

    for (let bracket of input) {
        if (bracket === Util.OPEN) {
            const consecutiveOpenCount = getNextNumberOfConsecutiveOpenBrackets(brackets, util);
            util.countConsecutiveOpen.push(consecutiveOpenCount);
        } else if (bracket === Util.CLOSE) {
            const consecutiveCloseCount = getNextNumberOfConsecutiveCloseBrackets(brackets, util);
            util.countConsecutiveClose.push(consecutiveCloseCount);
        }

        brackets.push(bracket);
        removeBalancedPairsWithTargetSize(brackets, util);
    }

    return brackets.join('');
};

/*
 A valid number is assigned to all opening brackets since each opening bracket could later form pairs 
 of size equal to targetSizeBalancedPairs. In other words, there are no opening brackets with PLACEHOLDER_VALUE.
 */
/**
 * @param {string[]} brackets
 * @param {Util} util
 * @return {number}
 */
function getNextNumberOfConsecutiveOpenBrackets(brackets, util) {
    const lastIndexOpen = util.countConsecutiveOpen.length - 1;
    const lastIndexBrackets = brackets.length - 1;
    let consecutiveOpenCount = 1;

    if (brackets.length > 0 && brackets[lastIndexBrackets] === Util.OPEN) {
        consecutiveOpenCount += util.countConsecutiveOpen[lastIndexOpen];
    }
    return consecutiveOpenCount;
}

/*
 A valid number is assigned only to those closing brackets, which could later form pairs of size equal 
 to targetSizeBalancedPairs. Otherwise, the closing brackets are assigned PLACEHOLDER_VALUE.
 */
/**
 * @param {string[]} brackets
 * @param {Util} util
 * @return {number}
 */
function getNextNumberOfConsecutiveCloseBrackets(brackets, util) {
    const lastIndexOpen = util.countConsecutiveOpen.length - 1;
    const lastIndexClose = util.countConsecutiveClose.length - 1;
    const lastIndexBrackets = brackets.length - 1;
    let consecutiveCloseCount = 1;

    if (brackets.length > 0
            && brackets[lastIndexBrackets] === Util.CLOSE
            && util.countConsecutiveClose[lastIndexClose] !== Util.PLACEHOLDER_VALUE) {
        consecutiveCloseCount += util.countConsecutiveClose[lastIndexClose];
    } else if (brackets.length === 0
            || (brackets[lastIndexBrackets] === Util.CLOSE && util.countConsecutiveClose[lastIndexClose] === Util.PLACEHOLDER_VALUE)
            || (brackets[lastIndexBrackets] === Util.OPEN && util.countConsecutiveOpen[lastIndexOpen] < util.targetSizeBalancedPairs)) {
        consecutiveCloseCount = Util.PLACEHOLDER_VALUE;
    }
    return consecutiveCloseCount;
}

/**
 * @param {string[]} brackets
 * @param {Util} util
 * @return {void}
 */
function  removeBalancedPairsWithTargetSize(brackets, util) {
    const lastIndex = util.countConsecutiveClose.length - 1;
    if (util.countConsecutiveClose.length === 0 || util.countConsecutiveClose[lastIndex] < util.targetSizeBalancedPairs) {
        return;
    }

    /*
     Alternative to the loops:
     
     brackets.splice(brackets.length - 2 * util.targetSizeBalancedPairs, 2 * util.targetSizeBalancedPairs);
     util.countConsecutiveOpen.splice(util.countConsecutiveOpen.length - util.targetSizeBalancedPairs, util.targetSizeBalancedPairs);
     util.countConsecutiveClose.splice(util.countConsecutiveClose.length - util.targetSizeBalancedPairs, util.targetSizeBalancedPairs);
     
     However, with splice the code runs slower, hence the the loop below.
     */
    for (let i = 0; i < util.targetSizeBalancedPairs; ++i) {
        brackets.pop();
        brackets.pop();
        util.countConsecutiveOpen.pop();
        util.countConsecutiveClose.pop();
    }
}

class Util {
    static OPEN = '(';
    static CLOSE = ')';
    static PLACEHOLDER_VALUE = 0;

    targetSizeBalancedPairs;
    countConsecutiveOpen = new Array();
    countConsecutiveClose = new Array();

    /**
     * @param {number} targetSizeBalancedPairs
     */
    constructor(targetSizeBalancedPairs) {
        this.targetSizeBalancedPairs = targetSizeBalancedPairs;
    }
}
