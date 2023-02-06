/* 
 * 	Team Number: 055
 * 
 * 	Alexios Papadopoulos-Siountris 	3210154
 *  Dimitrios Fotogiannopoulos 		3210214
 *  Dimitrios Toumazatos 			3210199
 *  
 */


import java.util.*;

public class mainApp {
	static BankDict bankDict = new BankDict();
	static BankSellers sellers = new BankSellers();
	static BankProducts products = new BankProducts();
	static Scanner in = new Scanner(System.in);
	
	static ReadFileApp fr = new ReadFileApp();
	
	public static String returnToMenu(String action) {
		String cont = "";
		System.out.print("\n" + action + "... Proceed? (Yes: 1/No: 2)\n> ");
		
		do {
			cont = in.nextLine();
			if (!cont.equals("1") && !cont.equals("2")) {
				System.out.print("Please type a valid answer (Yes: 1/No: 2)\n> ");
			}
		} while (!cont.equals("1") && !cont.equals("2"));			
		
		return cont;
	}
	
	public static double computeCommission(Seller seller) {
		System.out.println(seller);
		
		if(seller.getSales().isEmpty()) {
			seller.setCommission("total", 0);
			return 0;
		}

		double com = 0;
		double maxLoanCom = 0;
		
		System.out.println("\n\t(Credit Card Commissions)");
		for(ProductSale sale : seller.getSales()) {
			String pID = sale.getProductID();
			
			if(bankDict.getCard(pID) != null) {
				com += seller.getCommission(pID);
				
				System.out.println("\n[" +  bankDict.getCard(pID) + "]");
				System.out.printf("Seller Commission: %.2f Euro\n", seller.getCommission(pID));
			}
		}
		System.out.printf("\nTotal Credit Card Commission: %.2f Euro\n", com);
		
		System.out.println("\n\t(Loan Commissions)");
		for(ProductSale sale : seller.getSales()) {
			String pID = sale.getProductID();
			if(bankDict.containsLoan(pID)) {
				System.out.println("\n[" + bankDict.getLoan(pID) + "]");
				maxLoanCom += bankDict.getLoan(pID).getYearlyRate() * bankDict.getLoan(pID).getAmount();
			}
		}
		
		double loan_amount = seller.getCommission("loans");
		double loan_com = 0;
		
		if(loan_amount < 500000) {
			loan_com += loan_amount * 0.01;
			
		} else if (loan_amount < 2000000) {
			loan_com += loan_amount * 0.02;
			
		} else {
			loan_com += loan_amount * 0.025;
		}
		
		loan_com = (loan_com <= maxLoanCom) ? loan_com : maxLoanCom;
		
		com += loan_com;

		System.out.printf("\nTotal Loan Commission: %.2f Euro\n", loan_com);
		
		System.out.printf("\nTotal Seller Commission: %.2f Euro\n", com);
		
		seller.setCommission("total", com);
		return com;
	}
	
	public static void computeFinalCommission(Seller seller) {		
		if(seller.getSales().isEmpty()) {
			seller.setCommission("total", 0);
			return;
		}

		double com = 0;
		double maxLoanCom = 0;
		
		for(ProductSale sale : seller.getSales()) {
			String pID = sale.getProductID();
			
			if(bankDict.getCard(pID) != null) {
				com += seller.getCommission(pID);
			}
		}
		
		for(ProductSale sale : seller.getSales()) {
			String pID = sale.getProductID();
			if(bankDict.containsLoan(pID)) {
				maxLoanCom += bankDict.getLoan(pID).getYearlyRate() * bankDict.getLoan(pID).getAmount();
			}
		}
		
		double loan_amount = seller.getCommission("loans");
		double loan_com = 0;
		
		if(loan_amount < 500000) {
			loan_com += loan_amount * 0.01;
			
		} else if (loan_amount < 2000000) {
			loan_com += loan_amount * 0.02;
			
		} else {
			loan_com += loan_amount * 0.025;
		}
		
		loan_com = (loan_com <= maxLoanCom) ? loan_com : maxLoanCom;
		
		com += loan_com;
		
		seller.setCommission("total", com);
	}

