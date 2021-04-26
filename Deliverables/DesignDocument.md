# Design Document 


Authors: 

Date:

Version:


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<discuss architectural styles used, if any>
<report package diagram>

pattern used:
- MVC
- Layered- 3 tiered

```plantuml
package GUI{}
note left of EZShop: Contains application logic and model
package EZShop{}
GUI -- EZShop
```
# Low level design

<for each package, report class diagram>

## Package GUI

```plantuml
```
## Package EZShop

```plantuml
@startuml
class Shop{
    - Map<String, User> users
    - Map<Integer, ProductType> products
    - Map<Integer, Customer> customers
    - Map<String, LoyaltyCard> loyaltyCards
    - AccountBook book
    ' methods
    + login(String username, String password)
    + logout()
    + reset()
       + getAllProductTypes()
    + defineCustomer(String customerName)
    + modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
    + deleteCustomer(Integer id)
    + getCustomer(Integer id)
    + getAllCustomers()
    + createCard()
    + attachCardToCustomer(String customerCard, Integer customerId)
    + modifyPointsOnCard(String customerCard, int pointsToBeAdded)
    + startTicket()
    + addProductToSale(Integer transactionId, String productCode, int amount)
    + deleteProductFromSale(Integer transactionId, String productCode, int amount)
    + applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
    + applyDiscountRateToSale(Integer transactionId, double discountRate)
    + computePointsForSale(Integer transactionId)
    + closeTicket(Integer transactionId)
    + deleteSaleTicket(Integer ticketNumber)
    + getSaleTicket(Integer transactionId)
    + getTicketByNumber(Integer ticketNumber)
    + startReturnTransaction(Integer ticketNumber)
    + returnProduct(Integer returnId, String productCode, int amount)
    + endReturnTransaction(Integer returnId, boolean commit)
    + deleteReturnTransaction(Integer returnId)
    + receiveCashPayment(Integer ticketNumber, double cash)
    + receiveCreditCardPayment(Integer ticketNumber, String creditCard) 
    + returnCashPayment(Integer returnId)
    + returnCreditCardPayment(Integer returnId, String creditCard)
    + createProductType(String desc, String code, double price, String note)
    + updateProduct(int id, String newDesc, String newCode, double newPrice, String newNote)
    + deleteProduct(int id)
    + getProductTypeByBarCode(String barCode)
    + getProductTypesByDescription(String description)
    + updateQuantity(Integer productId, int toBeAdded)
    + updatePosition(Integer productId, String newPos)
    + issueReorder(String productCode, int quantity, double pricePerUnit)
    + payOrderFor(String productCode, int quantity, double pricePerUnit)
    + payOrder(Integer orderId)
    + recordOrderArrival(Integer orderId)
    + getAllOrders()
    + recordBalanceUpdate(double toBeAdded)
    + getCreditsAndDebits(LocalDate from, LocalDate to)
    + computeBalance()
    + createUser(String username, String password, String role)
    + deleteUser(int id)
    + updateUserRights(int id, String role)
    + getAllUsers()
    + getUser(int id)
}
class User{
    - int id
    - String username
    - String password
    - String role
    - Shop shop
}
' interface Role{
' }

Shop -- User


class AccountBook { 
    - Map<Integer, Ticket> ticket
    - Map<Integer, ReturnTransaction> return
    - Map<Integer, Order> order
    '  methods
    + addTicket(Ticket ticket)
    + addReturnTransaction(ReturnTransaction return)
    + addOrder(Order order)
    + getTicket(int id)
    + getReturnTransaction(int id)
    + getOrder(int id)
    

}
AccountBook - Shop
class BalanceOperation {
 - int id
 - String description
 - double amount
 - LocalDate date
}
AccountBook -- "*" BalanceOperation

' class Credit 
' class Debit
' 
' Credit --|> BalanceOperation
' Debit --|> BalanceOperation

'class Sale
'class Return
Order --|> BalanceOperation
Ticket --|> BalanceOperation
'Order --|> Debit
'Sale --|> Credit
'Return --|> Debit


class ProductType{
    - int id
    - String  barCode
    - String description
    - double sellPrice
    - int quantity
    - double discountRate
    - String notes
    - Position position
}

Shop - "*" ProductType

class Ticket {
    - Time time
    - String paymentType
    - double discountRate
    - Map<ProductType, Integer> products
    - Map<ProductType, Double> discountProduct
    - LoyaltyCard card
    + addProduct(ProductType product, int quantity)
    + deleteProduct(ProductType product, int quantity)
    + addProductDiscount(ProductType product, double discount)
    + checkout() 

}
Ticket - "*" ProductType


class LoyaltyCard {
    - int ID
    - int points
}

class Customer {
    - int id
    - String name
    - String surname
    - LoyaltyCard card
}

LoyaltyCard "0..1" - Customer

LoyaltyCard "*"-- Shop
Customer "*"-- Shop
Ticket "*" -- "0..1" LoyaltyCard



class Position {
    - int aisleID
    - int rackID
    - int levelID
}

ProductType - "0..1" Position


class Order {
  - String supplier
  - double pricePerUnit
  - int quantity
  - String status
}

Order "*" - ProductType

class ReturnTransaction {
  -Map<ProductType,Integer> returnProduct
  -Ticket ticket
  + addReturnProduct(ProductType product, int quantity)

}

ReturnTransaction --|> BalanceOperation
ReturnTransaction "*" - Ticket
ReturnTransaction "*" - ProductType

@enduml
```







# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>


| Class| FR1 |FR3 |FR4 |FR5 |FR6 |FR7 |FR8 |
|--|--|--|--|--|--|--|--|
|Shop                   |X|X| | | | | |
|User                   |X| | | | | | |
|AccountBook            | | |X| | | | |
|Cashier                | | | | | | | |
|ShopManager            | |X|X| | | | |
|Admin                  |X| | | | | | |
|BalanceOperation   | | | | | | | |
|Order                  | | |X| | | | |
|Ticket        | | | | | | | |
|ProductType            | |X| | | | | |
|ReturnTransaction      | | | | | | | |
|Customer               | | | | | | | |
|LoyaltyCard            | | | | | | | |
|Position               | | |X| | | | |








# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

