import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
        while (!input.equalsIgnoreCase(DONE)) {

            System.out.print(INVITATION_TO_ENTER_PRODUCT_CODE);
            input = scanner.nextLine().trim();
            if (!input.equalsIgnoreCase(DONE)) {
                if (!cashier.products.containsKey(input)) {
                    System.out.println(INVALID_PRODUCT_CODE + input);
                } else {
                    System.out.print(INVITATION_TO_ENTER_QUANTITY);
                    int quantity;
                    try {
                        quantity = Integer.parseUnsignedInt(scanner.nextLine());
                        shoppingCartItem.put(input, quantity);
                    } catch (Exception e) {
                        System.err.println(CANNOT_CONVERT_QUANTITY);
                        throw new IllegalArgumentException(CANNOT_CONVERT_QUANTITY);
                    }
                    System.out.printf("Product code '%s' with '%s' quantity added into the basket%n", input, quantity);
                }
            }
        }
        System.out.println(SEPARATOR);

        double totalPrice = cashier.calculateTotalPrice(shoppingCartItem);
        System.out.println(CLI_FOOTER);
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
                case FREE_RULE_TYPE:
                    FreeRule freeRule = (FreeRule) rule;
                    double freeDiscount = freeRule.calculateDiscount(productCounts, products);
                    if (freeDiscount > 0) {
                        System.out.println(DISCOUNT_CAPTION + freeRule.getType());
                        totalDiscount += freeDiscount;
                    }
                    totalPrice -= freeDiscount;
                    break;
                case REDUCED_RULE_TYPE:
                    ReducedPriceRule reducedPriceRule = (ReducedPriceRule) rule;
                    double reducedPriceDiscount = reducedPriceRule.calculateDiscount(productCounts, products);
                    if (reducedPriceDiscount > 0) {
                        System.out.println(DISCOUNT_CAPTION + reducedPriceRule.getType());
                        totalDiscount += reducedPriceDiscount;
                    }
                    totalPrice -= reducedPriceDiscount;
                    break;
                case FRACTION_RULE_TYPE:
                    FractionPriceRule fractionPriceRule = (FractionPriceRule) rule;
                    double fractionDiscount = fractionPriceRule.calculateDiscount(productCounts, products);
                    if (fractionDiscount > 0) {
                        System.out.println(DISCOUNT_CAPTION + fractionPriceRule.getType());
                        totalDiscount += fractionDiscount;
                    }
                    totalPrice -= fractionDiscount;
                    break;
                default:
                    System.out.println(INVALID_RULE_TYPE);
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
        productDataValidation(data);
    }

    private void productDataValidation(Map<String, Map<String, Object>> data) {
        for (Map.Entry<String, Map<String, Object>> product : data.entrySet()) {
            String productCode = product.getKey();
            String productName = productNameValidation(product);
            productPriceValidation(product, productCode, productName);
        }
    }

    private void productPriceValidation(Map.Entry<String, Map<String, Object>> product, String productCode, String productName) {
        try {
            double productPrice = (double) product.getValue().get(PRODUCT_PRICE_NAME);
            if (productPrice <= 0) { //TODO: not sure regarding price=0.0, maybe this is a valid case, something like a free price
                System.out.printf("Found that price value = '%s' into product.yml across to %s product code is setup " +
                        "to incorrect value%n%n", productPrice, product.getKey());
                System.err.printf("Please update price = '%s' across to '%s' product code on correct " +
                        "value in product.yml file and restart a application%n", productPrice, product.getKey());
                throw new IllegalArgumentException(INVALID_PRICE);
            }
            products.put(productCode, new Product(productCode, productName, productPrice));
        } catch (Exception e) {
            System.out.println("Found that price value into product.yml cannot been parsed to double");
            System.err.println("Please update price in product.yml and restart a application");
            throw new IllegalArgumentException(INVALID_PRICE_PARSING);
        }
    }

    private String productNameValidation(Map.Entry<String, Map<String, Object>> product) {
        String productName = (String) product.getValue().get(PRODUCT_NAME);
        if (!productName.isEmpty()) {
            return (String) product.getValue().get(PRODUCT_NAME);
        } else {
            System.out.println("Found that product name value is empty");
            System.err.printf("Please fill price with not empty value across to '%s' product code" +
                    " in product.yml file and restart a application%n", product.getKey());
            throw new IllegalArgumentException(INVALID_PRICE);
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
                case FREE_RULE_TYPE:
                    int numToBuy = (int) rule.getValue().get(QUANTITY_FOR_DISCOUNT);
                    int numFreeForFreeRule = (int) rule.getValue().get(FREE_QUANTITY);
                    discountRules.put(type, new FreeRule(numToBuy, numFreeForFreeRule));
                    break;
                case REDUCED_RULE_TYPE:
                    int numToBuyForReducedPrice = (int) rule.getValue().get(QUANTITY_FOR_DISCOUNT);
                    double newPrice = (double) rule.getValue().get(NEW_PRICE);
                    discountRules.put(type, new ReducedPriceRule(numToBuyForReducedPrice, newPrice));
                    break;
                case FRACTION_RULE_TYPE:
                    int numToBuyForFraction = (int) rule.getValue().get(QUANTITY_FOR_DISCOUNT);
                    double percentage = (double) rule.getValue().get(RULE_FRACTION);
                    discountRules.put(type, new FractionPriceRule(numToBuyForFraction, percentage));
                    break;
                default:
                    System.out.println("Invalid discount rule type");
                    throw new IllegalArgumentException(String.format("Invalid discount rule type: %s, " +
                            "please provide a correct rule type", type));
            }
        }
    }
}
