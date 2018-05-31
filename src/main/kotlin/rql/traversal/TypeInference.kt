package rql.traversal

import rql.RqlType
import rql.Variables
import rql.nodes.ArrayElement
import rql.nodes.Node
import rql.nodes.ObjectMember
import rql.nodes.Variable

class TypeInference(root: Node)
    : Traveller<Variables>(DepthFirstTraversal<Variables>(root)) {
    private val stack = mutableListOf<Node>()
    /**
     * Push the node on the stack the first time it is visited and
     * infer its type from parent node.
     * @param context User context of the renderer.
     */
    override fun preorder(context: Variables) {
        stack.add(0, current)

        val node = stack.first()

        if (node is Variable) {

            var type = RqlType.PRIMITIVE

            if (stack.size > 1) {
                var parent = stack[1]

                if (parent is ObjectMember) {
                    type = RqlType.OBJECT
                } else if (parent is ArrayElement) {
                    type = RqlType.ARRAY
                }
            }
            if (!context.contains(node.identifier)) {
                context[node.identifier] = mutableSetOf<RqlType>()
            }
            context[node.identifier]!!.add(type)
        }
    }

    /**
     * Pop the node from stack the last time it is visited.
     * @param context User context of the renderer.
     */
    override fun postorder(context: Variables) {
        stack.removeAt(0)
    }

    /**
     * Start getting the types of the variable.
     */
    fun run(): Variables {
        return traverse(mutableMapOf<String, MutableSet<RqlType>>())
    }
}

