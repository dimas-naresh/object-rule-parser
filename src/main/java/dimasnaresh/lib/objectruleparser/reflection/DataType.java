package dimasnaresh.lib.objectruleparser.reflection;

public enum  DataType {
    INTEGER("Integer"),
    STRING("String"),
    DOUBLE("Double"),
    BOOLEAN("Boolean"),
    LONG("Long");

    private final char[] typeChars;
    private final boolean hasPayload;

    private DataType(String type) {
        this.typeChars = type.toCharArray();
        this.hasPayload = this.typeChars.length == 0;
    }

    private DataType() {
        this("");
    }

    @Override
    public String toString() {
        return this.name() + (this.typeChars.length != 0 ? "(" + new String(this.typeChars) + ")" : "");
    }

    public boolean hasPayload() {
        return this.hasPayload;
    }

    public int getLength() {
        return this.typeChars.length;
    }
}
