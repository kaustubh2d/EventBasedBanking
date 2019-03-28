package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/* Command pattern */
public class CallableTransactionList implements Callable<List<ITransaction>>{
    private IBankAccount bankAccount;
    private LocalDate fromDate;
    private LocalDate toDate;

    public CallableTransactionList(IBankAccount bankAccount, LocalDate fromDate, LocalDate toDate) {
        this.bankAccount = bankAccount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public List<ITransaction> call() throws Exception {
        List<ITransaction> retList = new ArrayList<>();

        if (bankAccount != null)
            retList = bankAccount.getTransactions(fromDate, toDate);

        return  retList;
    }
}
