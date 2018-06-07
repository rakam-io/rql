package rql.nodes

import rql.Context
import rql.exceptions.ArrayIndexNotAnInteger
import rql.exceptions.GuruMeditation
import rql.exceptions.NonArray
import rql.exceptions.ResultIsNull

/**
 * Represents a array element access operator in the template string.
 * @param source: The corresponding source code.
 */
class ArrayElement(source: String) : Node(source, source) {
    /**
     * Return the value of an element in an array.
     * @param context The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        if (results.size == 2) {
            val (lhs, rhs) = results
            if(lhs is Failure) {
                return lhs
            }
            if(rhs is Failure) {
                return rhs
            }
            if (lhs is Success && rhs is Success) {
                return when {
                    lhs.value !is List<*> -> Failure(text, NonArray(lhs.text))
                    rhs.value !is Int -> Failure(text, ArrayIndexNotAnInteger(lhs.text, rhs.text))
                    null == lhs.value[rhs.value] -> Failure(text, ResultIsNull(text))
                    else -> Success(text, lhs.value[rhs.value])
                }
            }
        }
        throw GuruMeditation()
    }
}

