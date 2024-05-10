package com.lostrucos.jicig.core;

import java.util.List;

public interface GameState {

    // Restituisce l'indice del giocatore di cui è il turno nello stato corrente.
    int getPlayerToMove();

    // Restituisce una lista degli indici dei giocatori coinvolti in questo stato di gioco.
    List<Integer> getPlayersInGame();

    // Restituisce un valore booleano che indica se il giocatore specificato è ancora attivo (non eliminato) in questo stato di gioco.
    boolean isPlayerActive(int player);

    // Restituisce l'insieme di informazioni (InformationSet) note al giocatore specificato in quello stato.
    InformationSet getInformationSet(int player);

    // Restituisce un valore booleano che indica se lo stato corrente è un nodo terminale (ovvero un nodo con nessun figlio dove il gioco è concluso).
    boolean isTerminalNode();

    // Restituisce un valore booleano che indica se lo stato corrente è un nodo di chance (ad esempio, se c'è un evento casuale come il lancio di un dado).
    boolean isChanceNode();

    // Se lo stato corrente è un nodo di chance, restituisce una lista di possibili stati successivi con le relative probabilità.
    List<GameState> getChanceOutcomes();

    // Restituisce l'utilità (punteggio o ricompensa) per il giocatore specificato se lo stato corrente è uno stato terminale.
    double getUtility(int player);

}
