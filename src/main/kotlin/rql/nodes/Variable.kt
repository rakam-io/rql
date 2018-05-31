package rql.nodes

import rql.Context
import rql.exceptions.VariableIsNull
import rql.exceptions.VariableNotFound

/**
 * Represents a variable in the template string.
 * param source: The corresponding source code.
 * param identifier : The identifier of the variable.
 */
class Variable(source: String, val identifier: String) : Node(source, identifier) {
    /**
     * Returns the value of the variable.
     * @param context The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        return when {
            context.bindings.contains(identifier) -> {
                val value = context.bindings[identifier]
                value?.let {
                    Success(text, value)
                } ?: Failure(text, VariableIsNull(identifier))
            }
            else -> Failure(text, VariableNotFound(identifier))
        }

    }
}