	public static void main(String[] args) {
		
		// Reads inputs from each list from a file (located in bank-products/input/)
		
		System.out.print("\n-------------------\n\n");
		fr.ReadFile("product_list.txt");
		fr.ReadFile("sales_list.txt");
		fr.ReadFile("salesman_list.txt");
		fr.ReadFile("trn_list.txt");
		System.out.print("\n-------------------\n");
		
		// Fetches the ArrayLists with each object inputted from file
		
		ArrayList<Product> inputProducts = fr.getProducts();
		ArrayList<ProductSale> inputSales = fr.getSales();
		ArrayList<Seller> inputSellers = fr.getSellers();
		ArrayList<CardTransaction> inputTransactions = fr.getTransactions();
		
		
		for(Seller seller : inputSellers) { // Assigns seller key
			sellers.insertSeller(seller);
		}
		
		
		for (Product product : inputProducts) { // Assigns product key and adds product to corresponding hashmap (loan or creditcard)
			products.insertProduct(product);
			if(product instanceof Loan) bankDict.add(product.getID(), (Loan) product);
			else bankDict.add(product.getID(), (CreditCard) product);
		}
		
		
		for(ProductSale sale : inputSales) { // Assigns each sale to the corresponding seller and sets the product's seller_key to the corresponding key
			if(!sellers.sellerExists(sale.getSellerID()) || !products.productExists(sale.getProductID())) continue;
			int seller_key = sellers.getSellerKey(sale.getSellerID());
			String pID = sale.getProductID();
			sellers.getSeller(seller_key).addSale(sale);
			if(bankDict.containsLoan(pID)) bankDict.getLoan(pID).setSeller(seller_key);
			else bankDict.getCard(pID).setSeller(seller_key);
		}
		
		
		for(CardTransaction ct : inputTransactions) { // Assigns each cardtransaction to the corresponding card
			if(!bankDict.containsCard(ct.getCardID())) continue;
			bankDict.getCard(ct.getCardID()).addTransaction(ct);
		}
		
		
		/* Initializes loan amount for all sellers at the start of the program */
		
		for(Seller seller : sellers.getSellerList()) {
			double loanAmount = 0;
			for(ProductSale sale : seller.getSales()) {
				if(bankDict.containsLoan(sale.getProductID())) {
					loanAmount += bankDict.getLoanAmount(sale.getProductID());
				}
			}
			seller.setCommission("loans", loanAmount);
		}
		
		
		/* Initializes the commissions and cash flow for all the above card transactions */
		
		for(CreditCard cc : bankDict.getCCs()) {
			double transactionCommission = 0;
			
			for(CardTransaction ct : cc.getTransactions()) {
				transactionCommission += ct.getAmount() * cc.getCommission();
				cc.increaseCashFlow(ct.getAmount());
			}
			
			if(cc.getSellerKey() == 0) continue;			
			sellers.getSeller(cc.getSellerKey()).setCommission(cc.getID(), transactionCommission);
		}		
		
		
		Boolean done = false;
		
		while(!done) {
			System.out.println("\n\n\t[MENU]");
			System.out.println("1. Insert Seller");
			System.out.println("2. Insert Bank Product");
			System.out.println("3. Insert Product Sale");
			System.out.println("4. Insert Credit Card Transaction");
			System.out.println("5. Show Loans");
			System.out.println("6. Compute a Seller's Commission");
			System.out.println("7. Show Credit Card Transactions Linked to a Seller");
			System.out.println("8. Compute Commissions for all Sellers");
			System.out.println("9. Show the Commission Amount of all Sellers");
			System.out.println("10. Write Product Sales and Card Transactions to file");
			System.out.println("0. Exit");
			System.out.print("> ");
			
			String ans = in.nextLine();
			
			String sID, pID, reason, num, pTIN;
			int key, productKey;
			double amount, movelim, yearlim;
			CardTransaction ct;
			CreateFileApp fw = null;
			switch(ans) {			
				case "0":
					fw = new CreateFileApp(bankDict.getCCs(), sellers.getSellerList(), bankDict.getLoanMap());
					fw.CreateFile();
					done = true;
					break;
					
				case "1":
					if (returnToMenu("Inserting Seller").equals("2")) {
						break;
					}
					
					String fn, ln, TIN;					
					
					System.out.print("\nEnter Seller ID: ");
					sID = in.nextLine();
					System.out.print("\nEnter first name: ");
					fn = in.nextLine();
					System.out.print("\nEnter last name: ");
					ln = in.nextLine();
					System.out.print("\nEnter TIN: ");
					TIN = in.nextLine();
					new Seller(sID, fn, ln, TIN);
					break;
				
				case "2":										
					if (returnToMenu("Inserting Bank Product").equals("2")) {
						break;
					}
					
					String prod = "";
					while(!prod.equals("1") && !prod.equals("2") && !prod.equals("3")) {
						System.out.print("\nDo you want to insert a loan or a credit card? (1: Loan/2: Credit Card/3: Exit): ");
						prod = in.nextLine().strip();
					}
				
					if (prod.equals("1")) {
						double yr;
						
						System.out.print("\nEnter Loan ID: ");
						pID = in.nextLine();
						System.out.print("\nEnter Loan number: ");
						num = in.nextLine();
						System.out.print("\nEnter TIN: ");
						pTIN = in.nextLine();
						System.out.print("\nEnter amount: ");
						amount = Double.parseDouble(in.nextLine());
						System.out.print("\nEnter yearly rate (eg. 0.05 is 5%): ");
						yr = Double.parseDouble(in.nextLine());
						
						Loan l = new Loan(pID, num, pTIN, 0, amount, yr);
						bankDict.add(pID, l);
						
					} else if (prod.equals("2")) {
						double com;
						
						System.out.print("\nEnter Credit Card ID: ");
						pID = in.nextLine();
						System.out.print("\nEnter Credit Card number: ");
						num = in.nextLine();
						System.out.print("\nEnter TIN: ");
						pTIN = in.nextLine();
						System.out.print("\nEnter commission (eg. 0.05 is 5%): ");
						com = Double.parseDouble(in.nextLine());
						System.out.print("\nEnter max move limit: ");
						movelim = Double.parseDouble(in.nextLine());
						System.out.print("\nEnter max year limit: ");
						yearlim = Double.parseDouble(in.nextLine());
						bankDict.add(pID, new CreditCard(pID, num, pTIN, 0, com, movelim, yearlim));
					}
					break;
				
				case "3":
					if (returnToMenu("Inserting Product Sale").equals("2")) {
						break;
					}
					
					
					System.out.println("\n\t\t(Choose Seller)");
					sellers.printSellers();
					System.out.print("\nEnter Seller Key: ");
					key = Integer.parseInt(in.nextLine());
					
					if(!sellers.sellerExists(key)) {
						System.out.println("No seller with that key found.");
						break;
					}					
					
 					sID = sellers.getSeller(key).getID();
 					
 					
 					System.out.println("\n\t\t(Choose Unsold Product)");
 					if(!products.printProducts()) {
 						System.out.println("No unsold products left. Please first insert a new product.");
 						break;
 					}
 					System.out.print("\nEnter Product Key: ");
 					productKey = Integer.parseInt(in.nextLine());
 					
 					if(!products.productExists(productKey) || products.getProduct(productKey).getSellerKey() != 0) {
 						System.out.println("\nNo unsold product with that key found.");
 						break;
 					}
 					
					pID = products.getProduct(productKey).getID();
					
					System.out.print("\nEnter reason: ");
					reason = in.nextLine();
					
					ProductSale sale = new ProductSale(sID, pID, reason);
					sellers.getSeller(key).addSale(sale);
					
					if(bankDict.containsCard(pID)) {
						bankDict.getCard(pID).setSeller(key);
						break;
					}
					
					bankDict.getLoan(pID).setSeller(key);
					
					sellers.getSeller(key).setCommission("loans", sellers.getSeller(key).getCommission("loans") + bankDict.getLoan(pID).getAmount());
					
					break;
				
				case "4":
					if (returnToMenu("Inserting Credit Card Tansaction").equals("2")) {
						break;
					}
					
					
					System.out.println("\n\t\t(Choose Credit Card)");
 					products.printCards();
 					System.out.print("\nEnter Credit Card Key: ");
 					productKey = Integer.parseInt(in.nextLine());
 					
 					if(!products.productExists(productKey) || products.getProduct(productKey) instanceof Loan) {
 						System.out.println("\nNo Credit Card with that key found.");
 						break;
 					}
 					
					pID = products.getProduct(productKey).getID();
					
					if(bankDict.getCard(pID).getCashFlow() >= bankDict.getCard(pID).getYearLimit()) {
						System.out.printf("\nThis Credit Card has maxed out for the year. (%.2f Euro Yearly Limit Reached)\n", bankDict.getCard(pID).getYearLimit());
						break;
					}
					
					System.out.print("\nEnter Amount: ");
					amount = Double.parseDouble(in.nextLine());
					
					movelim = bankDict.getCard(pID).getMoveLimit();
					yearlim = bankDict.getCard(pID).getYearLimit();
					double cashFlow = bankDict.getCard(pID).getCashFlow();
					
					while(amount > movelim || amount + cashFlow > yearlim) {
						if(amount > movelim) {
							System.out.printf("\nThe amount you entered exceeds the card's max move limit (%.2f Euro)", movelim);
							System.out.print("\nEnter smaller amount: ");
						} else if(amount+cashFlow > yearlim) {
							System.out.printf("\nThis card can only use %.2f Euro for the rest of the year.", yearlim-cashFlow);
							System.out.print("\nEnter smaller amount: ");
						}
						
						amount = Double.parseDouble(in.nextLine());
					}
					
					System.out.print("\nEnter reason: ");
					reason = in.nextLine();
					
					CreditCard temp = bankDict.getCard(pID);
					temp.increaseCashFlow(amount);
					Seller tempSeller = sellers.getSeller(temp.getSellerKey());
					double sellerCommission = tempSeller.getCommission(pID) + amount * temp.getCommission();
					tempSeller.setCommission(pID, sellerCommission);
					
					ct = new CardTransaction(pID, amount, reason);
					bankDict.getCard(pID).addTransaction(ct);
					break;
					
				case "5":
					if (returnToMenu("Showing Loans").equals("2")) {
						break;
					}
					
					for(Loan loan : bankDict.getLoans()) {
						System.out.print("\n-------------------\n");
						if(loan.getSellerKey() != 0) System.out.printf("\nLoan: %s\n\n(Seller)\n%s\n", loan, sellers.getSeller(loan.getSellerKey()));
						else System.out.printf("\nLoan: %s\n\nNot Sold Yet\n", loan);
					}
					System.out.print("\n-------------------\n");
					break;
				
				case "6":
					if (returnToMenu("Computing a Seller's Commission").equals("2")) {
						break;
					}
					
					System.out.println("\n\t\t(Choose Seller)");
					sellers.printSellers();
					System.out.print("\nEnter Seller Key: ");
					key = Integer.parseInt(in.nextLine());
					
					if(!sellers.sellerExists(key)) {
						System.out.println("No seller with that key found.");
						break;
					}
					
					System.out.print("\n-------------------\n");
					System.out.println("\n\t(Seller)");
					computeCommission(sellers.getSeller(key));
					System.out.print("\n-------------------\n");
					break;
				
				case "7":
					if (returnToMenu("Showing Seller-Linked Credit Card Transactions").equals("2")) {
						break;
					}
					
					sellers.printSellers();
					key = Integer.parseInt(in.nextLine());
					
					if(!sellers.sellerExists(key)) {
						System.out.println("No seller with that key found.");
						break;
					}
					
					boolean hasTransactions = false;
					
					ArrayList<ProductSale> salesOfSeller = sellers.getSeller(key).getSales();
					for(ProductSale ps : salesOfSeller) {
						String prodID = ps.getProductID();
						if(bankDict.containsCard(prodID)) {
							for(CardTransaction transaction : bankDict.getCard(prodID).getTransactions()) {
								hasTransactions = true;
								System.out.println("\n" + transaction);
							}
						}
					}
					
					if(!hasTransactions) {
						System.out.println("\nNo Credit Card Transactions linked to this seller.");
					}
					break;
				
				case "8":
					if (returnToMenu("Computing Commission for all Sellers").equals("2")) {
						break;
					}
					
					for(Seller seller : sellers.getSellerList()) {
						System.out.print("\n-------------------\n");
						System.out.println("\n\t(Seller)");
						computeCommission(seller);
					}
					System.out.print("\n-------------------\n");
					break;
					
				case "9":
					if (returnToMenu("Showing the Commission Amount of all Sellers").equals("2")) {
						break;
					}
					
					double total_commissions = 0;
					
					for(Seller seller : sellers.getSellerList()) {
						computeFinalCommission(seller);
						double com = seller.getCommission("total");
						total_commissions += com;
						System.out.print("\n-------------------\n");
						System.out.printf("\n%s\nCommission Given: %.2f Euro\n", seller, com);
					}
					
					System.out.print("\n-------------------\n");
					
					System.out.printf("\nTotal Commissions: %.2f Euro\n", total_commissions);
					break;
				
				case "10":
					fw = new CreateFileApp(bankDict.getCCs(), sellers.getSellerList(), bankDict.getLoanMap());
					fw.CreateFile();
			}
			
			
		}
		
		in.close();
	}
	

}
