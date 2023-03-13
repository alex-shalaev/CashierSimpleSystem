import helper.ProductEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rules.Product;
import rules.discount.FreeRule;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FreeRuleTest {

        @DisplayName("Calculate discount with Free Product Rule")
        @ParameterizedTest(name = "Buy {0} products, get {1} products free")
        @CsvSource({
                "1, 2, 1, 0.00",
                "2, 2, 1, 2.50",
                "3, 1, 1, 7.50",
                "4, 4, 1, 2.50",
                "5, 2, 2, 0.00",
                "3, 2, 2, 2.50",
                "4, 2, 2, 5.00"
        })
        void calculateDiscount(int quantity, int numToBuy, int numFree, BigDecimal expectedDiscount) {
            Map<String, Integer> productQuantity = new HashMap<>();
            productQuantity.put(ProductEnum.GREEN_TEA_CODE.getValue(), quantity);
            Product product = new Product(ProductEnum.GREEN_TEA_CODE.getValue(), "Green Tea", new Double("2.50"));
            Map<String, Product> products = new HashMap<>();
            products.put(ProductEnum.GREEN_TEA_CODE.getValue(), product);
            FreeRule rule = new FreeRule(numToBuy, numFree);

            double actualDiscount = rule.calculateDiscount(productQuantity, products);

            assertEquals(expectedDiscount.doubleValue(), actualDiscount, 0.001);
        }
}

