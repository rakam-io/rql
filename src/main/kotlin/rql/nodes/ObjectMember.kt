package rql.nodes

import rql.Context
import rql.exceptions.GuruMeditation
import rql.exceptions.NonObject
import rql.exceptions.PropertyNotFound
import rql.exceptions.ResultIsNull

/**
 * Represents a object member access operator in the template string.
 * @param source: The corresponding source code.
 */
class ObjectMember(source: String) : Node(source, source) {
    /**
     * Returns the value of a member of an object variable.
     * @param context: The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        if (results.size == 2) {
            val (lhs, rhs) = results
            if (lhs is Success && rhs is Success) {
                return when {
                    lhs.value !is Map<*, *> -> Failure(text, NonObject(lhs.text))
                    rhs.value !is String -> throw GuruMeditation()
                    !lhs.value.containsKey(rhs.value) -> Failure(text, PropertyNotFound(lhs.text, rhs.text, lhs.value.keys))
                    null == lhs.value[rhs.value] -> Failure(text, ResultIsNull(text))
                    else -> Success(text, lhs.value[rhs.value])
                }
            }
        }
        throw GuruMeditation()
    }
}
