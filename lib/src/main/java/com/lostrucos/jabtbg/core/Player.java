package com.lostrucos.jabtbg.core;

/**
 * Represents a player entity in the game.
 */
public interface Player<T extends GameState<E>, E extends Action> {

    /**
     * Returns the index of the player this agent is controlling.
     *
     * @return the index of the player.
     */
    int getPlayerIndex();

    /**
     * Method that allows the agent to choose an action between the ones available in the given state.
     *
     * @param state the current state
     * @return the action selected by the agent
     */
    E getAction(T state);

    /**
     * Returns a representation of the agent.
     *
     * @return a string representation of the agent.
     */
    String toString();
}
