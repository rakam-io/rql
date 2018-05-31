package rql.nodes

import rql.Context
import rql.exceptions.ResultIsNull

/**
 * Represent an interpolated string in the template string.
 * param source: The corresponding source code.
 * param text: The textual represntation.
 */
class Interpolation(source: String, text: String) : Node(source, text) {
    /**
     * Returns the string representing all the node's children.
     * @param context: The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        val builder = StringBuilder()
        for (result in results) {
            if (result is Failure) {
                return result
            } else if (result is Success) {
                if (result.value == null) {
                    return Failure(text, ResultIsNull(result.text))
                } else {
                    builder.append(result.value.toString())
                }
            }
        }
        return Success(text, builder.toString())
    }
}
