# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

 ### **Class *class_name* - method *name***



**Criteria for method *name*:**
	

 - 
 - 





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||


## Class UserClass
### Constructor
**Criteria for method Constructor:**
- Signature of id
- Signature of username
- Signature of password
- Signature of role

**Predicates for method Constructor:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of id| > 0|
|   | <= 0|
| Signature of username| valid|
|   | null|
| Signature of password| valid |
|   | invalid |
| Signature of role| valid|
|   | invalid|
**Boundaries for method Constructor**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id| -inf, 0, +inf|
**Combination of predicates for method Constructor**

 | Signature of id | Signatue of username| Signature of password | Value of role|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|---|------------ | ---------------------------- | --------------- | ------|
| <= 0  | *     | *     |*| Invalid| T1(0,"username","password",RoleEnum.Administrator)->Exception| UserClassTest.testUserClassConstructor|
| *| null | *     | *|Invalid| T2(1,null,"password",RoleEnum.Administrator)->Exception| UserClassTest.testUserClassConstructor|
| *| * | null    | *|Invalid| T2(1,"username",null,RoleEnum.Administrator)->Exception| UserClassTest.testUserClassConstructor|
| *| * | *  | null|Invalid| T3(1,"username","password",null)->Exception| UserClassTest.testUserClassConstructor|
| >0| Valid    | Valid  | Valid | Valid | T4(1,"username","password",RoleEnum.Administrator)|UserClassTest.testUserClassConstructor|

### setUserId
**Criteria for method setUserId:**
- Signature of id

**Predicates for method setUserId:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of id| null or <= 0|
|   |   > 0|

**Boundaries for method setUserId**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id| null, -inf, 0, +inf|

**Combination of predicates for method setProductId**

| Signature of id| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------- | ---------------------------- | --------------- |
| null  or <= 0| Invalid | T1(null)->Exception <br> T1b(-1)->Exception| testSetUserId|
| > 0| Valid | T2(50)| testSetUserId|

### setUsername
- Signature of username
- Length of username

**Predicates for method setUsername:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of username | null|
|   | valid|
| Length of username | 0 |
| | > 0|

**Boundaries for method setUsername**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Length of username | 0, +inf |

**Combination of predicates for method setUsername**
| Signature of username| Length of username| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------- | ---------------------------- | --------------- |
| null  |  -  | Invalid | T1(null) -> Exception | testUsername|
| valid |  0  | Invalid | T2("")->Exception   |testSetUsername|
| Valid | >0  | Valid   | T3("username")         |testSetUsername|

### setPassword
- Signature of password
- Length of password

**Predicates for method setPassword:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of password | null|
|   | valid|
| Length of password | 0 |
| | > 0|

**Boundaries for method setPassword**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Length of password | 0, +inf |

**Combination of predicates for method setPassword**
| Signature of password| Length of password| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------- | ---------------------------- | --------------- |
| null  |  -  | Invalid | T1(null) -> Exception | testSetPassword|
| Valid |  0  | Invalid | T2("")->Exception   |testSetPassword|
| Valid | >0  | Valid   | T3("password")         |testSetPassword|

### setRole
- Signature of role
- Validity of role string

**Predicates for method setRole:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of role | invalid|
|| valid|
|Validity of role string| invalid|
||valid|

**Combination of predicates for method setRole**
| Signature of role |Validity of role string|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------|------------- | ---------------------------- | -----|
| null  | * | Invalid | T1(null) -> Exception | testSetRole|
| Valid | Invalid | Invalid   | T2("Administrator")-> Exception <br>T2b("Cashier")-> Exception<br>T2c("ShopManager")->Exception    |testsetRole|
| Valid | Valid | Valid   | T3("Administrator") <br>T3b("Cashier")<br>T3c("ShopManager")      |testsetRole|

## Class ProductTypeClass
### Method validateBarCode

**Criteria for method validateBarCode:**	
- Signature of String barcode
- Length of barcode
- Presence of not numeric characters in barcode
- Valid barcode

**Predicates for method validateBarCode:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of String barcode | null |
|             | valid |
| Length of barcode | < 12 || > 14|
|             | >= 12 && <= 14 |
| Presence of not numeric characters in barcode         | yes, not spaces          |
|             |     no or only spaces          |
| Valid barcode| yes|
|               | no|


**Boundaries for method validateBarCode**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Length of barcode| 0, 12, 14, +inf             |

 **Combination of predicates for method validateBarCode**

