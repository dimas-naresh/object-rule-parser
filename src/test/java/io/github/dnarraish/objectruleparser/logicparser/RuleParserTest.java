package io.github.dnarraish.objectruleparser.logicparser;

import io.github.dnarraish.objectruleparser.param.Address;
import io.github.dnarraish.objectruleparser.param.City;
import io.github.dnarraish.objectruleparser.param.ClientAddress;
import io.github.dnarraish.objectruleparser.param.Province;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

class RuleParserTest {

    @Test
    void parseBussinessRule() throws ScriptException {
        RuleParser ruleParser = new RuleParser();
        String rules = "<addresses.0.province.provinceName> == 'province number 0'";
        Assertions.assertTrue(ruleParser.parseBussinessRule(createClientAdress(), rules));
    }

    @Test
    void parseBussinessRule_InvokingMethod() throws ScriptException {
        RuleParser ruleParser = new RuleParser();
        String rules = "<addresses.0.province.getProvinceName()> == 'province number 0'";
        Assertions.assertTrue(ruleParser.parseBussinessRule(createClientAdress(), rules));
    }

    @Test
    void containsObjectTest() throws ScriptException{
        ClientAddress clientAddress = createClientAdress();
        RuleParser ruleParser = new RuleParser();
        Assertions.assertTrue(ruleParser.parseBussinessRule(clientAddress,
                "<containAddressName('address name number 1')> && <addresses.0.province.getProvinceName()> == 'province number 0'"));
    }

    @Test
    void checkingArraysNull() throws ScriptException{
        ClientAddress clientAddress = createClientAdress();
        RuleParser ruleParser = new RuleParser();
        Assertions.assertTrue(ruleParser.parseBussinessRule(clientAddress, "<addresses.SPArrayFilter{addressName == 'address namumber 0'}> == null"));
    }

    @Test
    void testing_Invoke_Single_Boolean() throws ScriptException {
        RuleParser ruleParser = new RuleParser();
        boolean logicTest = ruleParser.parseBussinessRule(createClientAdress(), "<containAddressName('address name number 1')>");
        Assertions.assertTrue(logicTest);
    }

    @Test
    void testing_Invoke_Single_Boolean_With_2_param() throws ScriptException {
        RuleParser ruleParser = new RuleParser();
        boolean logicTest = ruleParser.parseBussinessRule(createClientAdress(), "<containAddressNameAndCityName('address name number 1','city number 1')>");
        Assertions.assertTrue(logicTest);
    }

    @Test
    void testing_Invoke_Single_Boolean_With_2_param_Test_False() throws ScriptException {
        RuleParser ruleParser = new RuleParser();
        boolean logicTest = ruleParser.parseBussinessRule(createClientAdress(), "<containAddressNameAndCityName('address name number 1','city number 2')>");
        Assertions.assertFalse(logicTest);
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
}