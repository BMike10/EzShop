package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.io.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;


public class EZShop implements EZShopInterface {
	//private static Connection conn = null;
	private Map<Integer, ProductType> products;
	private Map<Integer, User> users;
	private Map<Integer, Customer> customers;
    private Map<String, LoyaltyCard> cards;
    private Map<LoyaltyCard,Customer> attachedCards;
	private User currentUser;
	private AccountBookClass accountBook;
	private Map<String,Double> CreditCardsMap = new HashMap<>();
	
	public EZShop() {
		Connect.connect();
		products = Connect.getProduct();
		users = Connect.getUsers();
		cards = Connect.getLoyaltyCard();
		customers = Connect.getCustomer(cards);
		attachedCards = Connect.getAttachedCard(cards, customers);
		Map<Integer,SaleTransaction> sales = Connect.getSaleTransaction(products, cards);
		accountBook = new AccountBookClass(sales, Connect.getOrder(products), Connect.getReturnTransaction(products, sales));
		try {
			File myObj = new File("creditCard.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(data.startsWith("#"))
					continue;
				System.out.println(data);
				String[] fields = data.split(";");
				CreditCardsMap.put(fields[0],Double.parseDouble(fields[1]));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}


    @Override
    public void reset() {
    	try {
    		Connect.deleteAll();
            accountBook = new AccountBookClass(new HashMap<>(), new HashMap<>(), new HashMap<>());
            products = new HashMap<>();
            /*users = new HashMap<>();
            cards = new HashMap<>();
            customers = new HashMap<>();
            attachedCards = new HashMap<>();*/
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {    	   	
    	 if(username==null || username.isEmpty()) throw new InvalidUsernameException();
    	 if(password==null || password.isEmpty()) throw new InvalidPasswordException();
    	 if(role==null || role.isEmpty() ) throw new InvalidRoleException();
     	int id = users.keySet().stream().max(Comparator.comparingInt(t->t)).orElse(0) + 1;
         User user = new UserClass(id, username, password, RoleEnum.valueOf(role));  
         users.put(id,user);
         if(!Connect.addUsers(id, username, password,role)) {
         	users.remove(id);
         	return -1;
         }   
     	return id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {   	
    	if(id<=0 ||id==null) throw new InvalidUserIdException();
        if(currentUser==null || !currentUser.getRole().equals("Administrator")) throw new UnauthorizedException();       
    	User u = users.remove(id);
    	if(!Connect.deleteUser(id)) {
        	users.put(id, u);
        	return false;
        }
		return true;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	if(currentUser==null || !currentUser.getRole().equals("Administrator")) throw new UnauthorizedException(); 
    	List<User> u = new ArrayList<>(users.values());
		return u;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(id<=0 ||id==null) throw new InvalidUserIdException();
    	if(currentUser==null || !currentUser.getRole().equals("Administrator")) throw new UnauthorizedException(); 
    	for(User user: users.values()) {
    		if(user.getId().equals(id))
    			return user;
    	}
    	return null;
    }
   

    
    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {      
    	if(currentUser==null || !currentUser.getRole().equals("Administrator")) throw new UnauthorizedException(); 
    	if(id==null || id <= 0) throw new InvalidUserIdException();
       	if(role==null || role.isEmpty()) throw new InvalidRoleException();       	
       	UserClass user = (UserClass) users.get(id);
        	if(user == null || user.getId() == null)
        		//user doesn't exist
        		return false;  
        		//old role
        	final String tmp = user.getRole(); 
        	user.setRole(role);
        	if(!Connect.updateUserRights(id,role)) {
        		user.setRole(tmp);
         	return false;
         }
     	return true;
    }


    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	if(username==null||username.isEmpty()) throw new InvalidUsernameException();
    	if(password==null||password.isEmpty()) throw new InvalidPasswordException(); 
    	for(User user: users.values()) {
    		if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
    	      currentUser = user;
    	      return currentUser;   		 
    		}
    	}
    	//wrong credentials
    	return null;
    	}

    @Override
    public boolean logout() {
    	if(currentUser==null) {
    		return false;
    	}
    	currentUser = null;
        return true;
    }


    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
        if(getProductTypeByBarCode(productCode)!=null)
        	return -1;
    	int nextId = products.keySet().stream().max(Comparator.comparingInt(t->t)).orElse(0) + 1;
        ProductType pt = new ProductTypeClass(nextId, description, productCode, pricePerUnit, note);
        products.put(nextId,  pt);
        // add to db
        if(!Connect.addProduct(nextId, productCode, description, pricePerUnit, note)) {
        	products.remove(nextId);
        	return -1;
        }
    	return nextId;
    }

    
    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
        if(id == null || id <= 0)
        	throw new InvalidProductIdException();
    	if(!products.containsKey(id)) 
        	return false;
        
        ProductType pt = new ProductTypeClass(id, newDescription, newCode, newPrice, newNote);
        ProductType tmp = products.get(id);
        int qty = tmp.getQuantity();
        String location = tmp.getLocation();
        pt.setQuantity(qty);
        pt.setLocation(location);
        if(!tmp.getBarCode().equals(newCode) && getProductTypeByBarCode(newCode) != null)
        	return false;
        products.put(id, pt);
        // add to db
        if(!Connect.updateProduct(id, newCode, newDescription, newPrice, newNote)) {
        	// rollback
        	if(tmp != null)
        		products.put(id, tmp);
        	return false;
        }
    	return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
    	if(id == null || id <= 0) 
        	throw new InvalidProductIdException();
    	if(!products.containsKey(id))
    		return false;
        ProductType tmp = products.remove(id);
        // db update 
        if(!Connect.removeProduct(id)) {
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
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
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
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
        
    	List<ProductType> res = new ArrayList<>();
    	if(description == null)
    		description="";
    	for(ProductType pt: products.values()) {
    		if(pt.getProductDescription().contains(description))
    			res.add(pt);
    	}
        return res;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
    	if(productId==null || productId <= 0)
    		throw new InvalidProductIdException();
    	
    	ProductTypeClass pt = (ProductTypeClass) products.get(productId);
    	if(pt == null || pt.getLocation() == null || pt.getLocation().isEmpty())
    		return false;
    	boolean updated = pt.updateQuantity(toBeAdded);
    	if(!updated)
    		return false;
    	if(!Connect.updateProductQuantity(productId, pt.getQuantity())) {
        	pt.updateQuantity(-toBeAdded);
        	return false;
        }
        return true;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
    	if(productId==null || productId <= 0)
    		throw new InvalidProductIdException();
    	
    	ProductTypeClass pt = (ProductTypeClass) products.get(productId);
    	if(pt == null)
    		return false;
    	final Position prev = pt.getPosition();
    	Position p = null;
    	try{
    		p = new Position(newPos);
    	}catch(Exception e) {
    		throw new InvalidLocationException();
    	}
    	// check for uniqueness of position
    	if(p.getAisleId() != -1) {
    		for(ProductType prod:products.values()) {
    			Position tmp = ((ProductTypeClass)prod).getPosition();
    			if(tmp!=null && tmp.equals(p))
    				return false;
    		}
    	}
    	pt.setLocation(p);
    	// db update
    	if(!Connect.updateProductPosition(productId, p)) {
        	pt.setLocation(prev);
        	return false;
        }
        return true;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
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
        //o.setOrderId(nextId);
        if(!Connect.addOrder(nextId, pricePerUnit, quantity, OrderStatus.ISSUED, pt.getId())) {
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
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
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
        nextId = accountBook.addOrder((Order) o);
    	// update balance
        double price = o.getPricePerUnit() * o.getQuantity();
        double currentBalance = accountBook.getBalance();
    	if(currentBalance - price < 0) {
    		// rollback
    		try {
				accountBook.removeOrder(o.getOrderId());
			} catch (InvalidTransactionIdException e) {
				e.printStackTrace();
			}
    		return -1;
    	}else {
    		accountBook.setBalance(currentBalance - price);
    	}
    	// update db
        if(!Connect.addOrder(nextId, pricePerUnit, quantity, OrderStatus.PAYED, pt.getId())) {
			try {
				accountBook.removeOrder(o.getOrderId());
				recordBalanceUpdate(o.getPricePerUnit() * o.getQuantity());
			} catch (InvalidTransactionIdException e1) {
				e1.printStackTrace();
			}
			return -1;
		}
    	return nextId;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException();
    	
    	Order o = null;
    	o = accountBook.getOrder(orderId);
    	if(o == null)
    		throw new InvalidOrderIdException();
    	
    	if(o.getStatus().equals(OrderStatus.PAYED.name()))
    		return false;
    	// update balance
        double price = o.getPricePerUnit() * o.getQuantity();
        double currentBalance = accountBook.getBalance();
    	if(currentBalance - price < 0) {
    		return false;
    	}else {
    		accountBook.setBalance(currentBalance - price);
    	}
    	o.setStatus("PAYED");
    	// save status on db
    	if(!Connect.updateOrderStatus(o.getOrderId().intValue(), OrderStatus.PAYED)) {
			// rollback
    		accountBook.setBalance(currentBalance);
			o.setStatus("ISSUED");
			return false;
		}
        return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
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
    	// position check
    	Position pos = pt.getPosition();
    	if (pos == null || pos.getAisleId()<0)
    		throw new InvalidLocationException();
    	// quantity update
    	try {
			updateQuantity(pt.getId(), o.getQuantity());
		} catch (InvalidProductIdException e) {
			e.printStackTrace();
		}
    	// record on db
		if(!Connect.updateOrderStatus(o.getOrderId().intValue(), OrderStatus.COMPLETED)) {
			// rollback
			try {
			updateQuantity(pt.getId(),-o.getQuantity());
			}catch(Exception e) {e.printStackTrace();}
			o.setStatus("PAYED");
			return false;
		}
		if(!Connect.updateProductQuantity(pt.getId(), pt.getQuantity())) {
			// rollback
			// delete completed status from db
			Connect.updateOrderStatus(o.getOrderId().intValue(), OrderStatus.PAYED);
			try {
			updateQuantity(pt.getId(), -o.getQuantity());
			}catch(Exception e) {e.printStackTrace();}
			o.setStatus("PAYED");
			return false;
		}
        return true;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if(currentUser==null || currentUser.getRole().equals("Cashier"))
        	throw new UnauthorizedException();
        return new ArrayList<>(Connect.getOrder(products).values());
    }


    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	 if(customerName==null ||customerName.isEmpty()) throw new InvalidCustomerNameException();
         if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();        
         if(customers.values().stream().map(e->e.getCustomerName()).anyMatch(e->e==customerName)) return -1;

         int id = customers.keySet().stream().max(Comparator.comparingInt(t->t)).orElse(0) + 1;
          Customer c = new CustomerClass(id, customerName,"",0);
          customers.put(id,c);
          c.setCustomerName(customerName);
          c.setId(id);
  		if(!Connect.addCustomer(id, customerName)) {
         	customers.remove(id);
         	return -1;
         }   
     	return id;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	if(newCustomerName==null ||newCustomerName.isEmpty()) throw new InvalidCustomerNameException();
    	// (newCustomerCard.isEmpty()||!CustomerClass.checkCardCode(newCustomerCard)) throw new InvalidCustomerCardException();
    	if(newCustomerCard==null) throw new InvalidCustomerCardException();
    	if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException(); 	
        CustomerClass c = (CustomerClass) customers.get(id);       
        String prevName= c.getCustomerName();
        String prevCardCode= c.getCustomerCard(); 
        
        if(newCustomerCard.isEmpty())
        {
        //any existing card code connected to the customer will be removed  
        	cards.remove(prevCardCode);
        	c.setCustomerCard("");
        	attachedCards.values().remove(c);   
        }
        
    	c.setCustomerCard(newCustomerCard);
    	c.setCustomerName(newCustomerName);
		if(!Connect.updateCustomer(id, newCustomerName, newCustomerCard)){
         	c.setCustomerName(prevName);
         	c.setCustomerCard(prevCardCode);
         	return false;
         }   
     	return true;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(id<=0 ||id==null) throw new InvalidCustomerIdException();
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();
    	Customer c = customers.remove(id);
		if(!Connect.removeCustomer(id)) {
        	customers.put(id,c);
        	return false;
        }
		return true;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(id<=0 ||id==null) throw new InvalidCustomerIdException();
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException();
        Customer c = customers.get(id);
    	return c;
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
          //String of 10 digits      
        LoyaltyCardClass newCard = new LoyaltyCardClass("",0);
        String number = newCard.createCardCode(10);
        newCard.setCardCode(number);
        cards.put(number,newCard);  
        
		if(!Connect.addLoyaltyCard(number)) {
        	cards.remove(number);
        	return "";     	
        }   
    	return number;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {      	 
    	if(currentUser == null || currentUser.getRole().isEmpty())throw new UnauthorizedException();
    	 if(customerId == null || customerId <= 0) throw new InvalidCustomerIdException();
    	 if(customerCard == null || customerCard.isEmpty()||!CustomerClass.checkCardCode(customerCard))throw new InvalidCustomerCardException();   	
    	 LoyaltyCard card = cards.get(customerCard);
    	 Customer customer = customers.get(customerId);  	 
    	 if(customer.getId() == null || !attachedCards.values().stream().map(e->e.getCustomerCard()).anyMatch(e->e.equals(customerCard)))  return false;   	 
    	
    	 attachedCards.put(card,customer); 
    	 customer.setCustomerCard(customerCard);

    	 return true;
    	
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	  if(currentUser == null ||currentUser.getRole().isEmpty()) throw new UnauthorizedException();
    	  if(customerCard == null || customerCard.isEmpty()||!CustomerClass.checkCardCode(customerCard)) throw new InvalidCustomerCardException();
    	  
    LoyaltyCardClass card= (LoyaltyCardClass) cards.get(customerCard);
    CustomerClass tmp = null;
    if(card == null) throw new InvalidCustomerCardException();
	boolean updated = card.updatePoints(pointsToBeAdded);
	if(!updated)
		return false;	
	for(Customer c: customers.values()) {
	 	   if(c.getCustomerCard().equals(customerCard)) {
	 		   tmp = (CustomerClass) c;
	 		   int tot =((CustomerClass) c).updateCustomerPoints(pointsToBeAdded); 
	 		   c.setPoints(tot);
	 	   }
	 	  } 
	
	if(!Connect.updateLoyaltyCard(customerCard, card.getPoints())) {
    	card.updatePoints(-pointsToBeAdded);
    	if(tmp!= null)
        	tmp.updateCustomerPoints(-pointsToBeAdded);
    	return false;
    }
    return true;
    }

	@Override
	public Integer startSaleTransaction() throws UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		SaleTransaction st=new SaleTransactionClass(new Time(System.currentTimeMillis()), SaleStatus.STARTED);
		int i=accountBook.addSaleTransaction(st);

		//st.setTicketNumber(i);
		return i;
	}

	@Override
	public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();
			
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if (st== null) {
			return false;
		}
		if(amount<=0) throw new InvalidQuantityException();
		ProductType pt = getProductTypeByBarCode(productCode);
		if(pt == null) return false;
		try {
			if(updateQuantity(pt.getId(), -amount)) {
				return st.addProduct(pt, amount);				//CHANGED!
			}
		} catch (InvalidProductIdException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if (st== null) {
			throw new InvalidTransactionIdException();
		}
		if(amount<=0) throw new InvalidQuantityException();
		ProductType pt = getProductTypeByBarCode(productCode);
		if(pt == null) 
			return false;
		st.deleteProduct(pt, amount);
		try {
			return updateQuantity(pt.getId(),amount);
		} catch (InvalidProductIdException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if (st== null || st.getStatus()!=SaleStatus.STARTED ) {
			throw new InvalidTransactionIdException();
		}
		ProductType pt = getProductTypeByBarCode(productCode);
		if(pt == null) throw new InvalidProductCodeException();
		if(discountRate<=0.0 || discountRate>=1.0) throw new InvalidDiscountRateException();
		st.addProductDiscount(pt, discountRate);
		return true;
	}

	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();
		if(discountRate < 0.0 || discountRate >= 1.0)
			throw new InvalidDiscountRateException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if (st == null) {
			return false;
		}
		st.setDiscountRate(discountRate);
		return true;
	}

	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if(st==null) throw new InvalidTransactionIdException();
		return (int)st.getPrice()/10;
	}

	@Override
	public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if (st == null || st.getStatus()==SaleStatus.CLOSED) {
			return false;
		}
		st.checkout();

		if(!Connect.addSaleTransaction(st, st.getBalanceId(), st.getDescription(), st.getPrice(), st.getPaymentType(), st.getDiscountRate(), st.getLoyaltyCard()))
			try {
				accountBook.removeSaleTransaction(st.getBalanceId());
				return false;
			} catch (InvalidTransactionIdException e) {
				e.printStackTrace();
			}
		//recordBalanceUpdate(st.getMoney());
		return true;
	}


