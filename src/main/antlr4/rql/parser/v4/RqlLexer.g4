lexer grammar RqlLexer;

@lexer::members {
  private boolean stripSpacesAroundTags = false;
  private boolean stripSingleLine = false;

  public RqlLexer(CharStream charStream, boolean stripSpacesAroundTags) {
    this(charStream, stripSpacesAroundTags, false);
  }

  public RqlLexer(CharStream charStream, boolean stripSpacesAroundTags, boolean stripSingleLine) {
      this(charStream);
      this.stripSpacesAroundTags = stripSpacesAroundTags;
      this.stripSingleLine = stripSingleLine;
    }
}

OutStart
 : ( {stripSpacesAroundTags && stripSingleLine}? SpaceOrTab* '{'
   | {stripSpacesAroundTags && !stripSingleLine}? WhitespaceChar* '{'
   | WhitespaceChar* '{-'
   | '{'
   ) -> pushMode(IN_TAG)
 ;

Other
 : .
 ;

fragment WhitespaceChar : [ \t\r\n];
fragment SStr           : '\'' ~'\''* '\'';
fragment DStr           : '"' ~'"'* '"';
fragment SpaceOrTab     : [ \t];
fragment LineBreak      : '\r'? '\n' | '\r';
fragment Digit          : [0-9];
fragment Letter         : [a-zA-Z];

mode IN_TAG;

  OutStart2 : '{' -> pushMode(IN_TAG);

  DotDot    : '..';
  Dot       : '.';
  OPar      : '(';
  CPar      : ')';
  OBr       : '[';
  CBr       : ']';

  OutEnd
   : ( {stripSpacesAroundTags && stripSingleLine}? '}' SpaceOrTab* LineBreak?
     | {stripSpacesAroundTags && !stripSingleLine}? '}' WhitespaceChar*
     | '-}' WhitespaceChar*
     | '}'
     ) -> popMode
   ;

  Pipe      : '|';
  Str : SStr | DStr;

  WS : WhitespaceChar+ -> channel(HIDDEN);

  Id : ( Letter | '_' ) (Letter | '_' | '-' | Digit)*;


mode IN_RAW;

  OtherRaw : . ;
