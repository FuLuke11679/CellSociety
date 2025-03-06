# Breakout Abstractions Lab Discussion

#### Names: Luke Fu, Ishan Madan, Daniel Rodriguez-Florido, and , and Palo Silva

### Block

This superclass's purpose as an abstraction:

```java
 public class Block {
  //defines block class that includes health and position
  public Block(int x, int y, int health){
    
  }
   //Updates block color depending on the number of times it was hit
    private abstract void updateBlockColor(){
      
    }
    //Takes in the game and arraylist of blocks to remove the corresponding block to the root
     public abstract void destroyBlock (Game myGame, ArrayList<Block> blocks){
      
     }
     
  
 }
```

#### Subclasses

Each subclass's high-level behavorial differences from the superclass

```java
//Needs to be set a different color 
public class default_Block extends Block {
  //defines block class that includes health and position
  public default_Block(int x, int y, int health){
    
  }
   //Updates block color depending on the number of times it was hit
   @override
    private void updateBlockColor(){
      
    }
    
    //Takes in the game and arraylist of blocks to simply remove the corresponding block to the root
    @override
     public abstract void destroyBlock (Game myGame, ArrayList<Block> blocks){
      
     }
     
  
 }
```

#### Affect on Game/Level class

Which methods are simplified by using this abstraction and why

* Methods of a seperate destroyBlock and updateColor for blocks that are included in the main Block
  class.
* UpdateColor is implicitely changed in the construction of each block as a seperate variable

### Power-up

This superclass's purpose as an abstraction: Reduces the number of variables passed and additional
methods in the main Block class

```java
//in this version of destroy block, power up is activated
public class powerup_Block extends Block {
  //defines block class that includes health and position
  public default_Block(int x, int y, int health){

  }
  //Updates block color depending on the number of times it was hit
  @override
  private void updateBlockColor(){

  }

  //Takes in the game and arraylist of blocks to simply remove the corresponding block to the root
  @override
  public abstract void destroyBlock (Game myGame, ArrayList<Block> blocks){

  }


}
```

#### Subclasses

Each subclass's high-level behavioral differences from the superclass:

* Different colors and behaviors when block is removed(exploding, speeding up, etc.)

#### Affect on Game/Level class

Which methods are simplified by using this abstraction and why

* DestroyBlock and updateBlockColor

### Others?
