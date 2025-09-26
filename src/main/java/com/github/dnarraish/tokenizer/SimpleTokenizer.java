package com.github.dnarraish.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTokenizer {

    private List<Map<String, Object>> tokenSequence;
    private String token;
    private int pos;
    private int max;

    private boolean isTimeToGetField;
    private boolean isTimeToGetFieldValue;

    private boolean isOpenToken;
    private boolean isFieldSplitter;
    private boolean isEqualTo;

    public SimpleTokenizer(String token) {
        this.tokenSequence = new ArrayList<>();
        this.token = token;
        this.pos = 0;
        this.max = token.length();
    }

    public SimpleTokenizer doGenerateTokenSequence() {
        char[] configurationToken = token.toCharArray();
        StringBuilder field = new StringBuilder();
        StringBuilder value = new StringBuilder();
        Map<String, Object> temporaryMap = new HashMap<>();
        while (pos < max) {
            isOpenToken = configurationToken[pos] == '[';
            isFieldSplitter = configurationToken[pos] == ',';
            isEqualTo = configurationToken[pos] == ':';
            boolean isCloseConfiguration = configurationToken[pos] == ']';
            doCheckOpenConfiguration();
            doCheckEqualTo();

            if (isFieldSplitter || isCloseConfiguration) {
                temporaryMap.put(field.toString().trim(), value.toString().trim());
                field.setLength(0);
                value.setLength(0);
            }
            if (isCloseConfiguration) {
                this.tokenSequence.add(temporaryMap);
                temporaryMap = new HashMap<>();
                isTimeToGetField = false;
                isTimeToGetFieldValue = false;
            }
            if (isTimeToGetField && isNotTokenSyntax(configurationToken[pos]))
                field.append(configurationToken[pos]);
            if (isTimeToGetFieldValue && isNotTokenSyntax(configurationToken[pos]))
                value.append(configurationToken[pos]);
            pos++;
        }
        return this;
    }

    private void doCheckOpenConfiguration() {
        if (isOpenToken || isFieldSplitter) {
            isTimeToGetField = true;
            isTimeToGetFieldValue = false;
        }
    }

    private void doCheckEqualTo() {
        if (isEqualTo) {
            isTimeToGetFieldValue = true;
            isTimeToGetField = false;
        }
    }

    private boolean isNotTokenSyntax(char token) {
        return token != '[' && token != ']' && token != ',' && token !=':';
    }

    public List<Map<String, Object>> getTokenSequence() {
        return this.tokenSequence;
    }

    @Override
    public String toString() {
        return token;
    }
}
