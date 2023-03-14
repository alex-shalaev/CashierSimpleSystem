import helper.ProductEnum;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static helper.StringValueHelper.ICON_TEST_ANSI;

public class PropertiesValidatorTest {

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: validation of invalid product code")
    @Description("Unit Test: validation of invalid product code")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
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
    @DisplayName(ICON_TEST_ANSI + "Unit Test: validation of quantity")
    @Description("Unit Test: validation of quantity")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    void testCannotConvertQuantity() {
        CashierSystem cashierSystem = new CashierSystem();
        Map<String, Integer> products = new HashMap<>();
        String validProductCode = ProductEnum.GREEN_TEA_CODE.getValue();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        products.put(validProductCode, -1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> cashierSystem.calculateTotalPrice(products));
    }
}
