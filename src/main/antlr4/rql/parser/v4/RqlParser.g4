parser grammar RqlParser;

options {
  tokenVocab=RqlLexer;
}

main
	: ( ( 
			startElvisIf elvis
			| startElvisIf elvisIllegal
			| startCode code
			| escapeSequence
			| text						
			)*  EOF )
		| startCode codeEof 
		| startElvisIf elvisIfEof 
		| startElvisIf elvisElseEof 
	;


text
	: ( LITERAL )+
	;

escapeSequence
	: ESCAPE_SEQUENCE
	;

code
	:	 ( legalCode | illegalCode) END_CODE
	;

legalCode
	: expression
	;

illegalCode
	: ( ~( END_CODE ) )*
	;

codeEof
	: (  legalCode | illegalCode)  EOF
	;

expression
	:	variable	
	|	expression member
	| expression subscript
	;  

variable
	: IDENTIFIER
	;

subscript
	:	START_SUBSCRIPT  ( index | expression ) END_SUBSCRIPT 
	;

index
	: INDEX
	;

member
	: DOT identifier
	;

identifier
	: IDENTIFIER
	;

elvis
	:	elvisIf START_ELVIS_ELSE elvisElse
	;

elvisIllegal
	: elvisIf ELVIS_ILLEGAL
	;

elvisIf
	:	 (text | escapeSequence | ( startCode code ) )* endElvisIf
	;

elvisIfEof
	:	 (text | escapeSequence | ( startCode code ) )* EOF
	;

elvisElse
	:  (text | escapeSequence | ( startCode code) )* endElvisElse
	;

elvisElseEof
	:  elvisIf START_ELVIS_ELSE elvisElse (text | escapeSequence | ( startCode code) )* EOF
	;

startCode
	: START_CODE
	;

startElvisIf
	: START_ELVIS_IF
	;

endElvisIf
	: END_ELVIS_IF
	;

endElvisElse
	: END_ELVIS_ELSE
	;

