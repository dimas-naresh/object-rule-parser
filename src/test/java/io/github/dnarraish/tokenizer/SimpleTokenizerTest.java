package io.github.dnarraish.tokenizer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTokenizerTest {
    @Test
    void testEmptyString() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("");
        tokenizer.doGenerateTokenSequence();
        assertTrue(tokenizer.getTokenSequence().isEmpty(), "Empty string should return empty sequence");
    }

    @Test
    void testSingleField() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("[key:value]");
        tokenizer.doGenerateTokenSequence();
        List<Map<String, Object>> result = tokenizer.getTokenSequence();
        assertEquals(1, result.size());
        assertEquals("value", result.get(0).get("key"));
    }

    @Test
    void testMultipleFieldsInOneConfiguration() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("[a:1,b:2,c:3]");
        tokenizer.doGenerateTokenSequence();
        List<Map<String, Object>> result = tokenizer.getTokenSequence();
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).get("a"));
        assertEquals("2", result.get(0).get("b"));
        assertEquals("3", result.get(0).get("c"));
    }

    @Test
    void testMultipleConfigurations() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("[x:100,y:200][z:300]");
        tokenizer.doGenerateTokenSequence();
        List<Map<String, Object>> result = tokenizer.getTokenSequence();
        assertEquals(2, result.size());
        assertEquals("100", result.get(0).get("x"));
        assertEquals("200", result.get(0).get("y"));
        assertEquals("300", result.get(1).get("z"));
    }

    @Test
    void testFieldsWithSpaces() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("[ name : Alice , age : 30 ]");
        tokenizer.doGenerateTokenSequence();
        List<Map<String, Object>> result = tokenizer.getTokenSequence();
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).get("name"));
        assertEquals("30", result.get(0).get("age"));
    }

    @Test
    void testToStringReturnsOriginalToken() {
        String input = "[foo:bar]";
        SimpleTokenizer tokenizer = new SimpleTokenizer(input);
        assertEquals(input, tokenizer.toString());
    }

    @Test
    void testComplexNestedLikeStringButFlatParsing() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("[data:1:2:3,value:x,y,z]");
        tokenizer.doGenerateTokenSequence();
        List<Map<String, Object>> result = tokenizer.getTokenSequence();
        assertEquals(1, result.size());
        assertEquals("123", result.get(0).get("data"));
        assertEquals("x", result.get(0).get("value"));
    }

    @Test
    void testEmptyConfigurationBrackets() {
        SimpleTokenizer tokenizer = new SimpleTokenizer("[]");
        tokenizer.doGenerateTokenSequence();
        List<Map<String, Object>> result = tokenizer.getTokenSequence();
        assertEquals(1, result.size());
        assertFalse(result.get(0).isEmpty());
    }
}