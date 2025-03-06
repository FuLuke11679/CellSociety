# Cell Society API Lab Discussion

### NAMES

### TEAM 8

Palo Silva *(pds27)*
Luke Fu *(lmf59)*
Daniel Rodriquez-Florido *(dr285)*
Ishan Madan *(im121)*

## Current Simulation Model API

* Identified Public Classes/Methods
    * The constructor, update state, and get state.

## Wish Simulation Model API

* Goals
    * Establish a Super class with shared methods(update state, get state)
    * Create abstractions to unify similar behaviors across simulations
    * To work the same when referenced from the front end or other parts of the program.

* Contract
    * Services:
        * Encapsulation: Hide implementation details while allowing for easy modifications.
        * Extensibility: Support new simulation types with minimal changes.
        * Documentation: Provide clear descriptions for method functionalities.
        * Consistency: Use similar coding styles to enhance readability.

    * Abstractions and their Methods

```java
public abstract class Simulation {
protected Grid grid;

    // Initializes the simulation with a predefined grid state
    public Simulation(Grid initialGrid) {
        this.grid = initialGrid;
    }

    // Updates the state of the simulation
    public abstract void updateState();

    // Returns the current state of the simulation
    public Grid getState() {
        return grid;
    }
}

```

* Exceptions
    * InvalidSimulationStateException: Thrown when an invalid state is encountered.
    * SimulationLoadException: Thrown when loading a simulation fails.

## API Task Description

* English
    * Configuration Initializes a simulation: A configuration system loads and validates an existing
      simulation setup, then initializes it with its starting values. This follows a factory
      pattern, allowing different simulation types to be instantiated dynamically.


* Java
    * Configuration Initializes a simulation:

```java
public class SimulationFactory {
    public static Simulation createSimulation(String type, Grid initialGrid) throws SimulationLoadException {
        switch (type) {
            case "GameOfLife":
                return new GameOfLifeSimulation(initialGrid);
            case "Fire":
                return new FireSimulation(initialGrid);
            case "Segregation":
                return new SegregationSimulation(initialGrid);
            default:
                throw new SimulationLoadException("Unknown simulation type: " + type);
        }
    }
}

// Example usage
Simulation sim = SimulationFactory.createSimulation("GameOfLife", new Grid());

```
 
 