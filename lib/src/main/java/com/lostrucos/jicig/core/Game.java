package com.lostrucos.jicig.core;

import java.util.List;

/**
 * Represents a game with complete but imperfect information.
 */
public interface Game {

    /**
     * Returns the list of possible actions for a given player in a given game state.
     *
     * @param playerIndex the index of the player.
     * @param gameState   the current state of the game.
     * @return the list of possible actions.
     */
    List<Action> getPlayerActions(int playerIndex, GameState gameState);

    /**
     * Returns the information set for a given player in a given game state.
     *
     * @param playerIndex the index of the player.
     * @param gameState the current state of the game.
     * @return the information set for the player.
     */
    InformationSet getInformationSet(int playerIndex, GameState gameState);

    /**
     * Returns the initial state of the game.
     *
     * @return the initial game state.
     */
    GameState getInitialState();

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players.
     */
    int getNumberOfPlayers();

    // Restituisce il nuovo stato di gioco risultante dall'applicazione delle azioni specificate allo stato di gioco corrente.
    GameState getNextState(GameState state, List<Action> actions);

    // Restituisce un valore booleano che indica se lo stato di gioco specificato è uno stato terminale.
    boolean isTerminal(GameState state);

    // Restituisce l'utilità (punteggio o ricompensa) per il giocatore specificato nello stato di gioco dato.
    double getUtility(GameState state, int player);

}
