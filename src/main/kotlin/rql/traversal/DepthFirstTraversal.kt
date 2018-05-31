package rql.traversal

import rql.nodes.Node

/**
 * Visits the parse tree in depth first search order.
 * @param T The user context of traveller.
 * @param root The root of the parse tree.
 */
class DepthFirstTraversal<T>(val root: Node) : Traversal<T> {
    override var current: Node = root
    private val stack = mutableListOf<Node>()
    private val indices = mutableListOf<Int>()
    /**
     * Starts traversing through the parse tree
     * @param traveller The traveller.
     * @param context User context of the traveller
     */
    override fun traverse(traveller: Traveller<T>, context: T): T {
        stack.clear()
        indices.clear()

        stack.add(0, root)
        indices.add(0, 0)

        preorder(traveller, context)

        while (!stack.isEmpty()) {

            if (stack.first().children.size == indices.first()) {

                postorder(traveller, context)

                stack.removeAt(0)
                indices.removeAt(0)

            } else {
                stack.add(0, stack.first().children[indices.first()])
                indices[0]++
                indices.add(0, 0)

                preorder(traveller, context)
            }
        }
        return context
    }

    /**
     * The first time the traveller visits a node.
     * @param traveller The traveller.
     * @param context User context of the traveller
     */
    private fun preorder(traveller: Traveller<T>, context: T) {
        current = stack.first()
        traveller.preorder(context)
    }

    /**
     * The last time the traveller visits a node.
     * @param traveller The traveller.
     * @param context User context of the traveller
     */
    private fun postorder(traveller: Traveller<T>, context: T) {
        current = stack.first()
        traveller.postorder(context)
    }
}


