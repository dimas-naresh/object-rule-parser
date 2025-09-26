package io.github.dnarraish.objectruleparser.reflection;

import io.github.dnarraish.objectruleparser.param.Address;
import io.github.dnarraish.objectruleparser.param.City;
import io.github.dnarraish.objectruleparser.param.ClientAddress;
import io.github.dnarraish.objectruleparser.param.Province;
import io.github.dnarraish.objectruleparser.param.expression.SimpleExpressionFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class NestedReflectionTest {
    private NestedReflection nestedReflection;

    @BeforeEach
    void initialize() {
        nestedReflection = new NestedReflection();
    }

    @Test
    void testInvokingMethod() {
        NumberParam numberParam = new NumberParam();
        Assertions.assertNull(nestedReflection.getObject(numberParam, "stringCallback(1, 'terlalu lama sendiri')"));

        Assertions.assertEquals("My name is Melanesian, i'am 23 years old, my sallary is around 2000000.0 Rupiah and i already have corona",
                nestedReflection.getObject(numberParam, "aStatement(23, 2000000D, true)"));

        Assertions.assertEquals("My name is Melanesian, i'am 17 years old, my sallary is around 2000000.0 Rupiah and i dont have corona",
                nestedReflection.getObject(numberParam, "aStatement(17, 2000000D, false)"));
    }

    @Test
    void testGet() {
        ClientAddress clientAddress = createClientAdress();
        Assertions.assertEquals( "province number 2", nestedReflection.getObject(clientAddress, "addresses.2.province.provinceName"));
        Assertions.assertEquals( "city number 1", nestedReflection.getObject(clientAddress, "addresses.1.city.cityName"));
        Assertions.assertEquals( "testing melanesian reflection", nestedReflection.getObject(clientAddress, "detailAdress"));
    }

    @Test
    void testGetField() throws IllegalAccessException{
        Province province = createProvince();
        Field field = nestedReflection.getField(province.getClass(), "provinceName");
        field.setAccessible(true);
        Assertions.assertEquals("SOME PROVINCE NAME", field.get(province).toString());

    }

    @Test
    void testGetWithSimpleExpressionToUpperCase() {
        ClientAddress clientAddress = createClientAdress();
        NestedReflection nsReflection = new NestedReflection();
        nsReflection.setFactoryOfExpressions(new SimpleExpressionFilter());
        Assertions.assertEquals("PROVINCE NUMBER 2", nsReflection.getObject(clientAddress,
                "addresses.2.province.provinceName.SimpleExpressionFilter{uppercase}"));
    }

    @Test
    void testGetWithSingleParamArrayFiltering() {
        ClientAddress clientAddress = createClientAdress();
        Assertions.assertEquals("address name number 0",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName.equals('address name number 0')}.addressName"));
        Assertions.assertEquals("address name number 1",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 1'}.addressName"));
        Assertions.assertEquals("address name number 2",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 2'}.addressName"));
        Assertions.assertEquals("address name number 3",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 3'}.addressName"));
        Assertions.assertEquals("address name number 4",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 4'}.addressName"));
    }

    @Test
    void testGetWithSingleParamArrayFiltering_CannotFind() {
        ClientAddress clientAddress = createClientAdress();
        Assertions.assertNull(nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address namumber 0'}"));
    }


    public class NumberParam {
        @SuppressWarnings("unused method. This method called by reflection")
        public void stringCallback(int value, String statement) {
            System.out.println(statement+" "+value);
        }

        @SuppressWarnings("unused method. This method called by reflection")
        public String aStatement(int value, Double doubleValue, boolean isHaveCorona) {
            return "My name is Melanesian, i'am "+value+
                    " years old, my sallary is around "+doubleValue+" Rupiah "
                    +"and i "+(isHaveCorona?"already have":"dont have")+" corona";
        }
    }

    private ClientAddress createClientAdress() {
        ClientAddress clientAddress = new ClientAddress();
        List<Address> addresses = new ArrayList<>();
        for (int i=0; i<5; i++) {
            City city = new City();
            Province province = new Province();
            Address address = new Address();

            city.setCityName("city number "+i);
            city.setCityCode(""+1);

            province.setProvinceName("province number "+i);
            province.setProvinceCode(""+i);
            address.setCity(city);
            address.setProvince(province);
            address.setAddressName("address name number "+i);
            addresses.add(address);
        }
        clientAddress.setAddresses(addresses);
        clientAddress.setDetailAdress("testing melanesian reflection");
        return clientAddress;
    }

    private Province createProvince() {
        Province province = new Province();
        province.setProvinceName("SOME PROVINCE NAME");
        province.setProvinceCode("SOME PROVINCE CODE");
        return province;
    }

}