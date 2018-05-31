parser grammar RqlParser;

options {
  tokenVocab=RqlLexer;
}

main
	: (  
			/*
			 * '[]()' if-else statements
			 */
			startElvisIf elvis
			/*
			 * '{}' Code tags
			 */
			| startCode code
			/*
			 * Escape sequences 
			 * - Normal : '\{','\['
			 * - If statement : '\{','\]'
			 * - Else statement : '\{', '\)'
			 */
			| escapeSequence
			/*
			 * Plain text
			 */
			| text						
			/*
			 * After a starting if statement '()', no '[' is found.
			 */
			| startElvisIf elvisIllegal 
		)* 
		( EOF 
			// Unexpected Eofs
			/*
			 * After a starting code tag '{', no closing '}' is found.
			 */
			| startCode codeEof 
			/*
			 * After a starting code tag in a if statement, '[{', no closing '}' is found. 
			 */
			| startElvisIf ( text | escapeSequence | ( startCode code ) )* startCode codeEof
			/* 
			 * After a starting code tag in a else statement '[]({', no closing '}' is found.
			 */
			| startElvisIf elvisIf START_ELVIS_ELSE (text | escapeSequence | ( startCode code) )*  startCode codeEof
			/*
			 * After a starting if statement '[', no closing ']' is found.
			 */
			| startElvisIf elvisIfEof  
			/*
			 * After a starting if statement '[]', EOF is encountered.
			 */
			| startElvisIf elvisIllegalEof
			/*
			 * After a starting else statement '[](', no closing ')' is found.
			 */
			| startElvisIf elvisElseEof 
		)
	;

codeEof
	: ( legalCode | illegalCode ) EOF
	;

elvisIfEof
	:	( text | escapeSequence | ( startCode code ) )* EOF
	;

elvisElseEof
	:  elvisIf START_ELVIS_ELSE (text | escapeSequence | ( startCode code) )* EOF
	;

elvisIllegalEof
	: elvisIf EOF
	;

elvisIllegal
	: elvisIf ELVIS_ILLEGAL
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

elvisIf
	:	 (text | escapeSequence | ( startCode code ) )* endElvisIf
	;

elvisElse
	:  (text | escapeSequence | ( startCode code) )* endElvisElse
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

