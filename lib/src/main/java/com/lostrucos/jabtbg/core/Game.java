package com.lostrucos.jabtbg.core;

import java.util.List;

/**
 * Represents a game with complete but imperfect information.
 */
public interface Game<T extends GameState<E>, E extends Action> {

    /**
     * Returns the initial state of the game.
     *
     * @return the initial game state.
     */
    T getInitialState();

    /**
     * Return the current state of the game
     *
     * @return the current game state
     */
    T getCurrentState();

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players.
     */
    int getNumberOfPlayers();


    /**
     * Updates the given game state by applying a list of actions
     * @param state the state to be updated
     * @param actions the actions to be applied
     * @return the new game state after having applied all the actions
     */
    T getNextState(T state, List<E> actions);

    /**
     * Updates the given game state by applying an action
     *
     * @param state  the state to be updated
     * @param action the action to be applied
     * @return the new state after having applied the action
     */
    T getNextState(T state, E action);



    /**
     * Method that returns the index of the current player
     *
     * @return the index of the current player
     */
    int getCurrentPlayer();




    boolean isTerminal(GameState state);  //TODO: da levare, ma da errore cfrm

    //TODO: da levare, ma da errore su altri algoritmi
    double getUtility(GameState state, int playerIndex);


    /**
     * Returns the list of possible actions for a given player in a given game state.
     *
     * @param playerIndex the index of the player.
     * @param gameState   the current state of the game.
     * @return the list of possible actions.
     */
    //Abbiamo deciso che la responsabilità è di GameState, per ora lo lascio qui
    List<E> getPlayerActions(int playerIndex, T gameState);


    /**
     * Returns the information set for a given player in a given game state.
     *
     * @param playerIndex the index of the player.
     * @param gameState   the current state of the game.
     * @return the information set for the player.
     */
    InformationSet getInformationSet(int playerIndex, T gameState);
}
