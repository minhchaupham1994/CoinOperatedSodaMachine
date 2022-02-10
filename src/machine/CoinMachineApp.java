/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

import machine.common.AppConfig;
import machine.common.Messages;
import machine.exception.InternalException;
import machine.exception.InvalidInputException;
import machine.model.DrinkManager;
import machine.model.TransactionManager;
import java.util.Scanner;

public class CoinMachineApp {

    private final String CMD_CANCEL = "cancel";

    private TransactionManager transactionManager = TransactionManager.INSTANCE;
    private DrinkManager drinkManager = DrinkManager.INSTANCE;

    public void start() {
        printToScreen(Messages.Welcome());

        while (true) {
            try {
                // Print prompt message
                switch (transactionManager.getCurrentStep()) {
                    case DrinkSelect:
                        printDrinks();
                        break;
                    case MoneyInput:
                        printToScreen(Messages.InputMoney(AppConfig.ALLOWED_BANKNOTES, transactionManager.getInputMoney(), transactionManager.getDrink().getPrice()));
                        break;
                }

                // Process input
                Scanner in = new Scanner(System.in);
                String input = in.nextLine();
                switch (transactionManager.getCurrentStep()) {
                    case DrinkSelect:
                        selectDrink(input);
                        break;
                    case MoneyInput:
                        inputMoney(input);
                        tryComplete();
                        break;
                    default:
                        throw new InternalException();
                }

            } catch (Throwable e) {
                if (transactionManager.getCurrentStep() == TransactionManager.Step.MoneyInput) {
                    cancel();
                }
            }
        }
    }

    private void printDrinks() {
        printToScreen(Messages.DrinkSelect(drinkManager.listAvailableDrinks()));
    }
    
    private void printToScreen(String message) {
        System.out.println(message);
    }

    private void selectDrink(String input) {
        try {
            int drink = Integer.parseInt(input.trim());
            transactionManager.start(drink);
            printToScreen(Messages.DrinkSelected(transactionManager.getDrink().getName()));
        } catch (NumberFormatException | InvalidInputException e) {
            printToScreen(Messages.InvalidInput());
        }
    }

    private void inputMoney(String input) {
        if (input.trim().equals(CMD_CANCEL)) {
            cancel();
            return;
        }

        try {
            int banknote = Integer.parseInt(input.trim());
            boolean success = transactionManager.inputMoney(banknote);
            if (!success) {
                printToScreen(Messages.UnsupportedBanknote());
            }
        } catch (NumberFormatException e) {
            printToScreen(Messages.InvalidInput());
        }
    }

    private void tryComplete() {
        if (transactionManager.isFulfill()) {
            int changeAmmount = transactionManager.getChangeAmmount();
            printToScreen(Messages.Success(transactionManager.getDrink().getName(), changeAmmount));

            transactionManager.complete();
        }
    }

    private void cancel() {
        int refundAmmount = transactionManager.getRefundAmount();
        printToScreen(Messages.Cancel(refundAmmount));

        transactionManager.cancel();
    }

    
}
