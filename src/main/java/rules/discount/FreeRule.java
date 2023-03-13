package rules.discount;

import helper.ProductEnum;
import helper.StringValueHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rules.DiscountRule;
import rules.Product;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class FreeRule implements DiscountRule {
    private final int numToBuy;
    private final int numFree;

    public String getType() {
        return StringValueHelper.FREE_RULE_TYPE;
    }

    /**
     * Method apply discount: Buy N get N free
     * @param productQuantity product quantity
     * @param products products map
     * @return discount value
     */
    public double calculateDiscount(Map<String, Integer> productQuantity,
                                    Map<String, Product> products) {
        double discount = 0.0;
        for (Map.Entry<String, Integer> productItem : productQuantity.entrySet()) {
            String code = productItem.getKey();
            if (code.equals(ProductEnum.GREEN_TEA_CODE.getValue())) {
                int quantity = productItem.getValue();
                if (quantity >= numToBuy) {
                    int freeCount = quantity / numToBuy * numFree;
                    discount += freeCount * products.get(code).getPrice();
                }
            }
        }
        return discount;
    }
}
