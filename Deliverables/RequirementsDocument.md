# Requirements Document 

Authors:

Date:

Version:

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|   Shop manager   	| Person who manages the shop and the inventory             | 
|	Cashier			| Person who uses the cash register and register sales		|
|	Accounting manager		| Person who is responsible for accounting					|
|	Warehouse manager| Person who is responsible of managing the inventory and warehouse of the shop|
|	Customer		| Customers of the shop who do sales in the shop			|
|	Developer		| Person who develop the EZShop application					|
| 	Cash register	| Instrument that register each sale happened in the shop	|
|	Product			| Product that is sold in the shop and is contained in the inventory|
|	Inventory		| List of available product in the shop	that can be contained into a database|
|	Supplier		| Person who sell the products to the shop manager			|
<br>
# Context Diagram and interfaces
## Context Diagram

<div hidden>
	@startuml context_diagram
		actor :Shop manager: as sm
		actor :Cashier: as cr
		actor :Accounting manager: as am
		actor :Customer management system: as cms
		actor :Warehouse manager: as wm
		'	actor :Inventory and Catalogue system: as i	:
		actor :Supplier: as s
		actor :Credit card system: as ccs
		rectangle System{
		usecase EZShop
		}
		sm --> EZShop
		cr --> EZShop
		am --> EZShop
		wm --> EZShop
		'	i --> EZShop
		s <-- EZShop
		EZShop <-- cms
		EZShop <-- ccs
	@enduml
</div>

## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Shop Manager    | GUI 		| Screen, keyboard, mouse on PC 	|
|	Cashier			| GUI 		| Screen, keyboard, mouse				|
|	Accounting manager| GUI		| Screen, keyboard, mouse	|
|	Warehouse manager| GUI		| Screen, keyboard, mouse	|
|	Supplier		| Email			| Email on the network			|
|	Credit card management system| Web service| Internet connection|
|	Customer management system	| Web service |	Internet connection|

<!--|	Inventory and catalogue system	| Queries		| Database connection over the network	|)-->
<br>

# Stories and personas

The following personas and stories represent possible actors profiles.
<br>
<br>
Matteo is 40, works, owns and manages a small food shop. He has to check the inventory every day and place orders to his suppliers for goods for his shop. He is always in hurry so he wants to spend less time as possible in doing managing stuff and concentrate more on his customers.
<br>
<br>
Laura is 60, she has always worked in the shop of her parents and now she owns it. She is not very familiar with technology stuff but she needs something to better manage the inventory of her shop. She has 2 nephews and she wants to spend as much time as possible with them so she has assumed a cashier for her shop that manages the sales and helps her.
<br>
<br>
Luca is 25, he works as a cashier in a small shop. During his work, he has to help the shop manager in updating the inventory level of the product sold in the shop. This requires him to stay in the shop after the closing hour to check the inventory. He would really like to spend this time with his friends so he wants that the inventory is as fast as possible.
<br>
<br>
Giorgia is 50, she works as a supplier for many small shops in Turin. Since she is very forgetful and, for that reason, she always looks to her email to check for the orders of the managers of the different shops. 
<br>
<br>
Giovanni is 45, he helps the manager of a small food shop in managing the accounting of the shop. He has a daughter and he would like to spend all of his time with her. The manager of the shop he works for requires daily, weekly and monthly reports about the entries and the bills of the shop. That requires a lot of time and he would like to do that faster in order to have more free time.
<br>
# Functional and non functional requirements

## Functional Requirements


