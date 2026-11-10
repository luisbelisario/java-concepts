package optionalStream;

import java.util.List;
import java.util.Optional;

public class Program {

    public static void main(String[] args) {

        List<String> products = List.of("Laptop", "Smartphone", "Tablet", "Headphones", "Smartwatch");

        String msgSuccess = findProduct(products, "Tablet")
                .map(product -> "Product found: " + product)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        System.out.println(msgSuccess);

        String msgFailure = findProduct(products, "Camera")
                .map(product -> "Product found: " + product)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        System.out.println(msgFailure);

        List<String> productsWithMoreThanFourLetters = getProductsWithMoreThanFourLetters(products);
        for (String product : productsWithMoreThanFourLetters) {
            System.out.println(product);
        }
    }


    private static Optional<String> findProduct(List<String> products, String productName) {
        return products.stream()
                .filter(product -> product.equalsIgnoreCase(productName))
                .findFirst();
    }

    private static List<String> getProductsWithMoreThanFourLetters(List<String> products) {
        return products.stream()
                .filter(product -> product.length() > 6)
                .toList();
    }
}
