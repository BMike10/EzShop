package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EZShop implements EZShopInterface {
	private static Connection conn = null;
	private Map<Integer, ProductType> products = new HashMap<>();
	private Map<Integer, User> users = new HashMap<>();
	private Map<Integer, Customer> customers = new HashMap<>();
    private Map<String, LoyaltyCard> cards = new HashMap<>();
    private Map<LoyaltyCard,Customer> attachedCards = new HashMap<>();
	private User currentUser;
	private AccountBookClass accountBook = new AccountBookClass(0);
	private Map<String,Double> CreditCardsMap = new HashMap<>();
	
    @Override
    public void reset() {
    	try {
    	if(conn == null) {
    		final String url = "jdbc:sqlite:db/ezshop.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            // drop all tables
            String sqlDrop = "drop table ProductTypes;"
            		+ "drop table SoldProducts;"
            		+ "drop table SaleTransactions;"
            		+ "drop table user;"
            		+ "drop table customer;"
            		+ "drop table loyaltyCard;"
            		+ "drop table orders;"
            		+ "drop table returnedProducts;"
            		+ "drop table ReturnTransactions;";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlDrop);
            System.out.println("All tables dropped");
            conn.close();
    	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {    	   	
    	 if(username==null || username.isEmpty()) throw new InvalidUsernameException();
    	 if(password==null || password.isEmpty()) throw new InvalidPasswordException();
    	 if(role==null || role.isEmpty() ) throw new InvalidRoleException();
    	 int id = users.size() + 1;
         User user = new UserClass(id, username, password, RoleEnum.valueOf(role));  
         users.put(id,user);
         String sql = "insert into tableUser( id, username, password, role)"
         		+ "values("+id+","
         		+ "'"+username+"',"
         		+ "'"+password+"',"
         		+"'"+role+")";
         try(Statement st = conn.createStatement()){
         	st.execute(sql);
         }catch(SQLException e) {
         	e.printStackTrace();
         	users.remove(id);
         	return -1;
         }   
     	return id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {   	
    	if(id<=0 ||id==null) throw new InvalidUserIdException();
        if(currentUser==null || !currentUser.getRole().equals("ADMIN")) throw new UnauthorizedException();       
    	User u = users.remove(id);
        String sql = "delete from tableUser"
        		+ " where id = "+id;
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	users.put(id, u);
        	return false;
        }
		return true;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	if(currentUser==null || !currentUser.getRole().equals("ADMIN")) throw new UnauthorizedException(); 
    	List<User> u = new ArrayList<>(users.values());
		return u;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(id<=0 ||id==null) throw new InvalidUserIdException();
    	if(currentUser==null || !currentUser.getRole().equals("ADMIN")) throw new UnauthorizedException(); 
    	for(User user: users.values()) {
    		if(user.getId().equals(id))
    			return user;
    	}
    	return null;
    }
   

    
    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {      
    	if(currentUser==null || !currentUser.getRole().equals("ADMIN")) throw new UnauthorizedException(); 
    	if(id==null || id <= 0) throw new InvalidUserIdException();
       	if(role==null || role.isEmpty()) throw new InvalidRoleException();
       	
       	UserClass user = (UserClass) users.get(id);
        	if(user == null || user.getId() == null)
        		//user doesn't exist
        		return false;  
        		//old role
        	final String tmp = user.getRole(); 
        	user.setRole(role);
    		String sql = "update tableUser"
         		+ "set "
         		+ "role = '"+role+"',"+
         		"where id = "+id;
         try(Statement st = conn.createStatement()){
         	st.execute(sql);
         }catch(SQLException e) {
         	e.printStackTrace();
         user.setRole(role);
         	return false;
         }
     	return true;
    }


    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	if(username==null||username.isEmpty()) throw new InvalidUsernameException();
    	if(password==null||password.isEmpty()) throw new InvalidPasswordException(); 
    	for(User user: users.values()) {
    		if(user.getUsername().equals(username) && user.getPassword().equals(password))
    		  user=currentUser;
    	      connect();
    			return user;   		    
    	}
    	//wrong credentials
    	return null;
    	}

    @Override
    public boolean logout() {
    	if(currentUser==null) {
    		return false;
    	}
        return true;
    }


    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("CASHIER"))
        	throw new UnauthorizedException();
        
    	int nextId = products.keySet().stream().max(Comparator.comparingInt(t->t)).orElse(0) + 1;
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
    	pt.setLocation(p);
    	// db update
    	String sql = "UPDATE ProductTypes SET position = '"+p.toString()+"' where id = "+productId;
    	try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	pt.setLocation(prev);
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
        
        // check for productCode
        ProductType pt = getProductTypeByBarCode(productCode);
        if(pt == null)
        	return -1;
        // add to account book
        int nextId=-1;
        OrderClass o = new OrderClass(productCode, pricePerUnit, quantity);
        nextId = accountBook.addOrder((Order) o);
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
			try {
				accountBook.removeOrder(o.getBalanceId());
			} catch (InvalidTransactionIdException e1) {
				e1.printStackTrace();
			}
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
			try {
				accountBook.removeOrder(o.getOrderId());
			} catch (InvalidTransactionIdException e1) {
				e1.printStackTrace();
			}
			return -1;
		}
    	// update balance
    	if(!recordBalanceUpdate(o.getPricePerUnit() * o.getQuantity())) {
    		// rollback
    		try {
				accountBook.removeOrder(o.getOrderId());
			} catch (InvalidTransactionIdException e) {
				e.printStackTrace();
			}
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
    	o = (OrderClass)accountBook.getOrder(orderId);
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
    	    	String prodCode = products.get(productId).getBarCode();
    	    	OrderClass o = new OrderClass(id, description, amount, date.toLocalDate(), supplier, prodCode, unitPrice, quantity, oStatus);
    	    	orders.add((Order) o);
    		}
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
        return orders;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	 if(customerName==null ||customerName.isEmpty()) throw new InvalidCustomerNameException();
         if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();
    	 int id = customers.size() + 1;
         Customer c = new CustomerClass(id, customerName); 
         customers.put(id,c);
         String sql = "insert into customerTable(id, customerName, customerCard, points)"
         		+ "values("+id+","
         		+"'"+customerName+")";
         try(Statement st = conn.createStatement()){
         	st.execute(sql);
         }catch(SQLException e) {
         	e.printStackTrace();
         	customers.remove(id);
         	return -1;
         }   
     	return id;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(id<=0 ||id==null) throw new InvalidCustomerIdException();
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();

    	Customer c = customers.remove(id);
        String sql = "delete from customerTable"
        		+ " where id = "+id;
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	customers.put(id,c);
        	return false;
        }
		return true;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(id<=0 ||id==null) throw new InvalidCustomerIdException();
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();

    	for(Customer c: customers.values()) {
    		if(c.getId().equals(id))
    			return c;
    	}
    	return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();
    	List<Customer> c = new ArrayList<>(customers.values());  	
    	return c;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();
        String number="card"+(cards.size()+1);       
        LoyaltyCardClass newCard = new LoyaltyCardClass(number,0);
        cards.put(number,newCard);        
        String sql = "insert into loyaltyCard(number,points)"
        		+ "values("+number+","
        		+"'"+0+")";
        try(Statement st = conn.createStatement()){
        	st.execute(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        	cards.remove(number);
        	return "";     	
        }   
    	return number;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	 if(currentUser == null || currentUser.getRole().isEmpty())throw new UnauthorizedException();
    	 if(customerId == null || customerId <= 0) throw new InvalidCustomerIdException();
    	 if(customerCard == null || customerCard.isEmpty())throw new InvalidCustomerCardException();
    	
    	 LoyaltyCard card = cards.get(customerCard);
    	 Customer customer = customers.get(customerId);
    	 if(customer.getId() == null || attachedCards.containsKey(customerCard) ) return false;    	     	 
    	 attachedCards.put(card,customer);  
    	 //creare tabella?
    	 return true;
    	
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	  if(currentUser == null ||currentUser.getRole().isEmpty())
    	    	throw new UnauthorizedException();
    	  //invalidformat? non ho un formato predefinito però bo
    LoyaltyCardClass card= (LoyaltyCardClass) attachedCards.get(customerCard);
    if(card == null) throw new InvalidCustomerCardException();
	boolean updated = card.updatePoints(pointsToBeAdded);
	if(!updated)
		return false;
    String sql = "update loyaltyCard"
    		+ "set "
    		+ "points = "+(card.getPoints())
    		+"where id = "+customerCard;
    try(Statement st = conn.createStatement()){
    	st.execute(sql);
    }catch(SQLException e) {
    	e.printStackTrace();
    	card.updatePoints(-pointsToBeAdded);
    	return false;
    }
    return true;
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

        /*
        if(ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();

        if(cash <= 0)
            throw new InvalidPaymentException();


        SaleTransactionClass saleTransaction = accountBook.getSaleTransaction(ticketNumber);
        //If transaction does not exist return -1?

        double saleAmount= saleTransaction.getMoney();
        double change = cash - saleAmount;

        if (change<0)
            //Cash is not enough
            return -1;

        //Payment ok(Change<=0) -> Update map and db(STATUS)
        //Update Map
        saleTransaction.setStatus("PAYED");;
        //Update DB
        String sql = "UPDATE SaleTransaction SET status = PAYED WHERE id = "+saleTransaction.getId();
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        //Update map and db(Balance)
        recordBalanceUpdate(saleAmount);
        */
		return 0;

	}


	@Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
		/*
    	double userCash = 0;

        if(ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();

        //Check card validity(creditCard consist of 13 or 16 elements)
		if(creditCard ==null || (creditCard.length()!=13 && creditCard.length()!=16)){
			throw new InvalidCreditCardException();
		}else{
			//Luhn Algorithm
			int[] ints = new int[creditCard.length()];
			for (int i = 0; i < creditCard.length(); i++) {
				ints[i] = Integer.parseInt(creditCard.substring(i, i + 1));
			}
			for (int i = ints.length - 2; i >= 0; i = i - 2) {
				int j = ints[i];
				j = j * 2;
				if (j > 9) {
					j = j % 10 + 1;
				}
				ints[i] = j;
			}
			int sum = 0;
			for (int anInt : ints) {
				sum += anInt;
			}
			if (sum % 10 == 0) {
				//VALID CREDIT CARD
				if(CreditCardsMap.get(creditCard)==null || CreditCardsMap.get(creditCard)==0)
					throw new InvalidCreditCardException();
				else
					userCash = CreditCardsMap.get(creditCard);

			} else {
				//INVALID CREDIT CARD
				throw new InvalidCreditCardException();
			}
		}

		//Credit card is validate and registered in the system
		SaleTransaction sale = accountBook.getSaleTransaction(ticketNumber);
		double saleAmount = sale.getPrice();
        if(userCash < saleAmount)
            throw new InvalidCreditCardException();

        //Payment completed -> Update map and db(STATUS)
        //Update Map
        sale.setStatus("CLOSED");;
        //Update DB
        String sql = "UPDATE SaleTransaction SET status ="+SaleTransaction.CLOSED.ordinal() +" WHERE id = "+sale.getId();
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        //Update map and db(Balance)
        recordBalanceUpdate(saleAmount);
        */
		return true;

	}

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        /*
        if(returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();


        ReturnTransaction returnTransaction =  accountBook.getReturnTransaction(returnId);
        //If transaction does not exist return -1?

        String status= returnTransaction.getStatus();

        if (!status.equals("CLOSED"))
            //Return Transaction is not ended
            return -1;

        //Return Transaction is ended-> Update map and db(STATUS)
        //Update Map
        returnTransaction.setStatus("PAYED");;
        //Update DB
        String sql = "UPDATE returnTransaction SET status = PAYED WHERE id = "+returnTransaction.getId();
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        //Update map and db(Balance)
        recordBalanceUpdate(-(returnTransaction.getAmount()));
        */


		return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		/*
    	double newCredit = 0;

        if(returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

		ReturnTransaction returnTransaction = accountBook.getReturnTransaction(returnId);

		//Check card validity(creditCard consist of 13 or 16 elements)
		if(creditCard ==null || (creditCard.length()!=13 && creditCard.length()!=16)){
			throw new InvalidCreditCardException();
		}else{
			//Luhn Algorithm
			int[] ints = new int[creditCard.length()];
			for (int i = 0; i < creditCard.length(); i++) {
				ints[i] = Integer.parseInt(creditCard.substring(i, i + 1));
			}
			for (int i = ints.length - 2; i >= 0; i = i - 2) {
				int j = ints[i];
				j = j * 2;
				if (j > 9) {
					j = j % 10 + 1;
				}
				ints[i] = j;
			}
			int sum = 0;
			for (int anInt : ints) {
				sum += anInt;
			}
			if (sum % 10 == 0) {
				//VALID CREDIT CARD
				if(CreditCardsMap.get(creditCard)==null)
					throw new InvalidCreditCardException();
			} else {
				//INVALID CREDIT CARD
				throw new InvalidCreditCardException();
			}
		}

		//Update Map with new credit -> is return amount only the return part on total saleAmount?
		newCredit = CreditCardsMap.get(creditCard) + returnTransaction.getAmount();
		CreditCardsMap.put(creditCard, newCredit);

        String status= returnTransaction.getStatus();

        if (!status.equals("CLOSED"))
            //Return Transaction is not ended
            return -1;

        //Return Transaction is ended-> Update map and db(STATUS)
        //Update Map
        returnTransaction.setStatus("PAYED");;
        //Update DB
		String sql = "UPDATE ReturnTransaction SET status ="+ReturnStatus.PAYED.ordinal() +" WHERE id = "+returnTransaction.getId();;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        //Update map and db(Balance)
        recordBalanceUpdate(-(returnTransaction.getAmount()));
		*/
    	return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

		//LOGIN
		String newType;
		double currentBalance = accountBook.getBalance();
		double newBalance = currentBalance + toBeAdded;

		//Negative new balance
		if(newBalance < 0)
			return false;

		//Positive new Balance
		if(toBeAdded < 0){
			//DEBIT
			newType = "DEBIT";
            /*
                //Add to Map
                OrderClass newOrderTransaction = new OrderClass();
                accountBook.addOrderTransaction(newOrderTransaction);

                 //Add order operation to DB - (Check that the CloseTransaction doesn't update DB too)
                String sql = "INSERT INTO Order(id, description, amount, date, supplier, status, productId, unitPrice) "
                    + "VALUES ("+newOrderTransaction.getBalanceId()
                    +", NULL, "
                    + toBeAdded +", "
                    + "DATE('now'), "
                    + "supplier ,"
                    + "status ,"
                    + "productId ,"
                    + "unitPrice ,"
                    + "quantity ";
                try(Statement st = conn.createStatement()){
                    st.execute(sql);
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            */
		}else{
			//CREDIT
			newType = "CREDIT";
            /*
                //Add to Map (saleTransactionMap && BalanceOperationMap)
                SaleTransactionClass newSaleTransaction = new SaleTransactionClass();
                accountBook.addSaleTransaction(newSaleTransaction);

                //Add sale transaction to DB -> are there information?
                String sql = "INSERT INTO SaleTransaction(id, description, amount, date, type) "
                    + "VALUES ("+newBalanceOperation.getBalanceId()
                    +", NULL, "
                    + toBeAdded +", "
                    + "DATE('now'), "
                    + "time ,"
                    + "paymentType ,"
                    + "discountRate ,"
                    + "status ,"
                    + "cardId , "
                    + "soldProducts ,"
                try(Statement st = conn.createStatement()){
                    st.execute(sql);
                }catch (SQLException e) {
                    e.printStackTrace();
                }

            */
		}

