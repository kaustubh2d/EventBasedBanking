package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Future;

public interface IBankService {
    long createAccount(String name);
    Future<Boolean> addMoneyAsync(long accountId, IMoney amount);
    Future<Boolean> withdrawMoneyAsync(long accountId, IMoney amount);
    Future<List<ITransaction>> getTransactionsAsync(long accountId, LocalDate fromDate, LocalDate toDate);
    Future<IMoney> getBalanceAsync(long accountId);
    IBankAccount getBankAccount(long accountId);
}
