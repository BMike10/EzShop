# Integration and API Test Documentation

Authors:

Date:

Version:

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>
```plantuml
@startuml
class EZShop{}
class User{}
class SaleTransaction {}
class LoyaltyCard {}
class Customer {}
class AccountBook {}
class BalanceOperation {}
class ProductType{}
class Position {}
class Order {}
class ReturnTransaction {}
class TicketEntry {}
EZShop --> User
EZShop --> SaleTransaction
EZShop --> ReturnTransaction
EZShop --> ProductType
EZShop --> AccountBook 
EZShop --> LoyaltyCard 
EZShop --> Customer 
EZShop --> Order
EZShop --> BalanceOperation

ProductType --> Position

SaleTransaction --> TicketEntry
TicketEntry --> ProductType 

AccountBook --> SaleTransaction
ReturnTransaction <-AccountBook 
AccountBook -> BalanceOperation
AccountBook --> Order

ReturnTransaction --> ProductType

SaleTransaction --> LoyaltyCard

SaleTransaction --> ProductType
@enduml
```
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    
## Bottom Up


#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1
| Classes  | JUnit test cases |
|--|--|
| Position | it.polito.ezshop.PositionTest|
| LoyaltyCard| it.polito.ezshop.LoyaltyCardClassTest|
| BalanceOperation| it.polito.ezshop.BalanceOperationTest|
| User | it.polito.ezshop.UserClassTest|
| Customer| it.polito.ezshop.CustomerClassTest|
| Order | it.polito.ezshop.OrderClassTest|


## Step 2
| Classes  | JUnit test cases |
|--|--|
| ProductType| it.polito.ezshop.ProductTypeTest|

## Step 3
| Classes  | JUnit test cases |
|--|--|
| ReturnTransaction | it.polito.ezshop.ReturnTransactionTest|
| TicketEntry | it.polito.ezshop.TicketEntryTest|

## Step 4
| Classes  | JUnit test cases |
|--|--|
| SaleTransaction | it.polito.ezshop.SaleTransactionTest|

## Step 5
| Classes  | JUnit test cases |
|--|--|
| AccountBook| it.polito.ezshop.AccountBookTest|
## Step 6  

| Classes  | JUnit test cases |
|--|--|
|EZShop| it.polito.ezshop.ProductAPITest <br> it.polito.ezshop.OrderAPITest |




# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |  
|  2     |  ... |

## Scenario UC1-4

| Scenario |  Delete product type |
| ------------- |:-------------:| 
|  Precondition     | Product type X exists |
|         | ShopManager S exists and is logged in|
|  Post condition     |  Product type X not exists |
| Step#        | Description  |
|  1     |  S select X's record |  
|  2     |  S deletes X |
|  3     |  X is deleted from the system|

## Scenario UC1-5

| Scenario |  List all product types |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
|  Post condition     | List of all product types is displayed |
| Step#        | Description  |
|  1     |  S asks for product list |  
|  2     |  system displays the product list |
## Scenario UC1-6

| Scenario |  Get Product type by barcode |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
|         | Product type X exists|
|  Post condition     | X information are shown |
| Step#        | Description  |
|  1     |  S enters X barcode |  
|  2     |  system searches for X |
|  3     |  System displays X information|
## Scenario UC1-7

| Scenario |  Get product types by description |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
|  Post condition     | List of products that matches entered description is shown  |
| Step#        | Description  |
|  1     |  S fills description |  
|  2     |  System searches for product that matches description |
|  3     |  System displays list of retrieved products|
## Scenario UC3-4

| Scenario |  Issue & Pay order |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
|         | Product type X exists|
|         | Order O not exists |
|         | Balance >= Order.units * Order.pricePerUnit |
|  Post condition     |  Order O is in PAYED state |
|         | Balance -= Order.units * Order.pricePerUnit|
|         | X.units not changed|
| Step#        | Description  |
|  1     |  S creates Order O|  
|  2     |  S fills quantity of product to  be ordered and the price per unit|
|  3     | S register payment done for O|
|  4     | O's state is set to PAYED|
|  5     | O is inserted into the system|

## Scenario UC3-5

| Scenario |  List all orders |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
|  Post condition     | Order list is shown |
| Step#        | Description  |
|  1     |  S asks for order list |  
|  2     |  the system displays Order list |

# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|  1-1         | FR3.1   | it.polito.ezshop.ProductAPITest.testCreateProductType|
|  1-2         | FR4.2   | it.polito.ezshop.ProductAPITest.testUpdateLocation|
|  1-3         | FR3.1   | it.polito.ezshop.ProductAPITest.testUpdateProduct|          
|  1-4         | FR3.2   | it.polito.ezshop.ProductAPITest.testDeleteProductType|
|  1-5         | FR3.3   | it.polito.ezshop.ProductAPITest.testGetAllProductTypes|
|  1-6         | FR3.4   | it.polito.ezshop.ProductAPITest.testGetProductTypeByBarcode |
|  1-7         | FR3.4   | it.polito.ezshop.ProductAPITest.testGetProductTypeByDescription|
|  3-1         | FR4.3   | it.polito.ezshop.OrderAPITest.testIssueOrder |
|  3-4         | FR4.4   | it.polito.ezshop.OrderAPITest.testPayOrderFor |
|  3-3         | FR4.6   | it.polito.ezshop.OrderAPITest.testRecordOrderArrival |
|  3-2         | FR4.5 | it.polito.ezshop.OrderAPITest.testPayOrder|
|  3-5         | FR4.6 | it.polito.ezshop.OrderAPITest.testGetAllOrders|
|  ..          | FRy                             |             |             
| ...          |                                 |             |             
| ...          |                                 |             |             
| ...          |                                 |             |             
| ...          |                                 |             |             



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|           NFR2             | ??? |
|           NFR4             |  it.polito.ezshop.ProductTypeTest.testValidateBarCode         |
|           NFR5             |  it.polito.ezshop.CheckCreditCardTest.testCheckCreditCardNumber|
|           NFR6             | Domain | The customer's card should be a string of 10 digits. | FR5 |


