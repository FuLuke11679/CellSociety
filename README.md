# cell society

## TEAM NUMBER 8

## NAMES

Daniel Rodriguez-Florido dr285

Luke Fu lmf59

Palo Silva pds27

Ishan Madan

This project implements a cellular automata simulator.

### Timeline

* Start Date: 2/2/2024

* Finish Date: 3/3/2024

* Hours Spent: 80

### Attributions

* Resources used for learning (including AI assistance)
  * Course materials and resources on cellular automata
  * JavaFX documentation for GUI implementation
  * AI assistance for debugging and refactoring

* Resources used directly (including AI assistance)
  * ChatGPT for debugging help and code improvement suggestions
  * JavaFX API for graphics
  * Java Reflection for dynamic class instantiation

### Running the Program

* Main class:

Main.java

* Data files needed:

At least one of the following:

- Fire1.xml
- Fire2.xml
- Fire3.xml
- Fire4.xml
- Fire5.xml
- GameOfLife5.xml
- GameOfLife_1.xml
- GameOfLife_2.xml
- GameOfLife_3.xml
- GameOfLife_4.xml
- Percolation1.xml
- Percolation2.xml
- Percolation3.xml
- Segregation1.xml
- Segregation2.xml
- Wator1.xml
- WatorWorld_5x5.xml
- WatorWorld_30x30.xml
- WatorWorld_50x50.xml
- SugarScape1.xml
- SugarScape2.xml
- SugarScape3.xml
- Simple.xml

* Interesting data files:

- SugarScape2.xml
- Fire4.xml
- Percolation2.xml

* Key/Mouse inputs:

- Click for buttons to start, pause, reset, and load simulations
- Slider for adjusting simulation speed
- Select for different edge, shape, and neighborhood types

### Notes/Assumptions

* Assumptions or Simplifications:
  * Controller now defined and split into SimulationController and SimulationScreen
  * XML input files must follow a specific format; incorrect XML structures may result in 
  underined behavior
  * Some simulations assume specific grid, edge, and shape type unless specified otherwise

* Known Bugs:
  * Occasional UI lag when loading large XML files
  * Speed bar is inconsistent when changing files
  * Some XML parsing errors are not caught properly, leading to runtime crashes

* Features implemented:

- Simulation: Conway’s Game of Life
- Simulation: Percolation
- Simulation: Spreading of Fire
- Simulation: General Conway
- Simulation: Schelling’s Model of Segregation
- Simulation: Wa-Tor World
- Simulation: Sugarscape
- Display Simulation Information
- Test Configurations: Game of Life Known Patterns
- XML-Based Simulation Configuration
- Cell Grid View
- Load New Configuration File
- Start Simulation
- Pause Simulation
- Reset Simulation
- Simulation Speed Adjustment
- Change Shape
- Change Neighborhood strategy
- Change Edge Handling
- Change Language
- Change Color/Theme
- Logging for Debugging Errors/Error Handling



* Features unimplemented:

- Edit Simulation State Dynamically
- Save Simulation State as XML
- Flip Grid
- Grid Outline
- Grid States
- Simulation: Rock Paper Scissors
- Simulation: Langston's Loop
- Simulation: Darwin

* Noteworthy Features:
  * Modular Design: New simulations can be added by defining rulesets and XML config files
  * Reflection-based Factories: Dynamically instantiates agents, rulesets, and grids without hardcoding dependencies
  * Graphical Representation: Displays cell states dynamically with JavaFX
  * Flexible XML Input: Simulations can be configured using external XML files, allowing users to create custom initial conditions

### Assignment Impressions

This assignment was an excellent opportunity to learn about encapsulation, abstraction, and modular 
design. Implementing simulations with different rulesets strengthened our understanding of object-oriented programming. 
Working with JavaFX and XML parsing was challenging but rewarding. Future improvements could include refactoring the UI for 
better responsiveness and expanding the project to include more user-defined simulations.
Working as a team presented several challenges, including coordinating schedules, dividing responsibilities, and maintaining consistent code 
quality.Despite these challenges, effective collaboration and regular check-ins helped us manage the workload successfully. Lessons learned include the importance of thorough documentation, breaking down tasks into manageable parts, and maintaining clear version control practices.

