class Loan extends Product {
	private double amount;
	private double yearlyRate;
	
	public Loan(String ID, String number, String TIN, int seller_key, double amount, double yearlyRate) {
		super(ID, number, TIN, seller_key);
		this.amount = amount;
		this.yearlyRate = yearlyRate;
	}
	
	public Loan(String ID, String number, String TIN, int seller_key, double amount, double yearlyRate, boolean fromRead) {
		super(ID, number, TIN, seller_key, fromRead);
		this.amount = amount;
		this.yearlyRate = yearlyRate;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public double getYearlyRate() {
		return yearlyRate;
	}
	
	public String toString() {
		return String.format("%s | Loan Amount: %.2f Euro | Yearly Interest Rate: %.2f%%", super.toString(), amount, yearlyRate*100);
	}
}
