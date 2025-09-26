package dimasnaresh.lib.objectruleparser.param.expression;

import dimasnaresh.lib.objectruleparser.reflection.interpreter.Expression;

public class SimpleExpressionFilter extends Expression {

    public SimpleExpressionFilter() {
        super(null, "{}");
    }

    @SuppressWarnings("unused method. This method call at ExpressionFactory using reflection")
    public SimpleExpressionFilter(Object value, String expression) {
        super(value, expression);
    }

    @Override
    public Object invokeExpression() throws IllegalAccessException {
        String value = getValue().toString();
        String param = getParams()[0];
        return param.equals("lowercase") ? value.toLowerCase() : value.toUpperCase();
    }
}
