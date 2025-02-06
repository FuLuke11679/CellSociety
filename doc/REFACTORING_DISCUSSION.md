# Refactoring Lab Discussion
#### NAMES
Ishan
Palo
Daniel
Luke
#### TEAM
8


## Design Principles

 * Open/Close
    * The open-close principle basically means that your code is open to extension but it is closed to modification. This way, we can extend our code to apply to differnet things, but it doesn;t modify any existing code there for its use. A real world example is electrical outlets in your house. When you want to use it, you dont rewiire your entire house (CLOSED) but you can plug in a new device into the outlet and get power (OPEN).

 * Liskov Substution
    * Basically, if a method is able to take a superclass as an input parameter, no subclasses of that superclass (when passed in as the input parameter) should cause issues in the code. For example, if a method takes an input of type Cell, any subclass of Cell should also work in the method without issue.



### Current Abstractions

#### Abstraction #1
 * Current embodiment of principles
    * Our cell super class is a good abstrucation because it demonstrates both of the above principle. You are able to easily extend the superclass to make a new cell type, keeping the original cell superclass unmodified. Also, when passing in a cell type into a method, we are able to pass in any subclass of the original cell superclass.

 * Improved embodiment of principles
    * We are happy with our current superclass structure.

#### Abstraction #2
 * Current embodiment of principles
    * CellState superclass allows you to quickly check the cellstate without having to check what type of cell it is. This allows us to streamline the getters and setters and prevent extra code.

 * Improved embodiment of principles
    * We can add to the CellState superclass so that it isn't empty anymore. We can possibly add a toString() method 

#### Abstraction #3
 * Current embodiment of principles
    * Ruleset superclass is another example because in each subclass of the ruleset (rules for a different simulation), we override the update method based on the unique simulation rules.

 * Improved embodiment of principles
    * We can give the ruleset class access to grid apis to get more information that it may need for future simulations


### New Abstractions

#### Abstraction #1
 * Description
    * Grid abstraction. In this abstraction, we aim to extend the grid class so that we can support a wider variety of unique simultions
 * How it supports making it easier to implement new features
    * certain simulations require different access to certain features in the grid. by creating a superclass and having subclasses for different simulations, we can grab relevant neighbor/grid info as needed in differnet simulations

#### Abstraction #2
 * Description
    * Parser superclass. We currently have a XML parser, but we want it to extend a parser superclass.

 * How it supports making it easier to implement new features
    * In the case that we have to get input from different file types in teh future, having a parser superclass with the main parsing methods would be helpful. We can then exted the superclass to write new parsing logic as needed.
 
#### Abstraction #3
 * Description
    * XMLEncoder class. Currently we are trying to encode the XML file through the main class. we want to make it a superclass with subclasses.
 * How it supports making it easier to implement new features
    * When we are adding the save button, by having a superclass to encode the XML file, then when we actually do the encoding for different simulation types, we can make subclasses for certain simulations to clearly save the simulation.