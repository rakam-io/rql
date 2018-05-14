parser grammar RqlParser;

options {
  tokenVocab=RqlLexer;
}

parse
 : block EOF
 ;

block
 : atom*
 ;

atom
 : output     #atom_output
 | other      #atom_others
 ;

id
 : Id

 ;

id2
 : id
 | Empty
 | Nil
 | True
 | False
 ;

expr
 : term                                          #expr_term
 ;

index
 : Dot id2
 | OBr expr CBr
 ;

term
 : DoubleNum      #term_DoubleNum
 | LongNum        #term_LongNum
 | Str            #term_Str
 | True           #term_True
 | False          #term_False
 | Nil            #term_Nil
 | lookup         #term_lookup
 | Empty          #term_Empty
 | OPar expr CPar #term_expr
 ;

lookup
 : id index* QMark?   #lookup_id_indexes
 | OBr Str CBr QMark? #lookup_Str
 | OBr Id CBr QMark?  #lookup_Id
 ;

output
 : outStart expr filter* OutEnd
 ;

raw_body
 : OtherRaw*
 ;

filter
 : Pipe Id params?
 ;

params
 : Col param_expr ( Comma param_expr )*
 ;

param_expr
 : id2 Col expr #param_expr_key_value
 | expr         #param_expr_expr
 ;

outStart
 : OutStart
 | OutStart2
 ;

other
 : Other+
 ;