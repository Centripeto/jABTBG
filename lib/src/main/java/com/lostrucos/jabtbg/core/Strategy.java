package com.lostrucos.jabtbg.core;

import java.util.List;

public interface Strategy<T extends GameState<E>, E extends Action> {

    /**
     * Perform a calculation of the utility value of an action available in a certain state for the giving player.
     *
     * @param state the current state of the game
     * @param playerIndex the player's index
     * @return the value derived from the calculation
     */
    double calculateUtility(T state, int playerIndex);

    /**
     * Returns a list of convenient actions that are calculated using the using of game related strategies.
     *
     * @param state the current state of the game
     * @param currentPlayer the current player's index
     * @return the list of convenient actions.
     */
    List<E> suggestStrategicMoves(T state, int currentPlayer);
}