| Signature of String barcode | Length of barcode | Presence of not numeric characters in barcode |Valid barcode| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ----------- | ----------- |----| ------------- | ---------------------------- | --------------- |
|  null       |  *          |  *          | *   |   invalid     |  T1(null) -> false           | testValidateBarCode            |
|  valid      | < 12 || > 14|  *          | *   |   invalid     |   T2("4006381333")->false <br> T2b("40063813339054")->false <br> T2c("")->false|  testValidateBarCode     |
|  valid | >= 12 && <= 14| yes, not spaces| *   | invalid  | T3("asdasdasdsdas")->false     |  testValidateBarCode               |
|  valid | >= 12 && <= 14| no or only spaces    | no  | invalid  | T4("4006381333939")->false <br> T4b("400638133396")->false <br> T4c("4006381333905")->false <br> T4d(" 400 638 133 3901 ")->false                             | testValidateBarCode                |
|  valid | >= 12 && <= 14| no or only spaces    | yes  | valid  | T5("4006381333931")->true <br> T5b("400638133390")->true <br> T5c("4006381333900")->true| testValidateBarcode |


### Constructor
**Criteria for method Constructor:**

- Signature of id
- Signature of description
- Signature of productCode
- Signature of unitPrice

**Predicates for method Constructor:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of id| > 0|
|   | <= 0|
| Signature of description| null|
|   | valid|
| Signature of productCode| invalid (null, empty or not valid)|
|   | valid|
| Signature of unitPrice| > 0|
|   | <= 0|

**Boundaries for method Constructor**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id| -inf, 0, +inf|
| Signature of unitPrice| -inf, 0, +inf|

**Combination of predicates for method Constructor**

| Signature of id | Signature of description | Signature of productCode |Signature of unitPrice|| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ----------- | ----------- |----| ------------- | ---------------------------- | --------------- |

| <= 0 | *  |*  |*  | Invalid | T1(0, "null", "4006381333900", 1.0, null)->Exception | testProductTypeConstructor|
| > 0   | null| *   | * | Invalid | T2(1, null, "4006381333900", 1.0, null)->Exception |testProductTypeConstructor|
| > 0   | valid| invalid| * | Invalid | T3(1, "null", "40063813339", 1.0, null)->Exception|testProductTypeConstructor|
| > 0   | valid| valid| <= 0 | Invalid | T4(1, "null", "4006381333900", -0.0, null)->Exception|testProductTypeConstructor|
| > 0   | valid| valid| > 0 | Valid | T5(1, "null", "4006381333900", 2.0, "notes")->ProductTypeClass|testProductTypeConstructor|
### updateQuantity
**Criteria for method updateQuantity:**
- Quantity + toBeAdded

**Predicates for method updateQuantity:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Quantity + toBeAdded | >= 0|
|   | < 0|

**Boundaries for method updateQuantity**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Quantity + toBeAdded | -inf, 0, +inf|

**Combination of predicates for method updateQuantity**

| Quantity + toBeAdded| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------- | ---------------------------- | --------------- |
| < 0   | Invalid | new ProductTypeClass(); updateQuantity(-1) | testUpdateQuantity|
| >= 0  | Valid   | T2(2)->true <br> T2b(0)->true   |testUpdateQuantity|

### setProductId
**Criteria for method setProductId:**
- Signature of description
- Length of description

**Predicates for method setProductId:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of description | null|
|   | valid|
| Length of description | 0 |
| | > 0|

**Boundaries for method setProductId**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Length of description | 0, +inf |

**Combination of predicates for method setProductId**

| Signature of description| Length of description| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------- | ---------------------------- | --------------- |
| null  |  -  | Invalid | new ProductTypeClass(); setProductId(null) -> Exception | testDescription|
| valid |  0  | Invalid | T2("")->Exception   |testDescription|
| Valid | >0  | Valid   | T3("prova")         |testDescription|

### setProductId
**Criteria for method setProductId:**
- Signature of id

**Predicates for method setProductId:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of id| null or <= 0|
|   |   > 0|

**Boundaries for method setProductId**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id| null, -inf, 0, +inf|

**Combination of predicates for method setProductId**

| Signature of id| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------- | ---------------------------- | --------------- |
| null  or <= 0| Invalid | T1(null)->Exception <br> T1b(-1)->Exception| testProductId|
| > 0| Valid | T2(1231)| testProductId|

### setLocation
**Criteria for method setLocation:**
- Signature of Location
- Valid location

**Predicates for method setLocation:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of Location | null|
|   | valid|
| Valid location | yes|
|       | no|

