# Lab Java Design Pattern Discussion

### Luke, Daniel, Palo, Ishan

Lab Java Design Pattern Discussion

Iterator

Problem solved

The Iterator pattern provides a way to traverse collections without exposing their underlying
representation. This allows for flexible traversal operations and abstraction of iteration logic.

Trade-offs

Pros: Decouples iteration logic from collection implementation, making it easier to modify
collections.

Cons: Can introduce overhead in cases where simple loops suffice.

Use in Java vs Description

Java's Iterator interface (java.util.Iterator) aligns with the pattern by offering hasNext() and
next() methods. This matches the pattern's intent by enabling controlled iteration without exposing
internal structures.

Factory (Static Methods)

Problem solved

The Factory pattern using static methods simplifies object creation by encapsulating instantiation
logic, often returning instances based on input parameters.

Trade-offs

Pros: Simplifies object creation, enhances code readability, and reduces tight coupling to
constructors.

Cons: Reduces flexibility for subclassing, as static methods cannot be overridden.

Use in Java vs Description

Java often employs static factory methods in classes like Integer.valueOf() and List.of(), which
improve performance and usability while hiding complex instantiation logic.

Factory

Problem solved

The Factory pattern provides a way to instantiate objects without specifying their concrete classes,
promoting loose coupling and scalability.

Trade-offs

Pros: Improves maintainability and encapsulation; supports different object types through
polymorphism.

Cons: Adds complexity; may require additional subclasses or interfaces.

Use in Java vs Description

Java implements this pattern in libraries such as javax.xml.parsers.DocumentBuilderFactory, ensuring
flexible instantiation of implementations without exposing concrete classes.

Strategy

Problem solved

The Strategy pattern allows behavior to be selected at runtime by encapsulating algorithms within
interchangeable classes.

Trade-offs

Pros: Increases flexibility and reusability; enables dynamic algorithm switching.

Cons: Adds complexity by requiring additional classes for each strategy.

Use in Java vs Description

Java uses the Comparator interface as a real-world example, enabling custom sorting logic to be
applied dynamically via Collections.sort().

Template Method

Problem solved

The Template Method pattern defines a method skeleton in a superclass, with subclasses providing
specific implementations for certain steps.

Trade-offs

Pros: Reduces duplicate code by enforcing a structured algorithm; ensures consistent behavior.

Cons: Can lead to rigid inheritance structures that limit flexibility.

Use in Java vs Description

Java examples include java.io.InputStream subclasses like FileInputStream, where core logic is in
place but specific behaviors differ across subclasses.

Builder

Problem solved

The Builder pattern simplifies object creation when dealing with complex constructors by allowing
incremental construction.

Trade-offs

Pros: Improves code readability and avoids long constructor parameter lists.

Cons: Introduces additional classes and verbosity.

Use in Java vs Description

Java employs this pattern in StringBuilder, java.time.LocalDate.Builder, and other APIs where
chaining methods simplify object instantiation.

Observer

Problem solved

The Observer pattern enables objects to notify dependents about state changes, decoupling subject
and observer.

Trade-offs

Pros: Supports event-driven systems and reactive programming; improves modularity.

Cons: Can lead to memory leaks if observers are not properly removed.

Use in Java vs Description

Java provides java.util.Observer (deprecated in Java 9) and alternative implementations using event
listeners and PropertyChangeListener.

Decorator

Problem solved

The Decorator pattern dynamically extends object functionality without modifying the base class.

Trade-offs

Pros: Promotes open-closed principle; allows feature additions without modifying existing code.

Cons: Can lead to a complex hierarchy with many small classes.

Use in Java vs Description

Java uses this in java.io (e.g., BufferedInputStream wrapping FileInputStream), adding additional
functionality through composition.

Discussion

Which patterns' use seem the most different from its general description?

The Factory pattern can be surprising in practice, especially with Java’s static factory methods.
Unlike traditional descriptions that emphasize subclass instantiation, Java often uses static
methods instead of separate factory classes.

Which patterns do you think you are already using?

Iterator (e.g., looping through data structures)

Strategy (e.g., passing different sorting comparators)

Observer (e.g., event-driven GUI programming)

Which patterns seem the most useful to use in the Sugarscape project?

Strategy for defining agent behaviors (movement, resource gathering, reproduction)

Observer for tracking global statistics or resource updates

Factory for instantiating different agent types dynamically

Decorator for modifying agent attributes without subclassing

What did you learn about design while studying these patterns?

Studying these patterns highlights the importance of separation of concerns, modularity, and
extensibility. While each pattern introduces some complexity, it helps in writing scalable and
maintainable code, especially in large simulation projects like Sugarscape.

