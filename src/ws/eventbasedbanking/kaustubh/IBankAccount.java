package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;
import java.util.List;

public interface IBankAccount {
    String getName();

    IMoney getBalance();

    boolean withdraw(IMoney amount);

    boolean deposit(IMoney amount);

    String getAccountCurrency();

    List<ITransaction> getTransactions(LocalDate fromDate, LocalDate toDate);
}
