package rql.nodes

import rql.Context
import rql.exceptions.ElvisStatementFailed

/**
 * Represent the elvis operator in the template string.
 * The operator returns the first interpolated string if it is evaluated successfully,
 * otherwise it returns the second interpolated string.
 * @param source: The corresponding source code.
 * @param text: The textual represntation.
 */
open class Elvis(source: String, text: String) : Node(source, text) {
    /**
     * Returns the first successful result of its childres.
     * @param context: The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        for (result in results) {
            if (result is Success) {
                return result
            }
        }
        return Failure(text, ElvisStatementFailed(text))
    }
}
