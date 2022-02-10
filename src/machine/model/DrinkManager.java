/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.model;

import machine.entity.Drink;
import machine.exception.InternalException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DrinkManager {

    public static final DrinkManager INSTANCE = new DrinkManager();
    
    private final String PERSIT_FILE = "drinks.txt";
    private final Map<Integer, Drink> allDrinks = new LinkedHashMap<>();

    private DrinkManager() {
        // cache drink list from DB (file)
        loadDrinks();
    }

    public void loadDrinks() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PERSIT_FILE));
            lines.forEach(line -> {
                String[] columns = line.split(",");
                Drink drink = new Drink();
                drink.setId(Integer.parseInt(columns[0]));
                drink.setName(columns[1]);
                drink.setPrice(Integer.parseInt(columns[2]));
                drink.setStock(Integer.parseInt(columns[3]));
                allDrinks.put(drink.getId(), drink);

            });
        } catch (IOException | NumberFormatException ex) {
            // TODO: logging
        }
    }

    public Drink getAvailaleDrink(int drinkId) {
        Drink drink = allDrinks.get(drinkId);
        if (drink == null) {
            return null;
        }
        return drink.getStock() > 0 ? drink : null;
    }

    public List<Drink> listAvailableDrinks() {
        return allDrinks.values()
                .stream()
                .filter(drink -> drink.getStock() > 0)
                .collect(Collectors.toList());
    }

    public void decreaseStock(int drinkId) {
        Drink drink = allDrinks.get(drinkId);
        if (drink != null) {
            drink.setStock(drink.getStock() - 1);
            persist();
        }
    }

    private void persist() {
        try {
        List<String> lines = allDrinks.values()
                .stream()
                .map(drink -> {
                    return String.format("%d,%s,%d,%d", drink.getId(), drink.getName(), drink.getPrice(), drink.getStock());
                })
                .collect(Collectors.toList());

            Files.write(Paths.get(PERSIT_FILE), String.join("\n", lines).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            throw new InternalException();
        }
    }
}
