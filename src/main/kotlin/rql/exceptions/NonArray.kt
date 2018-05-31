package rql.exceptions

/**
 * Thrown when a variable is not an object.
 * @param variable The variable.
 */
class NonArray(variable: String)
    : RqlRunTimeException() {
    override val message: String = "Variable '${variable}' is not an array"
}
