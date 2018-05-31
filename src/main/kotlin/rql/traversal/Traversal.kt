package rql.traversal

import rql.nodes.Node

/**
 * Represents the route through a parse tree.
 * @param T The user context of the traveller.
 */
interface Traversal<T> {
    /**
     * The node the traveller is currently at.
     */
    val current: Node get

    /**
     * Start the traveller's traversal through the parse tree.
     * @param traveller The traveller
     * @param context The traveller's user context.
     */
    fun traverse(traveller: Traveller<T>, context: T): T
}
