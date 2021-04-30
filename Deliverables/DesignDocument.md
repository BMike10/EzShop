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
@startuml
 package GUI{}
' Do we consider exceptions?

' package EXCEPTIONS{}
' EXCEPTIONS -- EZShop
note left of EZShop: Contains application logic and model
package EZShop{}
package Exceptions{}
GUI -- Exceptions
EZShop -- Exceptions
GUI -- EZShop
@enduml
```
# Low level design

<for each package, report class diagram>
<!--
## Package GUI
-->

## Package EZShop

### All classes are persistent
```plantuml
@startuml
class EZShop{
    - users: Map<Integer, User>
    - products: Map<Integer, ProductType>
    - customers: Map<Integer, Customer>
    - loyaltyCards: Map<String, LoyaltyCard> 
    - creditCards: Map<String, Double>
    - book: AccountBook 
    - currentUser: User 
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
    + startSaleTransaction()
    + addProductToSale(Integer transactionId, String productCode, int amount)
    + deleteProductFromSale(Integer transactionId, String productCode, int amount)
    + applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
    + applyDiscountRateToSale(Integer transactionId, double discountRate)
    + computePointsForSale(Integer transactionId)
    + endSaleTransaction(Integer transactionId)
    + deleteSaleTransaction(Integer transactionId)
    + getSaleTransaction(Integer transactionId)
    ' + getSaleTransactionByNumber(Integer SaleTransactionNumber)
    + startReturnTransaction(Integer transactionId)
    + returnProduct(Integer returnId, String productCode, int amount)
    + endReturnTransaction(Integer returnId, boolean commit)
    + deleteReturnTransaction(Integer returnId)
    + receiveCashPayment(Integer transactionId, double cash)
    + receiveCreditCardPayment(Integer transactionId, String creditCard) 
    + returnCashPayment(Integer returnId)
    + returnCreditCardPayment(Integer returnId, String creditCard)
    + createProductType(String desc, String code, double price, String note)
    + updateProduct(int id, String newDesc, String newCode, double newPrice, String newNote)
    + deleteProduct(int id)
    + getProductTypeByBarCode(String barCode)
    + getProductTypesByDescription(String description)
    + updateQuantity(Integer productId, int toBeAdded)
    + updatePosition(Integer productId, String newPos)
    + issueOrder(String productCode, int quantity, double pricePerUnit)
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
    - checkCreditCardNumber(String number)
}
class User{
    - id: int 
    - username: String 
    - password: String 
    - role: Role 
}
enum Role{
    + CASHIER
    + SHOP_MANAGER
    + ADMIN
}
Role -- User
User - EZShop


class AccountBook { 
    - saleTransactions: Map<Integer, SaleTransaction> 
    - returns: Map<Integer, ReturnTransaction> 
    - orders: Map<Integer, Order> 
    - otherTransactions: List<BalanceOperation>
    - balance: double
    '  methods
    + addSaleTransaction(SaleTransaction SaleTransaction)
    + addReturnTransaction(ReturnTransaction return)
    + addOrder(Order order)
    + addTransaction(BalanceOperation bo)
    + getSaleTransaction(int id)
    + getReturnTransaction(int id)
    + getOrder(int id)
    + updateBalance(double amount)
}
AccountBook - EZShop
class BalanceOperation {
 - id: int
 - description: String
 - amount: double 
 - date: LocalDate
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
SaleTransaction --|> BalanceOperation
'Order --|> Debit
'Sale --|> Credit
'Return --|> Debit


class ProductType{
    - id: int 
    - barCode: String 
    - description: String 
    - sellPrice: double 
    - quantity: int 
    - discountRate: double 
    - notes: String 
    - position: Position 

