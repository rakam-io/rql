package  rql.nodes

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import rql.exceptions.*
import rql.parser.v4.RqlParser
import rql.parser.v4.RqlParserBaseVisitor

/**
 * Represents a visitor is used to generates a parse tree of the template string.
 * @param parser The ANTLR parser generate by the grammar.
 */
class Tree(private val parser: RqlParser) : RqlParserBaseVisitor<Node>() {

    fun generate(): Node {
        return main(parser.main())
    }

    private fun main(context: RqlParser.MainContext): Node {
        return visitChildren(context, Interpolation(context.text, context.text))
    }

    override fun visitMain(context: RqlParser.MainContext): Node {
        return main(context)
    }

    override fun defaultResult(): Node {
        throw GuruMeditation()
    }

    override fun visitCodeEof(context: RqlParser.CodeEofContext): Node {
        throw NoClosingCodeTag(context.start.line, context.start.charPositionInLine)
    }

    override fun visitElvisIfEof(context: RqlParser.ElvisIfEofContext): Node {
        throw NoClosingIfTag(context.start.line, context.start.charPositionInLine)
    }

    override fun visitElvisElseEof(context: RqlParser.ElvisElseEofContext): Node {
        throw NoClosingElseTag(context.start.line, context.start.charPositionInLine)
    }

    override fun visitElvisIllegal(context: RqlParser.ElvisIllegalContext): Node {
        throw NoStartingElseTag(context.start.line, context.start.charPositionInLine)
    }

    override fun visitElvisIllegalEof(context: RqlParser.ElvisIllegalEofContext): Node {
        throw NoStartingElseTag(context.start.line, context.start.charPositionInLine)
    }

    override fun visitText(context: RqlParser.TextContext): Node {
        return Literal<String>(context.text, context.text)
    }

    override fun visitEscapeSequence(context: RqlParser.EscapeSequenceContext): Node {
        return Literal<String>(context.text, unescape(context.text))
    }

    override fun visitCode(context: RqlParser.CodeContext): Node {
        return visitChildren(context, Interpolation(context.text, "{" + context.text))
    }

    override fun visitIllegalCode(context: RqlParser.IllegalCodeContext): Node {
        return Literal<String>(context.text, "{${context.text}}")
    }

    override fun visitLegalCode(context: RqlParser.LegalCodeContext): Node {
        return visitChildren(context, Code(context.text, "{${context.text}}"))
    }

    override fun visitElvis(context: RqlParser.ElvisContext): Node {
        return visitChildren(context, Elvis(context.text, "[" + context.text))
    }

    override fun visitElvisIf(context: RqlParser.ElvisIfContext): Node {
        return visitChildren(context, Interpolation(context.text, "[" + context.text))
    }

    override fun visitElvisElse(context: RqlParser.ElvisElseContext): Node {
        return visitChildren(context, Interpolation(context.text, "(" + context.text))
    }

    override fun visitStartCode(context: RqlParser.StartCodeContext): Node {
        return Literal<String>(context.text, unescapeTag(context.text))
    }

    override fun visitStartElvisIf(context: RqlParser.StartElvisIfContext): Node {
        return Literal<String>(context.text, unescapeTag(context.text))
    }

    override fun visitEndElvisIf(context: RqlParser.EndElvisIfContext): Node {
        return Literal<String>(context.text, unescapeTag(context.text))
    }

    override fun visitEndElvisElse(context: RqlParser.EndElvisElseContext): Node {
        return Literal<String>(context.text, unescapeTag(context.text))
    }

    override fun visitExpression(context: RqlParser.ExpressionContext): Node {
        return when {
            (context.children.size == 1) -> visit(context.children[0])
            else -> {
                var lhs = visit(context.children[0])
                var rhs = visit(context.children[1])
                rhs.text = lhs.text + rhs.text
                rhs.children.add(0, lhs)
                rhs
            }
        }
    }

    override fun visitVariable(context: RqlParser.VariableContext): Node {
        return Variable(context.text, context.text)
    }

    override fun visitSubscript(context: RqlParser.SubscriptContext): Node {
        return visitChildren(context, ArrayElement(context.text))
    }

    override fun visitIndex(context: RqlParser.IndexContext): Node {
        return Literal<Int>(context.text, context.text.toInt())
    }

    override fun visitMember(context: RqlParser.MemberContext): Node {
        var node = ObjectMember(context.text)
        node.children.add(visit(context.identifier()))
        return node
    }

    override fun visitIdentifier(context: RqlParser.IdentifierContext): Node {
        return Literal<String>(context.text, context.text)
    }

    private fun unescapeTag(text: String): String {
        if (text.length > 1)
            return text.substring(0, text.length / 2)
        else return ""
    }

    private fun unescape(text: String): String {
        if (text.isNotEmpty())
            return text.substring(text.length / 2)
        else return ""
    }

    private fun visitChildren(context: ParserRuleContext, node: Node): Node {
        context.children.forEach { child ->
            if (child !is TerminalNode) {
                node.children.add(visit(child))
            }
        }
        return node
    }
}

