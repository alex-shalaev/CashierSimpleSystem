import helper.ProductEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TestExample {
    private CashierSystem cashier;

    public static final double DELTA = 0.01;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        cashier = new CashierSystem();
    }

    @Test
    public void testCalculateTotalPriceWithNoDiscounts() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 1);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 1);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 1);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(19.34, totalPrice, DELTA);
    }

    @Test
    public void testCalculateTotalPrice() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 2);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 3);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 3);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(33.455, totalPrice, DELTA);
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

    @Test
    public void testCalculateTotalPriceWithMixedRules() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 2);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 2);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 3);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(29.95, totalPrice, DELTA);
    }

    @Test
    public void testCalculateTotalPriceWithMixedRules2() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 3);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 2);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 3);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(33.06, totalPrice, DELTA);
    }

    @Test
    public void testCalculateTotalPriceWithMixedRules3() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 2);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 3);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 3);

        double totalPrice = cashier.calculateTotalPrice(cart);
        Assertions.assertEquals(33.45, totalPrice, DELTA);
    }
}
