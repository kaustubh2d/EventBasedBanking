package ws.eventbasedbanking.kaustubh;

import java.time.LocalDate;

public class AccountTransaction implements  ITransaction {

    private TransactionType type;
    private IMoney transactionAmount;
    private IMoney priorBalance;
    private LocalDate transactionDate;

    public AccountTransaction(TransactionType atype, IMoney tamount, IMoney pBalance, LocalDate tdate){
        this.type = atype;
        this.transactionAmount = tamount;
        this.priorBalance = pBalance;
        this.transactionDate = tdate;
    }

    @Override
    public TransactionType getTransactionType() {
        return type;
    }

    @Override
    public IMoney getTransactionAmount() {
        return transactionAmount;
    }

    @Override
    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}
