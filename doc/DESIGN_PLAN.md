# Cell Society Design Plan
### Team Number 8
### Names
Palo Silva *(pds27)*
Luke Fu *(lmf59)*
Daniel Rodriquez-Florido *(dr285)*
Ishan Madan *(im121)*


#### Examples




## Overview
The task at hand is to create a simulation game called Cell Society. The purpose of the game is to simulate the behavior of a population of cells where each instance of the game may have different rules that dictate how the cells behave. For a game like this, it is key to be flexible in the implementation of the rules that are followed since that is what dictates the behavior of the game. As a result, our team desires to close off things like visual rendering and the update algorithm, while maintaining things like rule sets and cell types open

## User Interface
![UI_Image](/doc/images/UI_Image.png)
The UI will be implemented in its own Display class. The Display class will have a javafx Scene instance variable and a getDisplay method, which will return the Scene object corresponding to the current generation of the simulation. In our main class we will first perform all of our backend updates in the step() method, then proceed with calling our updateDisplay method and getting the Scene it returns to set the javafx.Stage in the main class. The Display class will have an updateDisplay method, which will take a 2D grid of Cell objects and update the display accordingly. It will traverse the grid of Cells and call getCell() for each Cell which returns a javafx.Shape object that can be added to the Scene—the x and y coordinates of this Rect object are determined based on the position in the 2D grid. The size of each Cell will already be initialized when the cell objects are generated from the XML file, so resizing the application window will not be necessary when a new simulation is loaded.

The Display class will specify a variety of buttons, drop-down menus, and sliders for the UI. It will have a drop down menu to load a new simulation (which will allow the user to select an xml file), a play and pause button to start and stop the simulation, a slider to change the speed of the simulation, a button to save the current state of the simulation as an xml file, and a text box describing the loaded simulation. Event handlers will be built into all of these javafx objects and their methods written in the Display class. All of these features apart from the description text are consistent regardless of simulation type, so subclasses should not be necessary. 


## Configuration File Format
Below are 2 examples of configuration files. They display how we will handle several different issues, such as window/grid configuration, rule configurations, and initial state configurations.

**Example 1: Percolation Simulation with Random Initial State**
````xml
<?xml version="1.0" encoding="UTF-8"?>
<simulationConfig>
    <!-- window and grid configuration -->
    <display width="800" height="800" title="Percolation Simulation">
        <grid rows="50" columns="50" cellSize="16" />
        <!-- colors -->
        <colors>
            <alive>BLUE</alive>
            <dead>WHITE</dead>
        </colors>
    </display>
    
    <!-- rules configuration -->
    <rules type="Percolation">
        <!-- survival rules -->
        <survival>
            <!-- filled cell always stays filled -->
            <neighbor count="0" />
            <neighbor count="1" />
            <neighbor count="2" />
            <neighbor count="3" />
            <neighbor count="4" />
        </survival>
        <!-- birth rules -->
        <birth>
            <!-- open cell becomes filled if it has at least one filled neighbor -->
            <neighbor count="1" />
            <neighbor count="2" />
            <neighbor count="3" />
            <neighbor count="4" />
        </birth>
        <!-- death rules -->
        <death>
            <!-- cell cannot "die" in percolation -->
        </death>
    </rules>
    
    <!-- initial state -->
    <initialState type="random" density="0.4" />
</simulationConfig>

````

**Example 2: Game Of Life Simulation with Specific Initial State**
````xml
<?xml version="1.0" encoding="UTF-8"?>
<simulationConfig>
    <!-- window and grid configuration -->
    <display width="800" height="600" title="Game of Life">
        <grid rows="50" columns="50" cellSize="10" />
        <!-- colors for alive vs. dead -->
        <colors>
            <alive>BLACK</alive>
            <dead>WHITE</dead>
        </colors>
    </display>
    
    <!-- rule configuration -->
    <rules type="GameOfLife">
        <!-- survival rules -->
        <survival>
            <neighbor count="2" />
            <neighbor count="3" />
        </survival>
        <!-- birth rules -->
        <birth>
            <neighbor count="3" />
        </birth>
        <!-- death rules -->
        <death>
            <neighbor count="0" />
            <neighbor count="1" />
    </rules>
    
    <!-- initial state configuration -->
    <initialState type="specific">
        <!-- each noted cell will start as "alive" -->
        <cell row="1" column="1" />
        <cell row="2" column="2" />
        <cell row="2" column="3" />
        <cell row="1" column="3" />
        <cell row="0" column="2" />
    </initialState>
