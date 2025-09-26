package com.github.dnarraish.tokenizer.exception;

public class InternalParseException extends RuntimeException {
    public InternalParseException(SpelParseException cause) {
        super(cause);
    }

    @Override
    public synchronized SpelParseException getCause() {
        return (SpelParseException)super.getCause();
    }
}
