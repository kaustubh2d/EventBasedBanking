package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;

public interface ITransaction {
    TransactionType getTransactionType();
    IMoney getTransactionAmount();
    LocalDate getTransactionDate();
}
