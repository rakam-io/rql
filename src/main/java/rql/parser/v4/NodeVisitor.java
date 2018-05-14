package rql.parser.v4;

import rql.ParseSettings;
import rql.filters.Filter;
import rql.nodes.AtomNode;
import rql.nodes.BlockNode;
import rql.nodes.FilterNode;
import rql.nodes.KeyValueNode;
import rql.nodes.LNode;
import rql.nodes.LookupNode;
import rql.nodes.OutputNode;
import rql.parser.v4.RqlParserBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Map;

import static rql.parser.v4.RqlParser.AtomContext;
import static rql.parser.v4.RqlParser.Atom_othersContext;
import static rql.parser.v4.RqlParser.BlockContext;
import static rql.parser.v4.RqlParser.Expr_termContext;
import static rql.parser.v4.RqlParser.FilterContext;
import static rql.parser.v4.RqlParser.IndexContext;
import static rql.parser.v4.RqlParser.Lookup_IdContext;
import static rql.parser.v4.RqlParser.Lookup_StrContext;
import static rql.parser.v4.RqlParser.Lookup_id_indexesContext;
import static rql.parser.v4.RqlParser.OutputContext;
import static rql.parser.v4.RqlParser.Param_exprContext;
import static rql.parser.v4.RqlParser.Param_expr_exprContext;
import static rql.parser.v4.RqlParser.Param_expr_key_valueContext;
import static rql.parser.v4.RqlParser.ParseContext;
import static rql.parser.v4.RqlParser.Term_DoubleNumContext;
import static rql.parser.v4.RqlParser.Term_EmptyContext;
import static rql.parser.v4.RqlParser.Term_FalseContext;
import static rql.parser.v4.RqlParser.Term_LongNumContext;
import static rql.parser.v4.RqlParser.Term_NilContext;
import static rql.parser.v4.RqlParser.Term_StrContext;
import static rql.parser.v4.RqlParser.Term_TrueContext;
import static rql.parser.v4.RqlParser.Term_exprContext;
import static rql.parser.v4.RqlParser.Term_lookupContext;

