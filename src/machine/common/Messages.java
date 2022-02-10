/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.common;

import machine.entity.Drink;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author chaupdm
 */
public class Messages {
    public static String Welcome() {
        return "Welcome!!";
    }
    public static String DrinkSelect(List<Drink> drinks) {
        String options = drinks
                .stream()
                .map(drink -> drink.toString())
                .collect(Collectors.joining("  "));
        
        return String.format("\nEnter drink number as per your choice:\n%s", options);
    }
    
    public static String DrinkSelected(String drinkName) {
        return String.format("%s has been selected.", drinkName);
    }
    
    public static String InputMoney(List<Integer> notes, int currentAmount, int total) {
        String options = notes
                .stream()
                .map(drink -> drink.toString())
                .collect(Collectors.joining(", "));
        
        return String.format("\nInput your money (%d/%d). We only accept notes of %s.\nInput 'cancel' to cancel the transaction.", currentAmount, total, options);
    }
    
    public static String Cancel(int refundAmount) {
        return String.format("The transaction has been canceled. You will be refunded %d.\n=====================", refundAmount);
    }
    
    public static String Success(String drink, int changeAmount) {
        return String.format("Successfully bought %s. Your change is %d.\n=====================", drink, changeAmount);
    }
    
    public static String InvalidInput() {
        return "Invalid input. Please try again.";
    }
    
    public static String UnsupportedBanknote() {
        return "Unsupported banknote. Please try again.";
    }
}
