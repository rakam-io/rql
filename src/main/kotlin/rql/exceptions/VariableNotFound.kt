package rql.exceptions

/**
 * Thrown when the variable is not found in the name bindings.
 * @param identifier The identifier of the variable.
 */
class VariableNotFound(identifier: String)
    : RqlRunTimeException() {
    override val message = "Variable '${identifier}' doesn't exist"
}
