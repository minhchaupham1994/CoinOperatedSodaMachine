/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.model;

import machine.common.AppConfig;
import machine.entity.Drink;
import machine.entity.Transaction;
import machine.exception.InvalidInputException;

public class TransactionManager {

    public static final TransactionManager INSTANCE = new TransactionManager();

    private final DrinkManager drinkManager = DrinkManager.INSTANCE;

    private final Transaction EMPTY_TRANSACTION = new Transaction(new Drink());
    private Transaction currentTransaction;

    private TransactionManager() {
        currentTransaction = EMPTY_TRANSACTION;
    }

    public void start(int drinkId) throws InvalidInputException {
        Drink drink = drinkManager.getAvailaleDrink(drinkId);
        if (drink == null) {
            throw new InvalidInputException();
        }

        currentTransaction = new Transaction(drink);
    }

    public boolean inputMoney(int banknote) {
        if (!AppConfig.ALLOWED_BANKNOTES.contains(banknote)) {
            return false;
        }
        currentTransaction.addMoney(banknote);
        return true;
    }

    public void complete() {
        if (!isFulfill()) {
            return;
        }
        drinkManager.decreaseStock(currentTransaction.getDrink().getId());
        currentTransaction = EMPTY_TRANSACTION;
    }

    public void cancel() {
        currentTransaction = EMPTY_TRANSACTION;
    }

    public int getChangeAmmount() {
        return currentTransaction.getMoneyInput() - currentTransaction.getDrink().getPrice();
    }

    public int getRefundAmount() {
        return currentTransaction.getMoneyInput();
    }

    public Drink getDrink() {
        return currentTransaction.getDrink();
    }

    public int getInputMoney() {
        return currentTransaction.getMoneyInput();
    }

    public boolean isFulfill() {
        return currentTransaction != EMPTY_TRANSACTION && getChangeAmmount() >= 0;
    }

    public Step getCurrentStep() {
        return currentTransaction == EMPTY_TRANSACTION ? Step.DrinkSelect : Step.MoneyInput;
    }

    public enum Step {
        DrinkSelect, MoneyInput
    }
}
