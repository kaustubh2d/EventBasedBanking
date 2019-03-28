package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankService implements  IBankService{
    private Map<Long, IBankAccount> allAccounts;
    private Map<String, Long> nameAccountIds;

    private static AtomicLong lastAccountId ;

    private static volatile IBankService instance = null;
    private static volatile Lock singletonLock = new ReentrantLock();

    public  static IBankService getInstance(){
        if (instance != null)
            return instance;
        else {
            singletonLock.lock();
            if (instance == null) instance = new BankService();
            singletonLock.unlock();
            return instance;
        }
    }

    private BankService() {
        allAccounts = new ConcurrentHashMap<>();
        nameAccountIds = new ConcurrentHashMap<>();
        lastAccountId = new AtomicLong(0);
    }

    @Override
    public long createAccount(String name) {
        long retVal  = -1;

        if (nameAccountIds.containsKey(name))
            retVal = nameAccountIds.get(name);
        else {
            retVal = lastAccountId.incrementAndGet();
            nameAccountIds.put(name, retVal);
            allAccounts.put(retVal, new BankAccount(name, retVal, "INR"));
        }

        return retVal;
    }

    @Override
    public Future<Boolean> addMoneyAsync(long accountId, IMoney amount) {
        Future<Boolean> retVal = CompletableFuture.supplyAsync(()->false);

        IBankAccount bankAccount = allAccounts.get(accountId);
        if (bankAccount != null) {
            CallableDeposit callableDeposit = new CallableDeposit(bankAccount, amount);
            BankExecutorService bes = BankExecutorService.getInstance();

            retVal = bes.deposit(callableDeposit);
        }

        return retVal;
    }

    @Override
    public Future<Boolean> withdrawMoneyAsync(long accountId, IMoney amount) {
        Future<Boolean> retVal = CompletableFuture.supplyAsync(()->false);

        IBankAccount bankAccount = allAccounts.get(accountId);
        if (bankAccount != null) {
            CallableWithdrawal callableWithdrawal = new CallableWithdrawal(bankAccount, amount);
            BankExecutorService bes = BankExecutorService.getInstance();

            retVal = bes.withdraw(callableWithdrawal);
        }

        return retVal;
    }

    @Override
    public Future<List<ITransaction>> getTransactionsAsync(long accountId, LocalDate fromDate, LocalDate toDate) {
        Future<List<ITransaction>> retList =  CompletableFuture.supplyAsync(()-> new ArrayList<ITransaction>());

        IBankAccount bankAccount = allAccounts.get(accountId);
        if (bankAccount != null) {
            CallableTransactionList callableTransactionList = new CallableTransactionList(bankAccount, fromDate, toDate);
            BankExecutorService bes = BankExecutorService.getInstance();

            retList = bes.getTransactions(callableTransactionList);
        }

        return retList;
    }

    @Override
    public Future<IMoney> getBalanceAsync(long accountId){
        Future<IMoney> retBalance =  CompletableFuture.supplyAsync(()-> new Money(0, "INR"));

        IBankAccount bankAccount = allAccounts.get(accountId);
        if (bankAccount != null) {
            CallableGetBalance callableBalance = new CallableGetBalance(bankAccount);
            BankExecutorService bes = BankExecutorService.getInstance();

            retBalance = bes.getBalance(callableBalance);
        }

        return retBalance;
    }


    public IBankAccount getBankAccount(long accountId){
        return allAccounts.get(accountId);
    }
}