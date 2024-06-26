package com.lostrucos.jabtbg.core;

/**
 * Represents an action that can be taken in the game.
 */
public interface Action {

    /**
     * Applies this action to the given game state and returns the resulting game state.
     *
     * @param gameState the current state of the game.
     * @return the resulting game state after the action is applied.
     */
    GameState applyAction(GameState gameState); //siamo sicuri che questa è responsabilità di action?
    //Ciòè action dovrebbe rappresentare un'azione. ma non credo che sia action stessa ad applicarsi, ma è un
    //player o un game state che la utilizza. Da rivedere

    /**
     * Returns a representation of the action.
     *
     * @return a string representation of the action.
     */
    String toString();

    /**
     * Returns the index of the player performing this action.
     *
     * @return the index of the player.
     */
    int getPlayer();

}
