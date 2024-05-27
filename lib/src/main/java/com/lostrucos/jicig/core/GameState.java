package com.lostrucos.jicig.core;

import java.util.List;

/**
 * Represents the state of the game at a particular point in time.
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
     * @return true if it is a terminal node, false otherwise.
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

    /**
     * Returns a representation of the state of the game.
     *
     * @return a string representation of the game state.
     */
    String toString();

    // Restituisce una lista degli indici dei giocatori coinvolti in questo stato di gioco.
    List<Integer> getPlayersInGame();

    // Restituisce un valore booleano che indica se il giocatore specificato è ancora presente in gioco (non eliminato) in questo stato di gioco.
    boolean isPlayerStillInGame(int player);

    // Restituisce un valore booleano che indica se lo stato corrente è un nodo di chance (ad esempio, se c'è un evento casuale come il lancio di un dado).
    boolean isChanceNode();

    // Se lo stato corrente è un nodo di chance, restituisce una lista di possibili stati successivi con le relative probabilità.
    List<GameState> getChanceOutcomes();



}
