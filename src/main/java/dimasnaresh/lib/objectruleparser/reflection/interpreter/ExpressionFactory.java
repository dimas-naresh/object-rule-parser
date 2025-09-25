package dimasnaresh.lib.objectruleparser.reflection.interpreter;

import dimasnaresh.lib.objectruleparser.reflection.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * created by dimasNaresh(thread009)
 */
public class ExpressionFactory extends ReflectionHelper {


    public  Expression gettingExpression(Object object, String expression) {
        String expressionName = getExpressionName(expression);
        try {
            Class<?> expressionClass = Class.forName(ExpressionConstant.EXPRESSION_BASE_PACKAGE.concat(expressionName));
            Class<?>[] types = new Class[] {Object.class, String.class};
            Constructor<?> expressionConstructor = expressionClass.getConstructor(types);
            return (Expression) expressionConstructor.newInstance(object, expression);
        } catch (ClassNotFoundException clNFoundEx) {
            throw new IllegalStateException(String.format(ExpressionConstant.ERROR_MESSAGE_NO_SUCH_A_EXPRESSION, expressionName));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException nSuchMethodEx) {
            throw new IllegalStateException(nSuchMethodEx.getCause());
        }
    }

    public static ExpressionFactory newInstance() {
        return new ExpressionFactory();
    }


}
