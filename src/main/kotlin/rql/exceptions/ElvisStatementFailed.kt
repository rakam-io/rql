package rql.exceptions

/**
 * Thrown when both interpolated strings of an if-else statement failed to evaluate successfully.
 * @param code The offending code.
 */
class ElvisStatementFailed(code: String) : RqlRunTimeException() {
    override val message: String = "The if-else statement '${code}' failed"
}
