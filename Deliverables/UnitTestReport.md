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
## Class AccountBookClass

### Method removeSaleTransaction

**Criteria for method removeSaleTransaction:**
	

- Signature of returnTransactionId
- Presence of not numeric characters in Id
- Validity of returnTransactionId
- Existence of SaleTransaction object

**Predicates for method removeSaleTransaction:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of returnTransactionId | >=0 |
|             | <0 |
| Presence of not numeric characters in returnTransactionId         | yes          |
|             |     no         |
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
|   "     |  >=0       |  no      | Invalid    |   removeSaleTransaction(100); removeSaleTransaction(100) ->  InvalidTransactionIdException      |  |
|   "     |  "       |  yes   |   Valid    | removeSaleTransaction(100);  ->  SaleTransaction removed succesfully         |  |

### Method updateBalance

**Criteria for method updateBalance:**
	
- Signature of amount

**Predicates for method updateBalance:**

| Criterion   | Predicate     |
| ----------- | ------------- |
| Signature of returnTransactionId | >=0 |
|             | <0 |



**Boundaries for method updateBalance**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Signature of returnTransactionId  |    -1, +500       |


 **Combination of predicates for method updateBalance**

|   Validity of updateBalance | Valid/Invalid | Description of the test case: example of input and output |  JUnit test case  | 
| --------- | --------- | --------| --------| 



## Class BalanceOperation

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

### setType
**Criteria for method setType:**
- Validity of type
- Type allowed


**Predicates for method setType:**
| Criterion   | Predicate     |
| ----------- | ------------- |
| Validity of type | null |
| | valid|
| Type allowed | false |
|   | true |
| String contains only characters  |  false  |
|   |  true  |


**Boundaries for method setType**:

| Criterion   | Boundary values |
| ----------- | --------------- |
| Type allowed | "nothing","credit" |
| String contains only characters | "hi","hi67",'debit' |

**Combination of predicates for method setType**

| Validity of type | Type allowed | String contains only character | Valid/Invalid |Description of the test case: example of input and output |  JUnit test case  | 
| ----------- | --- | ------ |------------- | -------- | ------- |
| null  |  *  |  *  | Invalid | T1(null; error)  |     |
| valid | no  |  *  | Valid   | T2("cia90"; no output)   |   |
|       |       |       |       | T2b(nothing)  |   |
| *     | yes |  yes   | Valid   | T3("Credit")         |   |

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


# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|OrderClass| it.polito.ezshop.OrderClassTest.testWhiteBox|
|ProductTypeClass|it.polito.ezshop.ProductTypeClassTest.testWhiteBox|
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