    + updateQuantity(int qty)
}

EZShop - "*" ProductType

class SaleTransaction {
    - time: Time 
    - paymentType: String 
    - discountRate: double 
    - products: Map<ProductType, Integer> 
    - discountProduct: Map<ProductType, Double> 
    - status: SaleStatus
    - card: LoyaltyCard 
    + addProduct(ProductType product, int quantity)
    + deleteProduct(ProductType product, int quantity)
    + addProductDiscount(ProductType product, double discount)
    + checkout() 

}

enum SaleStatus{
    + CLOSED
    + PAYED
    + STARTED
}
SaleStatus -- SaleTransaction 
SaleTransaction - "*" ProductType


class LoyaltyCard {
    ' - ID: int 
    - points: int 
    - cardCode: String 
}

class Customer {
    - id: int
    - customerName: String 
    - card: LoyaltyCard 
}

LoyaltyCard "0..1" - Customer

LoyaltyCard "*"-- EZShop
Customer "*"-- EZShop
SaleTransaction "*" -- "0..1" LoyaltyCard



class Position {
    -  aisleID: int
    -  rackID: int
    -  levelID: int
}

ProductType - "0..1" Position


class Order {
  -  supplier: String
  -  pricePerUnit:double
  -  quantity: int
  - product: ProductType
  - status: OrderStatus 
  ' - String status
}
enum OrderStatus{
    ' + ISSUED
    + ORDERED
    + PAYED
    + COMPLETED
}
OrderStatus -- Order
Order "*" - ProductType

class ReturnTransaction {
  - returnProduct: Map<ProductType,Integer>
  - saleTransaction: SaleTransaction
  - status: ReturnStatus
  + addReturnProduct(ProductType product, int quantity)

}
enum ReturnStatus{
    + STARTED
    + CLOSED
}
ReturnStatus -- ReturnTransaction
ReturnTransaction --|> BalanceOperation
ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType

@enduml
```







# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>


| Class| FR1 |FR3 |FR4 |FR5 |FR6 |FR7 |FR8 |
|--|--|--|--|--|--|--|--|
|EZShop                   |X|X|X|X|X|X|X|
|User                   |X| | | | | | | 
|AccountBook            | | |X| | |X|X|
|BalanceOperation       | | |X| |X|X|X|
|Order                  | | |X| | | | |
|SaleTransaction        | | | | |X|X| |
|ProductType            | |X|X| |X| | |
|ReturnTransaction      | | | | |X| | |
|Customer               | | | |X| | | |
|LoyaltyCard            | | | |X| | | |
|Position               | | |X| | | | |
<!-- How do we menage FR7(Payment) ? --> 
<!-- Should we enter paymentState in SaleTransaction ? --> 








# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>
### Sequence diagram related to scenario 5.1
```plantuml
@startuml
actor "User" as au
participant EZShop as ezs
participant User as u
' API call
au -> ezs: login()
' role check
ezs -> u:getRole()
u --> ezs:return Role
ezs --> au: return true
@enduml
```

### Sequence diagram related to scenario 7.2
```plantuml
@startuml
actor Cashier as c
participant EZShop as EZS
participant User as u
participant AccountBook as ab

c ->EZS: receiveCreditCardPayment()

EZS -> u:getRole()
u --> EZS:return Role

EZS -> ab:getSaleTransaction()
ab -->EZS:return SaleTransaction

EZS ->EZS:checkCreditCardNumber()
EZS-->c:return false 

@enduml
```

### Sequence diagram related to scenario 3.1
```plantuml
@startuml
actor "Shop Manager" as sm
participant EZShop as ezs
participant User as u
participant Order as o
participant AccountBook as ab
' API call
sm -> ezs: issueOrder()
' role check
ezs -> u:getRole()
u --> ezs:return Role
' order creation
ezs -> o: new
' order insertion into map
ezs -> ab: addOrder()

ezs --> sm: return orderId
@enduml
```

### Sequence diagram related to scenario 6.2
```plantuml
@startuml
'l'actor non può usare i metodi di EZShop ma solo eseguire azioni che gli competono, ad esempio cliccare su un pulsante e cosi via, dopo di che questa si trasforma in una chiamata ad un metodo in ezshop
'Inoltre tutte quelle che rappresentano precondition e post condition non devono essere visualizzate(login..)
actor "Cashier" as c
participant EZShop as ezs
participant SaleTransaction as st
participant ProductType as pt
participant AccoountBook as ab
' start new sale transaction
ezs -> st: startSaleTransaction()
st -> ezs:return transactionID
' get product by barCode
c -> ezs:scan new product by barCode
ezs -> pt:getProductByBarCode()
pt -> ezs:return productId
' add new product to saleTransaction(succesfull)
' update available quantity(succesfull)
ezs -> st:addProductToSale()
st -> ezs:return True
ezs -> pt:updateQuantity()
pt -> ezs:return True
' cashier applies a discount
c -> ezs: apply discountRate
ezs -> pt:getDiscountRate()
pt -> ezs:return discount rate
' apply discount rate to product(succesfull)
ezs -> st:applyDiscountRateToProduct()
st ->ezs:return True
' cashier closes a transaction(succesfull)
c -> ezs: close transaction
ezs -> st: endSaleTransaction()
st -> ezs: return True
'cashier asks for payment type
c -> ezs:ask payment type
group Succesfull Payment(usecase7)
    ezs -> st:payment management
    st -> ezs: return double or boolean
end
'update of balance(succesfull)
c ->ezs: print sale receipt and update balance
ezs -> ab:recordBalanceUpdate()
ab -> ezs:return True

@enduml
```
### Sequence diagram related to scenario 6.4
```plantuml
@startuml
actor "Cashier" as c
participant EZShop as ezs
participant SaleTransaction as st
participant ProductType as pt
participant LoyalityCard as l 
participant AccountBook as a 

' start new sale transaction
c -> ezs: startSaleTransaction()
' get product by barCode
ezs -> pt:getProductByBarCode()
pt --> ezs:return productId
' add new product to saleTransaction(succesfull)
' update available quantity(succesfull)
ezs -> st:addProductToSale()
st --> ezs:return true
ezs -> pt:updateQuantity()
pt --> ezs:return true
' cashier closes a transaction(succesfull)
c -> ezs: endSaleTransaction()
'system asks C for payment type
c <-ezs:ask payment type
group credit card payment(Scenario 7-1)
ezs -> st: receiveCreditcardPayment()
st --> ezs: return true
end
ezs->l:modifyPointsOnCard()
l --> ezs: return true
ezs->a:recordBalanceUpdate()
a-->ezs: return true

@enduml
```
### Sequence diagram related to scenario 6.5
```plantuml
@startuml
actor "Customer" as cust
actor "Cashier" as c
participant EZShop as ezs
participant SaleTransaction as st
participant ProductType as pt
participant AccoountBook as ab
' start new sale transaction
ezs -> st: startSaleTransaction()
st -> ezs:return transactionID
' get product by barCode
c -> ezs:scan new product by barCode
ezs -> pt:getProductByBarCode()
pt -> ezs:return productId
' add new product to saleTransaction(succesfull)
' update available quantity(succesfull)
ezs -> st:addProductToSale()
st -> ezs:return True
ezs -> pt:updateQuantity()
pt -> ezs:return True
' cashier closes a transaction(succesfull)
c -> ezs: close transaction
ezs -> st: endSaleTransaction()
st -> ezs: return True
'cashier asks for payment type
c -> ezs:ask payment type
group Unsuccesfull Payment(usecase7)
    ezs -> st:payment management
    st -> ezs: return double or boolean
end
'Customer cancels the payment
cust -> c: cancel payment
c -> ezs: deleteTransaction
ezs -> st:deleteSaleTransaction()
st -> ezs: return True

@enduml
```

### Sequence diagram related to scenario 8.1
```plantuml
@startuml
actor "Cashier" as c
participant EZShop as ezs
participant User as u
participant ReturnTransaction as rt
participant AccountBook as ab
participant ProductType as pt
' API call
c -> ezs: startReturnTransaction()
' role check
ezs -> u:getRole()
u --> ezs:return Role
' get associated sale 
ezs -> ab: getSaleTransaction()
ab --> ezs: return SaleTransaction
' init transaction
ezs -> rt: new
' insert into account book
ezs -> ab: addReturnTransaction()
ab --> ezs: return returnId
' end
ezs --> c: return returnID

' add products to return 
c -> ezs: returnProduct()
' role check
ezs -> u:getRole()
u --> ezs:return Role
' add product to return t
ezs -> rt: addReturnProduct()
' update quantity available
ezs -> pt: updateQuantity()
' end add product
ezs -> c: return true

' credit card return
c -> ezs: returnCreditCardPayment()
' role check
ezs -> u:getRole()
u --> ezs:return Role
' get transaction and update balance
ezs -> ab: getReturnTransaction()
ab --> ezs: return ReturnTransaction
ezs-> ezs: checkCreditCardNumber()
ezs -> ab: updateBalance()
ezs -> c: return returnTransaction.getAmount()

' close return transaction
c -> ezs: endReturnTransaction()
' role check
ezs -> u:getRole()
u --> ezs:return Role
' get and close transaction
ezs -> ab: getReturnTransaction()
ab --> ezs: return ReturnTransaction
ezs -> rt: setStatus()
' end close return 
ezs --> c: return true
@enduml
```

### Sequence diagram related to scenario 9.1
```plantuml
@startuml
actor "Shop Manager" as sm
participant EZShop as ezs
participant User as u
participant AccountBook as ab

sm -> ezs:getCreditsAndDebits()
' role check
ezs -> u:getRole()
u --> ezs:return Role
' get all operation
ezs -> ab: getSaleTransactions()
ab --> ezs: return Map<Integer, SaleTransaction> 
ezs -> ab: getReturns()
ab --> ezs: return Map<Integer, ReturnTransaction> 
ezs -> ab: getOrders()
ab --> ezs: return Map<Integer, Order> 

ezs -> ab: getOtherTransactions()
ab --> ezs: return List<BalanceOperation>

ezs->sm: return List<BalanceOperation>

@enduml
```

EZS -> u:login()
u --> EZS:return User
