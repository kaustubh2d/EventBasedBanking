package ws.eventbasedbanking.kaustubh;

public class Money implements IMoney {
    private double amount;
    private String currency;

    public Money(double amount, String currency){
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean add(IMoney amount) {
        boolean retVal = false;

        if (amount.getCurrency().equals((String)this.currency)) {
            this.amount += amount.getAmount();
            retVal = true;
        }
        return  retVal;
    }

    @Override
    public boolean reduce(IMoney amount) {
        boolean retVal = false;

        if (amount.getCurrency().equals((String)this.currency) && amount.getAmount() < this.amount) {
            this.amount -= amount.getAmount();
            retVal = true;
        }
        return  retVal;
    }

    @Override
    public boolean equals(IMoney other) {
        return ((this.amount == other.getAmount()) && this.currency.equals(other.getCurrency()));
    }
}
