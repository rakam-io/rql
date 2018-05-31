package rql.exceptions

/**
 * Thrown when the index of an array  is not an integer.
 * @param variable The variable.
 * @param index The index.
 */
class ArrayIndexNotAnInteger(variable: String, index: String)
    : RqlRunTimeException() {
    override val message: String = "'${index}' cannot be used as the index of the array '${variable}'"
}
