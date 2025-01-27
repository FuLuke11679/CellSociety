# Cell Society Design Lab Discussion
#### Names and NetIDs
Palo Silva pds27

Luke Fu lmf59

Daniel Rodriquez-Florido dr285


### Simulations

* Commonalities
  * Each simulation creates/destroys a cell based on the state of that neighbor's cells

* Variations
  * They each model unique scenarios
    * Conway's Game of Life models population, one models the spreading of forest fires, 
    the other models segregation
  * Conway's game of life is deterministic, the fire model is probabilistic (depends on the probability 
  parameters p and f)



### Discussion Questions

* How does a Cell know what rules to apply for its simulation?

We design a specific class of cell for each simulation (i.e, one class for Conway game of life cell, one class for a fire cell, )
one for a segregation cell). They will all inherit from a cell superclass. 

* How does a Cell know about its neighbors?

One design we considered if having an adjacency list for each cell that stores 
pointers to all of its neighbors. This adjacency list must be independent from the game 
board, and can only be updated once we have traversed the entire game board and updated to the 
next state.

* How can a Cell update itself without affecting its neighbors update?

It can look through it's adjacency list of neighbors and then call its own updateCell method (which will be unique 
to that type of cell). 

However, this adjacency list could potentially be removed and updates would be optimized if 
we simply stored a previous and current state for each cell. If we traverse the board from left 
to right top to bottom then we know that when updating a cell we look at the previous state of 
its neighbors that are up and to the left and the current state of the cells that are to the right and 
down. 

* What behaviors does the Grid itself have?

The grid will store the game board of cells (implemented as a 2D array or a map). 
The cells will update themselves and their adjacency lists of neighbors. 

* How can a Grid update all the Cells it contains?

We will traverse the grid and at each cell, call update cell. Once we have traversed the entire 
grid, then we will read the new updated board and change the visual display. 

* What information about a simulation needs to be in the configuration file?

The configuration file will contain the type of simulation (and thus the type of cell class
being used), initial cell map, size of grid


* How is configuration information used to set up a simulation?

Size of grid to create the initial empty grid, then loop through the grid and use the initial map and type of simulation (cell type) to
create the new cells. 

* How is the graphical view of the simulation updated after all the cells have been updated?

Do one pass over the entire grid to update the cells, once they have been updated do another pass over the grid
to update the visual. 



### Alternate Designs

#### Design Idea #1

* Data Structure #1 and File Format #1

* Data Structure #2 and File Format #2


#### Design Idea #2

* Data Structure #1 and File Format #1

* Data Structure #2 and File Format #2



### High Level Design Goals



### CRC Card Classes

This class's purpose or value is to represent a cell:
```java
public class Cell {
      int id; //helps determine if we need to check prevState or currState
      char prevState;
      char currState; //d or a (dead or alive)
      List <Cell> adjlist;  //by having list of Cell objects, eliminates need to update adjacency list at end of each gameboard iteration
     // returns whether or not the given items are available to order
     public int updateCell(){
       //look through adjList 
       //check id of each cell, if id of adjacent cell is less than id of current cell
       //look at prevState when adding to count for num_alive
       //if id of adjacent cell is greater than id of current cell, look at currState
       //three lines above will be abstracted to getState() method below
       
     }
     public int getID ()
     // returns id of cell
    public char getState(int otherCellID)
       //compare ids and returns curr or previous state depending on id value
    
 }
 ```


This class's purpose or value is to manage the game board:
```java
public class Run {
     
     public int step (){
       //iterates over game board, 
     }
     
	 // creates an order from the given data
     public Order makeOrder (String structuredData)
 }
```


### Use Cases

* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
```

* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)
```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
```

* Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically
```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
```

* Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in a data file
```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
```

* Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one
```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
```