package helper;

public enum ProductEnum {
    GREEN_TEA_CODE("GR1"),
    STRAWBERRIES_CODE("SR1"),
    COFFEE_CODE("CF1"),
    INVALID_CODE("XXX");

    private final String value;

    ProductEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
