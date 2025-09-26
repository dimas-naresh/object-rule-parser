# object-rule-parser

**A lightweight Java library to parse and evaluate rules against objects (Spring Boot compatible).**

> _Short summary_: This repo provides utilities to build simple object-rule parsers that evaluate boolean conditions against Java objects. The project uses Gradle and is compatible with Spring Boot apps.

---

## Table of contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Build & test](#build--test)
- [Testing strategy & suggested tests to add](#testing-strategy--suggested-tests-to-add)
- [Example unit test](#example-unit-test)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- Rule parsing and evaluation against plain Java objects.
- Designed to integrate easily with Spring Boot projects.
- Focus on readability and small API surface so unit testing is straightforward.

## Prerequisites

- Java 21+ (project currently built with Java 21 compatibility; upgrade if needed).
- Gradle (wrapper included; use `./gradlew`).
- Git

## Build & test

Build the project and run tests with the Gradle wrapper:

```bash
# build and run tests
./gradlew clean build

# run only tests
./gradlew test
```
## Testing strategy & suggested tests to add

Aim for a mix of the following test types:

1. **Pure unit tests (fast, no Spring context)**
    - Parser logic: verify that given a rule string and an input object the parser returns expected boolean results.
    - Edge conditions: null inputs, empty rules, unsupported operators, malformed rule expressions — assert the correct exception or `false` result depending on intended behaviour.
    - Rule composition: `AND`, `OR`, `NOT` combinations and precedence.
    - Data type handling: numeric comparisons, strings, enums, booleans, collections.
    - Boundary values (e.g., large numbers, empty strings).

2. **Integration / slice tests (use minimal Spring features)**
    - If there are Spring `@Component` or `@Service` classes that wire the parser, use `@ExtendWith(SpringExtension.class)` and `@ContextConfiguration` or `@SpringBootTest` but keep them minimal and focused.

3. **Negative tests / contract tests**
    - Verify that invalid rules yield controlled exceptions with useful messages.

4. **Concurrency / thread-safety tests** (if parser is shared across threads)
    - Run the same parser concurrently from multiple threads and assert stability / no mutated shared state.

5. **Performance smoke tests** (optional)
    - Run the parser on larger inputs to track performance regressions (these can be gated out of unit test suite).

### Prioritized list of tests to add first

1. Parser basic evaluation (true/false cases).
2. Operator coverage (`==`, `!=`, `<`, `>`, `<=`, `>=`, `contains`, string ops).
3. Enum handling (if library reads enums via SpEL or custom mapping).
4. Null handling (null object, null fields, missing properties).
5. Error messages for malformed rules.

## Example unit test

Below is a simple unit testing example demonstrating how to unit test a parser class (`RuleParser`) — **adapt class & method names to match your codebase**.

```java
@Test
void parseBussinessRule() throws ScriptException {
    RuleParser ruleParser = new RuleParser();

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

    Assertions.assertTrue(ruleParser.parseBussinessRule(clientAddress, "<addresses.0.province.provinceName> == 'province number 0'"));
}

@Test
void containsObjectTest() throws ScriptException{
    ClientAddress clientAddress = createClientAdress();
    RuleParser ruleParser = new RuleParser();
    Assertions.assertTrue(ruleParser.parseBussinessRule(clientAddress,
            "<containAddressName('address name number 1')> && <addresses.0.province.getProvinceName()> == 'province number 0'"));
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
```

## Contributing

- Fork the repo.
- Create a feature branch for tests: `git checkout -b feat/add-unit-tests`.
- Add tests under `src/test/java` following package layout.
- Run `./gradlew test` locally and ensure all new tests pass.
- Create a PR and describe the tests and reasoning.

## License

This project is licensed under the [MIT License](./LICENSE).