</simulationConfig>
````

## Design Overview
The purpose of this design is to provide various Rulesets and Cells that abstract away the different custom functionalities while maintaining closed algorithmic processes. The ultimate goal would be to have the code be the exact same for administrative things like updating the display, updating the cell state, grid management, etc. by initializing a different Ruleset and Cell type, each which contain the methods of their superclasses (Ruleset and Cell, respectively). The different implementations in each subclass will dictate how the simulation behaves.

![Design_Overview](/doc/images/Design_Overview.png)

## Design Details
**_1. Core Abstractions and Classes_**
   - **1.1 Cell Class**:  
     - Functionality Requirements Handled:  
       - Represents the state and behavior of an individual cell in the grid.  
       - Handles state transitions based on rules provided by the RuleSet.  

     - Collaborations:  
       - Works with the Grid class to update its state based on neighboring cells.  
       - Interacts with the RuleSet class to determine its next state.  

     - Resources Needed:  
       - Access to its neighbors (provided by the Grid class).  
       - Rules for state transitions (provided by the RuleSet class). 

     - Method Signature:
        ````java
        public class Cell {
            private int state;
            private final Position position;

            public Cell(int state, Position position) { ... }
            public int getState() { ... }
            public void setState(int newState) { ... }
            public void updateState(List<Cell> neighbors, RuleSet ruleSet) { ... }
            public void getCurrent() {...}
            public void getPast() {...}
        }
        ````
        
     - Encapsulation
         - The Cell class does not expose its internal state directly. Instead, it provides methods to interact with its state.


   - **1.2 Grid Class**:  
     - Functionality Requirements Handled:  
       - Manages the 2D grid of cells.
       - Provides methods to initialize the grid, update cell states, and retrieve neighbors.

     - Collaborations:  
       - Works with the Cell class to manage individual cells.
       - Collaborates with the Simulation class to update the grid during each step.

     - Resources Needed:  
       - A data structure to store cells (e.g., ````List<List<Cell>>```` or a custom grid implementation).
       - Access to the RuleSet to apply rules during updates.

     - Method Signature:
        ````java
        public class Grid {
            private final int width;
            private final int height;
            private final List<List<Cell>> cells;

            public Grid(int width, int height) { ... }
            public void initialize(List<List<Integer>> initialState) { ... }
            public List<Cell> getNeighbors(Cell cell) { ... }
            public void updateGrid(RuleSet ruleSet) { ... }
            public Cell getCell(int x, int y) { ... }
        }
        ````

     - Encapsulation
         - The grid's internal data structure is hidden. The getNeighbors method abstracts the neighborhood calculation, allowing flexibility in neighborhood types.


   - **1.3 RuleSet Class**:  
     - Functionality Requirements Handled:  
       - Encapsulates the rules for state transitions in a cellular automaton.
       - Provides a concrete implementation for applying rules to a cell based on its neighbors.

     - Collaborations:  
       - Used by the Cell class to determine the next state.
       - Can be extended or modified for different simulation types (e.g., Game of Life, Percolation).

     - Resources Needed:  
       - Access to the cell's current state and its neighbors.

     - Method Signature:
        ````java
        public class RuleSet {
            private final Map<String, Object> parameters;

            public RuleSet(Map<String, Object> parameters) {
                this.parameters = parameters;
            }

            public int applyRules(int currentState, List<Cell> neighbors) {
                // Default implementation (can be overridden by subclasses)
                return currentState; // Placeholder logic
            }

            protected Map<String, Object> getParameters() {
                return parameters;
            }
        }
        ````

     - Encapsulation
         - N/A


   - **1.4 Simulation Class**:  
     - Functionality Requirements Handled:  
       - Manages the execution of the simulation.
       - Handles stepping through the simulation, resetting, and loading configurations.

     - Collaborations:  
       - Works with the Grid class to update the simulation state.
       - Interacts with the XMLParser class to load configurations.

     - Resources Needed:  
       - Access to the Grid and RuleSet to execute the simulation.
       - A mechanism to load and save configurations (e.g., XMLParser).

     - Method Signature:
        ````java
        public class Simulation {
            private Grid grid;
            private RuleSet ruleSet;
            private int timeStep;

            public Simulation(Grid grid, RuleSet ruleSet) { ... }
            public void run(int steps) { ... }
            public void step() { ... }
            public void reset() { ... }
            public void loadConfiguration(String filePath) { ... }
            public void saveConfiguration(String filePath) { ... }
        }
        ````

     - Encapsulation
         - The Simulation class does not expose the grid's internal structure or the rule implementation details.


   - **1.5 XMLParser Class**:  
     - Functionality Requirements Handled:  
       - Parses XML configuration files to initialize simulations.
       - Handles saving the current simulation state to an XML file.

     - Collaborations:  
       - Works with the Simulation class to load and save configurations.

     - Resources Needed:  
       - Access to the XML file and the simulation state.

     - Method Signature:
        ````java
        public class XMLParser {
            public SimulationConfig loadConfig(String filePath) { ... }
            public void saveConfig(String filePath, SimulationConfig config) { ... }
        }
        ````

     - Encapsulation
         - The XML parsing logic is hidden from the rest of the program. The SimulationConfig class (a data transfer object) is used to pass configuration data.

