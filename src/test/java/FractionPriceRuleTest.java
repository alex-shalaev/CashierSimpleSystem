import static helper.StringValueHelper.DELTA;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import helper.ProductEnum;
import rules.Product;
import rules.discount.FractionPriceRule;

public class FractionPriceRuleTest {

    @ParameterizedTest(name = "{index} => numToBuy={0}, fraction={1}, quantity={2}, expectedDiscount={3}")
    @MethodSource("provideTestData")
    @DisplayName("Unit tests: for Fraction Price Rule")
    void testCalculateFractionDiscountShouldReturnExpectedDiscount(int numToBuy, double fraction, int quantity, double expectedDiscount) {
        FractionPriceRule rule = new FractionPriceRule(numToBuy, fraction);
        Map<String, Integer> productQuantity = new HashMap<>();
        productQuantity.put(ProductEnum.COFFEE_CODE.getValue(), quantity);
        Product product = new Product(ProductEnum.COFFEE_CODE.getValue(), "Coffee", 5.0);
        Map<String, Product> products = Collections.singletonMap(ProductEnum.COFFEE_CODE.getValue(), product);

        double actualDiscount = rule.calculateDiscount(productQuantity, products);

        assertEquals(expectedDiscount, actualDiscount, DELTA);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(2, 50.0, 1, 0.0),
                Arguments.of(2, 50.0, 2, 5.0),
                Arguments.of(2, 50.0, 3, 7.5),
                Arguments.of(3, 33.33, 2, 0.0),
                Arguments.of(3, 33.33, 3, 5.0),
                Arguments.of(3, 33.33, 4, 6.66)
        );
    }
}

