package rules;

import java.util.Map;

/**
 * Contract for discount rules classes
 */
public interface DiscountRule {
    double calculateDiscount(Map<String, Integer> productQuantity, Map<String, Product> products);
    String getType();
}
