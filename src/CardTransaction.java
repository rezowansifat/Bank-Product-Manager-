class CardTransaction {

    private String cardID;
    private String reason;
    private double amount;
    
    public CardTransaction(String cardID, double amount, String reason) {
        this.cardID = cardID;
        this.amount = amount;
        this.reason = reason;
    }

    public String getCardID() {
        return this.cardID;
    }

    public double getAmount() {
        return this.amount;
    }
    
    public String getReason() {
    	return this.reason;
    }
    
    public String toString() {
        return "Card ID: "+ this.cardID + "\nAmount: " + this.amount + " Euro\nReason: " + this.reason;
    }
}