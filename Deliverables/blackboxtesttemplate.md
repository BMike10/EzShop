# Unit Testing Documentation template

Authors:

Date:

Version:





# Black Box Unit Tests

```
<Define here criteria, predicates and the combination of predicates for each function of each class.
Define test cases to cover all equivalence classes and boundary conditions.
In the table, report the description of the black box test case and the correspondence with the JUnit black box test case name/number>
```

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


