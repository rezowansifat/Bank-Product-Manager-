import java.util.HashMap;
import java.util.Map;

class BankProducts {
	static private int key = 0;
	static private HashMap<Integer, Product> products = new HashMap<Integer, Product>();
	static private HashMap<String, Integer> productKeys = new HashMap<String, Integer>();
	
	public void insertProduct(Product product) {
		key++;
		products.put(key, product);
		productKeys.put(product.getID(), key);
	}
	
	public boolean printProducts() {
		boolean unsoldProducts = false;
		
		for(Map.Entry<Integer, Product> product : products.entrySet()) {
			if(product.getValue().getSellerKey() != 0) continue;
			unsoldProducts = true;
			System.out.printf("\n[Key: %d] | ID: %s | Number: %s | TIN: %s", product.getKey(), product.getValue().getID(), product.getValue().getNumber(), product.getValue().getTIN());
		}
		
		return unsoldProducts;
	}
	
	public void printCards() {
		for(Map.Entry<Integer, Product> product : products.entrySet()) {
			if(product.getValue() instanceof Loan) continue;
			System.out.printf("\n[Key: %d] | ID: %s | Number: %s | TIN: %s", product.getKey(), product.getValue().getID(), product.getValue().getNumber(), product.getValue().getTIN());
		}
	}
	
	public boolean productExists(int key) {
		return products.containsKey(key);
	}
	
	public boolean productExists(String ID) {
		return productKeys.containsKey(ID);
	}
	
	public Product getProduct(int key) {
		return products.get(key);
	}
}
