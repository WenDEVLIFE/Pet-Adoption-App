package adapter;

public class Transactions {
    public String transactiondetails;

    public  Transactions(String transactiondetails) {
        this.transactiondetails = transactiondetails;
    }

    public String getTransactiondetails() {
        return transactiondetails;
    }

    public void setTransactiondetails(String transactiondetails) {
        this.transactiondetails = transactiondetails;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "transactiondetails='" + transactiondetails + '\'' +
                '}';
    }
}
