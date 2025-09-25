package dimasnaresh.lib.objectruleparser.reflection.interpreter;

import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;

/**
 * created by dimasNaresh(thread009)
 */
public class SPArrayFilter extends Expression {

    @SuppressWarnings("unused method. This method call at ExpressionFactory using reflection")
    public SPArrayFilter(Object value, String expression) {
        super(value, expression);
    }

    @Override
    public Object invokeExpression() throws IllegalAccessException {
        ArrayList<?> value = (ArrayList<?>) getValue();
        IllegalStateException stEx = new IllegalStateException();
        Object filteredValue = value.stream().filter(o -> {
            org.springframework.expression.ExpressionParser sprExpressionParser = new SpelExpressionParser();
            org.springframework.expression.Expression sprExpression = sprExpressionParser.parseExpression(getParams()[0]);
            return (boolean) sprExpression.getValue(o);
        }).findAny().orElse(null);

        if (stEx.getStackTrace() == null)
            throw new IllegalAccessException("cannot found field ".concat(getParams()[0]).concat("on array ").concat(value.getClass().getName()));
        else
            return filteredValue;
    }
}
