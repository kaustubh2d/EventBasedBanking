package ws.eventbasedbanking.kaustubh;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class BankAccountTest {
    @Test
    public void getBalance() {
        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personB");
        IBankAccount ba = bs.getBankAccount(accountId);

        IMoney initialBalance = ba.getBalance();
        Assert.assertTrue("getBalance: verify zero balance",
                initialBalance.equals(new Money(0, "INR")));

        ba.deposit(new Money(100, "INR"));
        IMoney balAfterDeposit = ba.getBalance();
        Assert.assertTrue("getBalance: verify non zero balance",
                balAfterDeposit.equals(new Money(100, "INR")));

        boolean invalidDeposit = ba.deposit(new Money(200, "USD"));
        Assert.assertFalse("deposit: verify balance after invalid deposit", invalidDeposit);
        IMoney balAfterUSDeposit = ba.getBalance();
        Assert.assertTrue("getBalance: verify balance after invalid deposit",
                balAfterUSDeposit.equals(new Money(100, "INR")));
    }

    @Test
    public void withdraw() {
        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personW");
        IBankAccount ba = bs.getBankAccount(accountId);

        boolean intialWithdraw = ba.withdraw(new Money(100, "INR"));
        Assert.assertFalse(intialWithdraw);
        IMoney initialBalance = ba.getBalance();
        Assert.assertTrue("getBalance:Withdraw with zero balance",
                initialBalance.equals(new Money(0, "INR")));

        ba.deposit(new Money(100, "INR"));
        boolean validWithdrawal = ba.withdraw(new Money(25, "INR"));
        Assert.assertTrue("withdraw:valid withdrawal", validWithdrawal);
        IMoney balAfterWithdrawal = ba.getBalance();
        Assert.assertTrue("getBalance:Verify Balance after successful withdrawal",
                balAfterWithdrawal.equals(new Money(75, "INR")));


        boolean inValidCurWithdrawal = ba.withdraw(new Money(25, "USD"));
        Assert.assertFalse("withdraw:invalid currency withdrawal", inValidCurWithdrawal);
        IMoney balAfterInvalidCurWithdrawal = ba.getBalance();
        Assert.assertTrue("getBalance:Verify Balance after invalid currency withdrawal",
                balAfterInvalidCurWithdrawal.equals(new Money(75, "INR")));

    }

    @Test
    public void deposit() {
        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personD");
        IBankAccount ba = bs.getBankAccount(accountId);


        boolean intialWithdraw = ba.withdraw(new Money(100, "INR"));
        Assert.assertFalse(intialWithdraw);
        IMoney initialBalance = ba.getBalance();
        Assert.assertTrue("getBalance:Withdraw with zero balance",
                initialBalance.equals(new Money(0, "INR")));

        boolean validDeposit = ba.deposit(new Money(100, "INR"));
        Assert.assertTrue("deposit:valid deposit", validDeposit);
        IMoney balAfterdeposit = ba.getBalance();
        Assert.assertTrue("getBalance:Verify Balance after sucessful deposit",
                balAfterdeposit.equals(new Money(100, "INR")));


        boolean inValidDeposit = ba.deposit(new Money(25, "USD"));
        Assert.assertFalse("deposit:invalid currency deposit", inValidDeposit);
        IMoney balAfterInvalidCurDeposit = ba.getBalance();
        Assert.assertTrue("getBalance:Verify Balance after invalid currency deposit",
                balAfterInvalidCurDeposit.equals(new Money(100, "INR")));
    }

    @Test
    public void getTransactions() {

        IBankService bs = BankService.getInstance();
        long accountId = bs.createAccount("personT");
        IBankAccount ba = bs.getBankAccount(accountId);

        List<ITransaction> zeroTransactions = ba.getTransactions( LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        Assert.assertEquals("getTransactions:Invoked with zero operations",
                0, zeroTransactions.size());

        ba.deposit(new Money(100, "INR"));
        ba.withdraw(new Money(25, "INR"));
        ba.deposit(new Money(100, "USD"));
        ba.withdraw(new Money(25, "USD"));

        List<ITransaction> twoTransactions = ba.getTransactions( LocalDate.now().plusDays(-1), LocalDate.now().plusDays(1));
        Assert.assertEquals("getTransactions:Invoked 2 correct and 2 incorrect operations",
                2, twoTransactions.size());

    }
}