	@Override
	public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(saleNumber == null || saleNumber <= 0)
			throw new InvalidTransactionIdException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(saleNumber);
		if (st == null || st.getStatus() == SaleStatus.PAYED) {
			return false;
		}
		if(!Connect.removeSaleTransaction(saleNumber)) {
			return false;
		}
		accountBook.removeSaleTransaction(saleNumber);
		//dovrei reinserire i prodotti acquistati
		for(int i=0; i<st.getEntries().size(); i++) {
			TicketEntryClass te=(TicketEntryClass)st.getEntries().get(i);
			ProductType pt=te.getProductType();
			try {
				this.updateQuantity(pt.getId(), te.getAmount());
			} catch (InvalidProductIdException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(transactionId==null || transactionId<0) throw new InvalidTransactionIdException();
		SaleTransactionClass t=(SaleTransactionClass) accountBook.getSaleTransaction(transactionId);
		if(!t.getStatus().equals(SaleStatus.CLOSED)) return null;
		return t;
	}

	@Override
	public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(saleNumber == null || saleNumber <= 0)
			throw new InvalidTransactionIdException();
		SaleTransactionClass st=(SaleTransactionClass) accountBook.getSaleTransaction(saleNumber);
		if(st==null || (st.getStatus()!=SaleStatus.CLOSED && st.getStatus()!=SaleStatus.PAYED)) return -1;
		ReturnTransaction rt=new ReturnTransactionClass(st, ReturnStatus.STARTED);

		return accountBook.addReturnTransaction(rt);
	}

