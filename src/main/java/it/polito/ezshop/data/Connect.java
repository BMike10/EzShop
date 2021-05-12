package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connect {
    private static Connection conn = null;

    public static void connect() {

        try {
            // db parameters
            final String url = "jdbc:sqlite:db/ezshop.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            createTables();
            System.out.println("Connection to SQLite has been established.");
            Statement stmt = conn.createStatement();
            String insert = "INSERT INTO USER(id, username, password, role) values (2, \"admin2\", \"admin\", 0)";
            try {
                stmt.execute(insert);
            }catch(Exception e) {}
            String query = "SELECT * FROM USER";

            ResultSet result = stmt.executeQuery(query);
            while(result.next()) {
                System.out.println(result.getString("username"));
            }
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


        //CREDITCARDTABLE???//

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

    // USER(COMPLETED)
    public static Map<Integer,User> getUsers() {

        Map<Integer, User> users = new HashMap<>();
        try (Statement stmt = conn.createStatement()) {
            String sql = "Select * from User";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                users.put(id,  new UserClass(id, rs.getString("username"), rs.getString("password"), RoleEnum.values()[rs.getInt("role")]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public static boolean addUsers(int id,String username,String password, String role) {
        String sql = "insert into User( id, username, password, role)"
                + "values("+id+","
                + "'"+username+"',"
                + "'"+password+"',"
                + RoleEnum.valueOf(role).ordinal()+")";
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean updateUserRights(int id, String role) {
        String sql = "update User"
                + " set "
                + "role = "+RoleEnum.valueOf(role).ordinal()+
                " where id = "+id;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean deleteUser(int id) {
        String sql = "delete from User"
                + " where id = "+id;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // PRODUCT TYPES(COMPLETED)
    public static Map<Integer,ProductType> getProduct(){
        Map<Integer, ProductType> products = new HashMap<>();
        try (Statement stmt = conn.createStatement()) {
            String sql = "select * from ProductTypes";
            ResultSet rs = stmt.executeQuery(sql);
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
        } catch (SQLException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
            e.printStackTrace();
        }
        return products;
    }
    public static boolean addProduct(int nextId,String productCode,String description,double pricePerUnit,String note){
        String sql = "insert into ProductTypes( id, barcode, description, sellPrice, quantity, notes, position)"
                + "values("+nextId+","
                + "'"+productCode+"',"
                + "'"+description+"',"
                + pricePerUnit+","
                +"0,"
                +(note==null?"NULL,":"'"+note+"',")+
                "NULL)";
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean updateProduct(int id,String newCode,String newDescription,double newPrice,String newNote){
        String sql = "update ProductTypes "
                + "set "
                + "barcode = '"+newCode+"',"
                + "description = '"+newDescription+"',"
                + "sellPrice="+newPrice+","
                +"notes="+(newNote==null?" NULL ":"'"+newNote+"' ")+
                " where id = "+id;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateProductPosition(int productId,Position p){
        String sql = "UPDATE ProductTypes SET position = '"+p.toString()+"' where id = "+productId;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateProductQuantity(int productId,int quantity){
        String sql = "update ProductTypes "
                + "set "
                + "quantity = "+quantity
                +" where id = "+productId;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean removeProduct(int id){
        // db update
        String sql = "delete from ProductTypes where id = "+id;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //LOYALTY CARD(COMPLETED)
    public static Map<String,LoyaltyCard> getLoyaltyCard(){

        Map<String,LoyaltyCard> cards = new HashMap<>();
        try (Statement stmt = conn.createStatement()) {
            String sql = "select * from LoyaltyCard";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String number = rs.getString("number");
                int points = rs.getInt("points");
                cards.put(number, new LoyaltyCardClass(number, points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public static boolean addLoyaltyCard(String number){
        String sql = "insert into loyaltyCard(number,points)"
                + "values("+number+","
                +"'"+0+")";
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateLoyaltyCard(String customerCard,int points){
        String sql = "update loyaltyCard"
                + "set "
                + "points = "+points
                +"where id = "+customerCard;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //CUSTOMERS(COMPLETED)
    public static Map<Integer, Customer> getCustomer(){

        Map<Integer, Customer> customers = new HashMap<>();
        Map<String, LoyaltyCard> cards = getLoyaltyCard();
        try (Statement stmt = conn.createStatement()) {
            String sql = "select * from customer";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customerName");
                String cardId = rs.getString("cardId");
                LoyaltyCard usrCard = cards.get(cardId);
                CustomerClass c = new CustomerClass(id, customerName);
                c.setCustomerCard(cardId);
                customers.put(id,  c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    public static boolean addCustomer(int id,String customerName){
        String sql = "insert into Customer(id, customerName, customerCard, points)"
                + "values("+id+","
                +"'"+customerName+")";
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean updateCustomer(int id,String newCustomerName,String newCustomerCard){
        String sql = "update Customer"
                + "set "
                + "customerName = "+(newCustomerName)
                + "cardId = "+(newCustomerCard)
                +"where id = "+id;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean removeCustomer(int id){

        String sql = "delete from Customer"
                + " where id = "+id;
        try(Statement st = conn.createStatement()){
            st.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Map<Integer, Order> getOrder(){
        // ORDER
        Map<Integer, Order> orders = new HashMap<>();
        Map<Integer, ProductType> products = getProduct();

        try (Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM orders";
            ResultSet rs = stmt.executeQuery(sql);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static Map<Integer, SaleTransaction> getSaleTransaction(){
        // SaleTransaction
        HashMap<Integer, SaleTransaction> sales = new HashMap<>();
        Map<Integer, ProductType> products = getProduct();

        try (Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM SaleTransactions";
            ResultSet rs = stmt.executeQuery(sql);
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
                    ProductType pt = products.get(productId);
                    TicketEntryClass te = new TicketEntryClass(pt.getBarCode(), pt.getProductDescription(), qty, discount, id);
                    entries.add(te);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;

    }

    public static Map<Integer, ReturnTransaction> getReturnTransaction(){
       // RETURN TRANSACTION
        HashMap<Integer, ReturnTransaction> returns = new HashMap<>();
        Map<Integer, SaleTransaction> sales = getSaleTransaction();
        Map<Integer, ProductType> products = getProduct();

        try (Statement stmt = conn.createStatement()) {
            String sql = "select * from ReturnTransactions";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                Date date = rs.getDate("date");
                int status = rs.getInt("status");
                int saleId = rs.getInt("saleId");
                ReturnStatus rstatus = ReturnStatus.values()[status];
                //Is it ok??
                SaleTransaction s = sales.get(saleId);
                //
                Map<ProductType, Integer> returnedProducts = new HashMap<>();
                String getReturnedProd = "select * from ReturnedProducts where id = "+id;
                ResultSet rs1 = stmt.executeQuery(getReturnedProd);
                while(rs1.next()) {
                    int productId = rs1.getInt("productId");
                    int qty = rs1.getInt("quantity");
                    ProductType pt = products.get(productId);
                    returnedProducts.put(pt, qty);
                }
                ReturnTransactionClass rt = new ReturnTransactionClass(id, description, amount, date.toLocalDate(), "RETURN", returnedProducts, s, rstatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returns;
    }

}