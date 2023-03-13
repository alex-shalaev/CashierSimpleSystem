import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import helper.StringValueHelper;
import org.yaml.snakeyaml.Yaml;
import rules.DiscountRule;
import rules.Product;
import rules.discount.FractionPriceRule;
import rules.discount.FreeRule;
import rules.discount.ReducedPriceRule;

import static helper.StringValueHelper.*;

public class CashierSystem {

    private Map<String, Product> products;
    private Map<String, DiscountRule> discountRules;

    public CashierSystem() throws FileNotFoundException {
        loadProducts();
        loadDiscountRules();
    }

    public static void main(String[] args) throws FileNotFoundException {
        CashierSystem cashier = new CashierSystem();
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> shoppingCartItem = new HashMap<>();

        System.out.println(CLI_HEADER);
        String input = EMPTY_STRING;
        while (!input.equalsIgnoreCase(StringValueHelper.DONE)) {

            System.out.print(INVITATION_TO_ENTER_PRODUCT_CODE);
            input = scanner.nextLine().trim();
            if (!input.equalsIgnoreCase(StringValueHelper.DONE)) {
                if (!cashier.products.containsKey(input)) {
                    System.out.println(INVALID_PRODUCT_CODE + input);
                } else {
                    System.out.print(INVITATION_TO_ENTER_QUANTITY);
                    int quantity;
                    try {
                        quantity = Integer.parseInt(scanner.nextLine());
                        shoppingCartItem.put(input, quantity);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(CANNOT_CONVERT_QUANTITY);
                    }
                    System.out.printf("Product code '%s' with '%s' quantity added into the basket%n", input, quantity);
                }
            }
        }
        System.out.println(SEPARATOR);

        double totalPrice = cashier.calculateTotalPrice(shoppingCartItem);
        System.out.println(TOTAL_BILL);
        System.out.println("Final price: £" + String.format("%.2f", totalPrice));
    }

    public double calculateTotalPrice(Map<String, Integer> cart) {
        double totalPrice = 0.0;
        double totalDiscount = 0.0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            if (entry.getValue() < 0 || entry.getValue() == 0) {
                throw new IllegalArgumentException(CANNOT_CONVERT_PRODUCT_CODE);
            }
        }

        Map<String, Integer> productCounts = new HashMap<>();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String code = entry.getKey();
            int count = entry.getValue();
            if (!products.containsKey(code)) {
                throw new IllegalArgumentException(INVALID_PRODUCT_CODE + code);
            }
            totalPrice += products.get(code).getPrice() * count;
            System.out.printf("%s: %s%.2f%n", PRICE_WITHOUT_DISCOUNT_CAPTION, POUND_CURRENCY, totalPrice);
            productCounts.put(code, count);
        }

        for (DiscountRule rule : discountRules.values()) {
            switch (rule.getType()) {
                case StringValueHelper.FREE_RULE_TYPE:
                    FreeRule freeRule = (FreeRule) rule;
                    double freeDiscount = freeRule.calculateDiscount(productCounts, products);
                    if (freeDiscount > 0) {
                        System.out.println(DISCOUNT_CAPTION + freeRule.getType());
                        totalDiscount += freeDiscount;
                    }
                    totalPrice -= freeDiscount;
                    break;
                case StringValueHelper.REDUCED_RULE_TYPE:
                    ReducedPriceRule reducedPriceRule = (ReducedPriceRule) rule;
                    double reducedPriceDiscount = reducedPriceRule.calculateDiscount(productCounts, products);
                    if (reducedPriceDiscount > 0) {
                        System.out.println(DISCOUNT_CAPTION + reducedPriceRule.getType());
                        totalDiscount += reducedPriceDiscount;
                    }
                    totalPrice -= reducedPriceDiscount;
                    break;
                case StringValueHelper.FRACTION_RULE_TYPE:
                    FractionPriceRule fractionPriceRule = (FractionPriceRule) rule;
                    double fractionDiscount = fractionPriceRule.calculateDiscount(productCounts, products);
                    if (fractionDiscount > 0) {
                        System.out.println(DISCOUNT_CAPTION + fractionPriceRule.getType());
                        totalDiscount += fractionDiscount;
                    }
                    totalPrice -= fractionDiscount;
                    break;
                default:
                    throw new IllegalArgumentException(INVALID_RULE_TYPE + rule.getType());
            }
        }

        System.out.printf("%s: %s%.2f%n", TOTAL_DISCOUNT_CAPTION, POUND_CURRENCY, totalDiscount);
        return totalPrice;
    }

    private void loadProducts() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        File file = new File(PRODUCTS_PATH); //TODO: don't forget to change loading path from .property file
        FileInputStream inputStream = new FileInputStream(file);
        Map<String, Map<String, Object>> data = yaml.load(inputStream);
        products = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> product : data.entrySet()) {
            String code = product.getKey();
            String name = (String) product.getValue().get(StringValueHelper.PRODUCT_NAME);
            double price = (double) product.getValue().get(StringValueHelper.PRODUCT_PRICE_NAME);
            products.put(code, new Product(code, name, price));
        }
    }

    private void loadDiscountRules() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        File file = new File(RULES_PATH); //TODO: don't forget to change loading path from .property file
        FileInputStream inputStream = new FileInputStream(file);
        Map<String, Map<String, Object>> data = yaml.load(inputStream);
        discountRules = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> rule : data.entrySet()) {
            String type = rule.getKey();
            switch (type) {
                case StringValueHelper.FREE_RULE_TYPE:
                    int numToBuy = (int) rule.getValue().get(StringValueHelper.QUANTITY_FOR_DISCOUNT);
                    int numFreeForFreeRule = (int) rule.getValue().get(StringValueHelper.FREE_QUANTITY);
                    discountRules.put(type, new FreeRule(numToBuy, numFreeForFreeRule));
                    break;
                case StringValueHelper.REDUCED_RULE_TYPE:
                    int numToBuyForReducedPrice = (int) rule.getValue().get(StringValueHelper.QUANTITY_FOR_DISCOUNT);
                    double newPrice = (double) rule.getValue().get(StringValueHelper.NEW_PRICE);
                    discountRules.put(type, new ReducedPriceRule(numToBuyForReducedPrice, newPrice));
                    break;
                case StringValueHelper.FRACTION_RULE_TYPE:
                    int numToBuyForFraction = (int) rule.getValue().get(StringValueHelper.QUANTITY_FOR_DISCOUNT);
                    double percentage = (double) rule.getValue().get(StringValueHelper.RULE_FRACTION);
                    discountRules.put(type, new FractionPriceRule(numToBuyForFraction, percentage));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Invalid discount rule type: %s, " +
                            "please provide a correct rule type", type));
            }
        }
    }
}
