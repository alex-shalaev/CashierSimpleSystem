import helper.ProductEnum;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static helper.StringValueHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class CashierSystemTest {

    private CashierSystem cashier;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        cashier = new CashierSystem();
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: Calculation of total price")
    @Description("Unit Test: Calculation of total price")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 1);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 1);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 2);
        double totalPrice = cashier.calculateTotalPrice(cart);
        assertEquals((3.11 * 1) + (5.00 * 1) + (11.23 * 2), totalPrice, DELTA);
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: Calculation of total discount")
    @Description("Unit Test: Calculation of total discount")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceWithDiscountsTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.GREEN_TEA_CODE.getValue(), 3);
        cart.put(ProductEnum.STRAWBERRIES_CODE.getValue(), 2);
        cart.put(ProductEnum.COFFEE_CODE.getValue(), 1);
        double totalPrice = cashier.calculateTotalPrice(cart);
        assertEquals(27.45, totalPrice, DELTA);
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: Validate invalid product code")
    @Description("Unit Test: Validate invalid product code")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceInvalidProductCodeTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.INVALID_CODE.getValue(), 1);
        assertThrows(IllegalArgumentException.class, () -> cashier.calculateTotalPrice(cart));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: Validate negative quantity value")
    @Description("Unit Test: Validate negative quantity value")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void calculateTotalPriceNegativeQuantityTest() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put(ProductEnum.COFFEE_CODE.getValue(), -1);
        assertThrows(IllegalArgumentException.class, () -> cashier.calculateTotalPrice(cart));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: Validate a positive scenario I/O")
    @Description("Unit Test: Unit Test: Validate a positive scenario I/O")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void mainPositiveExecutionInputOutputTest() throws FileNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream("GR1\n2\ndone\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
        CashierSystem.main(new String[0]);
        assertTrue(out.toString().contains(CLI_HEADER));
        assertTrue(out.toString().contains(INVITATION_TO_ENTER_PRODUCT_CODE));
        assertTrue(out.toString().contains(SEPARATOR));
        assertTrue(out.toString().contains(CLI_FOOTER));
        assertTrue(out.toString().contains("Price without discount: £6.22"));
        assertTrue(out.toString().contains("Was applied the following discount: FreeRule"));
        assertTrue(out.toString().contains("Total discount: £3.11"));
        assertTrue(out.toString().contains("Final price: £3.11"));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI + "Unit Test: Validate a negative scenario I/O: invalid product code")
    @Description("Unit Test: Unit Test: Validate a negative scenario I/O")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void mainInvalidProductCodeExecutionInputOutputTest() throws FileNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream("XXX\ndone\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
        CashierSystem.main(new String[0]);
        assertTrue(out.toString().contains(CLI_HEADER));
        assertTrue(out.toString().contains(INVITATION_TO_ENTER_PRODUCT_CODE));
        assertTrue(out.toString().contains(SEPARATOR));
        assertTrue(out.toString().contains(CLI_FOOTER));
        assertTrue(out.toString().contains("Enter product code or type 'done' to finish: Invalid product code: 1"));
        assertTrue(out.toString().contains("Total discount: £0.00"));
        assertTrue(out.toString().contains("Final price: £0.00"));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI  + "Unit Test: Validate a negative scenario I/O: invalid quantity parsing")
    @Description("Unit Test: Unit Test: Validate a negative scenario I/O")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void mainInvalidParseQuantityExecutionInputOutputTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("GR1\n-2\ndone\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        assertThrows(IllegalArgumentException.class,
                () -> CashierSystem.main(new String[0]));
    }

    @Test
    @DisplayName(ICON_TEST_ANSI  + "Unit Test: Validate a negative scenario I/O: invalid float quantity parsing")
    @Description("Unit Test: Unit Test: Validate a negative scenario I/O")
    @Severity(SeverityLevel.MINOR)
    @Owner("Alex Sh.")
    @Issue("TEST-#")
    public void mainInvalidParseFloatQuantityExecutionInputOutputTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("GR1\n-1.5\ndone\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

        assertThrows(IllegalArgumentException.class,
                () -> CashierSystem.main(new String[0]));
    }
}

