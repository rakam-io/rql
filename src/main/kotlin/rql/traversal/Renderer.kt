package rql.traversal

import rql.Anything
import rql.Context
import rql.Settings
import rql.exceptions.GuruMeditation
import rql.nodes.Failure
import rql.nodes.Node
import rql.nodes.Result
import rql.nodes.Success

class Renderer(root: Node) : Traveller<Context>(DepthFirstTraversal<Context>(root)) {
    private val stack = mutableListOf<Node>()
    private val arguments = mutableListOf<MutableList<Result>>()
    /**
     * Push the node on the stack the first time the renderer visits a node.
     * @param context User context of the renderer.
     */
    override fun preorder(context: Context) {
        stack.add(0, current)
        arguments.add(0, mutableListOf<Result>())
    }

    /**
     *  Evaluate the node the last time the renderer visits a node.
     *  @param context User context of the renderer.
     */
    override fun postorder(context: Context) {

        var result = current.eval(context, *(arguments.first().toTypedArray()))
        context.result = result

        arguments.removeAt(0)
        stack.removeAt(0)

        if (!arguments.isEmpty()) {
            arguments.first().add(result)
        }
    }

    /**
     * Starts the rendering.
     * @param settings The settings used for the rendering.
     * @param variables The variables used for the rendering.
     */
    fun run(settings: Settings, variables: Map<String, Anything>): String {
        var context = Context()
        context.bindings.push(variables)
        context.settings = settings

        stack.clear()
        arguments.clear()
        traverse(context)

        var result = context.result

        if (result is Success) {
            return result.value.toString()
        } else if (result is Failure) {
            throw result.exception
        }
        throw GuruMeditation()
    }
}
