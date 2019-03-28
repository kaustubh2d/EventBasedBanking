package ws.eventbasedbanking.kaustubh;

public interface IMoney {
    boolean equals(IMoney other);
    double getAmount() ;
    String getCurrency();
    boolean add(IMoney amount);
    boolean reduce(IMoney amount);
}

