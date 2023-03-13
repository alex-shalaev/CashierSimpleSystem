import helper.ProductEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static helper.StringValueHelper.DELTA;

public class TestExample {
    private CashierSystem cashier;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        cashier = new CashierSystem();
    }

    @Test
    public void testOnlyOneRule() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 2);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(3.11, totalPrice, DELTA);
    }

    @Test
    public void testOnlyOneRuleNegative() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), -2);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cashier.calculateTotalPrice(cart));
    }

    @Test
    public void testOnlyOneRuleZero() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 0);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cashier.calculateTotalPrice(cart));
    }

    @Test
    public void testOnlyOneRuleNotApplicable() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 1);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(3.11, totalPrice, DELTA);
    }

    @Test
    public void testOnlyOneRuleNotApplicable3() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 20);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(31.10, totalPrice, DELTA);
    }

    @Test
    public void testOnlyOneRule2() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 3);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(6.22, totalPrice, DELTA);
    }

    @Test
    public void testCalculateTotalPriceWithInvalidProduct() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.INVALID_CODE.getValue(), 1);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cashier.calculateTotalPrice(cart));
    }

    @Test
    public void testCalculateTotalPriceWithReducedPriceRule() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 3);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(13.5, totalPrice, DELTA);
    }

    @Test
    public void testCalculateTotalPriceWithFractionPriceRule() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 6);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(33.69, totalPrice, DELTA);
    }

    @ParameterizedTest
    @CsvSource({"1,1,1,19.34", "2,1,1,19.34", "1,3,1,27.84", "1,2,3,29.95",
    "2,3,2,39.07", "1,3,3,33.455", "2,1,3,24.955"})
    public void testCalculateTotalPriceWithNoDiscounts(int greenTea, int strawberries,
                                                       int coffee, double expectedTotalPrice) {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), greenTea);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), strawberries);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), coffee);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(expectedTotalPrice, totalPrice, DELTA);
    }
}
