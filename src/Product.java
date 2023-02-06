class Product extends BankProducts {
	protected String ID;
	protected String number;
	protected String TIN;
	protected int seller_key;
	
	public Product(String ID, String number, String TIN, int seller_key) {
		this.ID = ID;
		this.number = number;
		this.TIN = TIN;
		this.seller_key = seller_key;
		super.insertProduct(this);
	}
	
	public Product(String ID, String number, String TIN, int seller_key, boolean fromRead) {
		this.ID = ID;
		this.number = number;
		this.TIN = TIN;
		this.seller_key = seller_key;
		if(!fromRead) super.insertProduct(this);
	}

	public void setSeller(int key) {
		seller_key = key;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getTIN() {
		return TIN;
	}
	
	public int getSellerKey() {
		return seller_key;
	}
	
	public String toString() {
		return String.format("ID: %s | Number: %s | TIN: %s", ID, number, TIN);
	}
}
