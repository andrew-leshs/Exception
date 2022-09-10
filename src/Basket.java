import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Basket{

    private int[] prices;
    private String[] nameOfPrices;

    private String toWrite;
    private StringBuilder stringBuilder = new StringBuilder();
    private List<String> list = new ArrayList<>();
    private List<Integer> integerList = new ArrayList<>();
    private List<String> strings = new ArrayList<>();

    public Basket(int[] prices, String[] namesOfPrices) {
        this.prices = prices;
        this.nameOfPrices = namesOfPrices;
    }

    public void addToCart(int productNum, int amount) {
        boolean b = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(nameOfPrices[productNum])) {
                amount += integerList.get(i);
                strings.remove(i);
                b = true;
            }
        }
        if (!b) {
            list.add(nameOfPrices[productNum]);
            integerList.add(amount);
        }

        toWrite = nameOfPrices[productNum] + " " + amount + " шт " + prices[productNum] + " руб/шт "
                + (amount * prices[productNum]) + " руб в сумме." + "\n";
        strings.add(toWrite);
    }

    public void printCart() {
        for (String string : strings) {
            System.out.println(string.replace("\n", ""));
        }
    }

    public void saveTxt(File textFile) {
        try {
            if (textFile.exists()) {
                saveWithCodeClean(textFile);
            } else if (textFile.createNewFile()) {
                saveWithCodeClean(textFile);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String s;
            Basket b = new Basket(Main.prices, Main.products);
            while ((s = reader.readLine()) != null) {
                String[] splitS = s.split("\n");
                for (String split : splitS) {
                    String[] elements = split.split(" ");
                    for (int i = 0; i < Main.products.length; i++) {
                        if (Main.products[i].equals(elements[0])) {
                            b.addToCart(i, Integer.parseInt(elements[1]));
                        }
                    }
                }
            }
            return b;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getBasketArray() {
        return strings;
    }

    private void saveWithCodeClean(File file) {
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            for (String s : strings) {
                fw.append(s);
            }
            fw.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