**Boundaries for method setLocation**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| | |

**Combination of predicates for method setLocation**

| Signature of location| Valid location | Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------- | ---------------------------- | --------------- |
| null | - | Invalid | T1((String)null) <br> T1b((Position)null)|testSetPosition|
| valid| no| Invalid | T2("")|testSetPosition|
| valid| yes| Valid | T3("1_a_1") |testSetPosition|

## Class Position
### Constructor
**Criteria for method Constructor:**
- Valid position
- Fields of location = 3

**Predicates for method Constructor:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Fields of location = 3| yes|
| | no|
| Valid position | yes|
|       | no|

**Boundaries for method Constructor**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| | |

**Combination of predicates for method Constructor**

| Valid position | Fields of location = 3|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------- | ---------------------------- | --------------- |
| yes | no|Valid | T1(null) <br> T2("") | PositionTest.testConstructor|
| "   | yes| Valid| T3("1-b-2") | PositionTest.testConstructor|
| no  | no | Invalid| T4("-a")|PositionTest.testConstructor|
| "   | yes| Invalid| T5("1--4")->Exception <br> T6("a-2-c")->Exception <br> T7("1_2_3_4")->Exception|PositionTest.testConstructor|

### Equals
**Criteria for method Equals:**
- Signature of oth
- Fields of oth Position
- Class of oth

**Predicates for method Equals:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of oth | null|
|       | valid|
| Class of oth| same|
| | different|
| Fields of oth Position| all same|
| | >= 1 different|

**Boundaries for method Equals**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| | |

**Combination of predicates for method Equals**

| Signature of oth | Class of oth|Fields of oth Position|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|---|------------ | ---------------------------- | --------------- |
| null | * | * | Invalid | T1(null)->false| PositionTest.testEquals|
| valid|different| *| Invalid| T2("")->false| PositionTest.testEquals|
| valid| same| >= 1 different| Invalid | Position p1 = new Position("1-a-1"); T3("2-a-1")->false <br> Position p1 = new Position("1-a-1"); T3b("1-b-1")->false <br> Position p1 = new Position("1-a-1"); T3c("1-a-2")->false|PositionTest.testEquals|
|valid|same| all same| Valid| Position p1 = new Position("1-a-1"); T3c("1-a-1")->true|PositionTest.testEquals|
## Class OrderClass
### Constructor
**Criteria for method Constructor:**
- Signature of productCode
- Value of unitPrice
- Value of quantity

**Predicates for method Constructor:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of productCode| null|
| | valid|
| Value of unitPrice | > 0.0|
|       | <= 0.0| 
| Value of quantity| > 0|
| | <= 0|

**Boundaries for method Constructor**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of unitPrice| -inf, 0, +inf |
| Value of quantity| -inf, 0, +inf |

**Combination of predicates for method Constructor**

| Signature of productCode | Value of unitPrice| Value of quantity|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|---|------------ | ---------------------------- | --------------- |
| null | *      | *     | Invalid| T1(1, "ORDER", 10, LocalDate.now(), null, null, 1, 10, OrderStatus.ISSUED)->Exception| OrderClassTest.testConstructor|
| valid| <= 0   | *     | Invalid| T2(1, "ORDER", 10, LocalDate.now(), null, "4006381333931", -0.0, 10, OrderStatus.ISSUED)->Exception| OrderClassTest.testConstructor|
| valid| > 0    | <=0   | Invalid| T3(1, "ORDER", 10, LocalDate.now(), null, "4006381333931", 1, -10, OrderStatus.ISSUED)->Exception|OrderClassTest.testConstructor|
| valid| > 0    | > 0   | Valid | T4(, "ORDER", 10, LocalDate.now(), null, "4006381333931", 1, 10, OrderStatus.ISSUED)|OrderClassTest.testConstructor|

### setOrderId
**Criteria for method setOrderId:**
- Signature of orderId
- Value of orderId

**Predicates for method setOrderId:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of orderId | null|
|       | valid|
| Value of orderId| > 0|
| | <= 0|

**Boundaries for method setOrderId**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of orderId| -inf, 0, +inf |

**Combination of predicates for method setOrderId**

| Signature of orderId | Value of orderId|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------ | ---------------------------- | --------------- |
| null  | - | Invalid| T1(null)->Exception| OrderClassTest.testSetOrderId|
| valid |<= 0| Invalid| T2(-1)->Exception| OrderClassTest.testSetOrderId|
| valid | > 0| Valid  | T3(2)|OrderClassTest.testSetOrderId|
### setQuantity
**Criteria for method setQuantity:**
- Value of quantity