public class NodeVisitor
        extends RqlParserBaseVisitor<LNode>
{
    private Map<String, Filter> filters;
    private final ParseSettings parseSettings;
    private boolean isRootBlock = true;

    public NodeVisitor(Map<String, Filter> filters, ParseSettings parseSettings)
    {
        if (filters == null) { throw new IllegalArgumentException("filters == null"); }

        if (parseSettings == null) { throw new IllegalArgumentException("parseSettings == null"); }

        this.filters = filters;
        this.parseSettings = parseSettings;
    }

    // parse
    //  : block EOF
    //  ;
    @Override
    public BlockNode visitParse(ParseContext ctx)
    {
        return visitBlock(ctx.block());
    }

    // block
    //  : atom*
    //  ;
    @Override
    public BlockNode visitBlock(BlockContext ctx)
    {

        BlockNode node = new BlockNode(isRootBlock);
        isRootBlock = false;

        for (AtomContext child : ctx.atom()) {
            node.add(visit(child));
        }

        return node;
    }

    // atom
    // : ...
    // | other      #atom_others
    // ;
    @Override
    public LNode visitAtom_others(Atom_othersContext ctx)
    {
        return new AtomNode(ctx.getText());
    }

    // output
    //  : outStart expr filter* OutEnd
    //  ;
    @Override
    public OutputNode visitOutput(OutputContext ctx)
    {

        OutputNode node = new OutputNode(visit(ctx.expr()));

        for (FilterContext child : ctx.filter()) {
            node.addFilter(visitFilter(child));
        }

        return node;
    }

    // filter
    //  : Pipe Id params?
    //  ;
    //
    // params
    //  : Col param_expr (Comma param_expr)*
    //  ;
    @Override
    public FilterNode visitFilter(FilterContext ctx)
    {

        FilterNode node = new FilterNode(ctx, filters.get(ctx.Id().getText()));

        if (ctx.params() != null) {
            for (Param_exprContext child : ctx.params().param_expr()) {
                node.add(visit(child));
            }
        }

        return node;
    }

    // param_expr
    //  : id2 Col expr #param_expr_key_value
    //  | ...
    //  ;
    @Override
    public LNode visitParam_expr_key_value(Param_expr_key_valueContext ctx)
    {
        return new KeyValueNode(ctx.id2().getText(), visit(ctx.expr()));
    }

    // param_expr
    //  : ...
    //  | expr         #param_expr_expr
    //  ;
    @Override
    public LNode visitParam_expr_expr(Param_expr_exprContext ctx)
    {
        return visit(ctx.expr());
    }

    // expr
    //  : ...
    //  | term                                 #expr_term
    //  ;
    @Override
    public LNode visitExpr_term(Expr_termContext ctx)
    {
        return visit(ctx.term());
    }

    // term
    //  : DoubleNum      #term_DoubleNum
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_DoubleNum(Term_DoubleNumContext ctx)
    {
        return new AtomNode(new Double(ctx.DoubleNum().getText()));
    }

    // term
    //  : ...
    //  | LongNum        #term_LongNum
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_LongNum(Term_LongNumContext ctx)
    {
        return new AtomNode(new Long(ctx.LongNum().getText()));
    }

    // term
    //  : ...
    //  | Str            #term_Str
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_Str(Term_StrContext ctx)
    {
        return fromString(ctx.Str());
    }

    // term
    //  : ...
    //  | True           #term_True
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_True(Term_TrueContext ctx)
    {
        return new AtomNode(true);
    }

    // term
    //  : ...
    //  | False          #term_False
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_False(Term_FalseContext ctx)
    {
        return new AtomNode(false);
    }

    // term
    //  : ...
    //  | Nil            #term_Nil
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_Nil(Term_NilContext ctx)
    {
        return new AtomNode(null);
    }

    // term
    //  : ...
    //  | lookup         #term_lookup
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_lookup(Term_lookupContext ctx)
    {
        return visit(ctx.lookup());
    }

    // term
    //  : ...
    //  | Empty          #term_Empty
    //  | ...
    //  ;
    @Override
    public LNode visitTerm_Empty(Term_EmptyContext ctx)
    {
        return AtomNode.EMPTY;
    }

    // term
    //  : ...
    //  | OPar expr CPar #term_expr
    //  ;
    @Override
    public LNode visitTerm_expr(Term_exprContext ctx)
    {
        return visit(ctx.expr());
    }

    // lookup
    //  : id index* QMark?   #lookup_id_indexes
    //  | ...
    //  ;
    //
    // index
    //  : Dot id2
    //  | OBr expr CBr
    //  ;
    @Override
    public LookupNode visitLookup_id_indexes(Lookup_id_indexesContext ctx)
    {

        LookupNode node = new LookupNode(ctx.id().getText());

        for (IndexContext index : ctx.index()) {

            if (index.Dot() != null) {
                node.add(new LookupNode.Hash(index.id2().getText()));
            }
            else {
                node.add(new LookupNode.Index(visit(index.expr())));
            }
        }

        return node;
    }

    // lookup
    //  : ...
    //  | OBr Str CBr QMark? #lookup_Str
    //  | ...
    //  ;
    @Override
    public LookupNode visitLookup_Str(Lookup_StrContext ctx)
    {
        return new LookupNode(strip(ctx.Str().getText()));
    }

    // lookup
    //  : ...
    //  | OBr Id CBr QMark?  #lookup_Id
    //  ;
    @Override
    public LookupNode visitLookup_Id(Lookup_IdContext ctx)
    {
        return new LookupNode("@" + ctx.Id().getText());
    }

    private static AtomNode fromString(TerminalNode str)
    {
        return new AtomNode(strip(str.getText()));
    }

    private static String strip(String str)
    {
        return str.substring(1, str.length() - 1);
    }
}
