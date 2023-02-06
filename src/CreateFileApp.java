import java.util.*;
import java.io.*;

class CreateFileApp {
	private static ArrayList<CreditCard> cards;
	private static ArrayList<Seller> sellers;
	private static HashMap<String, Loan> loans;

	public CreateFileApp(ArrayList<CreditCard> productList, ArrayList<Seller> sellerList, HashMap<String, Loan> loanDict) {
		cards = productList;
		sellers = sellerList;
		loans = loanDict;
	}
	
	public void CreateFile() {
		BufferedWriter writer = null;
		System.out.println("[Writing cards list to file trn_list.txt ...]");
		
		try {
			writer = new BufferedWriter(new FileWriter(new File("trn_list.txt")));
			writer.write("TRN_LIST\n{\n");
			for(CreditCard card : cards) {
				for(CardTransaction trn : card.getTransactions()) {
					writer.write(String.format("  TRN\n  {\n    BANKITEM_CODE %s\n    VAL %.2f\n    JUSTIFICATION \"%s\"\n  }\n", card.getID(), trn.getAmount(), trn.getReason()));
				}
			}
		    writer.write("}");
			writer.close();
			
			System.out.println("[Writing product sale list to file sales_list.txt ...]");
			writer = new BufferedWriter(new FileWriter(new File("sales_list.txt")));
			writer.write("SALES_LIST\n{\n");
			for(Seller seller : sellers) {
				for(ProductSale sale : seller.getSales()) {
					String type = (loans.containsKey(sale.getProductID())) ? "Loan" : "Card";
					writer.write(String.format("  SALE\n  {\n    SALESMAN_CODE %s\n    BANKITEM_TYPE %s\n    BANKITEM_CODE %s\n    REASON \"%s\"\n  }\n", sale.getSellerID(), type, sale.getProductID(), sale.getReason()));
				}
			}
			writer.write("}");
			writer.close();
		} catch (IOException e){
			System.out.println("Error writing file...");			
		}
	}
}