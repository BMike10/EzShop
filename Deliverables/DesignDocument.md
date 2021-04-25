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
}
class User{
    - int id
    - String username
    - String password
    - Role role
    - Shop shop
}
' interface Role{
' }
class Cashier{
    + getAllProductTypes()
    + defineCustomer(String customerName)
    + modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
    + deleteCustomer(Integer id)
    + getCustomer(Integer id)
    + getAllCustomers()
    + createCard()
    + attachCardToCustomer(String customerCard, Integer customerId)
    + modifyPointsOnCard(String customerCard, int pointsToBeAdded)
    + startSaleTransaction()
    + addProductToSale(Integer transactionId, String productCode, int amount)
    + deleteProductFromSale(Integer transactionId, String productCode, int amount)
    + applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
    + applyDiscountRateToSale(Integer transactionId, double discountRate)
    + computePointsForSale(Integer transactionId)
    + closeSaleTransaction(Integer transactionId)
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

}
class ShopManager{
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
}
class Admin{
    + createUser(String username, String password, String role)
    + deleteUser(int id)
    + updateUserRights(int id, String role)
    + getAllUsers()
    + getUser(int id)
}
User -- Cashier
Shop -- User
Cashier <|-- ShopManager
ShopManager <|-- Admin

class AccountBook {
    - Map<Integer, FinancialTransaction> transactions
    '  methods

}
AccountBook - Shop
class FinancialTransaction {
 - int id
 - String description
 - double amount
 - LocalDate date
}
AccountBook -- "*" FinancialTransaction

' class Credit 
' class Debit
' 
' Credit --|> FinancialTransaction
' Debit --|> FinancialTransaction

'class Sale
'class Return
Order --|> FinancialTransaction
SaleTransaction --|> FinancialTransaction
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
}

Shop - "*" ProductType

class SaleTransaction {
    - Time time
    - String paymentType
    - double discountRate
    - Map<ProductType, Integer> products
    - LoyaltyCard card
}
SaleTransaction - "*" ProductType


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

SaleTransaction "*" -- "0..1" LoyaltyCard



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
  -int quantity
  -double returnedValue
}

ReturnTransaction "*" - SaleTransaction
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
|FinancialTransaction   | | | | | | | |
|Order                  | | |X| | | | |
|SaleTransaction        | | | | | | | |
|ProductType            | |X| | | | | |
|ReturnTransaction      | | | | | | | |
|Customer               | | | | | | | |
|LoyaltyCard            | | | | | | | |
|Position               | | |X| | | | |








# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

