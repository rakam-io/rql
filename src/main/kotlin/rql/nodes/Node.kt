package rql.nodes

import rql.Context

/**
 * Represent a node in the parse tree of the template string.
 * @param source: The corresponding source code of the node
 * @param text: The textual represntation of the node.
 */
abstract class Node(val source: String, var text: String) {
    /**
     * The list of all the node's children.
     */
    val children: MutableList<Node> = mutableListOf<Node>()

    /**
     * Evaluate the node using the results of all the node's children.
     * @param context The environment for the evaluation of the parse tree.
     * @param results The results of all the node's children.
     */
    abstract fun eval(context: Context, vararg results: Result): Result
}
