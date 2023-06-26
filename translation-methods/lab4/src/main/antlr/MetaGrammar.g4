grammar MetaGrammar;

input returns [Grammar grammar] @init {
    Grammar grammar = new Grammar();
    $grammar = grammar;
}
: GRAMMAR grammarName SEMICOLON { $grammar.setName($grammarName.text); } lines[grammar] EOF;

lines[Grammar grammar]
: (importLine[grammar] | terminal[grammar] | rule[grammar])*;

importLine[Grammar grammar]
: IMPORT imported SEMICOLON { $grammar.addImport($imported.text); };

terminal[Grammar grammar]
: tokenName COLON regexp SEMICOLON { $grammar.addToken($tokenName.text, $regexp.text); };

rule[Grammar grammar]
: ruleName inheritedAttr? (RETURNS calculatedAttr)? {RuleDefinition ruleDefinition = new RuleDefinition($ruleName.text, $inheritedAttr.text, $calculatedAttr.text); }
    COLON ruleCase[grammar, ruleDefinition] (OR ruleCase[grammar, ruleDefinition])* SEMICOLON;

ruleCase[Grammar grammar, RuleDefinition ruleDefinition] @init {
    List<TokenOrRuleCalling> ruleCallings = new ArrayList<>();
}
: (tokenOrRuleCalling[ruleCallings])+ { $grammar.addRule($ruleDefinition, new RuleBody(ruleCallings, null)); }
| EPS code? { $grammar.addRule($ruleDefinition, new RuleBody(List.of(), $code.text)); };

tokenOrRuleCalling[List<TokenOrRuleCalling> callings]
: tokenOrRuleName heritableArgs? code? { callings.add(new TokenOrRuleCalling($tokenOrRuleName.text, $heritableArgs.text, $code.text)); };

grammarName: GRAMMAR_NAME;
imported: IMPORTED;
tokenName: TOKEN_NAME;
ruleName: RULE_NAME;
tokenOrRuleName: RULE_NAME | TOKEN_NAME;
code: CODE;
regexp: REGEXP;
calculatedAttr: ATTR;
inheritedAttr: ATTR;
heritableArgs: ARGS;

GRAMMAR: 'grammar';
IMPORT: 'import';
IMPORTED: '/' .*? '/';
RETURNS: 'returns';
ATTR: '[' .*? ']';
CODE: '{' .*? '}';
ARGS: '(' .*? ')';
OR: '|';
EPS: 'ε';
COLON: ':';
SEMICOLON: ';';
REGEXP: '"'.*?'"';
TOKEN_NAME: [A-Z]+;
RULE_NAME: [a-z][a-zA-Z]*;
GRAMMAR_NAME: [a-zA-Z]+;

WHITESPACE: [ \t\r\n]+ -> skip;
