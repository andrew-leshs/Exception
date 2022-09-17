package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    public static int[] prices = {50, 100, 250};
    public static String[] products = {"Bread", "Milk", "Cheese"};

    public static void main(String[] args) {

        ClientLog clientLog = new ClientLog();
        Scanner scanner = new Scanner(System.in);

        int[] amount = new int[products.length];


        System.out.println("List of possible items to buy: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " rub/item");}
        int productNumber = 0;
        int productCount = 0;
        int[] oldProductsNumber;
        int[] oldProductsCount;
        int sum = 0;

        Basket basket = new Basket(prices, products);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        if (new File("basket.json").exists()) {
            try {
                Object obj = parser.parse(new FileReader("basket.json"));
                jsonObject = (JSONObject) obj;

                JSONArray jsonArray = (JSONArray) jsonObject.get("array");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    Long productNum = (Long) object.get("productNumber");
                    String productAmount = (String) object.get("amount");
                    basket.addToCart(productNum, Long.parseLong(productAmount));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



        while (true) {
            System.out.println("\n" + "Enter the product number and quantity separated by a space. To checkout, enter \"end\".");
            String input = scanner.nextLine();
            if (input.equals("") || input.equals(" ")) {
                System.out.println("Attention! You only need to enter numbers. You entered a space or left a blank line.");
                continue;
            }

            if (input.equals("end")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("\n" +
                        "Attention! You only need to enter two numeric values! You have entered: " + input);
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
                clientLog.log(productNumber + 1, productCount);
                amount[productNumber] += productCount;
                int sumProducts = productCount * prices[productNumber];
                sum += sumProducts;
            }
        }

        clientLog.exportAsCSV(new File("log.csv"));


        JSONObject rootObject = new JSONObject();
        System.out.println(basket.getBasketArray());
        rootObject.put("name", "basket");
        JSONArray jsonArray = new JSONArray();
        for (String element : basket.getBasketArray()) {
            element = element.substring(0, element.length() - 1);
            JSONObject elementObject = new JSONObject();
            String[] elements = element.split(" ");
            elementObject.put("name", elements[0]);
            for (int i = 0; i < Main.products.length; i++) {
                if (Main.products[i].equals(elements[0])) {
                    elementObject.put("productNumber", i);
                }
            }
            elementObject.put("amount", elements[1]);
            elementObject.put("price", elements[3]);
            elementObject.put("sum", elements[5]);
            jsonArray.add(elementObject);
        }
        rootObject.put("array", jsonArray);
        System.out.println();

        try (FileWriter fileWriter = new FileWriter("basket.json")) {
            fileWriter.write(rootObject.toJSONString());
            fileWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Your basket: ");
        basket.printCart();
    }
}
