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
|	Customer		| Customers of the shop who do sales and receive accounting|
|	Developer		| Person who develop the EZShop application					|
| 	Cash register	| Instrument that register each sale happened in the shop	|
|	Product			| Product that is sold in the shop and is contained in the inventory|
|	Inventory		| List of available product in the shop	that can be contained into a database|
|	Supplier		| Person who sell the products to the shop manager			|
<br>
# Context Diagram and interfaces
## Context Diagram
\<Define here Context diagram using UML use case diagram>
<div hidden>
	@startuml context_diagram
		actor :Shop manager: as sm
		actor :Cashier: as cr
		actor :Inventory: as i
		actor :Supplier: as s
		usecase EZShop

		sm --> EZShop
		cr --> EZShop
		i --> EZShop
		s <-- EZShop
	@enduml
</div>

\<actors are a subset of stakeholders>

## Interfaces
\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Shop Manager    | GUI on PC | Screen, keyboard, mouse on PC 	|
|	Cashier			| GUI on cash register 			| Screen, keyboard				|
|	Inventory		| Queries	| Database connection over the network				|
|	Supplier		| Email		| Email on the network			|

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>

The following personas and stories represent possible actors profiles.
<br>
<br>
Matteo is 40, works, owns and manages a small food shop. He has to check the inventory every day and place orders to his suppliers for goods for his shop. He is always in hurry so he wants to spend less time as possible in doing managing stuff and concentrate more on his customers.
<br>
<br>
Laura is 60, she has always worked in the shop of her parents and now she owns it. She is not very familiar with technology stuff but she needs something to better manage the inventory of her shop. She has 2 nephews and she wants to spend as much time as possible with them so she has assumed a cashier for her shop that manages the sales and helps her.
<br>
# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID        | Description  |
| ------------- |:-------------:| 
|  FR1     |  |
|  FR2     |   |
| FRx..  | | 

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |   |  | |
|  NFR2     | |  | |
|  NFR3     | | | |
| NFRx .. | | | | 


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
		note right of ShopPersonnel: Represent all people working \nin the shop including the \nmanager
		class ShopPersonnel{
			name
			surname
			age
		}
		class Cashier{
		}

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

		Shop -- Inventory
		Shop -- Catalogue
		Inventory -- "*"Product
		Product"*" -- ProductType
		Shop -- "*"ShopPersonnel: works in <
		ShopPersonnel <|-- Cashier
		ShopPersonnel <|-- Manager

		Order -- ProductType
		Order -- Supplier
		Order -- Manager: is placed by >

		Sale -- "*"Product: contains >
		Sale"*" -- Cashier: is registered by >

		Catalogue -- "*"ProductType: contains >

		Customer -- "0..*"FidelityCard
		Shop -- "*"Customer
		Customer"0..1" -- "*"Sale

		Cashier -- CashRegister: manages >
	@enduml
</div>
# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >

