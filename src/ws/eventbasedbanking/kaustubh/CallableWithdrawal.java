package ws.eventbasedbanking.kaustubh;

import java.util.concurrent.Callable;

/* Command pattern */
public class CallableWithdrawal implements Callable<Boolean> {
    private IBankAccount bankAccount;
    private IMoney amount;


    public CallableWithdrawal(IBankAccount bankAccount, IMoney amount) {
        this.bankAccount = bankAccount;
        this.amount = amount;

    }

    @Override
    public Boolean call() throws Exception {
        Boolean retval = false;

        if (bankAccount != null)
            retval = bankAccount.withdraw(amount);

        return  retval;
    }
}
