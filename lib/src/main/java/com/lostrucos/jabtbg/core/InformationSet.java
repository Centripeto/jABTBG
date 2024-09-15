package com.lostrucos.jabtbg.core;

import java.util.List;

/**
 * Represents the information set known to a player in a given state of the game.
 */
public interface InformationSet<T extends GameState<E>, E extends Action> {

    /**
     * Returns the index of the player to which this set of information belongs.
     *
     * @return the index of the player
     */
    int getPlayerIndex();

    /**
     * Determines a pseudo-state for this information set.
     *
     * @return a pseudo-state.
     */
    T determinePseudoState();

    /**
     * Returns a list of possible game states consistent with the information available to the player.
     *
     * @return the list of possible game states.
     */
    List<T> getPossibleStates();

    /**
     * Returns a list of valid actions game states consistent with the information available to the player.
     *
     * @param playerIndex the index of the player
     * @return the list of possible game states.
     */
    List<E> getPlayerActions(int playerIndex);

    /**
     * Returns the new set of information resulting from the application of the specified action.
     *
     * @param action the action that generate the new information set
     * @return the new information set
     */
    InformationSet<T, E> getNextInformationSet(E action);

    /**
     * Checks if the current information state is a terminal node.
     *
     * @return true if this state is a terminal information set, false otherwise.
     */
    boolean isTerminal();

    /**
     * Returns a representation of the information set.
     *
     * @return a string representation of the information set.
     */
    String toString();
}
