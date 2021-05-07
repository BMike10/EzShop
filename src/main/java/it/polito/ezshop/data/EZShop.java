package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EZShop implements EZShopInterface {
	private static Connection conn = null;
	private Map<Integer, ProductType> products;
	private User currentUser;
	
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
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
        
    	int nextId = products.size() + 1;
        ProductType pt = new ProductTypeClass(nextId, description, productCode, pricePerUnit, note);
        products.put(nextId,  pt);
        // add to db
        String sql = "insert into ProductsTypes( id, barcode, description, sellPrice, quantity, discountRate, notes, position)"
        		+ "values("+nextId+","
        		+ "'"+productCode+"',"
        		+ "'"+description+"',"
        		+ pricePerUnit+","
        		+"0,"
        		+"0,"
        		+(note==null?"NULL,":"'"+note+"',")+
        		"NULL)";
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	products.remove(nextId);
        	return -1;
        }
    	return nextId;
    }

    
    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(!products.containsKey(id)) 
        	throw new InvalidProductIdException();
        if(getProductTypeByBarCode(newCode) != null)
        	return false;
        
        ProductType pt = new ProductTypeClass(id, newDescription, newCode, newPrice, newNote);
        ProductType tmp = products.put(id, pt);
        // add to db
        String sql = "update ProductsTypes"
        		+ "set "
        		+ "barcode = '"+newCode+"',"
        		+ "description = '"+newDescription+"',"
        		+ "sellPrice="+newPrice+","
        		+"notes="+(newNote==null?"NULL,":"'"+newNote+"',")+
        		"where id = "+id;
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	// rollback
        	if(tmp != null)
        		products.put(id, tmp);
        	else
        		products.remove(id);
        	return false;
        }
    	return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(!products.containsKey(id)) 
        	throw new InvalidProductIdException();
    	
        ProductType tmp = products.remove(id);
        // db update 
        String sql = "delete from ProductTypes where id = "+id;
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	// rollback
        	products.put(id, tmp);
        	return false;
        }
        return true;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	if(currentUser == null)
    		throw new UnauthorizedException();
    	
    	List<ProductType> res = new ArrayList<>(products.values());
        return res;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(!ProductTypeClass.validateBarCode(barCode))
    		throw new InvalidProductCodeException();
    	
    	for(ProductType pt: products.values()) {
    		if(pt.getBarCode().equals(barCode))
    			return pt;
    	}
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
        
    	List<ProductType> res = new ArrayList<>();
    	if(description == null || description.length() <= 0)
    		return res;
    	for(ProductType pt: products.values()) {
    		if(pt.getProductDescription().contains(description))
    			res.add(pt);
    	}
        return res;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(productId==null || productId <= 0)
    		throw new InvalidProductIdException();
    	
    	ProductTypeClass pt = (ProductTypeClass) products.get(productId);
    	if(pt == null || pt.getLocation() == null)
    		return false;
    	boolean updated = pt.updateQuantity(toBeAdded);
    	if(!updated)
    		return false;
    	String sql = "update ProductsTypes"
        		+ "set "
        		+ "quantity = "+(pt.getQuantity())
        		+"where id = "+productId;
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	pt.updateQuantity(-toBeAdded);
        	return false;
        }
        return true;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(productId==null || productId <= 0)
    		throw new InvalidProductIdException();
    	
    	ProductTypeClass pt = (ProductTypeClass) products.get(productId);
    	if(pt == null)
    		return false;
    	final Position prev = pt.getPosition();
    	Position p = new Position(newPos);
    	// check for uniqueness of position
    	if(p.getAisleId() == -1) {
    		for(ProductType prod:products.values()) {
    			Position tmp = ((ProductTypeClass)prod).getPosition();
    			if(tmp!=null && tmp.equals(p))
    				return false;
    		}
    	}
    	pt.setPosition(p);
    	// db update
    	String sql = "UPDATE ProductTypes SET position = '"+p.toString()+"' where id = "+productId;
    	try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	pt.setPosition(prev);
        	return false;
        }
        return true;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	// parameters check
    	if(quantity <= 0)
        	throw new InvalidQuantityException();
        if(pricePerUnit <= 0)
        	throw new InvalidPricePerUnitException();
        if(productCode == null || productCode.length() <= 0)
        	throw new InvalidProductCodeException();
        // get next id and check if product exixts
    	/*String getMaxId = "SELECT MAX(id) FROM Orders";
        String getProduct = "SELECT * FROM ProductTypes WHERE barCode = "+productCode;
        int nextId = 1;
        int productId = -1;
        try(Statement stmnt = conn.createStatement()){
        	ResultSet rs = stmnt.executeQuery(getMaxId);
        	if(rs.next())
        		nextId = rs.getInt(0) + 1;
        	rs = stmnt.executeQuery(getProduct);
        	if(!rs.next())
        		throw new InvalidProductCodeException();
        	rs.getInt("id");
        }catch(SQLException e) {
        	e.printStackTrace();
			throw new UnauthorizedException(e.getMessage());
        }*/
        // check for productCode
        ProductType pt = getProductTypeByBarCode(productCode);
        if(pt == null)
        	return -1;
        // add to account book
        int nextId=-1;
        OrderClass o = new OrderClass(productCode, pricePerUnit, quantity);
        //nextId = accountBook.addOrder((Order) o);
        o.setOrderId(nextId);
        // insert into db
    	String sql = "INSERT INTO Orders(id, description, amount, date, status, productId, unitPrice, quantity) "
        		+ "VALUES ("+nextId
        		+", NULL, "
        		+ (pricePerUnit * quantity) +", "
        		+ "DATE('now'), "
        		+ OrderStatus.ISSUED.ordinal()+", "
        		+ pt.getId()+", "
        		+ pricePerUnit+", "
        		+ quantity+")";    	
    	try(Statement st = conn.createStatement()){
    		st.execute(sql);
    	}catch (SQLException e) {
			e.printStackTrace();
			// rollback
			// accountBook.removeOrder(o);
			return -1;
		}
    	return nextId;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	// parameters check
    	if(quantity <= 0)
        	throw new InvalidQuantityException();
        if(pricePerUnit <= 0)
        	throw new InvalidPricePerUnitException();
        if(productCode == null || productCode.length() <= 0)
        	throw new InvalidProductCodeException();
        // check for productCode
        ProductType pt = getProductTypeByBarCode(productCode);
        if(pt == null)
        	return -1;
        // add order to account book
    	int nextId=-1;
    	OrderClass o = new OrderClass(productCode, pricePerUnit, quantity, OrderStatus.PAYED);
        //nextId = accountBook.addOrder((Order) o);
    	o.setOrderId(nextId);
    	// update db
    	String sql = "INSERT INTO Orders(id, description, amount, date, status, productId, unitPrice, quantity) "
        		+ "VALUES ("+nextId
        		+", NULL, "
        		+ (pricePerUnit * quantity) +", "
        		+ "DATE('now'), "
        		+ OrderStatus.ISSUED.ordinal()+", "
        		+ pt.getId()+", "
        		+ pricePerUnit+", "
        		+ quantity+")";    	
    	try(Statement st = conn.createStatement()){
    		st.execute(sql);
    	}catch (SQLException e) {
			e.printStackTrace();
			// rollback
			// accountBook.removeOrder((Order) o);
			return -1;
		}
    	// update balance
    	if(!recordBalanceUpdate(o.getPricePerUnit() * o.getQuantity())) {
    		// rollback
    		// accountBook.removeOrder((Order) o);
    		return -1;
    	}
    	return nextId;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException();
    	
    	Order o = null;
    	// o = accountBook.getOrder(orderId);
    	if(o == null)
    		throw new InvalidOrderIdException();
    	
    	if(o.getStatus().equals(OrderStatus.PAYED.name()))
    		return false;
    	o.setStatus("PAYED");
    	// save status on db
    	String sql = "UPDATE Orders SET status = "+OrderStatus.PAYED.ordinal() + " WHERE id = "+o.getOrderId();
    	try(Statement st = conn.createStatement()){
    		st.execute(sql);
    	}catch (SQLException e) {
			e.printStackTrace();
			// rollback
			o.setStatus("ISSUED");
			return false;
		}
    	if(!recordBalanceUpdate(o.getPricePerUnit() * o.getQuantity())) {
    		// rollback
			o.setStatus("ISSUED");
			return false;
    	}
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException();
    	
    	OrderClass o = null;
    	//o = (OrderClass)accountBook.getOrder(orderId);
    	if(o == null)
    		throw new InvalidOrderIdException();
    	if(o.getOrderStatus() == OrderStatus.ISSUED)
    		return false;
    	if(o.getOrderStatus() == OrderStatus.COMPLETED)
    		return true;
    	
    	String productCode = o.getProductCode();
    	// find product
    	ProductTypeClass pt = null;
    	try {
			 pt = (ProductTypeClass)getProductTypeByBarCode(productCode);
		} catch (InvalidProductCodeException e) {
			e.printStackTrace();
			return false;
		}
    	if(pt == null)
    		return false;
    	// position cehck
    	Position pos = pt.getPosition();
    	if (pos == null || pos.getAisleId()<0)
    		throw new InvalidLocationException();
    	// quantity update
    	pt.updateQuantity(o.getQuantity());
    	// record on db
    	String sql = "UPDATE Orders SET status = "+OrderStatus.COMPLETED.ordinal() + " WHERE id = "+o.getOrderId();
    	try(Statement st = conn.createStatement()){
    		st.execute(sql);
    	}catch (SQLException e) {
			e.printStackTrace();
			// rollback
			pt.updateQuantity(-o.getQuantity());
			o.setStatus("PAYED");
			return false;
		}
    	String sql2 = "UPDATE ProductTyeps SET quantity = "+pt.getQuantity() + " WHERE id = "+pt.getId();
    	try(Statement st = conn.createStatement()){
    		st.execute(sql2);
    	}catch (SQLException e) {
			e.printStackTrace();
			// rollback
			// delete completed status from db
			String sql3 = "UPDATE Orders SET status = "+OrderStatus.PAYED.ordinal() + " WHERE id = "+o.getOrderId();
	    	try(Statement st = conn.createStatement()){
	    		st.execute(sql3);
	    	}catch (SQLException e1) {
				e.printStackTrace();
			}
			pt.updateQuantity(-o.getQuantity());
			o.setStatus("PAYED");
			return false;
		}
        return true;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
        
    	List<Order> orders = new ArrayList<Order>();
    	try(Statement stmnt = conn.createStatement()){
    		String sql = "SELECT * FROM orders";
    		ResultSet rs = stmnt.executeQuery(sql);
    		while(rs.next()) {
    			int id = rs.getInt("id");
    	    	String description = rs.getString("description"); 
    	    	double amount = rs.getDouble("amount");
    	    	Date date = rs.getDate("date");
    	    	String supplier = rs.getString("supplier");
    	    	int status = rs.getInt("status");
    	    	int productId = rs.getInt("productId");
    	    	double unitPrice = rs.getDouble("unitPrice");
    	    	int quantity = rs.getInt("quantity");
    	    	OrderStatus oStatus = OrderStatus.values()[status];
    	    	ResultSet rs1 = stmnt.executeQuery("Select barCode from ProductTypes where id = "+id);
    	    	rs1.next();
    	    	OrderClass o = new OrderClass(id, rs1.getString(0), unitPrice, quantity, oStatus);
    	    	orders.add((Order) o);
    		}
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
        return orders;
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
