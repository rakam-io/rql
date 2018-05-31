package rql.exceptions

/**
 * Thrown when the result of a segment of code is null.
 * @param code The offending code.
 */
class ResultIsNull(code: String)
    : RqlRunTimeException() {
    override val message = "The result of '${code}' is null"
}