	@Override
	public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();
		if(productCode == null || productCode.isEmpty() || !ProductTypeClass.validateBarCode(productCode))
			throw new InvalidProductCodeException();
		if(amount <= 0)
			throw new InvalidQuantityException();
		ReturnTransactionClass rt=(ReturnTransactionClass) accountBook.getReturnTransaction(returnId);
		if(rt==null) return false;
		SaleTransactionClass st=(SaleTransactionClass) rt.getSaleTransaction();
		if(!st.getProductsEntries().containsKey(productCode)) return false;
		int q=st.getProductsEntries().get(productCode).getAmount();
		if(q<amount) return false;
		ProductType pt = this.getProductTypeByBarCode(productCode);
		if(pt == null) return false;
		int a=rt.addReturnProduct(pt, amount);
		if(a==-1) return false;
		else return true;
	}

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") && !currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();
		ReturnTransactionClass rt=(ReturnTransactionClass) accountBook.getReturnTransaction(returnId);
		if (rt == null) {
			return false;
		}
		if(commit) {
			SaleTransactionClass st=(SaleTransactionClass)rt.getSaleTransaction();
			rt.setStatus("CLOSED");
			rt.getReturnedProduct().forEach((p,q)->{
				try {
					this.updateQuantity(p.getId(), q);
					st.deleteProduct(p, q);
				} catch (InvalidProductIdException | UnauthorizedException e) {
					e.printStackTrace();
				}
			});
			st.checkout(); 		// compute the new total
			if(!Connect.addReturnTransaction(rt, returnId, "RETURN", rt.getMoney(), ReturnStatus.CLOSED, st.getBalanceId())) {
				//rollback
				rt.getReturnedProduct().forEach((p,q)->{
					try {
						this.updateQuantity(p.getId(), -q);
						st.deleteProduct(p, -q);
					} catch (InvalidProductIdException | UnauthorizedException e) {
						e.printStackTrace();
					}
				});
				return false;
			}
			// update the sale on db
			if(!Connect.removeSaleTransaction(st.getBalanceId()) || 
					!Connect.addSaleTransaction(st, st.getBalanceId(), st.getDescription(), st.getMoney(), st.getPaymentType(), st.getDiscountRate(), st.getLoyaltyCard()))
				System.out.println("Error saving sale update on DB");
			return true;
		}
		else {
			//how should i undo the return transaction?
			accountBook.removeReturnTransaction(returnId);
			return true;
		}
	}
	@Override
	public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
		if(currentUser==null || (!currentUser.getRole().equals("Cashier") && !currentUser.getRole().equals("ShopManager") &&!currentUser.getRole().equals("Administrator")))
			throw new UnauthorizedException();
		if(returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();
		ReturnTransactionClass rt=(ReturnTransactionClass) accountBook.getReturnTransaction(returnId);
		if (rt == null || !rt.getStatus().equals(ReturnStatus.CLOSED.name())) {
			return false;
		}
		if(!Connect.deleteReturnTransaction(rt)) {
			return false;
		}
		SaleTransactionClass st=(SaleTransactionClass) rt.getSaleTransaction();
		Map<String, TicketEntryClass> entries = st.getTicketEntries();
		Map<ProductType, Integer> returnedProducts = rt.getReturnedProduct();
		//put back the products into the transaction
		//for each of the returned products, find the matching ticket entry of the ST and update the amount
		for(ProductType pt: returnedProducts.keySet()) {
			TicketEntryClass te = entries.get(pt.getBarCode());
			te.setAmount(te.getAmount() + returnedProducts.get(pt));
			((ProductTypeClass)pt).updateQuantity(returnedProducts.get(pt));
		}
		st.checkout();
		/*for(int i=0; i<rt.getReturnedProduct().size(); i++) {
			st.getEntries().forEach(e->{
				if(rt.getReturnedProduct().containsKey(( (TicketEntryClass) e).getProductType())) {
					e.setAmount(e.getAmount()+rt.getReturnedProduct().get(( (TicketEntryClass) e).getProductType()));
					//update the quantity of the product
					try {
						this.updateQuantity(this.getProductTypeByBarCode(( (TicketEntryClass) e).getBarCode()).getId(), rt.getReturnedProduct().get(( (TicketEntryClass) e).getProductType()));
					} catch (InvalidProductIdException | UnauthorizedException | InvalidProductCodeException e1) {
						e1.printStackTrace();
					}
				}
			});
		}*/
		accountBook.removeReturnTransaction(returnId);
		return true;
	}

    @Override
    public double receiveCashPayment(Integer transactionId, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

    	//LOGIN
		if(currentUser==null)
			throw new UnauthorizedException();

        if(transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        if(cash <= 0)
            throw new InvalidPaymentException();

        SaleTransaction saleTransaction = accountBook.getSaleTransaction(transactionId);
        if (saleTransaction==null)
        	//Sale Transaction isn't in AccountBook
        	return  -1;

        double saleAmount= ((SaleTransactionClass)saleTransaction).getMoney();
        double change = cash - saleAmount;

        if (change<0)
            //Cash is not enough
            return -1;

        //Payment ok -> Update map and db(STATUS)
        //Update Map
        ((SaleTransactionClass) saleTransaction).setStatus(SaleStatus.valueOf("PAYED"));
        //Update DB
        if(!Connect.updateSaleTransactionStatus(transactionId,SaleStatus.valueOf("PAYED")))
        	return -1;

        //Update map and db(Balance)
        recordBalanceUpdate(saleAmount);

		return change;

	}


	@Override
    public boolean receiveCreditCardPayment(Integer transactionId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		//LOGIN
		if(currentUser==null)
			throw new UnauthorizedException();

        if(transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        //Check card validity(creditCard consist of 13 or 16 elements)
		if(creditCard ==null || creditCard.isEmpty() || (creditCard.length()!=13 && creditCard.length()!=16)){
			throw new InvalidCreditCardException();
		}else{
			//Luhn Algorithm
			checkCreditCardNumber(creditCard);
		}

		//Credit card is validate and registered in the system
		if(!CreditCardsMap.containsKey(creditCard))
			return false;

		double userCash = CreditCardsMap.get(creditCard);

		SaleTransaction sale = accountBook.getSaleTransaction(transactionId);
		if (sale==null)
			return false;

		double saleAmount = sale.getPrice();
        if(userCash < saleAmount)
            return false;

        //Payment completed -> Update map and db(STATUS)
        //Update Map
		((SaleTransactionClass)sale).setStatus(SaleStatus.valueOf("PAYED"));
        //Update DB
		if(!Connect.updateSaleTransactionStatus(transactionId,SaleStatus.valueOf("PAYED")))
			return false;

		//Update map and db(Balance)
        recordBalanceUpdate(saleAmount);
        //Update new CreditCardSale
		updateCreditCardTxt(creditCard,userCash-saleAmount);

		return true;
	}

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

		//LOGIN
		if(currentUser==null)
			throw new UnauthorizedException();

        if(returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        ReturnTransaction returnTransaction =  accountBook.getReturnTransaction(returnId);
        if (returnTransaction==null)
        	return -1;

        String status= returnTransaction.getStatus();

        if (!status.equals("CLOSED"))
            //Return Transaction is not ended
            return -1;

        //Return Transaction is ended-> Update map and db(STATUS)
        //Update Map
        returnTransaction.setStatus("PAYED");
        //Update DB
		if(!Connect.updateReturnTransaction(returnId, ReturnStatus.PAYED)) {
			return -1;
		}

        recordBalanceUpdate(-(((ReturnTransactionClass)returnTransaction).getMoney()));

		return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		//CONTROLLARE
		//LOGIN
		if(currentUser==null)
			throw new UnauthorizedException();

    	double newCredit = 0;

        if(returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        ReturnTransaction returnTransaction = accountBook.getReturnTransaction(returnId);

        //ReturnTransaction does not exist
        if(returnTransaction==null)
        	return -1;

		//Return Transaction is not ended
		String status= returnTransaction.getStatus();
		if (!status.equals("CLOSED"))
			return -1;

        //Credit card is validate and registered in the system
		if(!CreditCardsMap.containsKey(creditCard))
			throw new InvalidCreditCardException();

		//Check card validity(creditCard consist of 13 or 16 elements)
		if(creditCard ==null || creditCard.isEmpty() || (creditCard.length()!=13 && creditCard.length()!=16)){
			throw new InvalidCreditCardException();
		}else{
			//Luhn Algorithm
			checkCreditCardNumber(creditCard);
		}


		newCredit = CreditCardsMap.get(creditCard) + ((ReturnTransactionClass)returnTransaction).getMoney();
        //Return Transaction is ended-> Update map and db
		//Update Map
		returnTransaction.setStatus("PAYED");
		CreditCardsMap.replace(creditCard, newCredit);
		//Update Txt with new credit
		updateCreditCardTxt(creditCard,newCredit);

		//Update DB
		if(!Connect.updateReturnTransaction(returnTransaction.getReturnId(), ReturnStatus.PAYED)) {
			return -1;
		}

        //Update map and db(Balance)
        recordBalanceUpdate(-(((ReturnTransactionClass) returnTransaction).getMoney()));

    	return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

		//LOGIN
		if(currentUser==null || currentUser.getRole().equals("Cashier"))
			throw new UnauthorizedException();

		double currentBalance = accountBook.getBalance();
		double newBalance = currentBalance + toBeAdded;

		//Negative new balance
		if(newBalance < 0)
			return false;

		Connect.balanceUpdate(newBalance);
		accountBook.setBalance(newBalance);
		return true;
	}


	@Override
	public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
		//LOGIN
		if(currentUser==null || currentUser.getRole().equals("Cashier"))
			throw new UnauthorizedException();

		LocalDate newFrom = from;
		LocalDate newTo = to;

		if(from!= null && to!= null){
			if(!from.isBefore(to)){
				//Order Data Correction
				newFrom = to;
				newTo = from;
			}
		}
		return accountBook.getBalanceOperationByDate(newFrom,newTo);
	}

	public void checkCreditCardNumber(String creditCard) throws InvalidCreditCardException {
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

	public void updateCreditCardTxt(String creditCard, double newSale){
		// read file one line at a time
		// replace line as you read the file and store updated lines in StringBuffer
		// overwrite the file with the new lines
		try {
			// input the (modified) file content to the StringBuffer "input"
			BufferedReader file = new BufferedReader(new FileReader("creditCard.txt"));
			StringBuilder inputBuffer = new StringBuilder();
			String line;

			while ((line = file.readLine()) != null)  {
				if(line.startsWith(creditCard))
					line = creditCard+";"+newSale; // replace the line here
				inputBuffer.append(line).append('\n');
			}
			file.close();

			// write the new string with the replaced line OVER the same file
			FileOutputStream fileOut = new FileOutputStream("creditCard.txt");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();

		} catch (Exception e) {
			System.out.println("Problem reading file.");
		}
	}


	@Override
	public double computeBalance() throws UnauthorizedException {
		//LOGIN
		return accountBook.getBalance();
	}

}
