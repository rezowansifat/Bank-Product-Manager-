import java.util.*;

class BankSellers {
	static private int key = 0;
	static private HashMap<Integer, Seller> sellers = new HashMap<Integer, Seller>();
	static private HashMap<String, Integer> sellerKeys = new HashMap<String, Integer>();
	
	public void insertSeller(Seller seller) {
		key++;
		sellers.put(key, seller);
		sellerKeys.put(seller.getID(), key);
	}
	
	public void printSellers() {
		for(Map.Entry<Integer, Seller> Seller : sellers.entrySet()) {
			System.out.printf("[Key: %d] | ID: %s | Name: %s | TIN: %s%n", Seller.getKey(), 
					Seller.getValue().getID(), Seller.getValue().getName(), 
					Seller.getValue().getTIN());
		}
	}
	
	public boolean sellerExists(int key) {
		return sellers.containsKey(key);
	}
	
	public boolean sellerExists(String sID) {
		return sellerKeys.containsKey(sID);
	}
	
	public ArrayList<Seller> getSellerList() {
    	return new ArrayList<Seller>(sellers.values());
    }
	
	public int getSellerKey(String sID) {
		return sellerKeys.get(sID);
	}
	
	public Seller getSeller(int key) {
		return sellers.get(key);
	}
	
	public Seller getSeller(String sID) {
		return sellers.get(sellerKeys.get(sID));
	}

}
