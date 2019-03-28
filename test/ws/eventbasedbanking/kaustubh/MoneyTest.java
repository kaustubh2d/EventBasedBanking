package ws.eventbasedbanking.kaustubh;

import org.junit.Assert;


public class MoneyTest {
    @org.junit.Test
    public void add_pass() {
        IMoney money1 = new Money(10.5 , "INR");
        IMoney money2 = new Money(0.5 , "INR");
        money1.add(money2);

        Assert.assertTrue(money1.equals(new Money(11, "INR")));
    }


    @org.junit.Test
    public void add_differentcurrency() {
        IMoney money1 = new Money(10.5 , "INR");
        IMoney money2 = new Money(0.5 , "USD");
        money1.add(money2);

        Assert.assertFalse(money1.equals(new Money(11, "INR")));
    }


    @org.junit.Test
    public void reduce_pass() {
        IMoney money1 = new Money(10.5 , "INR");
        IMoney money2 = new Money(0.5 , "INR");
        money1.reduce(money2);

        Assert.assertTrue(money1.equals(new Money(10, "INR")));
    }

    @org.junit.Test
    public void reduce_fail() {
        IMoney money1 = new Money(10.5 , "INR");
        IMoney money2 = new Money(11.5 , "INR");
        money1.reduce(money2);

        Assert.assertFalse(money1.equals(new Money(10, "INR")));
    }

}