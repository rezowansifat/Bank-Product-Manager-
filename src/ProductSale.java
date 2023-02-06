class ProductSale {
    private String sellerID;
    private String productID;
    private String reason;
    
    public ProductSale(String sellerID,String productID,String reason) {
        this.sellerID=sellerID;
        this.productID=productID;
        this.reason = reason;
    }
    
    public String getSellerID() {
        return sellerID;
    }
    
    public String getProductID() {
        return productID;
    }
    
    public String getReason() {
    	return reason;
    }
    
    public String toString() {
    	return "Seller ID: " + sellerID + " | Product ID: " + productID + " | " + "Reason: " + reason;
    }
}