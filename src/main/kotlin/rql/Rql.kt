package rql;

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import rql.exceptions.ConflictingTypes
import rql.exceptions.RqlException
import rql.nodes.Node
import rql.nodes.Tree
import rql.parser.v4.RqlLexer
import rql.parser.v4.RqlParser
import rql.traversal.Renderer
import rql.traversal.TypeInference

/**
 * Represents a parsed template string.
 * @param source The template string.
 * @param settings The settings used to parse and render the template string.
 */
class Rql private constructor(source: String, private val settings: Settings) {
    companion object {
        /**
         * Parse the template string into a parse tree.
         * @param source The template string.
         * @param settings The settings for the parsing and rendering
         */
        @JvmStatic
        @Throws(RqlException::class)
        fun parse(source: String, settings: Settings): Rql {
            return Rql(source, settings)
        }

        /**
         * Parse the template string into a parse tree.
         * @param source The template string.
         */
        @JvmStatic
        @Throws(RqlException::class)
        fun parse(source: String): Rql {
            return Rql(source, Settings())
        }
    }

    private val root = compile(source)
    private val renderer = Renderer(root)
    /**
     * The types of the variables
     */
    val variables = infer(root)

    /**
     * Compiles the template string into a parse tree.
     */
    private fun compile(source: String): Node {
        var cs = ANTLRInputStream(source)
        var lexer = RqlLexer(cs, settings.stripNewlines)
        lexer.removeErrorListeners()
        lexer.addErrorListener(ErrorListener())
        var tokens = CommonTokenStream(lexer)
        var parser = RqlParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(ErrorListener())
        return Tree(parser).generate()
    }

    private fun infer(root: Node): Variables {
        var declarations = TypeInference(root).run()
        for ((identifier, types) in declarations) {
            if (types.size > 1) {
                if (settings.checkType) {
                    throw ConflictingTypes(identifier, types)
                }
            }
        }
        return declarations
    }

    /**
     * Renderer the template string.
     * @param definitions The definitions of the variables used in the template string.
     */
    @Throws(RqlException::class)
    fun render(definitions: Map<String, Anything> = mapOf<String, Anything>()): String {
        return renderer.run(settings, definitions)
    }

}
