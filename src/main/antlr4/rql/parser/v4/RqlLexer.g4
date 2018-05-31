lexer grammar RqlLexer;

@lexer::members {
	private boolean stripNewlines = false;

	public RqlLexer(CharStream charStream, boolean stripNewlines) {
		this(charStream);
		this.stripNewlines = stripNewlines;
	}

}

tokens { ESCAPE_SEQUENCE, LITERAL, START_CODE}

fragment
Digit
: [0-9]
;

fragment
Number
: (Digit)+
;

fragment
Letter 
: [a-zA-Z]
;

fragment
Whitespace
: [ \t]
;


fragment
Newline
: [\r\n]
;

fragment 
Identifier
: ( Letter | '_' ) ( Letter | '_' | Digit | '-' )*
;

DEFAULT_STRIP_NEWLINES
: { stripNewlines }? Newline+ -> skip
;

DEFAULT_ESCAPE_SEQUENCE
:	( '\\\\' )* '\\' ( '{' | '[' ) -> type(ESCAPE_SEQUENCE)
;

START_CODE
:	( '\\\\' )* '{' -> pushMode(CODE_MODE)
;

START_ELVIS_IF
:	( '\\\\' )* '[' -> pushMode(ELVIS_IF_MODE)
;

DEFAULT_LITERAL
: . -> type(LITERAL)
;

mode CODE_MODE;

	CODE_STRIP_NEWLINES 
	: { stripNewlines }? Newline+ -> skip
	;

	IDENTIFIER
	: Identifier
	;

	DOT
	: '.'
	;

	START_SUBSCRIPT
	: '['
	;

	INDEX
	: Number
	;

	END_SUBSCRIPT
	: ']'
	;

	END_CODE
	: '}' -> popMode
	;

	CODE_ILLEGAL
	: . 
	;

mode ELVIS_IF_MODE;

	ELVIS_IF_STRIP_NEWLINES
	: { stripNewlines }? Newline+ -> skip
	;

	ELVIS_IF_ESCAPE_SEQUENCE
	:	( '\\\\' )* '\\' ( ']' | '{') -> type(ESCAPE_SEQUENCE)
	;

	ELVIS_IF_START_CODE
	:	(	'\\\\' )* '{' -> type(START_CODE), pushMode(CODE_MODE)
	;

	END_ELVIS_IF
	:	( '\\\\' )* ']' -> popMode, pushMode(INTERMEDIATE_MODE)
	;

	ELVIS_IF_LITERAL
	: . -> type(LITERAL)
	;

mode INTERMEDIATE_MODE;

	START_ELVIS_ELSE
	: '('  -> popMode, pushMode(ELVIS_ELSE_MODE)
	;

	ELVIS_ILLEGAL
	: .  -> popMode
	;

mode ELVIS_ELSE_MODE;

	ELVIS_ELSE_STRIP_NEWLINES
	: { stripNewlines }? Newline+ -> skip
	;

	ELVIS_ELSE_ESCAPE_SEQUENCE
	:	( '\\\\' )* '\\' ( ')' | '{') -> type(ESCAPE_SEQUENCE)
	;

	ELVIS_ELSE_START_CODE
	:	(	'\\\\' )* '{' -> type(START_CODE), pushMode(CODE_MODE)
	;

	END_ELVIS_ELSE
	:	( '\\\\' )* ')' -> popMode
	;

	ELVIS_ELSE_LITERAL
	: . -> type(LITERAL)
	;