//		BalanceOperationClass newBalanceOperation = new BalanceOperationClass(toBeAdded,newType);
//		//DB update
//		//Add balance operation to DB
//		String sql = "INSERT INTO BalanceTransaction(id, description, amount, date, type) "
//				+ "VALUES ("+newBalanceOperation.getBalanceId()
//				+", NULL, "
//				+ toBeAdded +", "
//				+ "DATE('now'), "
//				+ OrderStatus.ISSUED.ordinal()+", "
//				+ newType+")";
//
//		try(Statement st = conn.createStatement()){
//			st.execute(sql);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}

		return true;
	}


	@Override
	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
		//LOGIN?

		List<BalanceOperation> balanceOperations = new ArrayList<>();
		LocalDate newFrom = from,newTo = to;
		String sql2 = "";

		if(from!= null && to!= null){
			if(from.isBefore(to)){
				//Order Data Correction
				newFrom = to;
				newTo = from;
			}
		}

		if(newFrom== null && newTo!=null){
			//All balance operations before newTo
			sql2 = "WHERE date <="+newTo;
		}else if(newFrom!= null && newTo==null) {
			//All balance operations after newFrom
			sql2 = "WHERE date >=" + newFrom;
		}else if(newFrom!=null){
			//newFrom!=null && newTo!=null because second condition is always verified thanks to second control
			sql2 = "WHERE date >=" + newFrom + "&& date <=" + newTo;
		}
		//if together are null -> no conditions(all balance operations)

		//Download a list of Balance Operations
		try(Statement stm = conn.createStatement()){
			String sql = "SELECT * FROM BalanceOperation" + sql2;
			ResultSet rs = stm.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String description = rs.getString("description");
				double amount = rs.getDouble("amount");
				LocalDate date =  rs.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				String type = rs.getString("type");
				BalanceOperationClass boc = new BalanceOperationClass(id, description, amount, date, type);
				balanceOperations.add(boc);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}

		return balanceOperations;
	}


	@Override
	public double computeBalance() throws UnauthorizedException {
		//LOGIN
		return accountBook.getBalance();
	}


	public void connect() {
        
        try {
            // db parameters
            final String url = "jdbc:sqlite:db/ezshop.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            createTables();
            System.out.println("Connection to SQLite has been established.");
            Statement stmt = conn.createStatement();
            String insert = "INSERT INTO USER(id, username, password, role) values (1, \"admin\", \"admin\", 2)";
            stmt.execute(insert);
            String query = "SELECT * FROM USER";
            
            ResultSet result = stmt.executeQuery(query);
            while(result.next()) {
            	System.out.println(result.getString("username"));
            }
            
            // read all data
            // USER 
            String sql = "Select * from User";
            ResultSet rs = stmt.executeQuery(sql);
            users = new HashMap<>();
            while(rs.next()) {
            	int id = rs.getInt("id");
            	users.put(id,  new UserClass(id, rs.getString("username"), rs.getString("password"), RoleEnum.values()[rs.getInt("role")]));
            }
            // PRODUCT TYPES
            products = new HashMap<>();
            sql = "select * from ProductTypes";
    		rs = stmt.executeQuery(sql);
    		while(rs.next()) {
    			int id = rs.getInt("id");
    	    	String description = rs.getString("description"); 
    	    	String barcode = rs.getString("barCode");
    			double sellPrice = rs.getDouble("sellPrice");
    			int qty = rs.getInt("quantity");
    			// double discount = rs.getDouble("discountRate");
    			String notes = rs.getString("notes");
    			String position = rs.getString("position");
    			ProductTypeClass pt = new ProductTypeClass(id, description, barcode, sellPrice, notes);
    			pt.setLocation(position);
    			pt.setQuantity(qty);
    			products.put(id,  pt);
    		}
            // ORDERS
            sql = "SELECT * FROM orders";
    		rs = stmt.executeQuery(sql);
    		HashMap<Integer, Order> orders = new HashMap<>();
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
    	    	String prodCode = products.get(productId).getBarCode();
    	    	OrderClass o = new OrderClass(id, description, amount, date.toLocalDate(), supplier, prodCode, unitPrice, quantity, oStatus);
    	    	orders.put(id, (Order) o);
    		}
    		//accountBook.setOrders(orders);
    		// LOYALTY CARDS
    		cards = new HashMap<>();
    		sql = "select * from LoyaltyCard";
    		rs = stmt.executeQuery(sql);
    		while(rs.next()) {
    	    	String number = rs.getString("number"); 
    	    	int points = rs.getInt("points");
    	    	cards.put(number, new LoyaltyCardClass(number, points));
    		}
    		// CUSTOMERS
    		customers = new HashMap<>();
    		sql = "select * from customer";
    		rs = stmt.executeQuery(sql);
    		while(rs.next()) {
    			int id = rs.getInt("id");
    	    	String customerName = rs.getString("customerName"); 
    	    	String cardId = rs.getString("cardId"); 
    	    	LoyaltyCard usrCard = cards.get(cardId);
    	    	CustomerClass c = new CustomerClass(id, customerName);
    	    	c.setCustomerCard(cardId);
    	    	customers.put(id,  c);
    		}
    		// SALE TRANSACTIONS
    		sql = "SELECT * FROM SaleTransactions";
    		rs = stmt.executeQuery(sql);
    		HashMap<Integer, SaleTransaction> sales = new HashMap<>();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    	    	String description = rs.getString("description"); 	 
    	    	double amount = rs.getDouble("amount");
    	    	Date date = rs.getDate("date");
    	    	Time time = rs.getTime("time");
    	    	int status = rs.getInt("status");
    	    	//SaleTransactionClass s = new SaleTransactionClass(id, description, amount, date, time, status);
    	    	List<TicketEntry> entries = new ArrayList<>();
    	    	String sql2 = "select * from SoldProducts where id="+id;
    	    	ResultSet rs1 = stmt.executeQuery(sql2);
    	    	while(rs1.next()) {
    	    		int productId = rs1.getInt("productId");
    	    		int qty = rs1.getInt("quantity");
    	    		double discount = rs1.getDouble("discountRate");
    	    		String pCode = products.get(productId).getBarCode();
    	    		/*TicketEntryClass te = new TicketEntryClass(pCode, qty, discount);
    	    		entries.add(te);*/
    	    	}
    	    	//sales.put(id, s);
    		}
    		accountBook.setSaleTransactionMap(sales);
    		// RETURN TRANSACTION
    		sql = "select * from ReturnTransactions";
    		rs = stmt.executeQuery(sql);
    		HashMap<Integer, ReturnTransaction> returns = new HashMap<>();
    		while(rs.next()) {
    			int id = rs.getInt("id");
    	    	String description = rs.getString("description"); 	 
    	    	double amount = rs.getDouble("amount");
    	    	Date date = rs.getDate("date");
    	    	int status = rs.getInt("status");
    	    	int saleId = rs.getInt("saleId");
    	    	ReturnStatus rstatus = ReturnStatus.values()[status];
    	    	SaleTransaction s = accountBook.getSaleTransaction(id);
    	    	Map<ProductType, Integer> returnedProducts = new HashMap<>();
    	    	String getReturnedProd = "select * from ReturnedProducts where id = "+id;
    	    	ResultSet rs1 = stmt.executeQuery(getReturnedProd);
    	    	while(rs1.next()) {
    	    		int productId = rs1.getInt("productId");
    	    		int qty = rs1.getInt("quantity");
    	    		ProductType pt = products.get(productId);
    	    		returnedProducts.put(pt, qty);
    	    	}
    	    	//ReturnTransactionClass rt = new ReturnTransactionClass(id, description, amount, date.toLocalDate(), "RETURN", returnedProducts, s, rstatus);
    		}
    		accountBook.setReturnTransactionMap(returns);
    		
        } catch (Exception e) {
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
    			+ "number text NOT NULL PRIMARY KEY)";
    	String customerTable = "CREATE TABLE IF NOT EXISTS Customer("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "customerName text NOT NULL,"
    			+ "cardId text NOT NULL,"
    			+ "FOREIGN KEY (cardId) references LoyaltyCard(number))";
    	String productTypes = "CREATE TABLE IF NOT EXISTS ProductTypes("
    			+ "id INTEGER NOT NULL PRIMARY KEY,"
    			+ "barCode text NOT NULL,"
    			+ "description text NOT NULL,"
    			+ "sellPrice number NOT NULL,"
    			+ "quantity integer not null,"
    			//+ "discountRate number not null,"
    			+ "notes text,"
    			+ "position text"
    			+ ")";
    	String soldProduct = "CREATE TABLE IF NOT EXISTS SoldProducts("
    			+ "id INTEGER NOT NULL,"
    			+ "productId integer NOT NULL,"
    			+ "quantity integer NOT NULL,"
    			+ "discountRate number,"
    			+ "PRIMARY KEY(id, productId),"
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
    			+ "id INTEGER NOT NULL,"
    			+ "productId integer NOT NULL,"
    			+ "quantity integer NOT NULL,"
    			+ "PRIMARY KEY(id, productId),"
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
		//BALANCETRANSACTIONTABLE?//
		String balanceTransaction = "CREATE TABLE IF NOT EXISTS BalanceTransaction("
				+ "id INTEGER NOT NULL PRIMARY KEY,"
				+ "description text NOT NULL,"
				+ "amount number NOT NULL,"
				+ "date date NOT NULL,"
				+ "type text not null";
		//BALANCETRANSACTIONTABLE?//
		//ACCOUNTBOOKTABLE?//
		String accountBookTable = "CREATE TABLE IF NOT EXISTS AccountBookTable("
				+ "id INTEGER NOT NULL PRIMARY KEY,"
				+ "balance number NOT NULL,"
				+ "type text not null"
				+ "balanceTransactionId integer not null, "
				+ "returnTransactionId integer not null,"
				+ "orderTransactionId integer not null,"
				+ "saleTransactionId integer not null,"
				+ "FOREIGN KEY (balanceTransactionId) references balanceTransaction(id),"
				+ "FOREIGN KEY (saleTransactionId) references SaleTransactions(id))"
				+ "FOREIGN KEY (orderTransactionId) references orderTransaction(id),"
				+ "FOREIGN KEY (returnTransactionId) references returnTransaction(id))";
		//ACCOUNTBOOKTABLE?//

		//CREDITCARDTABLE//

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
			//stmt.executeUpdate(balanceTransaction);
			//stmt.executeUpdate(balanceTransaction);

		} catch (SQLException e) {
  	      e.printStackTrace();
  	    }
    }
}
