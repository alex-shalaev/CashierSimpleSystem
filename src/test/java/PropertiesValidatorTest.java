import helper.ProductEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class PropertiesValidatorTest {

    @Test
    void testInvalidProductCode() {
        CashierSystem cashierSystem = new CashierSystem();
        Map<String, Integer> products = new HashMap<>();
        String invalidProductCode = "INVALID_PRODUCT_CODE";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        products.put(invalidProductCode, 1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cashierSystem.calculateTotalPrice(products));
    }

    @Test
    void testCannotConvertQuantity() {
        CashierSystem cashierSystem = new CashierSystem();
        Map<String, Integer> products = new HashMap<>();
        String validProductCode = ProductEnum.GREEN_TEA_CODE.getValue();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        products.put(validProductCode, -1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cashierSystem.calculateTotalPrice(products));
    }

    // TODO: don't forget to complete this tests...
}
