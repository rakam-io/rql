package rql.nodes

/**
 * Represents the result of the evaluation of a node in the parse tree.
 * @param text The textual representaion of the node that returns the result.
 * @param status The status of the evaluation.
 */
abstract class Result(val text: String, private val status: Status) {
    fun success(callback: (() -> Unit)): Result {
        if (status == Status.SUCCESS) {
            callback()
        }
        return this
    }

    fun failure(callback: (() -> Unit)): Result {
        if (status == Status.FAILURE) {
            callback()
        }
        return this
    }
}