**Predicates for method setQuantity:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Value of quantity| > 0|
| | <= 0|

**Boundaries for method setQuantity**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of quantity| -inf, 0, +inf |

**Combination of predicates for method setQuantity**

| Value of quantity|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------ | ---------------------------- | --------------- |
| <= 0 | Invalid| T1(-1)->Exception| OrderClassTest.testSetQuantity|
| > 0  | Valid| T2(1)| OrderClassTest.testSetQuantity|
### setPricePerUnit
**Criteria for method setPricePerUnit:**
- Value of pricePerUnit

**Predicates for method setPricePerUnit:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Value of pricePerUnit| > 0|
| | <= 0|

**Boundaries for method setPricePerUnit**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of pricePerUnit| -inf, 0, +inf |

**Combination of predicates for method setPricePerUnit**

| Value of pricePerUnit|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------ | ---------------------------- | --------------- |
| <= 0 | Invalid| T1(-0.0)->Exception| OrderClassTest.testSetPricePerUnit|
| > 0  | Valid| T2(2.0)| OrderClassTest.testSetPricePerUnit|

### setProductCode
**Criteria for method setProductCode:**
- Signature of productCode
- Validity of productCode

**Predicates for method setProductCode:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of productCode | null|
|       | valid|
| Validity of productCode| valid|
| | invalid|

**Boundaries for method setProductCode**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| ||

**Combination of predicates for method setProductCode**

| Signature of productCode | Validity of productCode|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------ | ---------------------------- | --------------- |
| null  | -     | Invalid| T1(null)->Exception  | OrderClassTest.testSetProductCode|
| valid | invalid| Invalid| T2("")->Exception   | OrderClassTest.testSetProductCode|
| valid | valid | Valid   | T3("4006381333900") | OrderClassTest.testSetProductCode|

## Class SaleTransactionClass
### Constructor

**Criteria for method Constructor:**

- Signature of price
- Signature of payment type
- Validity of Time
- Validity of SaleStatus
- Validity of loyaltyCard
- Signature of transactionId
- Validity of ticketEntries
- Signature of discount rate

**Predicates for method Constructor:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of price| (> 0)      |
| | <=0      |
| Signature of payment type| null       |
| | valid      |
| Validity of payment type  |   valid |
|   |   invalid |
| Validity of Time | valid |
|                    |  No |
| Validity of SaleStatus | valid      |
| | invalid      |
| Validity of loyaltyCard | valid|
|               | null|
| Signature of transactionId | (>=0) |
|             | <0 |
| Validity of ticketEntries | valid |
|   | null |
| Signature of discount rate | >=0  && <=1|
| | <0 or >1      |

**Boundaries for method Constructor**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of transactionId  |    0, +inf       |
| Signature of price  | 0, +inf  |
| Signature of discount rate  | 0, 1  |

**Combination of predicates for method Constructor**

