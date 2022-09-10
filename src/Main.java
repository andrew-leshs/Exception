import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int[] prices = {50, 100, 250};
    public static String[] products = {"Хлеб", "Молоко", "Сыр"};
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int[] amount = new int[products.length];

        System.out.println("Список возможных товаров для покупки: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");}
        int productNumber = 0;
        int productCount = 0;
        int[] oldProductsNumber;
        int[] oldProductsCount;
        int sum = 0;

        Basket basket = new Basket(prices, products);
        basket = Basket.loadFromTxtFile(new File("basket.txt"));





        while (true) {
            System.out.println("\n" + "Введите номер продукта и его количество через пробел. Для оформления корзины введите \"end\".");
            String input = scanner.nextLine();
            if (input.equals("") || input.equals(" ")) {
                System.out.println("Внимание! Вводить необходимо только числа. Вы же ввели пробел или оставили пустую строку.");
                continue;
            }

            if (input.equals("end")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Внимание! Вводить необходимо только два числовых значения! Вы же ввели: " + input);
                continue;
            }

            try {
                productNumber = Integer.parseInt(parts[0]) - 1;
                productCount = Integer.parseInt(parts[1]);

            } catch (NumberFormatException e) {
                System.out.println("Внимание! Вводить необходимо только числа. Вы же ввели: " + input);
                continue;
            }

            if ((productNumber + 1) > (products.length)) {
                System.out.println("Внимание! \"НОМЕР ПРОДУКТА\" в списке товаров не найден.");
                continue;
            }

            if ((productNumber + 1) <= 0) {
                System.out.println("Внимание! \"НОМЕР ПРОДУКТА\" может быть только положительным числом. Вы же ввели: " + (productNumber + 1));
                continue;
            }

            if (productCount <= 0) {
                System.out.println("Внимание! \"КОЛИЧЕСТВО ПРОДУКТА\" может быть только положительным числом. Вы же ввели: " + productCount);
                continue;
            }

            if (productNumber < products.length) {
                basket.addToCart(productNumber, productCount);
                amount[productNumber] += productCount;
                int sumProducts = productCount * prices[productNumber];
                sum += sumProducts;
                System.out.println(basket.getBasketArray());
            }
        }

        basket.saveTxt(new File("basket.txt"));
        System.out.println("Ваша корзина: ");
        basket.printCart();
    }
}