**_2. View and GUI Components_**
   - **2.1 GridView Class**:  
     - Functionality Requirements Handled:  
       - Displays the grid of cells in the GUI.
       - Updates the display as the simulation progresses.

     - Collaborations:  
       - Works with the Grid class to retrieve cell states.
       - Interacts with the Simulation class to update the display.  

     - Resources Needed:  
       - Access to the Grid to retrieve cell states.
       - A GUI framework (OpenJFX) to render the grid.

     - Method Signature:
        ````java
        public class GridView {
            private Grid grid;

            public GridView(Grid grid) { ... }
            public void render() { ... }
            public void update() { ... }
        }
        ````
        
     - Model-View Separation
         - The GridView class does not modify the model. It only reads data from the Grid to render the display.


   - **2.2 SimulationController Class**:  
     - Functionality Requirements Handled:  
       - Manages user interactions (e.g., start, pause, reset).
       - Updates the GUI based on simulation state changes.

     - Collaborations:  
       - Works with the Simulation class to control the simulation.
       - Interacts with the GridView class to update the display.

     - Resources Needed:  
       - Access to the Simulation and GridView classes.

     - Method Signature:
        ````java
        public class SimulationController {
            private Simulation simulation;
            private GridView gridView;

            public SimulationController(Simulation simulation, GridView gridView) { ... }
            public void start() { ... }
            public void pause() { ... }
            public void reset() { ... }
        }
        ````
        
     - Model-View Separation
         - The controller acts as an intermediary between the view and the model, ensuring they remain decoupled.

**_3. Handling Specific Requirements_**
   - **3.1 XML Configuration (CELL-07)**:  
     - The XMLParser class handles loading and saving configurations. The SimulationConfig class encapsulates the configuration data, ensuring the rest of the program is agnostic to the file format.
   - **3.2 Grid View (CELL-08)**:  
     - The GridView class maps cell states to colors and updates the display as the simulation progresses.
   - **3.3 Simulation Control (CELL-11, CELL-12, CELL-13)**:
     - The SimulationController class provides methods to start, pause, and adjust the simulation speed.
   - **3.4 Error Handling (DESIGN-14)**:
     - Custom exceptions (e.g., InvalidConfigurationException) are used to handle errors during configuration loading or simulation execution.

**_4. Design Principles in Action_**
   - **4.1 Encapsulation**:  
     - The grid's data structure is hidden behind the Grid class. The getNeighbors method abstracts the neighborhood calculation, allowing flexibility in implementation.
   - **4.2 Abstraction**:  
     - The RuleSet interface allows for multiple rule implementations without modifying the Cell or Grid classes.
   - **4.3 Model-View Separation**:
     - The GridView and SimulationController classes handle GUI interactions and rendering, while the Simulation and Grid classes manage the simulation logic.
   - **4.4 External Configuration**:
     - Simulation parameters and initial states are loaded from XML files, ensuring the program is flexible and configurable.


## Design Considerations
One of the major design issues that the team discussed at length was the issue of how to keep track of a cell’s neighbors. We came up with three potential solutions as follows:

