import java.util.ArrayList;
import java.util.HashMap;

class Seller extends BankSellers {

    private String ID;
    private String firstName;
    private String lastName;
    private String TIN;
    private ArrayList<ProductSale> sales = new ArrayList<ProductSale>();
    private HashMap<String, Double> commission = new HashMap<String, Double>();

    public Seller(String ID, String firstName, String lastName, String TIN) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.TIN = TIN;
        this.commission.put("total", 0.0);
        super.insertSeller(this);
    }
    
    public Seller(String ID, String firstName, String lastName, String TIN, boolean fromRead) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.TIN = TIN;
        this.commission.put("total", 0.0);
        if(!fromRead) super.insertSeller(this);
    }
    
    public void addSale(ProductSale sale) {
		sales.add(sale);
	}
    
    public void setCommission(String ID, double com) {
    	if(commission.containsKey(ID)) {
    		commission.replace(ID, com);
    		return;
    	}
    	commission.put(ID, com);
    }
    
    public ArrayList<ProductSale> getSales() {
    	return sales;
    }

    public String getID() {
        return ID;
    }
    
    public String getName() {
    	return firstName + " " + lastName;
    }
    
    public String getTIN() {
    	return TIN;
    }
    
    public double getCommission(String ID) {
    	if(commission.containsKey(ID)) return commission.get(ID);
    	return 0;
    }
    
    public String toString() {
        return "Key: " + this.getSellerKey(this.ID) + "\nID Number: " + this.ID + "\nLast Name: " + this.lastName + "\nFirst Name: " + this.firstName + "\nTIN: " + this.TIN;
    }
}