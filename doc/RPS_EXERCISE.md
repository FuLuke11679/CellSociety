# Rock Paper Scissors Lab Discussion
#### Names and NetIDs
Daniel Rodriguez-Florido dr285

Palo Silva pds27

Luke Fu lmf59

Ishan Madan

### High Level Design Goals
Abstract away the details of the moves and pass on determination of winning a move
to each possible Move. In other words, the main class shouldn't care what the move
that was made by a player actually was and simply return whether it was a winning or losing move,
similar to how the wager example in class "did not care" about what kind of bet it was
when it placed a bet.


### CRC Card Classes

This class's purpose or value is to represent a Player in the game:

| Player                     |   |
|----------------------------|---|
| Move generateMove()        |   |
| int getScore()             |   |
| int setScore(int newScore) |   |

| Move                               |      |
|------------------------------------|------|
| boolean abstract winsAgainst(Move) | Move |

| Rock extends Move            |      |
|------------------------------|------|
| boolean winsAgainst(Move)    | Move |

This class's purpose or value is to represent a customer's order:
```java
public class Order {
     // returns whether or not the given items are available to order
     public boolean isInStock (OrderLine items)
     // sums the price of all the given items
     public double getTotalPrice (OrderLine items)
     // returns whether or not the customer's payment is valid
     public boolean isValidPayment (Customer customer)
     // dispatches the items to be ordered to the customer's selected address
     public void deliverTo (OrderLine items, Customer customer)
 }
 ```

This class's purpose or value is to manage something:
```java
public class Something {
     // sums the numbers in the given data
     public int getTotal (Collection<Integer> data)
	 // creates an order from the given data
     public Order makeOrder (String structuredData)
 }
```


### Use Cases

* A new game is started with two players, their scores are reset to 0.
 ```java
Player p1 = new Player();
Player p2 = new Player();
}
 ```

* A player chooses his RPS "weapon" with which he wants to play for this round.
 ```java
Move m1 = p1.generateMove();
Move m2 = p2.generateMove();
 ```

* Given three players' choices, one player wins the round, and their scores are updated.
 ```java
if (m1.winsAgainst(m2)) {
    p1.setScore(p1.getScore() + 1);
} else {
    p2.setScore(p2.getScore() + 1);
}
 ```

* A new choice is added to an existing game and its relationship to all the other choices is updated.
 ```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
 ```

* A new game is added to the system, with its own relationships for its all its "weapons".
 ```java
 Something thing = new Something();
 Order o = thing.makeOrder("coffee,large,black");
 o.update(13);
 ```