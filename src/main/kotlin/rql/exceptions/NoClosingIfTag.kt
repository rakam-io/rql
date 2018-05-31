package rql.exceptions

/**
 * Thrown when no closing if tag is found.
 * @property message The exception message.
 * @constructor Instantiate a new instance.
 */
class NoClosingIfTag(override val message: String)
    : SyntaxException() {

    constructor(line: Int, index: Int)
            : this("No ']' found starting from line ${line}, index ${index}") {

    }
}
