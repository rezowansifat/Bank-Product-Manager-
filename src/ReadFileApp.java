import java.util.*;
import java.io.*;

class ReadFileApp {
	private ArrayList<Product> products;
	private ArrayList<Seller> sellers;
	private ArrayList<ProductSale> sales;
	private ArrayList<CardTransaction> transactions;
	
	public ReadFileApp() {
		this.products = new ArrayList<Product>();
		this.sellers = new ArrayList<Seller>();
		this.sales = new ArrayList<ProductSale>();
		this.transactions = new ArrayList<CardTransaction>();
	}
	
	public void ReadFile(String file) {
		BufferedReader reader = null;
		System.out.printf("[Reading from file: %s]\n", file);
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			
			line = (line != null) ? line.trim() : line;
			
			String ID, sID, reason, num, TIN, fn, ln;
			double amount, yr;
			// HashMap to store the input values (eg. for salesman: <"CODE", "1001">
			HashMap<String, String> item = new HashMap<String, String>();
			
			// Stack with "{" and "}" to check when an input block "{...}" ends
			Stack<String> checkBraces = new Stack<String>();
			
			int linenum, linecount;
			linenum = linecount = 1;
			switch (line) {
			case "BANKITEM_LIST":	
				while((line = reader.readLine() ) != null) {
					linecount++;
					line = line.trim();
					
					
					// If there is a type other than BANKITEM in the file it skips lines until its over
					if(checkBraces.size() == 1 && !line.equals("BANKITEM") && !line.equals("{") && !line.equals("}")) {
						System.out.println("\nNon BANKITEM type entry found. Ignoring it...");
						while((line = reader.readLine()) != null && !line.trim().equals("}")) {
							linecount++;
						}
						linecount++;
					}
					
					
					if(line.toUpperCase().equals("BANKITEM")) {
						linenum = linecount;
					}
					
					if(line.equals("{")) {
						checkBraces.push(line);
						
						// Initialize default values
						
						item.put("AFM", "123456789");
						item.put("AMOUNT", "5000");
						item.put("YRATE", "0.05");
						item.put("COM", "0.1");
						item.put("LIM", "5000");
						item.put("YLIM", "50000");
					}
					
					// If stack has a size of 1, it only contains
					// the "{" at the beginning of the file
					// so at that point we are at the end of the file
					// (last "}" character)
					else if(line.equals("}") && checkBraces.size() > 1) {
						
						if(!item.containsKey("DESCR") || !item.containsKey("CODE") || !item.containsKey("TYPE")) {
							System.out.println("\nBANKITEM at line " + linenum + " missing one or more required fields\n");
							item.clear();
							checkBraces.pop();
							continue;
						}
						
						
						checkBraces.pop();
						ID = item.get("CODE");
						num = item.get("DESCR");
						num = (num != null) ? num.substring(0, num.length()-1) : num; // Removes quotes at the end
						TIN = item.get("AFM");
						
						if(item.get("TYPE").equalsIgnoreCase("Loan")) {
							amount = Double.parseDouble(item.get("AMOUNT"));
							yr = Double.parseDouble(item.get("YRATE"));
							products.add(new Loan(ID, num, TIN, 0, amount, yr, true));
						} else {
							double com = Double.parseDouble(item.get("COM"));
							double lim = Double.parseDouble(item.get("LIM"));
							double ylim = Double.parseDouble(item.get("YLIM"));
							products.add(new CreditCard(ID, num, TIN, 0, com, lim, ylim, true));
						}
						item.clear();					
						
					} else if(line.contains(" ")) {
						String[] splited;
						if(line.contains("\"")) splited = line.split(" \"");
						else splited = line.split("\\s+");
						item.put(splited[0].toUpperCase(), splited[1]);
					}
				}
				break;
				
			case "SALES_LIST":
				while((line = reader.readLine() ) != null) {
					linecount++;
					line = line.trim();
					
					
					// If there is a type other than SALE in the file it skips lines until its over
					if(checkBraces.size() == 1 && !line.equals("SALE") && !line.equals("{") && !line.equals("}")) {
						System.out.println("\nNon SALE type entry found. Ignoring it...");
						while((line = reader.readLine()) != null && !line.trim().equals("}")) {
							linecount++;
						}
						linecount++;
					}
					
					
					if(line.toUpperCase().equals("SALE")) linenum = linecount;
					
					if(line.equals("{")) {
						checkBraces.push(line);
						
						// Initialize default values
						
						item.put("REASON", "No reason given");
					}
					
					else if(line.equals("}") && checkBraces.size() > 1) {
						
						if(!item.containsKey("SALESMAN_CODE") || !item.containsKey("BANKITEM_CODE") || !item.containsKey("BANKITEM_TYPE")) {
							System.out.println("\nSALE at line " + linenum + " missing one or more required fields\n");
							item.clear();
							checkBraces.pop();
							continue;
						}
						
						
						checkBraces.pop();
						sID = item.get("SALESMAN_CODE");
						ID = item.get("BANKITEM_CODE");
						reason = item.get("REASON");
						reason = (reason != null) ? reason.substring(0, reason.length()-1) : reason; // Removes quotes at the end
						sales.add(new ProductSale(sID, ID, reason));
						item.clear();					
						
					} else if(line.contains(" ")) {
						String[] splited;
						if(line.contains("\"")) splited = line.split(" \"");
						else splited = line.split("\\s+");
						item.put(splited[0].toUpperCase(), splited[1]);
					}
				}
				break;
				
				
			case "SALESMAN_LIST":
				while((line = reader.readLine() ) != null) {
					linecount++;
					line = line.trim();
					
					
					// If there is a type other than SALESMAN in the file it skips lines until its over
					if(checkBraces.size() == 1 && !line.equals("SALESMAN") && !line.equals("{") && !line.equals("}")) {
						System.out.println("\nNon SALESMAN type entry found. Ignoring it...");
						while((line = reader.readLine()) != null && !line.trim().equals("}")) {
							linecount++;
						}
						linecount++;
					}
					
					
					if(line.toUpperCase().equals("SALESMAN")) linenum = linecount;
					
					if(line.equals("{")) {
						checkBraces.push(line);
						
						// Initialize default values
						
						item.put("AFM", "123456789");
					}
					
					else if(line.equals("}") && checkBraces.size() > 1) {
						
						if(!item.containsKey("CODE") || !item.containsKey("SURNAME") || !item.containsKey("FIRSTNAME")) {
							System.out.println("\nSALESMAN at line " + linenum + " missing one or more required fields\n");
							item.clear();
							checkBraces.pop();
							continue;
						}
						
						
						checkBraces.pop();
						ID = item.get("CODE");
						ln = item.get("SURNAME");
						ln = (ln != null) ? ln.substring(0, ln.length()-1) : ln; // Removes quotes at the end
						
						fn = item.get("FIRSTNAME");
						fn = (fn != null) ? fn.substring(0, fn.length()-1) : fn; // Removes quotes at the end
						
						TIN = item.get("AFM");
						
						sellers.add(new Seller(ID, fn, ln, TIN, true));
						item.clear();
						
					} else if(line.contains(" ")) {
						String[] splited;
						if(line.contains("\"")) splited = line.split(" \"");
						else splited = line.split("\\s+");
						item.put(splited[0].toUpperCase(), splited[1]);
					}
				}
				break;
				
			case "TRN_LIST":
				while((line = reader.readLine() ) != null) {
					linecount++;
					line = line.trim();
					
					
					// If there is a type other than TRN in the file it skips lines until its over
					if(checkBraces.size() == 1 && !line.equals("TRN") && !line.equals("{") && !line.equals("}")) {
						System.out.println("\nNon TRN type entry found. Ignoring it...");
						while((line = reader.readLine()) != null && !line.trim().equals("}")) {
							linecount++;
						}
						linecount++;
					}
					
					
					if(line.toUpperCase().equals("TRN")) linenum = linecount;
					
					if(line.equals("{")) {
						checkBraces.push(line);
						
						// Initialize default values
						
						item.put("JUSTIFICATION", "No justification given");
					}
					
					else if(line.equals("}") && checkBraces.size() > 1) {
						
						if(!item.containsKey("BANKITEM_CODE") || !item.containsKey("VAL")) {
							System.out.println("\nTRN at line " + linenum + " missing one or more required fields\n");
							item.clear();
							checkBraces.pop();
							continue;
						}
						
						
						checkBraces.pop();
						ID = item.get("BANKITEM_CODE");
						amount = Double.parseDouble(item.get("VAL"));
						
						reason = item.get("JUSTIFICATION");
						reason = (reason != null) ? reason.substring(0, reason.length()-1) : reason; // Removes quotes at the end
						
						transactions.add(new CardTransaction(ID, amount, reason));
						item.clear();
						
					} else if(line.contains(" ")) {
						String[] splited;
						if(line.contains("\"")) splited = line.split(" \"");
						else splited = line.split("\\s+");
						item.put(splited[0].toUpperCase(), splited[1]);
					}
				}
				break;
			}
			reader.close();			
		} catch (IOException e) {
			System.out.printf("[Error when reading from file: %s]\nException: [%s]", file, e);
		}
	}
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	public ArrayList<ProductSale> getSales() {
		return sales;
	}
	
	public ArrayList<Seller> getSellers() {
		return sellers;
	}
	
	public ArrayList<CardTransaction> getTransactions() {
		return transactions;
	}
	

}
