package rql.nodes

import rql.Context

/**
 * Represent a literal value.
 * @param T The type of the value
 * @param source: The corresponding source code.
 * @param value: The value.
 */
class Literal<T>(source: String, private val value: T) : Node(source, value.toString()) {
    /**
     * Returns the literal value.
     * @param context The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        return Success(text, value)
    }
}
