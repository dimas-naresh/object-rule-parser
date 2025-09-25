package dimasnaresh.lib.objectruleparser.reflection.interpreter;

import dimasnaresh.lib.objectruleparser.param.TestingClassInvoking;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SPArrayFilterTest {
    @Test
    void invokeExpression() throws IllegalAccessException{
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
        Expression expression = new SPArrayFilter(arrayClassNumberOne, "arr{fieldNumberOne == 'code_number_two'}");

        TestingClassInvoking filteredObject = (TestingClassInvoking) expression.invokeExpression();
        System.out.println(filteredObject.getFieldNumberOne().concat(", ")
                .concat(filteredObject.getFieldNumberTwo())
                .concat(", ").concat(filteredObject.getFieldNumberThree()));


    }
}