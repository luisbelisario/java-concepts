package modernStrings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Program {

    public static void main(String[] args) {
        String recipe = generateRecipe("MERCADO DO JOÃO", "Arroz", 5.99, 3);
        System.out.println(recipe);
    }

    private static String generateRecipe(String marketName, String productName, double price, int quantity) {

        double total = price * quantity;

        return """
                %s
                %s
                %s x %s = $%s
                TOTAL: $%s
                %s
                %s
                """.formatted(marketName,
                "-".repeat(30),
                String.valueOf(quantity),
                productName,
                getDolarFormat(price),
                getDolarFormat(total),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                "Obrigado pela preferência!");
    }

    private static String getDolarFormat(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