| Validity of price | Signature of price | Validity of payment type | Signature of payment type | Existence of Time object | Existence of SaleStatus |   Existence of FidelityCard object | Validity of transactionId  | Signature of transactionId | Validity of ticketEntries | Validity of discount rate | Signature of discount rate |  Valid/Invalid | Description of the test case: example of input and output |  JUnit test case  | 
| --------- | --------- | ------- |--------| ------ | -------- | ------- | -------- | -------- | ------- | ------- | ------- | ------- | ------ | ------ |
|   null     |  *       |    *     | * | * | * | * | * | * | * | * | * | * |   Invalid    |   T1(null, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   invalid     |  <=0      |    *     | * | * | * | * | * | * | * | *  | * | * |   Invalid    |   T2(-1, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    null     | * | * | * | * | * | * | * | * | * | * |   Invalid    |   T3(10, null, new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  InvalidPaymentException      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    invalid     | "other" | * | * | * | * | * | * | * | * | * |   Invalid    |   T4(10, "other", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  InvalidPaymentException      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | null | * | * | * | * | * | * | * | * |   Invalid    |   T5(10, "CASH", null, SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | null | * | * | * | * | * | * |   Invalid    |   T6(10, "CASH", new Time(System.currentTimeMillis), null, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | null | * | * | * | * | * |   Invalid    |   T7(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, null, 10, new HashMap<>(), 0.1) ->  InvalidCustomerCardException      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | valid | valid | null | * | * | * |   Invalid    |   T8(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), null, new HashMap<>(), 0.1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | valid | valid | invalid | <= 0 | * | * |   Invalid    |   T9(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), -1, new HashMap<>(), 0.1) ->  InvalidTransactionIdException      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | valid | valid | valid | > 0 | null | * |   Invalid    |   T10(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, null, 0.1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | valid | valid | valid | > 0 | valid | null | * |   Invalid    |   T11(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), null) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | valid | valid | valid | > 0 | valid | invalid | -1 |   Invalid    |   T12(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), -1) ->  Exception      | SaleTransactionTest.testSaleTransactionConstructor  |
|   valid     |  > 0       |    valid     | valid | valid | valid | valid | valid | valid | valid | > 0 | valid | valid | 0.1 |   Invalid    |   T13(10, "CASH", new Time(System.currentTimeMillis), SaleStatus.STARTED, new LoyaltyCard("1234567890", 1), 10, new HashMap<>(), 0.1) ->  new SaleTransactionClass succesfully generated      | SaleTransactionTest.testSaleTransactionConstructor  |


### setTime
**Criteria for method setTime:**
- Validity of time

**Predicates for method setTime:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of time | null |
|       | valid instance of Time.class |

**Boundaries for method setTime**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| | |

**Combination of predicates for method setTime**

| Signature of time | Value of time |Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------ | ---------------------------- | --------------- |
| null  | - | Invalid| T1(null)->Exception | SaleTransactionClassTest.testTime |
| valid | new Time(System.currentTimeMillis) | Valid | T2(System.currentTimeMillis)-> correctly set the time of the Sale Transaction Class object| SaleTransactionClassTest.testTime |

### setStatus
**Criteria for method setStatus:**
- Signature of status

**Predicates for method setStatus:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of status | null|
|       | valid instance of SaleStatus.class |

**Boundaries for method setStatus**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| | |

**Combination of predicates for method setStatus**

| Signature of time | Value of time |Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------ | ---------------------------- | --------------- |
| null  | - | Invalid| T1(null)->Exception | SaleTransactionClassTest.testStatus|
| valid | SaleStatus.STARTED | Valid | T2(SaleStatus.STARTED)-> correctly set the status of the Sale Transaction Class object | SaleTransactionClassTest.testStatus |
| valid | SaleStatus.PAYED | Valid | T2(SaleStatus.PAYED)-> correctly set the status of the Sale Transaction Class object | SaleTransactionClassTest.testStatus |
| valid | SaleStatus.CLOSED | Valid | T2(SaleStatus.CLOSED)-> correctly set the status of the Sale Transaction Class object | SaleTransactionClassTest.testStatus |


### setTicketNumber
**Criteria for method setTicketNumber:**
- Signature of ticketNumber
- Validity of ticketNumber

**Predicates for method setTicketNumber:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of ticket number | null |
|       | valid  |
| Validity of ticket number | <0 |
|       | >0  |

**Boundaries for method setTicketNumber**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of ticketNumber | 0, +inf |

**Combination of predicates for method setTicketNumber**

| Signature of ticketNumber | Value of ticketNumber |Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------ | ---------------------------- | --------------- |
| null  | - | Invalid| T1(null)->InvalidTransactionIdException | SaleTransactionClassTest.testTicketNumber |
| valid | <0 | invalid | T2(-1)->InvalidTransactionIdException | SaleTransactionClassTest.testTicketNumber |
| valid  | >0  | T3(5)-> correctly set the ticketNumber of the Sale Transaction Class object | SaleTransactionClassTest.testTicketNumber |



### setDiscountRate
**Criteria for method setDiscountRate:**
- Signature of discountRate

**Predicates for method setDiscountRate:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of discountRate | <0 |
|       | >1  |
|       | 0<x<1 |

**Boundaries for method setDiscountRate**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of discountRate | 0, 1 |

**Combination of predicates for method setDiscountRate**

| Value of discountRate |Valid/Invalid | Description of the test case | JUnit test case |
| ---|------------ | ---------------------------- | --------------- |
| <0  | Invalid| T1(-1)->InvalidDiscountRateException | SaleTransactionClassTest.testDiscountRate |
| >1 | invalid | T2(1.2)->InvalidTransactionIdException | SaleTransactionClassTest.testDiscountRate |
| 0<x<1 | T3(0.5)-> correctly set the discountRate of the Sale Transaction Class object | SaleTransactionClassTest.testDiscountRate |


### setPaymentType
**Criteria for method setPaymentType:**
- Validity of paymentType
- Signature of paymentType


**Predicates for method setPaymentType:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Validity of paymentType | null |
|                         | valid |
| Signature of paymentType | invalid |
|       | "CREDIT_CARD" |
|       | "CASH" |

**Boundaries for method setPaymentType**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Value of paymentType | "CASH", "CREDIT_CARD" |

**Combination of predicates for method setPaymentType**

| Validity of paymentType | Signature of paymentType | Valid/Invalid | Description of the test case | JUnit test case |
| ---- | ------------ | ---------- | ---------------------------- | --------------- |
| null | * | Invalid | T1(null)->InvalidPaymentException | SaleTransactionClassTest.testPaymentType |
| valid | !="CASH" && !="CREDIT_CARD" | invalid | T2("other")->InvalidPaymentException | SaleTransactionClassTest.testPaymentType |
| valid | "CASH" or "CREDIT_CARD" | T3("CASH")-> correctly set the paymentType of the Sale Transaction Class object | SaleTransactionClassTest.testPaymentType |






## Class AccountBookClass

### Method removeSaleTransaction

**Criteria for method removeSaleTransaction:**
	

- Signature of returnTransactionId
- Validity of returnTransactionId
- Existence of SaleTransaction object

**Predicates for method removeSaleTransaction:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of returnTransactionId | >=0 |
|             | <0 |
| Validity of returnTransactionId| valid|
|               | null|
| Existence of SaleTransaction object | Yes |
|                                     |  No |


**Boundaries for method removeSaleTransaction**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of returnTransactionId  |    -1, 0, +1       |


 **Combination of predicates for method removeSaleTransaction**

|   Validity of returnTransactionId | Signature of returnTransactionId | Existence of SaleTransaction object | Valid/Invalid | Description of the test case: example of input and output |  JUnit test case  | 
| --------- | --------- | ------- |--------| ------ | -------- |
|   null     |  *       |    *    |    Invalid    |   T1(null) ->  InvalidTransactionIdException      |  |
|   valid     |  <0       |   *   |    Invalid    |    T2(-6) -> InvalidTransactionIdException      |  |
|   "     |  (>=)0       |  no      | Invalid    |   removeSaleTransaction(100); removeSaleTransaction(100) ->  InvalidTransactionIdException      |  |
|   "     |  "       |  yes   |   Valid    | removeSaleTransaction(100);  ->  SaleTransaction removed succesfully         |  |

### Method setBalance

**Criteria for method setBalance:**
	
- Signature of amount

**Predicates for method setBalance:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of balance | >=0 |
|             | <0 |

**Boundaries for method setBalance**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of balance |    -1, +1       |


 **Combination of predicates for method setBalance**

 Signature of amount  | Valid/Invalid | Description of the test case: example of input and output |  JUnit test case  | 
| --------- | --------| --------| --------| 
|  <0    | Invalid         |   T1(-500) ->  false     |   |
|  (>=0)    | Valid         |   T1(500) ->  true     |   |

### Method getSaleTransaction

**Criteria for method getSaleTransaction:**


- Signature of SaleTransactionId
- Validity of SaleTransactionId
- Existence of SaleTransaction object

**Predicates for method getSaleTransaction:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of returnTransactionId | >=0 |
|             | <0 |
| Validity of returnTransactionId| valid|
|               | null|
| Existence of SaleTransaction object | Yes |
|                                     |  No |


**Boundaries for method getSaleTransaction**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of SaleTransactionId  |    -5, 0, +5       |


**Combination of predicates for method getSaleTransaction**

|   Validity of returnTransactionId | Signature of returnTransactionId | Existence of SaleTransaction object | Valid/Invalid | Description of the test case: example of input and output |  JUnit test case  | 
| --------- | --------- | ------- |--------| ------ | -------- |
|   null     |  *       |    *    |    Invalid    |   T1(null) ->  InvalidTransactionIdException      |  |
|   valid     |  <0       |   *   |    Invalid    |    T2(-5) -> InvalidTransactionIdException      |  |
|   "     |  (>=)0       |  no      | Invalid    |   removeSaleTransaction(500); getSaleTransaction(500) ->  InvalidTransactionIdException      |  |
|   "     |  "       |  yes   |   Valid    | addSaleTransaction(500); getSaleTransaction(500);  ->  SaleTransaction removed successfully         |  |


## Class BalanceOperation

### BalanceOperationClass(double,String)
**Criteria for method BalanceOperationClass:**
- Signature of money
- Validity of type
- Presence of numeric character in type

**Predicates for method BalanceOperationClass:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of money | =0 |
| | <0 || >0 |
| Presence of numeric character| true |
|       |   false|
| Validity of type | true |
|                   | false|



**Boundaries for method BalanceOperationClass**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of money | 0, 1 |

**Combination of predicates for method BalanceOperationClass**

| Signature of money | Presence of numeric character| Validity of BalanceId | Valid/Invalid |Description of the test case: example of input and output |  JUnit test case  | 
| ----------- | ---| ---|------------- | -------- | ------- |
| =0  |  *  | *  | Invalid | T1(-1)  |     |
| <0 OR >0 |  yes  | ---| Invalid | T2(deb1t)   |   |
| " | no  | no | Invalid   | T3(ciao)         ||
| " | "  | yes | Valid   | T4(credit)         ||


### setBalanceId
**Criteria for method setBalanceId:**
- Signature of BalanceId
- Validity of BalanceId

**Predicates for method setBalanceId:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of BalanceId | <0 |
| | >= 0|
| Validity of BalanceId | null|
|   | valid|


**Boundaries for method setBalanceId**:

| Criterion   | Boundary values |
| ----------- | --------------- |
|  |  |

**Combination of predicates for method setBalanceId**

| Signature of BalanceId | Validity of BalanceId | Valid/Invalid |Description of the test case: example of input and output |  JUnit test case  | 
| ----------- | ---|------------- | -------- | ------- |
| <0  |  *  | Invalid | T1(-1)  |     |
| * |  null  | Invalid | T2(null)   |   |
| >0 | valid  | Valid   | T3(10)         ||

### setDescription
**Criteria for method setDescription:**
- Validity of Description
- Length of string

**Predicates for method setDescription:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Validity of Description | null |
| | valid|
| Length of string | <=1000 |
|   | (>1000) |



**Boundaries for method setDescription**:

| Criterion   | Boundary values |
| ----------- | --------------- |


**Combination of predicates for method setDescription**

| Validity of Description | Length of string | Valid/Invalid |Description of the test case: example of input and output |  JUnit test case  | 
| --- | ------ |------------- | -------- | ------- |
| null  |  *  | Invalid | T1(null; error)  |     |
| valid | (>1000)  | Invalid   | T2("cia90......";)   |   |
| "    | <=1000 | Valid   | T3("Nuova transazione")         |   |

## Class ReturnTransactionClass

### setStatus
**Criteria for method setStatus:**
- Validity of status
- Status allowed
- String contains only characters

**Predicates for method setStatus:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Validity of type | null |
| | valid|
| Type allowed | false |
|   | true |
| String contains only characters  |  false  |
|   |  true  |


**Boundaries for method setStatus**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Type allowed | "finish","payed" |
| String contains only characters | "closed50",'closed' |

**Combination of predicates for method setStatus**

| Validity of type | Type allowed | String contains only character | Valid/Invalid |Description of the test case: example of input and output |  JUnit test case  | 
| ----------- | --- | ------ |------------- | -------- | ------- |
| null  |  *  |  *  | Invalid | T1(null; error)  |     |
| valid | no  |  *  | Valid   | T2("pa1ed"; no output)   |   |
|       |       |       |       | T2b(closed50)  |   |
| *     | yes |  yes   | Valid   | T3b("closed")         |   |

## Class CustomerClass
### Method CheckCardCode
**Criteria for method  checkCardCode:**

- Signature of String newCustomerCard
- Length of newCustomerCard
- Valid newCustomerCard

**Predicates for method checkCardCode:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of String newCustomerCard | null |
|             | valid |
| Length of barcode | 10 |
|             | > 10 && < 10 |
| Valid newCustomerCard| yes|
|               | no|

**Boundaries for method checkCardCode**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Length of newCustomerCard| 0, 10,+inf   

**Combination of predicates for method checkCardCode**

| Signature of newCardCode | Length of newCardCode || Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ----------- | ----------- | ------------- | ---------------------------- | --------------- |
|  null       |  *          |           |   invalid     |  T1(null) -> false           | testcheckCardCode    
|  valid | 0|   | invalid  | T2("")->false     |  testcheckCardCode               |
|  valid | >10 |   | invalid  | T4("abcde123456")->false  |testcheckCardCode                           | 
|  valid | <10 |   | invalid  | T4("abcde1234")->false                             | testcheckCardCode               |
|  valid | 10 |   | valid  | T5("abcde12345")->true <br> T5b("AN34d5vtA1")->true |<br>testcheckCardCode |


### Constructor
**Criteria for method Constructor:**
- Signature of id
- Signature of customerName
- Signature of customerCard
- Signature of points

**Predicates for method Constructor:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of id| > 0|
|   | <= 0|
| Signature of customerName| valid|
|   | null|
| Signature of customerCard| valid |
|   | invalid (null, empty or not valid)|
| Signature of points| >= 0|
|   | < 0|
**Boundaries for method Constructor**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id| -inf, 0, +inf|
| Signature of points| -inf, 0, +inf|
**Combination of predicates for method Constructor**

 | Signature of id | Signatue of customerName| Signature of cardCode| Value of points|Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|---|------------ | ---------------------------- | --------------- | ------|
| <= 0  | *      | *     |*| Invalid| T1(0,"customerName","abcde12345",0)->Exception| CustomerClassTest.testCustomerClassConstructor|
| *| null | *     | *|Invalid| T2(1,"","abcde12345",0)->Exception| CustomerClassTest.testCustomerClassConstructor|
| *| *  | null  | * | Invalid | T3(1,"customerName","null",0)->Exception|CustomerClassTest.testCustomerClassConstructor|
| >0| Valid    | Valid  | Valid |>=0 | T4(1,"customerName","abcde12345",0)|CustomerClassTest.testCustomerClassConstructor|

### setCustomerId
**Criteria for method setCustomerId:**
- Signature of id

**Predicates for method setCustomerId:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of id| null or <= 0|
|   |   > 0|

**Boundaries for method setCustomerId**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id| null, -inf, 0, +inf|

**Combination of predicates for method setProductId**

| Signature of id| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------- | ---------------------------- | --------------- |
| null  or <= 0| Invalid | T1(null)->Exception <br> T1b(-1)->Exception| testSetCustomerId|
| > 0| Valid | T2(50)| testSetCustomerId|

### setCustomerName
- Signature of customerName
- Length of customerName

**Predicates for method setCustomerName:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of customerName | null|
|   | valid|
| Length of customerName | 0 |
| | > 0|
**Boundaries for method setCustomerName**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Length of customerName | 0, +inf |

**Combination of predicates for method setCustomerName**
| Signature of customerName| Length of customerName| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ---|------------- | ---------------------------- | --------------- |
| null  |  -  | Invalid | T1(null) -> Exception | testSetcustomerName|
| valid |  0  | Invalid | T2("")->Exception   |testSetcustomerName|
| Valid | >0  | Valid   | T3("customerName")         |testSetcustomerName|
## Class LoyaltyCardClass
### Method CreateCardCode
- Signature of i

**Predicates for method createCardCode:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of i|  = 10|
|   |   !=10|

**Boundaries for method createCardCode**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of id|   0, 10, +inf|

**Combination of predicates for method createCardCode**

| Signature of id| Valid/Invalid | Description of the test case:example of input and output | JUnit test case |
| ----------- | ------------- | ---------------------------- | --------------- |
| <10 or >10 | Invalid | T1(9;"")<br> T1b(11;"")| testCreateCardCode|
| 10 | Valid | T2(10)| testCreateCardCode|

### Method updatePoints
**Criteria for method updatePoints:**
- Points + toBeAdded

**Predicates for method updatePoints:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Ponints + toBeAdded | >= 0|
|   | < 0|

**Boundaries for method updatePoints**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Points + toBeAdded | -inf, 0, +inf|

**Combination of predicates for method updatePoints**

| Quantity + toBeAdded| Valid/Invalid | Description of the test case | JUnit test case |
| ----------- | ------------- | ---------------------------- | --------------- |
| < 0   | Invalid | new LoyaltyCardClass (); updatePoints(-1) | testupdatePoints|
| >= 0  | Valid   | T2(3)->true <br> T2b(0)->true   |testupdatePoints|

# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|OrderClass| it.polito.ezshop.OrderClassTest.testWhiteBox|
|ProductTypeClass|it.polito.ezshop.ProductTypeClassTest.testWhiteBox|
|CustomerClass|it.polito.ezshop.CustomerClassTest.testWhiteBox|
|UserClass|it.polito.ezshop.UserClassTest.testWhiteBox|
|LoyaltyCardClass|it.polito.ezshop.LoyaltyCardClassTest.testWhiteBox||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|ProductTypeClass| 7| 0 | Impossible|
|ProductTypeClass| 7| 1 | Impossible|
|ProductTypeClass| 7| 12| ProductTypeTest.testValidateBarCode|
|CustomerClass|5|1|Impossible|
|CustomerClass|5|10|CustomerClassTest.testCreateCardCode|



