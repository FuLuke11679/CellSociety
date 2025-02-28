# Cell Society API Lab Discussion
### Team Number 8
### Names
Palo Silva *(pds27)*
Luke Fu *(lmf59)*
Daniel Rodriquez-Florido *(dr285)*
Ishan Madan *(im121)*

Current Configuration API

Identified Public Classes/Methods

Cell: Represents individual cells in the simulation.

Grid: Manages a collection of Cell objects and their interactions.

Simulation: Controls the execution and progression of the simulation.

ConfigurationLoader: Reads and processes configuration files.

State: Represents different states a cell can have.

Wish Configuration API

Goals

Improve modularity and reusability.

Support multiple simulation types with minimal changes.

Provide a flexible mechanism for defining cell behaviors.

Enhance error handling for invalid configurations.

Contract

Services

Load and parse configuration files.

Initialize grid and simulation parameters.

Provide an API for retrieving and modifying simulation state.

Abstractions and their Methods

Cell

getState(): Retrieves the current state.

setState(State newState): Updates the state.

Grid

initialize(List<Cell>): Sets up the grid.

getNeighbors(Cell c): Returns neighboring cells.

Simulation

run(): Starts the simulation.

step(): Advances simulation by one step.

ConfigurationLoader

loadConfig(String filePath): Reads and parses a configuration file.

Exceptions

InvalidConfigurationException: Thrown when configuration is missing required parameters.

SimulationException: Handles errors during simulation execution.

API Task Description

English

The API should provide methods to configure and run simulations dynamically. Users should be able to define rules and behaviors through configuration files without modifying core code.

Java

The implementation should include interfaces for defining cell behavior, abstract classes for common functionality, and a robust system for loading and validating configurations.

