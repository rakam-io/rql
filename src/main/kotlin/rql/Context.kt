package rql

import rql.nodes.Result

/**
 *  The user context of the renderer.
 *  @constructor Empty constructor
 */
class Context {
    /**
     * The variables in scope.
     */
    val bindings = Bindings()
    /**
     * The settings used to evaluate the parse tree.
     */
    var settings = Settings()
    /**
     * The result of evaluation of the parse tree.
     */
    var result: Result? = null
}
