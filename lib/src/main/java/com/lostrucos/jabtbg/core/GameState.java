package com.lostrucos.jabtbg.core;

import java.util.List;

/**
 * Represents the state of the game and has all the game informations at a particular point in the game.
 * A GameState is derived from the current composition of the game information.
 */
public interface GameState<E extends Action>{

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

    boolean isTie();

    GameState<E> applyAction(E action);
    GameState<E> copius();

    /**
     * Returns the utility value for a given player in this state.
     *
     * @param playerIndex the index of the player.
     * @return the utility value.
     */
    double getUtility(int playerIndex); //metodo già presente in game, ha senso?

    /**
     * Returns the information set (InformationSet) known to the specified playerIndex in that state.
     *
     * @param playerIndex the index of the player.
     * @return the playerIndex's InformationSet.
     */
    InformationSet getInformationSet(int playerIndex);  //ok, qui forse ha più senso
    //Mi spiego, se gioco a briscola con 2 giocatori, e ci troviamo nello stato x, ed è il turno del player 1,
    //L'information set mi restituisce le informazioni che finora possiede il player 1, ovvero:
    //le carte in mano sua, la pila delle carte giocate, e tutti i game stati attraversati fino a quel momento.
    //Le informazioni mancanti in questo information set sono le carte dell'avversario e le carte del mazzo.

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

    List<E> getAvailableActions(int playerIndex);  //restituisce le azioni disponibili per un giocatore in questo stato

    GameState<E> getNextState(E action); //restituisce il prossimo stato di gioco
}
