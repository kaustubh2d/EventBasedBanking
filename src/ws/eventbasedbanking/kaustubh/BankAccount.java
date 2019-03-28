package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount implements IBankAccount {
    private long accountId;
    private String name;
    private IMoney balance;
    private String accountCurrency;
    private List<ITransaction> accountTransactions;


    public  BankAccount(String name, long accountId, String accountCurrency){
        this.accountId =  accountId;
        this.name = name;
        this.accountCurrency = accountCurrency;
        balance = new Money(0, accountCurrency);
        accountTransactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public IMoney getBalance() {
        return balance;
    }

    @Override
    public boolean withdraw(IMoney amount) {
        boolean retVal = true;

        IMoney priorBalance = new Money(balance.getAmount(), balance.getCurrency());
        IMoney newBalance = new Money(balance.getAmount(), balance.getCurrency());
        retVal = newBalance.reduce(amount);

        if (retVal) {
            saveTransaction(amount, priorBalance, TransactionType.DEBIT);
            balance = newBalance;

            reconcileBalance();


        }

        return retVal;
    }

    @Override
    public boolean deposit(IMoney amount) {
        boolean retVal = true;

        IMoney priorBalance = new Money(balance.getAmount(), balance.getCurrency());
        IMoney newBalance = new Money(balance.getAmount(), balance.getCurrency());
        retVal = newBalance.add(amount);

        if (retVal) {
            saveTransaction(amount, priorBalance, TransactionType.CREDIT);
            balance = newBalance;

            reconcileBalance();


        }

        return retVal;
    }

    @Override
    public String getAccountCurrency() {
        return accountCurrency;
    }

    @Override
    public List<ITransaction> getTransactions(LocalDate from, LocalDate to) {
        List<ITransaction> retTransaction = new ArrayList() ;

        for (ITransaction transaction: accountTransactions)
            if ((transaction.getTransactionDate().isAfter(from) && transaction.getTransactionDate().isBefore(to)) ||
                    transaction.getTransactionDate().equals(from) ||
                    transaction.getTransactionDate().equals(to))
                retTransaction.add(transaction);

        return retTransaction;
    }

    private void saveTransaction(IMoney tamount, IMoney pBalance, TransactionType transactionType){
        LocalDate tranDate = LocalDate.now();
        ITransaction transaction = new AccountTransaction(transactionType, tamount, pBalance, tranDate);
        accountTransactions.add(transaction);
    }

    private boolean reconcileBalance() {
        boolean retVal = false;

        IMoney totalTransactionNumber = new Money(0, accountCurrency);
        for(ITransaction transaction: accountTransactions){
            if (transaction.getTransactionType() == TransactionType.CREDIT )
                totalTransactionNumber.add(transaction.getTransactionAmount());
            else
                totalTransactionNumber.reduce(transaction.getTransactionAmount());
        }

        retVal = totalTransactionNumber.equals(balance);

        return retVal;
    }
}

