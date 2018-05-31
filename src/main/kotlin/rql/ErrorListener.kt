package rql

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import rql.exceptions.OffendingSymbol

/**
 *  Provides a listener for the errors the recognizer encounters.
 */
class ErrorListener : BaseErrorListener() {
    /**
     * @param  recognizer The source of the error.
     * @param offendingSymbol The offending token.
     * @param line The line number.
     * @param index The character position in the line.
     * @param msg The message to emit.
     * @param e The exception thrown by the parser.
     */
    override fun syntaxError(recognizer: Recognizer<*, *>, offendingSymbol: Anything, line: Int, index: Int, msg: String, e: RecognitionException) {
        throw OffendingSymbol(offendingSymbol.toString(), line, index, e)
    }
}
