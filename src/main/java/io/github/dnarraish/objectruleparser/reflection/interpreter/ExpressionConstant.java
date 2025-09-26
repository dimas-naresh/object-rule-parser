package io.github.dnarraish.objectruleparser.reflection.interpreter;

public class ExpressionConstant {

    private ExpressionConstant() {
        throw new IllegalStateException("This is class for some constant value in melanesian reflection");
    }

    public static final String BACK_SLASH = "\\";
    public static final String COMMA_STRING = ",";
    public static final String ARGUMENTS_SEPARATOR = "\\.(?=(((?!\\".concat(ExpressionConstant.BRACKET_CLOSED_STRING)
            .concat(").)*\\")
            .concat(ExpressionConstant.BRACKET_OPEN_STRING)
            .concat(")|[^\\")
            .concat(ExpressionConstant.BRACKET_OPEN_STRING)
            .concat("\\")
            .concat(ExpressionConstant.BRACKET_CLOSED_STRING)
            .concat("]*$)");

    public static final char BRACKET_OPEN_CHARACTER = '{';
    public static final char BRACKET_CLOSED_CHARACTER = '}';

    public static final char PARENTHESES_OPEN_CHARACTER = '(';
    public static final char PARENTHESES_CLOSED_CHARACTER = ')';

    public static final String BRACKET_OPEN_STRING = "{";
    public static final String BRACKET_CLOSED_STRING = "}";

    public static final String PARENTHESES_OPEN_STRING = "(";
    public static final String PARENTHESES_CLOSED_STRING = ")";

    static final String ERROR_MESSAGE_NO_SUCH_A_EXPRESSION = "Cannot find expression %s at factory";

    static final String EXPRESSION_BASE_PACKAGE = "com.github.dnarraish.objectruleparser.reflection.interpreter.";
}
