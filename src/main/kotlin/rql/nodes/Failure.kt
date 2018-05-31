package rql.nodes

/**
 * Represents the failed result of the evaluation of a node in the parse tree.
 * @param text The textual representaion of the node in the parse tree.
 * @param exception The exception that caused the failure.
 */
class Failure(text: String, val exception: Exception) : Result(text, Status.FAILURE)
