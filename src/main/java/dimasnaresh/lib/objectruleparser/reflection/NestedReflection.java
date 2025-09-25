package dimasnaresh.lib.objectruleparser.reflection;

import dimasnaresh.lib.objectruleparser.reflection.interpreter.Expression;
import dimasnaresh.lib.objectruleparser.reflection.interpreter.ExpressionConstant;
import dimasnaresh.lib.objectruleparser.reflection.interpreter.ExpressionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class NestedReflection extends ReflectionHelper implements Reflection {

    /**
     * Returns the value of a (nested) field on a bean.
     * NOTE:    This method need to refactor and must be put inside interpreter, so all reflection expression must be
     *          triggered by same pattern
     *
     * @param bean      java bean
     * @param fieldName field name
     * @return java object
     */
    public Object getObject(Object bean, String fieldName) {
        if (bean == null || fieldName == null)
            throw new IllegalStateException(String.format("bean: %s, fieldName: %s", bean, fieldName));

        String[] nestedFields = fieldName.split(ExpressionConstant.ARGUMENTS_SEPARATOR);
        Class < ? > componentClass = bean.getClass();
        Object value = bean;
        boolean isArray = false;

        try {
            for (String nestedField : nestedFields) {
                Field field;
                if(nestedField.contains(ExpressionConstant.BRACKET_OPEN_STRING)){
                    Expression expression = ExpressionFactory.newInstance().gettingExpression(value, nestedField);
                    value = expression.invokeExpression();
                    isArray = false;
                } else if (nestedField.contains(ExpressionConstant.PARENTHESES_OPEN_STRING)) {
                    Method method = getMethod(componentClass, nestedField);
                    method.setAccessible(true);
                    value = method.invoke(value, removeSingleQuotesString(getParameters(nestedField)));
                    isArray = false;
                } else if (isArray) {
                    value = ((List<?>) value).get(Integer.parseInt(nestedField));
                    isArray = false;
                } else {
                    field = getField(componentClass, nestedField);
                    field.setAccessible(true);
                    value = field.get(value);
                    isArray = false;
                }

                if (value != null) {
                    componentClass = value.getClass();
                    if (value instanceof List<?>) {
                        isArray = true;
                    }
                }
            }
        } catch (ClassCastException | InvocationTargetException | IllegalAccessException iae) {
            throw new IllegalStateException(iae);
        }

        return value;

    }

    private Object[] removeSingleQuotesString(Object[] values) {
        Object[] newValues = new Object[values.length];
        for (int i=0; i<values.length; i++) {
            if (values[i] instanceof String)
                newValues[i] = values[i].toString().replaceAll("^\'|\'$", "");
            else
                newValues[i] = values[i];
        }
        return newValues;
    }
}
