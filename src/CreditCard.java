import java.util.ArrayList;

class CreditCard extends Product{
	private double commission;
	private double moveLimit;
	private double yearLimit;
	private double cashFlow;
	private ArrayList<CardTransaction> transactions;
	
	public CreditCard(String ID, String number, String TIN, int seller_key, double commission, double moveLimit, double yearLimit) {
		super(ID, number, TIN, seller_key);
		this.commission = commission;
		this.moveLimit = moveLimit;
		this.yearLimit = yearLimit;
		this.cashFlow = 0;
		this.transactions = new ArrayList<CardTransaction>();
	}
	
	public CreditCard(String ID, String number, String TIN, int seller_key, double commission, double moveLimit, double yearLimit, boolean fromRead) {
		super(ID, number, TIN, seller_key, fromRead);
		this.commission = commission;
		this.moveLimit = moveLimit;
		this.yearLimit = yearLimit;
		this.cashFlow = 0;
		this.transactions = new ArrayList<CardTransaction>();
	}
	
	public void addTransaction(CardTransaction ct) {
		transactions.add(ct);
	}
	
	public void increaseCashFlow(double amount) {
		cashFlow += amount;
	}
	
	public ArrayList<CardTransaction> getTransactions() {
		return transactions;
	}
	
	public double getCommission() {
		return commission;
	}
	
	public double getCashFlow() {
		return cashFlow;
	}
	
	public double getMoveLimit() {
		return moveLimit;
	}
	
	public double getYearLimit() {
		return yearLimit;
	}
	
	public String toString() {
		return String.format("%s | Commission%% : %.2f%% | Move Limit: %.2f Euro | Year Limit: %.2f Euro ", super.toString(), commission*100, moveLimit, yearLimit);
	}
}