| ID        | Description  |
| ------------- |:-------------:| 
|   FR1     |  	Manage Inventory|
|	FR1.1	|	Check inventory level for a product type|
|	FR1.2	|	Update inventory level for a product type (increment or decrement products quantity) |
|	FR1.3	|	Add a new product type|
|	FR1.4	|	Remove a product type|
|	FR1.5	|	Update product type information|
|	FR1.6	|	List all product types|
|	FR2		|	Manage sales|
|	FR2.1	|	Register into system a sale for a product|
|	FR2.2	|	Remove a previous sale|
|	??? FR2.3	|	Manage replacement (remove a previous sale and use the credit to buy a new item)	
|	FR3		|	Manage accounting|
|	FR3.1	|	See all bills|
|	FR3.2	|	Get current balance|
|	FR3.3	|	Add a new bill|
|	FR3.4	|	Set a bill as payed|
|	FR3.5	|	Filter bills using supplier, product type, total amount, ...
|	FR3.6	|	Generate report with balance, bills and entries|
|	??? FR3.5	|	Pay a bill|
|	FR4		|	Manage customers|
|	FR4.1	|	Add a new customer|
|	FR4.2	|	See all customers|
|	FR4.3	|	Remove a customer|
|	FR4.4	|	Add a fidelity card for a customer|
|	FR4.5	|	Update fidelity card|
|	FR5		|	Manage credit card system|
|	FR5.1	|	Add a new credit card|
|	FR5.2	|	Get list of movements|
|	FR5.3	|	Remove a credit card|
|	FR6		|	Manage users (manager, cashier,...)|
|	FR6.1	|	Add a new user|
|	FR6.2	|	Remove a user|
|	FR6.3	|	View all users|
|	FR6.4	|	Change access rights for a user|
|	FR6.5	|	Manage user authentication|
|	FR7		|	Manage suppliers|
|	FR7.1	|	Add a new supplier|
|	FR7.2	|	List all suppliers|
|	FR7.3	|	Remove a supplier|
|	FR8		|	Manage orders|
|	FR8.1	|	Place an order to supplier for a given product type|
|	FR8.2	|	Repeat a previous order to a supplier|
|	FR8.3	|	Abort a previously inserted order|

<br>

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | efficiency  	| Time to show the whole inventory < 10ms  | FR1.6 |
|  NFR2     | usability 	| Maximum number of different product types >= 2000  | FR1|
|  NFR3     | efficiency 	| More sales can be registered at the same time | FR2.1|
|  NFR4		| efficiency	| Show customer list time < 1ms | FR4.2 |
|  NFR5		| efficiency	| Login procedure time < 10ms| FR6.5|
|  Domain1	| 				| Currency is EURO |


# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>


\<next describe here each use case in the UCD>
### Use case 1, UC1
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |  
|  Post condition     | \<Boolean expression, must evaluate to true after UC is finished> |
|  Nominal Scenario     | \<Textual description of actions executed by the UC> |
|  Variants     | \<other executions, ex in case of errors> |

##### Scenario 1.1 

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the scenario can start> |
|  Post condition     | \<Boolean expression, must evaluate to true after scenario is finished> |
| Step#        | Description  |
|  1     |  |  
|  2     |  |
|  ...     |  |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2
..

### Use case x, UCx
..



# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>
<div hidden>
	@startuml glossary
		class Shop{
			name
			address
			dimension
		}
		class ProductType{
			id
			firm
			unit price
			type
		}
		class Product{
			code
		}
		class Inventory{
		}
		class Catalogue
		note top of Catalogue: Contains all product \ntypes sold in the shop 
		note right of User: Represent all people working \nin the shop including the \nmanager
		class User{
			name
			surname
			age
		}
		class Role
		class Cashier{
		}
		class AccountingResponsible
		class WarehouseManager
		together {
		class Manager{
		}
		class Supplier{
			name
			surname
			address
		}
		class Order{
			date
			quantity
		}
		}
		class Sale{
			date
			time
			discount
			total amount
		}

		class Customer{
			name
			surname
			gender
			date of birth
		}
		class FidelityCard{
			id
			expiration date
		}
		class CashRegister{
			id
			firm
			model
		}
		class Bill{
			id
			amount
			due date
		}
		class Balance{
			date
			total earnings
			total expenses
			profit
		}


		Shop -- Inventory
		Shop -- Catalogue
		Inventory -- "*"Product
		Product"*" -- ProductType
		Shop -- "*"User: works in <
		User -- "1..*" Role
		Role <|-- Cashier
		Role <|-- Manager
		Role <|-- WarehouseManager
		Role <|-- AccountingResponsible

		Order -- ProductType
		Order -- Supplier
		Manager -- Order: places >
		Order -- Bill: produces >

		Sale -- "*"Product: contains >
		Cashier -- "*"Sale: is registered by >

		Catalogue -- "*"ProductType: contains >

		Customer -- "1..*"FidelityCard
		Shop -- "*"Customer
		Customer"0..1" -- "*"Sale

		Cashier -- CashRegister: manages >
		WarehouseManager -- Inventory: manages >

		Balance -- Sale
		Balance -- Bill
		AccountingResponsible -- Balance: manages >
	@enduml
</div>
# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >

