package com.lostrucos.jabtbg.core;

import java.util.List;

/**
 * Represents the state of the game and has all the game information at a particular point in the game.
 * A GameState is derived from the current composition of the game information.
 */
public interface GameState<E extends Action> {

    /**
     * Returns the index of the current player.
     *
     * @return the index of the current player.
     */
    int getCurrentPlayer();

    /**
     * Checks if the current state is a terminal node.
     *
     * @return true if this state is a terminal node, false otherwise.
     */
    boolean isTerminalNode();

    /**
     * Tells if the current game state is a tie.
     *
     * @return true if the game is a tie false otherwise
     */
    boolean isTie();

    /**
     * Modifies the current game state by applying an action
     *
     * @param action the action to be applied to the current game state
     * @return the new game state after the action is applied.
     */
    GameState<E> applyAction(E action);

    /**
     * Creates a deep copy of the current game state
     *
     * @return the cloned game state
     */
    GameState<E> deepCopy();

    /**
     * Method that returns a list of all the available actions in this game state for the given player
     *
     * @param playerIndex the index of the player
     * @return the actions available to that player for this game state
     */
    List<E> getAvailableActions(int playerIndex);

    /**
     * Utility function that gives us the utility for the given player in this game state
     *
     * @param playerIndex the index of the player
     * @return the utility the player receives
     */
    double getUtility(int playerIndex);

    /**
     * Returns a list of all the indexes of the players still in game in this game state
     *
     * @return the indexes of the players still in game
     */
    List<Integer> getPlayersInGame();

    /**
     * Method that tells whether a player, represented by his index, is still in the game in this game state
     *
     * @param player the index of the player
     * @return true if the player is still in the game false otherwise
     */
    boolean isPlayerStillInGame(int player);

    /**
     * Returns a representation of the state of the game.
     *
     * @return a string representation of the game state.
     */
    String toString();
}
