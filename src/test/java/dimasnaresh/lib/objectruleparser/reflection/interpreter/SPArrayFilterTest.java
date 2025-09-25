package dimasnaresh.lib.objectruleparser.reflection.interpreter;

import dimasnaresh.lib.objectruleparser.param.TestingClassInvoking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SPArrayFilterTest {
    @Test
    void invokeExpression_FilterArrayIndex2() throws IllegalAccessException{
        List<TestingClassInvoking> arrayClassNumberOne = getTestingClassInvokings();
        Expression expression = new SPArrayFilter(arrayClassNumberOne, "arr{fieldNumberOne == 'code_number_two'}");

        TestingClassInvoking filteredObject = (TestingClassInvoking) expression.invokeExpression();

        Assertions.assertNotNull(filteredObject);
        Assertions.assertEquals("code_number_two", filteredObject.getFieldNumberOne());
        Assertions.assertEquals("field_two_at_class_number_two", filteredObject.getFieldNumberTwo());
        Assertions.assertEquals("field_three_at_class_number_two", filteredObject.getFieldNumberThree());
    }

    private static List<TestingClassInvoking> getTestingClassInvokings() {
        List<TestingClassInvoking> arrayClassNumberOne = new ArrayList<>();
        TestingClassInvoking numberOne = new TestingClassInvoking();
        TestingClassInvoking numberOne1 = new TestingClassInvoking();
        numberOne.setFieldNumberOne("code_number_one");
        numberOne.setFieldNumberTwo("field_two_at_class_number_one");
        numberOne.setFieldNumberThree("field_three_at_class_number_one");

        numberOne1.setFieldNumberOne("code_number_two");
        numberOne1.setFieldNumberTwo("field_two_at_class_number_two");
        numberOne1.setFieldNumberThree("field_three_at_class_number_two");
        arrayClassNumberOne.add(numberOne);
        arrayClassNumberOne.add(numberOne1);
        return arrayClassNumberOne;
    }

    @Test
    void testFilterMatchFound() throws Exception {
        TestingClassInvoking obj1 = new TestingClassInvoking();
        obj1.setFieldNumberOne("yes");
        TestingClassInvoking obj2 = new TestingClassInvoking();
        obj2.setFieldNumberOne("no");

        ArrayList<TestingClassInvoking> list = new ArrayList<>(Arrays.asList(obj1, obj2));

        Expression filter = new SPArrayFilter(list, "arr{fieldNumberOne == 'yes'}");
        Object result = filter.invokeExpression();

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(TestingClassInvoking.class, result);
        Assertions.assertEquals("yes", ((TestingClassInvoking) result).getFieldNumberOne());
    }

    @Test
    void testFilterNoMatch() throws Exception {
        TestingClassInvoking obj = new TestingClassInvoking();
        obj.setFieldNumberOne("no");

        ArrayList<TestingClassInvoking> list = new ArrayList<>(Collections.singletonList(obj));

        Expression filter = new SPArrayFilter(list, "arr{fieldNumberOne == 'yes'}");
        Object result = filter.invokeExpression();

        Assertions.assertNull(result); // no match found
    }
}