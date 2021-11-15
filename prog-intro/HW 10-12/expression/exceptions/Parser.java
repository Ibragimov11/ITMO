package expression.exceptions;

import expression.operations.TripleExpression;

public interface Parser {
    TripleExpression parse(String expression) throws /* Change me */ Exception;
}
