package rql.exceptions

/**
 * Thrown when no starting else tag is found after the closing if tag.
 * @property message The exception message.
 * @constructor Instantiate a new instance.
 */
class NoStartingElseTag(override val message: String)
    : SyntaxException() {

    constructor(line: Int, index: Int) : this("No '(' found on line ${line}, index ${index}")
}
