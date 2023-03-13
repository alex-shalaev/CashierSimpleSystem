package rules;

import java.util.Map;

public interface DiscountRule {
    double calculateDiscount(Map<String, Integer> productQuantity, Map<String, Product> products);
    String getType();
}
