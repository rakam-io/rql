package rql.exceptions

/**
 * Thrown when an offending symbol is recognized by the lexer.
 * @param message The exception message.
 * @param cause The exception.
 */
class OffendingSymbol(override val message: String, override val cause: Throwable)
    : RqlStaticException() {

    constructor(symbol: String, line: Int, index: Int, cause: Throwable)
            : this("'Unexpected ${symbol}' on line ${line}, index ${index}", cause) {

    }
}
