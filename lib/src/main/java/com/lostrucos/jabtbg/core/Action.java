package com.lostrucos.jabtbg.core;

/**
 * Represents an action that can be taken in the game.
 * In the case of perfect-information games, an action links a given game state to a new child of the one in which the action was taken.
 * In the case of imperfect-but-complete-information games, on the other hand, taking an action creates a new information set.
 */
public interface Action {

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
