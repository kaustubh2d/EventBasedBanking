package ws.eventbasedbanking.kaustubh;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class BankServiceTest {
    @Test
    public void getBalanceAsync() throws ExecutionException, InterruptedException {
        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personB");


        Future<IMoney> initialBalance = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync: verify zero balance",
                initialBalance.get().equals(new Money(0, "INR")));

        bs.addMoneyAsync(accountId, new Money(100, "INR"));
        Future<IMoney> balAfterDeposit = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync: verify non zero balance",
                balAfterDeposit.get().equals(new Money(100, "INR")));

        Future<Boolean> invalidDeposit = bs.addMoneyAsync(accountId, new Money(200, "USD"));
        Assert.assertFalse("addMoneyAsync: verify balance after invalid deposit",invalidDeposit.get());
        Future<IMoney> balAfterUSDeposit = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync: verify balance after invalid deposit",
                balAfterUSDeposit.get().equals(new Money(100, "INR")));
    }

    @Test
    public void withdrawAsync() throws ExecutionException, InterruptedException {
        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personW");

        Future<Boolean> intialWithdraw = bs.withdrawMoneyAsync(accountId, new Money(100, "INR"));
        Assert.assertFalse("withdrawMoneyAsync: Withdraw with zero balance",intialWithdraw.get());
        Future<IMoney> initialBalance = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync:Withdraw with zero balance",
                initialBalance.get().equals(new Money(0, "INR")));

        bs.addMoneyAsync(accountId, new Money(100, "INR"));
        Future<Boolean> validWithdrawal = bs.withdrawMoneyAsync(accountId, new Money(25, "INR"));
        Assert.assertTrue("withdrawMoneyAsync:valid withdrawal", validWithdrawal.get());
        Future<IMoney> balAfterWithdrawal = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync:Verify Balance after successful withdrawal",
                balAfterWithdrawal.get().equals(new Money(75, "INR")));


        Future<Boolean> inValidCurWithdrawal = bs.withdrawMoneyAsync(accountId, new Money(25, "USD"));
        Assert.assertFalse("withdrawMoneyAsync:invalid currency withdrawal", inValidCurWithdrawal.get());
        Future<IMoney> balAfterInvalidCurWithdrawal = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync:Verify Balance after invalid currency withdrawal",
                balAfterInvalidCurWithdrawal.get().equals(new Money(75, "INR")));

    }

    @Test
    public void depositAsync() throws ExecutionException, InterruptedException {
        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personD");

        Future<Boolean> intialWithdraw = bs.withdrawMoneyAsync(accountId, new Money(100, "INR"));
        Assert.assertFalse("withdrawMoneyAsync:Withdraw with zero balance",intialWithdraw.get());
        Future<IMoney> initialBalance = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync:Withdraw with zero balance",
                initialBalance.get().equals(new Money(0, "INR")));

        Future<Boolean> validDeposit = bs.addMoneyAsync(accountId,new Money(100, "INR"));
        Assert.assertTrue("addMoneyAsync:valid deposit", validDeposit.get());
        Future<IMoney> balAfterdeposit = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync:Verify Balance after sucessful deposit",
                balAfterdeposit.get().equals(new Money(100, "INR")));


        Future<Boolean> inValidDeposit = bs.addMoneyAsync(accountId, new Money(25, "USD"));
        Assert.assertFalse("addMoneyAsync:invalid currency deposit", inValidDeposit.get());
        Future<IMoney> balAfterInvalidCurDeposit = bs.getBalanceAsync(accountId);
        Assert.assertTrue("getBalanceAsync:Verify Balance after invalid currency deposit",
                balAfterInvalidCurDeposit.get().equals(new Money(100, "INR")));
    }

    @Test
    public void getTransactionsAsync() throws ExecutionException, InterruptedException {

        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personT");

        Future<List<ITransaction>> zeroTransactions = bs.getTransactionsAsync( accountId, LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2));
        Assert.assertEquals(0, zeroTransactions.get().size());

        bs.addMoneyAsync(accountId, new Money(100, "INR"));
        bs.withdrawMoneyAsync(accountId, new Money(25, "INR"));
        bs.addMoneyAsync(accountId, new Money(100, "USD"));
        bs.withdrawMoneyAsync(accountId, new Money(25, "USD"));

        Future<List<ITransaction>> twoTransactions = bs.getTransactionsAsync(accountId, LocalDate.now().plusDays(-1),
                LocalDate.now().plusDays(1));
        Assert.assertEquals("getTransactionsAsync:Invoked 2 correct and 2 incorrect transactions",
                2, twoTransactions.get().size());

    }
}