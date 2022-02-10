/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.entity;

/**
 *
 * @author chaupdm
 */
public class Transaction {
    private final Drink drink;
    private int moneyInput;

    public Transaction(Drink drink) {
        this.drink = drink;
    }
    
    public void addMoney(int ammount) {
        moneyInput += ammount;
    }
    
    public int getMoneyInput() {
        return this.moneyInput;
    }

    public Drink getDrink() {
        return drink;
    }
}
