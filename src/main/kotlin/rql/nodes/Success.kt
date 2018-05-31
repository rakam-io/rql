package rql.nodes

import rql.Anything

/**
 * Represents the successful result of the evaluation of a node in the parse tree.
 * @param value The return value of the evaluation.
 */
class Success(text: String, val value: Anything) : Result(text, Status.SUCCESS)