1. Delegate all of the neighbor checking to the Grid class and passing the update condition to the Cell. For example, in the case of Conway’s game of life, the Grid class would be responsible for checking how many neighbors are alive and then passing that information into the Cell class to appropriately update. One advantage to this technique is that the Grid contains all the neighbors simply with an index, therefore, it would be straightforward to address to the neighbors and check their status. The major downside to this is that the Grid class would have a large set of responsibilities that perhaps should not be designated to the class and would also have difficulty scaling. For example, if the simulation type were to change, we would’ve had to implement a different checking system, like in the case of the forest fire simulation.
<br>
2. Have each instance of a Cell maintain its own group of neighbors with a list of the objects it is a neighbor to. With this approach, each cell would be able to locally count the statuses of the neighbors, and with inheritance, could implement different status checkers in each implementation of a subclass without having to check what kind of game it is, giving us the closed algorithmic process we wanted. However, this design has many moving pieces and uses a lot of storage. In the event of a 1000x1000 grid, we could have 1 million cells, each with instance variables and a list of 8 objects, resulting in the storage of 8 million cells. Additionally, we may run into tricky-to-solve memory pointer issues with different versions of the game, such as in segregation when a cell may want to move to a random vacant cell. This approach is also tightly coupled with the Grid, essentially storing double information, which is not necessary.
<br>
3. The third approach is the one we ultimately deciding on pursuing. The procedural Cell state updates will be invoked by the Grid class. From this class, we can invoke the update function that is handled by an instance of a Ruleset subclass. This subclass will take in a cell, a list of its neighbors provided by the grid, and be able to implement custom rules depending on which ruleset is instantiated. Additionally, we will have different subclasses of cells that will maintain different potential states, such as alive or dead in conway’s game of life while a segregation cell may have an enum class embedded that only allows colors, for example. The combination of the two makes the addition of a new simulation very flexible, since you would only need to add a new ruleset extension and a new corresponding cell type extension, while maintaining the algorithmic parts of the program closed. This solves the issue from design #2 where we no longer have to store all the cell’s neighbors within a cell so we avoid the potential tricky memory pointer issues as well as save space by passing in a temporary object.

Another major design consideration we discussed was how to handle the previous and current states of a Cell. For example, if a Cell was queued for update but the left one had already been updated while the one to the right had old information, how do we accurately get a picture of the original state? We considered two possible designs for this issue.

1. Update each cell into an auxiliary grid while maintaining the original states in the original grid then replace the original grid with the auxiliary array. The advantage to this is that checking each cell’s adjacent cells would be straightforward and can simply use the information at hand in the current grid. Additionally, each Cell would only have to maintain it’s active state, as the new copy of the Cell in the auxiliary array would contain the new state while the old state was contained in the original grid. The Display class would also have a straightforward job as once the new grid is copied into the old grid, the front end can update all at once. The disadvantage to this design is that for each frame or instant that the game simulates the next life, the program would be copying up to 1 million (1000x1000) cells into a new grid and replacing that grid, leading to potential issues with the speed slider. The team was not sure that this large scale could be replicated at a fast speed, therefore we decided not to go with this approach.
<br>
2. The design option that we ultimately decided on was to update each Cell in place. To solve this issue, we added three fields to the Cell class: a previous state variable, a current state variable, and a Cell ID. The Cell ID would encode the row and column of the cell by being row*gridWidth + column while the two state variables would maintain the previous and current states of a cell (alive or dead, on fire or not, etc.). With the combination of these three variables, we unlock all the information we need for any given cell. The idea is to traverse the grid from left to right, top to bottom and update in place as the program encounters a new cell. Using this idea, we know that any adjacent cell with an ID less than that of the current cell will need to use the information stored in previous state while any adjacent cell with an ID greater than that of the current cell will use the information stored in the current state field. To solve the issue of displaying a ripple effect, the program can wait for all the cells to be updated and display the new information all at once instead of displaying each cell changing one at a time. This also does not use any auxiliary storage. A disadvantage to this approach could be the dilution of the purpose of the Cell object in that it now contains more information that is used for a specific condition (updating cell status).


## Use Cases
Palo:
1. Adjust simulation speed
    ```java
    Display myDisplay = new Display(Cell[][] grid)
    myDisplay.getScene().setOnAction(this.handleSpeedButtonClick());
    //will likely use a slider, not a button to implement this
    //need to look into appropriate javafx implementation
    
    Methods in Display class:
    public Scene getScene(){
        //returns Scene object 
    }
    public static void handleSpeedButtonClick(){
        //increment/decrement speed of simulation loop
    }
    ```
2. Save simulation state to XML file 
    ```java
    Cell[][] grid; //let grid contains current simulation state
    String filepath = “...”  //desire XML filepath
    saveConfiguration(filepath)
        Methods in Simulation class:
            public static void saveConfiguration(String filepath){
            //traverses current grid and translate state of all cells
            //into XML file format
    }
    ```

