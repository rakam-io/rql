package rql.traversal

/**
 * Represent a traveller through the parse tree.
 * @property traversal Delegate that determines the path through the parse tree.
 */
abstract class Traveller<T>(private val traversal: Traversal<T>)
    : Traversal<T> by traversal {

    /**
     * The first time the traveller visits a node.
     * @param context User context of the traveller
     */
    open fun preorder(context: T) {}

    /**
     * The last time the traveller visits a node.
     * @param context User context of the traveller
     */
    open fun postorder(context: T) {}

    /**
     * Starts traversing through the parse tree
     * @param context User context of the traveller
     */
    fun traverse(context: T): T {
        return traverse(this, context)
    }
}
