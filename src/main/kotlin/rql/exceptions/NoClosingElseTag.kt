package rql.exceptions

/**
 * Thrown when no closing else tag is found.
 * @property message The exception message.
 * @constructor Instantiate a new instance.
 */
class NoClosingElseTag(override val message: String)
    : SyntaxException() {

    constructor(line: Int, index: Int)
            : this("No ')' found starting from line ${line}, index ${index}") {

    }
}
