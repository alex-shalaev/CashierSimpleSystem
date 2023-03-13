import helper.ProductEnum;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rules.Product;
import rules.discount.ReducedPriceRule;

import java.util.HashMap;
import java.util.Map;

import static helper.StringValueHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReducedPriceRuleTest {

    @ParameterizedTest(name = "{index} => numToBuy={0}, newPrice={1}, quantity={2}, expectedDiscount={3}")
    @CsvSource({
            "2, 2.0, 1, 0.0",
            "2, 2.0, 2, 5.0",
            "2, 2.0, 3, 7.5",
            "3, 2.5, 2, 0.0",
            "3, 2.5, 3, 6.0",
            "3, 2.5, 4, 8.0"
    })
    @DisplayName(ICON_TEST_ANSI  + "Unit Test: calculation free rule discount")
    @Description("Unit Test: calculation free rule reduced discount")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    void calculateReducedPriceDiscountTest(int numToBuy, double newPrice, int quantity, double expectedDiscount) {
        ReducedPriceRule rule = new ReducedPriceRule(numToBuy, newPrice);
        Map<String, Integer> productQuantity = new HashMap<>();
        productQuantity.put(ProductEnum.STRAWBERRIES_CODE.getValue(), quantity);
        Map<String, Product> products = new HashMap<>();
        products.put(ProductEnum.STRAWBERRIES_CODE.getValue(),
                new Product(ProductEnum.STRAWBERRIES_CODE.getValue(), STRAWBERRIES, 4.5));
        double discount = rule.calculateDiscount(productQuantity, products);
        assertEquals(expectedDiscount, discount, DELTA);
    }
}
