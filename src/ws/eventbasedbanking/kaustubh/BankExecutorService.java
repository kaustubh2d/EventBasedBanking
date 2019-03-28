package ws.eventbasedbanking.kaustubh;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BankExecutorService {

    private volatile static  BankExecutorService instance = null;
    private volatile static Lock singletonLock = new ReentrantLock();

    private ExecutorService es;

    public  static BankExecutorService getInstance(){
        if (instance != null)
            return instance;
        else {
            singletonLock.lock();
            if (instance == null) instance = new BankExecutorService();
            singletonLock.unlock();
            return instance;
        }
    }

    private BankExecutorService() {
        es = Executors.newSingleThreadExecutor();
    }

    public Future<Boolean> deposit(CallableDeposit deposit){
        return es.submit(deposit);
    }

    public Future<Boolean> withdraw(CallableWithdrawal withdrawal){
        return es.submit(withdrawal);
    }

    public Future<List<ITransaction>> getTransactions(CallableTransactionList transactionList){
        return es.submit(transactionList);
    }

    public Future<IMoney> getBalance(CallableGetBalance callableGetBalance){
        return es.submit(callableGetBalance);
    }
}
