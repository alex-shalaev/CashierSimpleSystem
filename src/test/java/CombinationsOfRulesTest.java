import helper.ProductEnum;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static helper.StringValueHelper.DELTA;
import static helper.StringValueHelper.ICON_TEST_ANSI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CombinationsOfRulesTest {
    private CashierSystem cashierInstance;

    @BeforeEach
    public void setUp() {
        cashierInstance = new CashierSystem();
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate when quantity is negative")
    @Description("Validate when quantity is negative")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void validateWhenQuantityIsNegativeTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), -2);

        assertThrows(IllegalArgumentException.class,
                () -> cashierInstance.calculateTotalPrice(cart));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate when quantity is zero")
    @Description("Validate when quantity is zero")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void validateWhenQuantityEqualZeroTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 0);

        assertThrows(IllegalArgumentException.class,
                () -> cashierInstance.calculateTotalPrice(cart));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate when product code is not contains into rule file")
    @Description("Validate when product code is not contains into rule file")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithInvalidProductTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.INVALID_CODE.getValue(), 1);

        assertThrows(IllegalArgumentException.class,
                () -> cashierInstance.calculateTotalPrice(cart));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate only one Free Rule")
    @Description("Validate only one Free Rule")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithFreeRuleTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 3);

        double totalPrice = cashierInstance.calculateTotalPrice(cart);
        assertEquals(6.22, totalPrice, DELTA);
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate when one Free Rule is not applicable")
    @Description("Validate when one Free Rule is not applicable")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithFreeRuleNotApplicableTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 1);

        double totalPrice = cashierInstance.calculateTotalPrice(cart);
        assertEquals(3.11, totalPrice, DELTA);
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate when one Reduced Price Rule is not applicable")
    @Description("Validate when one Reduced Price Rule is not applicable")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithReducedPriceRuleTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 3);

        double totalPrice = cashierInstance.calculateTotalPrice(cart);
        assertEquals(13.5, totalPrice, DELTA);
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Validate when one Fraction Rule is not applicable")
    @Description("Validate when one Fraction Rule is not applicable")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithFractionPriceRuleTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 6);

        double totalPrice = cashierInstance.calculateTotalPrice(cart);
        assertEquals(33.69, totalPrice, DELTA);
    }

    @ParameterizedTest
    @CsvSource({"1,1,1,19.34",
            "2,1,1,19.34",
            "1,3,1,27.84",
            "1,2,3,29.95",
            "2,3,2,39.07",
            "1,3,3,33.455",
            "2,1,3,24.955",
            "999,1,1,1571.23",
            "1,999,1,4509.83",
            "1,1,999,5617.49",
            "999,999,999,11659.88",
    })
    @DisplayName(ICON_TEST_ANSI + "Parameterized mixed of applicable rules")
    @Description("Parameterized mixed of applicable rules")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithMixedAppliedRulesTest(int greenTea, int strawberries,
                                                             int coffee, double expectedTotalPrice) {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), greenTea);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), strawberries);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), coffee);

        double totalPrice = cashierInstance.calculateTotalPrice(cart);
        assertEquals(expectedTotalPrice, totalPrice, DELTA);
    }
}
