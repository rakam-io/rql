package rql.nodes

import rql.Context
import rql.exceptions.ResultHasInvalidType
import rql.exceptions.ResultIsNull
import java.time.LocalDate

/**
 * Represent a code segment in the template string.
 * @param source: The corresponding source code.
 * @param text: The textual represntation.
 */
class Code(source: String, text: String) : Node(source, text) {
    /**
     * Returns the result of the evaluation of the code segment.
     * @param context The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    override fun eval(context: Context, vararg results: Result): Result {
        val builder = StringBuilder()
        for (result in results) {
            if (result is Failure) {
                return result
            } else if (result is Success) {
                if (result.value == null) {
                    return Failure(text, ResultIsNull(result.text))
                } else if (result.value !is String && context.settings.onlyValidTypes &&
                        !context.settings.validTypes.contains(result.value::class.java.name)) {

                    return Failure(text, ResultHasInvalidType(result.text, result.value::class.java.name))
                } else {

                    if (result.value is Long || result.value is Int ||
                            result.value is Short || result.value is Byte) {
                        builder.append(result.value.toString())

                    } else if (result.value is LocalDate) {
                        builder.append("date '${result.value.toString()}'")

                    } else {
                        builder.append("'${result.value.toString()}'")
                    }
                }
            }
        }
        return Success(text, builder.toString())
    }
}
