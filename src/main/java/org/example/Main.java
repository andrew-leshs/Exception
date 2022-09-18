package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int[] prices = {50, 100, 250};
    public static String[] products = {"Bread", "Milk", "Cheese"};

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

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

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        int count = 0;
        List<String> loadList = new ArrayList<>();
        List<String> saveList = new ArrayList<>();
        List<String> logList = new ArrayList<>();


        for (int i = 0; i < nodeList.getLength(); i++) {
            if (i == 1) {
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node book = nodeList.item(j);
                    if (book.getNodeType() != Node.TEXT_NODE) {
                        NodeList bookProps = book.getChildNodes();
                        if (book.getNodeName().equals("load")) {
                            for(int o = 0; o < bookProps.getLength(); o++) {
                                Node bookProp = bookProps.item(o);
                                if (bookProp.getNodeType() != Node.TEXT_NODE) {
                                    loadList.add(bookProp.getChildNodes().item(0).getTextContent());
                                }
                            }
                        }
                        else if (book.getNodeName().equals("save")) {
                            for(int o = 0; o < bookProps.getLength(); o++) {
                                Node bookProp = bookProps.item(o);
                                if (bookProp.getNodeType() != Node.TEXT_NODE) {
                                    saveList.add(bookProp.getChildNodes().item(0).getTextContent());
                                }
                            }
                        }
                        else if (book.getNodeName().equals("log")) {
                            for(int o = 0; o < bookProps.getLength(); o++) {
                                Node bookProp = bookProps.item(o);
                                if (bookProp.getNodeType() != Node.TEXT_NODE) {
                                    logList.add(bookProp.getChildNodes().item(0).getTextContent());
                                }
                            }
                        }

                    }
                }
            }
        }

        String fileName = "";
        for (int i = 0; i < loadList.size(); i++) {
            if (i == 0) {
                if (loadList.get(i).equals("true")) {

                } else {
                    break;
                }
            } else if (i == 1) {
                fileName = loadList.get(i);
            } else {
                fileName = fileName + "." + loadList.get(i);
            }
        }

        String saveFile = "";
        for (int i = 0; i < saveList.size(); i++) {
            if (i == 0) {
                if (saveList.get(i).equals("true")) {

                } else {
                    break;
                }
            } else if (i == 1) {
                saveFile = saveList.get(i);
            } else {
                saveFile = saveFile + "." + saveList.get(i);
            }
        }
        String log = "";
        for (int i = 0; i < logList.size(); i++) {
            if (i == 0) {
                if (logList.get(i).equals("true")) {

                } else {
                    break;
                }
            } else if (i == 1) {
                log = logList.get(i);
            } else {
                log = log + "." + logList.get(i);
            }
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        if (new File(fileName).exists()) {
            try {
                Object obj = parser.parse(new FileReader(fileName));
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
                if (logList.get(0).equals("true")) {
                    clientLog.log(productNumber + 1, productCount);
                }
                amount[productNumber] += productCount;
                int sumProducts = productCount * prices[productNumber];
                sum += sumProducts;
            }
        }

        if (logList.get(0).equals("true")) {
            clientLog.exportAsCSV(new File(fileName));
        }

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

        if (!saveList.get(0).equals("false")) {
            if (saveList.get(2).equals("json")) {
                try (FileWriter fileWriter = new FileWriter(saveFile)) {
                    fileWriter.write(rootObject.toJSONString());
                    fileWriter.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                basket.saveTxt(new File(saveFile));
            }
        }

        System.out.println("Your basket: ");
        basket.printCart();
    }
}
