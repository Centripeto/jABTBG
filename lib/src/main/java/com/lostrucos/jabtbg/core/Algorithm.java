package com.lostrucos.jabtbg.core;

/**
 * Represents an algorithm used by an agent in order to take the best action during the game.
 */
public interface Algorithm<T extends GameState<E>, E extends Action> {

    /**
     * Initializes the algorithm with the given state of the game
     *
     * @param state the state of the game
     */
    void initialize(T state);

    /**
     * Resets the algorithm internal information.
     */
    void reset();

    /**
     * Sets the strategy.
     */
    void setStrategy(Strategy<T, E> strategy);

    /**
     * Returns the action chosen by the algorithm for the given game state.
     *
     * @param gameState the current state of the game.
     * @return the chosen action.
     */
    E chooseAction(T gameState);

    /**
     * Applies a pseudo action to a game state during the simulation.
     * Using this function, the original state of the game remains unchanged and no changes are made.
     * To ensure data invariability, a deep copy of the node represented by the state from which the action will be applied is first performed.
     *
     * @param state the current state of the simulation, not the actual game state
     * @param action the action to apply
     */
    void applyPseudoAction(T state, E action);

    /**
     * Updates the algorithm's internal state after an action has been taken.
     *
     * @param gameState the new state of the game.
     * @param action the action that was taken.
     */
    void updateAfterAction(T gameState, E action);

    /**
     * Returns a representation of the algorithm.
     *
     * @return a string representation of the algorithm.
     */
    String toString();
}
