package rql.exceptions

/**
 * Thrown when a variable is not an object.
 * @param variable The variable.
 */
class NonObject(val variable: String)
    : RqlRunTimeException() {
    override val message: String = "Variable '${variable}' is not an object"
}
