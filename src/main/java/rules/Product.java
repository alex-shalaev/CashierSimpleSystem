package rules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private final String code;
    private final String name;
    private final double price;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }
}
