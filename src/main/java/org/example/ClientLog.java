package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClientLog {
    private List<Employee> list = new ArrayList<>();

    public void log(int productNum, int amount) {
        Employee employee = new Employee(productNum, amount);
        list.add(employee);
    }

    public void exportAsCSV(File file) {
        if (file.exists()) {
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                String[] strings = new String[2];
                for (Employee element : list) {
                    strings = element.toString().split(",");
                    csvWriter.writeNext(strings);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try (Writer writer = new FileWriter(file)) {
                System.out.println(list);
                StatefulBeanToCsv<Employee> sbtc =
                        new StatefulBeanToCsvBuilder<Employee>(writer)
                                .withMappingStrategy(strategy())
                                .build();
                sbtc.write(list);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ColumnPositionMappingStrategy<Employee> strategy() {
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping("productNum", "amount");

        return strategy;
    }
}
