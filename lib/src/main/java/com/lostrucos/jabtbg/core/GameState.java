package com.lostrucos.jabtbg.core;

import java.util.List;

/**
 * Represents the state of the game and has all the game informations at a particular point in the game.
 * A GameState is derived from the current composition of the game information.
 */
public interface GameState {

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
     * Returns the utility value for a given player in this state.
     *
     * @param playerIndex the index of the player.
     * @return the utility value.
     */
    double getUtility(int playerIndex);

    /**
     * Returns the information set (InformationSet) known to the specified playerIndex in that state.
     *
     * @param playerIndex the index of the player.
     * @return the playerIndex's InformationSet.
     */
    InformationSet getInformationSet(int playerIndex);

    // Restituisce una lista degli indici dei giocatori coinvolti in questo stato di gioco.
    List<Integer> getPlayersInGame();

    // Restituisce un valore booleano che indica se il giocatore specificato è ancora presente in gioco (non eliminato) in questo stato di gioco.
    boolean isPlayerStillInGame(int player);

    /**
     * Checks if the current state is a chance node.
     *
     * @return true if this state is a chance node, false otherwise.
     */
    // Restituisce un valore booleano che indica se lo stato corrente è un nodo di chance (ad esempio, se c'è un evento casuale come il lancio di un dado).
    boolean isChanceNode();

    /**
     * Returns a list of states, one for every outcome derived from this state.
     *
     * @return the list of states for every outcome.
     */
    // Se lo stato corrente è un nodo di chance, restituisce una lista di possibili stati successivi con le relative probabilità.
    List<GameState> getChanceOutcomes();

    /**
     * Returns a representation of the state of the game.
     *
     * @return a string representation of the game state.
     */
    String toString();
}
