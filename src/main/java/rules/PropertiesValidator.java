package rules;

import java.util.Map;

import static helper.StringValueHelper.*;

public class PropertiesValidator {

    public void productDataValidation(Map<String, Map<String, Object>> data,
                                      Map<String, Product> products) {
        for (Map.Entry<String, Map<String, Object>> product : data.entrySet()) {
            String productCode = product.getKey();
            String productName = productNameValidation(product);
            productPriceValidation(product, productCode, productName, products);
        }
    }

    private void productPriceValidation(Map.Entry<String, Map<String, Object>> product,
                                       String productCode,
                                       String productName,
                                       Map<String, Product> products) {
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
}