Ishan:
1. Supporting Multiple Configuration Formats
    - Scenario Description
        - A student team is working on their simulation project and wants to support both XML and JSON configuration files. Some team members prefer writing configurations in XML, while others prefer JSON. The system should work seamlessly with either format.
        <br>
        ```java
        // The core parser interface that all format-specific parsers must implement
        abstract class ConfigurationParser {
            // Protected variables that store parsed configuration
            protected gridSize, colors, rules, simulationType
            
            // Main parsing method - each parser implements its own version
            abstract void parseConfiguration(filename)
            
            // Common methods any parser must provide
            getGridSize()
            getColors()
            getRules()
            getSimulationType()
        }

        // XML-specific implementation
        class XMLParser extends ConfigurationParser {
            parseConfiguration(filename) {
                // Read XML file
                // Extract configuration into protected variables
                // Handle XML-specific validation
            }
        }

        // JSON-specific implementation for future use
        class JSONParser extends ConfigurationParser {
            parseConfiguration(filename) {
                // Read JSON file
                // Extract configuration into same format as XML parser
                // Handle JSON-specific validation
            }
        }
        ```

2. Validating Configuration Files
    - A team member has created a new configuration file but made some mistakes. The parser needs to catch these errors and provide helpful feedback so they can fix the issues. The validation should check for:
        - Missing required elements (like grid size or rules)
        - Invalid values (like negative grid sizes)
        - Incorrect data types (like text where numbers are expected)
        - Simulation-specific requirements (like having the right states defined)
        <br>
    ```java
    abstract class ConfigurationParser {
        validateConfiguration() {
            // Check for required elements
            ensureRequiredElementsExist()
            
            // Validate specific elements
            validateGridSize()
            validateColors()
            validateRules()
            
            // Check simulation-specific requirements
            validateSimulationRequirements()
        }
        
        handleError(errorType, message) {
            // Create detailed error message
            // Include line number if possible
            // Suggest how to fix the error
            reportError(errorType, message)
        }
    }
    ```

Luke:
1. Moving to a random vacant neighborhood:
    ```python
    empty_cells = self.find_empty_cells()
        if empty_cells:
            new_position = random.choice(empty_cells)
            old_position = cell.position
            // Move the cell to the new position
            self.cells[new_position[0]][new_position[1]] = cell
            cell.move_to_new_position(new_position)
            # Mark the old position as empty
            self.cells[old_position[0]][old_position[1]] = Cell(0, old_position)
    ```

2. Loading a new Simulation:
    ```java
    RuleSet ruleSet = new GameOfLifeRules();
    Simulation simulation = new Simulation(grid, ruleSet);
    // Run the simulation
    simulation.run(100);
    ```

Daniel:
1. Pause Simulation
    - If the user wishes to pause the simulation, the animation must be halted from the Main class where the JavaFX step function is located. Within this step function, we intend to have the cells be updated from the Grid Class using the proper Ruleset instantiation that affects the Cell instantiation and the display class rendering a display at an instant. When the animation is paused, these two function will not be called, and therefore the image will remain static as there will be no updates or renders.
2. Restarting Game
    - If the user wishes restart the game, the original config file that was parsed by the ParseXML may be reloaded into the game. The reparsing can be done from the ParseXML class while the grid can be reinstated to these original conditions via the GridManager class using some a setGrid() method.

## Use Case Method Flow
1. Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
    - Storing the cell neighbors in the object of a cell.
    - Can set the state of each cell with the changeState method.
<br>
2. Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)
    - Reading neighbors through configuring the XML file to encode the neighbors accordingly
    - We can also access edge cells based off of cell id
<br>
3. Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically
    - Storing previous and next states within the object of each states (Cell Class)
    - We know which state to use based on if the id of the neighbor cell is less than the current cell.
    - First we update the cells (from Grid) and then display after cells are updated (from Display)
<br>
4. Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one
    - Instantiation is handled through XML parsing
    - The new conditions are contained within a cell subclass that corresponds to a different simulation
<br>
5. Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in a data file
    - Can be included as a final variable within the fire cell subclass


## Team Responsibilities

Denote primary and secondary responsibilities, also include timetable

* Palo Silva: _Front-End_, Design Features **10, 11, 12, 13, 14**

Sunday 2/2: Complete features 10,11, maybe 12

Monday 2/3: Complete 12, 13, 14

* Luke: _Front-End_, Design Features **8 & 9**

* Ishan: _Back-End_, Design Features **02, 04, 06**

* Daniel: _Back-End_, Design Features **01, 03, 05**

Friday 1/31: Implement 01
Sunday 2/2: Implement 05
Monday 2/3: Implement 03
