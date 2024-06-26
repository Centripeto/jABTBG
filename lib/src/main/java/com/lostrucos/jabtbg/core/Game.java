package com.lostrucos.jabtbg.core;

import java.util.List;

/**
 * Represents a game with complete but imperfect information.
 */
public interface Game <T extends GameState<E>, E extends Action>{

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
     * @param gameState the current state of the game.
     * @return the information set for the player.
     */
    //Non so se ha senso, cioè, se io mi trovo in uno stato, allora non sto nell'information set(?) boh, da rivedere
    InformationSet getInformationSet(int playerIndex, T gameState);

    /**
     * Returns the initial state of the game.
     *
     * @return the initial game state.
     */
    T getInitialState(); //Non serve, basta lo stato corrente

    T getCurrentState(); //Metodo fondamentale per capire in che stato si trova il gioco

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players.
     */
    int getNumberOfPlayers();

    // Restituisce il nuovo stato di gioco risultante dall'applicazione delle azioni specificate allo stato di gioco corrente.
    T getNextState(T state, List<E> actions); //non utilizzato poiché viene applicata sempre un'azione alla volta

    T getNextState(T state, E action);

    // Restituisce un valore booleano che indica se lo stato di gioco specificato è uno stato terminale.
    boolean isTerminal(T state); //dovrebbe interrogare il gamestate e chiedere a lui

    // Restituisce l'utilità (punteggio o ricompensa) per il giocatore specificato nello stato di gioco dato.
    double getUtility(T state, int player); //In realtà è un metodo che serve solo all'algoritmo

    int getCurrentPlayer(); //restituisce l'indice del giocatore di turno

}
