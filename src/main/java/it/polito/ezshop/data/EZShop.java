package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;


public class EZShop implements EZShopInterface {
	private static Connection conn = null;

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        return null;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        return null;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	connect();
        return null;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return null;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        return null;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return null;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
    
    public static void connect() {
        
        try {
            // db parameters
            String url = "jdbc:sqlite:db/ezshop.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            createTables();
            System.out.println("Connection to SQLite has been established.");
            Statement stmt = conn.createStatement();
            String insert = "INSERT INTO USER(id, username, password, role) values (2, \"dfjkskdkj\", \"fkdjkdfjkda\", 1)";
            stmt.execute(insert);
            String query = "SELECT * FROM USER";
            
            ResultSet result = stmt.executeQuery(query);
            while(result.next()) {
            	System.out.println(result.getString("username"));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } /*finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    }
    public static void createTables() {
    	String tableUser = "CREATE TABLE IF NOT EXISTS User("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "username text NOT NULL,"
    			+ "password text NOT NULL,"
    			+ "role INTEGER NOT NULL)";
    	String loyaltyCard = "CREATE TABLE IF NOT EXISTS LoyaltyCard("
    			+ "points INTEGER NOT NULL,"
    			+ "number text NOT NULL PRIMARY KEY,"
    			+ "role INTEGER NOT NULL)";
    	String customerTable = "CREATE TABLE IF NOT EXISTS Customer("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "customerName text NOT NULL,"
    			+ "cardId text NOT NULL,"
    			+ "role INTEGER NOT NULL,"
    			+ "FOREIGN KEY (cardId) references LoyaltyCard(number))";
    	String productTypes = "CREATE TABLE IF NOT EXISTS ProductTypes("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "barCode text NOT NULL,"
    			+ "description text NOT NULL,"
    			+ "sellPrice number NOT NULL,"
    			+ "quantity integer not null,"
    			+ "discountRate number not null,"
    			+ "notes text,"
    			+ "position text"
    			+ ")";
    	String soldProduct = "CREATE TABLE IF NOT EXISTS SoldProducts("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "productId integer NOT NULL,"
    			+ "quantity integer NOT NULL,"
    			+ "discountRate number,"
    			+ "FOREIGN KEY (productId) references ProductTypes(id))";
    	String saleTransaction = "CREATE TABLE IF NOT EXISTS SaleTransactions("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "description text NOT NULL,"
    			+ "amount number NOT NULL,"
    			+ "date date NOT NULL,"
    			+ "time time not null,"
    			+ "paymentType text,"
    			+ "discountRate number,"
    			+ "status integer not null,"
    			+ "cardId text, "
    			+ "soldProducts integer not null,"
    			+ "FOREIGN KEY (soldProducts) references SoldProducts(id),"
    			+ "FOREIGN KEY (cardId) references LoyaltyCard(number))";
    	String orders = "CREATE TABLE IF NOT EXISTS Orders("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "description text NOT NULL,"
    			+ "amount number NOT NULL,"
    			+ "date date NOT NULL,"
    			+ "supplier text,"
    			+ "status integer not null,"
    			+ "productId integer not null,"
    			+ "unitPrice number not null,"
    			+ "quantity integer not null,"
    			+ "FOREIGN KEY (productId) references ProductType(id))";
    	String returnedProduct = "CREATE TABLE IF NOT EXISTS ReturnedProducts("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "productId integer NOT NULL,"
    			+ "quantity integer NOT NULL,"
    			+ "FOREIGN KEY (productId) references ProductTypes(id))";
    	String returnTransaction = "CREATE TABLE IF NOT EXISTS ReturnTransactions("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "description text NOT NULL,"
    			+ "amount number NOT NULL,"
    			+ "date date NOT NULL,"
    			+ "status integer not null,"
    			+ "saleId integer not null, "
    			+ "returnedProductsId integer not null,"
    			+ "FOREIGN KEY (returnedProductsId) references ReturnedProducts(id),"
    			+ "FOREIGN KEY (saleId) references SaleTransactions(id))";
    	try (Statement stmt = conn.createStatement()) {
  	      stmt.executeUpdate(tableUser);
  	      stmt.executeUpdate(loyaltyCard);
  	      stmt.executeUpdate(customerTable);
  	      stmt.executeUpdate(productTypes);
  	      stmt.executeUpdate(soldProduct);
  	      stmt.executeUpdate(saleTransaction);
  	      stmt.executeUpdate(orders);
  	      stmt.executeUpdate(returnedProduct);
  	      stmt.executeUpdate(returnTransaction);
  	    } catch (SQLException e) {
  	      e.printStackTrace();
  	    }
    }
}
