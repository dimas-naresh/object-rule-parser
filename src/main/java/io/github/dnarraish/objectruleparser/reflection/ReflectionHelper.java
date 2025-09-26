package io.github.dnarraish.objectruleparser.reflection;

import io.github.dnarraish.objectruleparser.reflection.interpreter.ExpressionConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * created by dimasNaresh(thread009)
 */
public class ReflectionHelper {

    private final DataType[] types = new DataType[]{
            DataType.INTEGER,
            DataType.DOUBLE,
            DataType.LONG,
            DataType.STRING,
            DataType.BOOLEAN
    };

    /**
     * Getting expression name
     *
     * @param expression full expression
     * @return String of expression name
     */
    protected String getExpressionName(String expression) {
        return expression.replaceAll(ExpressionConstant.BACK_SLASH
                .concat(ExpressionConstant.BRACKET_OPEN_STRING).concat("[^")
                .concat(ExpressionConstant.BRACKET_CLOSED_STRING)
                .concat("]*\\".concat(ExpressionConstant.BRACKET_CLOSED_STRING)), "");
    }

    /**
     * Getting expression parameters
     *
     * @param expression full expression
     * @return Array of String parameters
     */
    protected String[] getExpressionParams(String expression) {
        String ex = expression;

        ex = ex.substring(ex.indexOf(ExpressionConstant.BRACKET_OPEN_CHARACTER) + 1);
        ex = ex.substring(0, ex.indexOf(ExpressionConstant.BRACKET_CLOSED_CHARACTER));

        return ex.split(ExpressionConstant.COMMA_STRING);
    }

    /**
     * Get the specified field on the class. If the field is not found on the class itself will recursively check
     * the superclass.
     *
     * @param clazz object class
     * @param fieldName field name
     * @return java.lang.reflect.Field
     */
    Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsf) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw new IllegalStateException("failed to get field");
        }
    }

    /**
     *
     * Get the specified method on the class. If the method is not found on the class
     * itself will recursively check the superclass.
     * NOTE:    Move to ReflectionHelper
     *
     * @param claxx object class
     * @param method method name
     * @return java.lang.reflect.Method
     */
    Method getMethod(Class<?> claxx, String method) {
        try {
            String methodName = method.replaceAll(ExpressionConstant.BACK_SLASH
                    .concat(ExpressionConstant.PARENTHESES_OPEN_STRING).concat("[^")
                    .concat(ExpressionConstant.PARENTHESES_CLOSED_STRING)
                    .concat("]*\\".concat(ExpressionConstant.PARENTHESES_CLOSED_STRING)), "");
            Class<?>[] parameters = getParameterTypes(method);

            return claxx.getDeclaredMethod(methodName, parameters);
        } catch (NoSuchMethodException nsf) {
            if (claxx.getSuperclass() != null) {
                return getMethod(claxx.getSuperclass(), method);
            }
            throw new IllegalStateException("failed to get method");
        }
    }

    /**
     * Returning parameter types of method
     * NOTE:    Move to ReflectionHelper
     *
     * @param method String of method
     * @return array of parameter types
     */
    private Class<?>[] getParameterTypes(String method) {
        Object[] values = getParameters(method);
        Class<?>[] classes = new Class[values.length];

        for (int i=0; i<values.length; i++) {
            if (values[i] instanceof Boolean)
                classes[i] = boolean.class;
            else if (values[i] instanceof Integer)
                classes[i] = int.class;
            else if (values[i] instanceof Double)
                classes[i] = Double.class;
            else if (values[i] instanceof Long)
                classes[i] = Long.class;
            else if (values[i] instanceof String && values[i].toString().trim().startsWith("'") && values[i].toString().trim().endsWith("'"))
                classes[i] = String.class;
            else
                classes[i] = Object.class;
        }
        return classes;
    }

    /**
     * Generate method parameter values
     * NOTE:    Move to ReflectionHelper
     *
     * @param method method initialize
     * @return array of java.lang.Object
     */
    Object[] getParameters(String method) {
        String ex = method;

        ex = ex.substring(ex.indexOf(ExpressionConstant.PARENTHESES_OPEN_CHARACTER) + 1);
        ex = ex.substring(0, ex.indexOf(ExpressionConstant.PARENTHESES_CLOSED_CHARACTER));

        String[] valueSplitter = ex.split(",");

        return ex.isEmpty() ? new Object[0] :
                convertSuggestedType(new Object[valueSplitter.length],valueSplitter,0,0);
    }

    /**
     * checking all values and convert to suggested data type
     * NOTE:    Move to ReflectionHelper
     *
     * @param results array of objects
     * @param values method parameter values
     * @param point starting point
     * @param type data type index
     * @return array of java.lang.Object
     */
    private Object[] convertSuggestedType(Object[] results, String[] values, int point, int type) {
        try {
            if (values[point].trim().equals("true") || values[point].trim().equals("false")) {
                results[point] = Boolean.parseBoolean(values[point].trim());
            } else {
                switch (types[type]){
                    case INTEGER:
                        results[point] = Integer.parseInt(values[point].trim());
                        break;
                    case DOUBLE:
                        results[point] = Double.parseDouble(values[point].trim());
                        break;
                    case LONG:
                        results[point] = Long.valueOf(values[point].trim());
                        break;
                    case STRING:
                        results[point] = String.valueOf(values[point]);
                        break;
                    default:
                        throw new IllegalStateException("cannot found best data type for ");
                }
            }
            if (point == (values.length - 1))
                return results;
            else {
                point++;
                return convertSuggestedType(results, values, point, 0);
            }
        } catch (Exception e) {
            type++;
            return convertSuggestedType(results, values, point, type);
        }
    }
}
