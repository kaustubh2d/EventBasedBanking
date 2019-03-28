package ws.eventbasedbanking.kaustubh;

import java.util.concurrent.Callable;

/* Command pattern */
public class CallableGetBalance implements Callable<IMoney> {
    private IBankAccount bankAccount;


    public CallableGetBalance(IBankAccount bankAccount) {
        this.bankAccount = bankAccount;

    }

    @Override
    public IMoney call() throws Exception {
        IMoney retval = new Money(0, "INR");

        if (bankAccount != null)
            retval = bankAccount.getBalance();

        return  retval;
    }
}
