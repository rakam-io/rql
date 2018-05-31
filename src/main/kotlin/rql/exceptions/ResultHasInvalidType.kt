package rql.exceptions

/**
 * Thrown when the result of a segment of code is has an invalid type.
 * @param code The offending code.
 * @param type The invalid type.
 */
class ResultHasInvalidType(code: String, type: String)
    : RqlRunTimeException() {
    override val message = "The result of '${code}' has an invalid type '${type}'"
}
