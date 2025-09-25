package dimasnaresh.lib.objectruleparser.reflection.interpreter;

import dimasnaresh.lib.objectruleparser.reflection.ReflectionHelper;

public abstract class Expression extends ReflectionHelper {

    private final Object value;
    private final String[] params;

    Expression(Object value, String expression) {
        this.params = getExpressionParams(expression);
        this.value = value;
    }

    public abstract Object invokeExpression() throws IllegalAccessException;

    String[] getParams() {
        return params;
    }

    Object getValue() {
        return value;
    }
}
