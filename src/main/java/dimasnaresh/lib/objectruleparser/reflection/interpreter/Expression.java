package dimasnaresh.lib.objectruleparser.reflection.interpreter;

import dimasnaresh.lib.objectruleparser.reflection.ReflectionHelper;

public abstract class Expression extends ReflectionHelper {

    private final Object value;
    private final String[] params;

    public abstract Object invokeExpression() throws IllegalAccessException;

    protected Expression(Object value, String expression) {
        this.params = getExpressionParams(expression);
        this.value = value;
    }

    protected String[] getParams() {
        return params;
    }

    protected Object getValue() {
        return value;
    }
}
