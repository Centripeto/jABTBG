package com.lostrucos.jicig.core;

import java.util.List;

public interface Game {

    // Restituisce lo stato iniziale del gioco.
    GameState getInitialState();

    // Restituisce il numero di giocatori nel gioco.
    int getPlayerCount();

    // Restituisce una lista di azioni valide che il giocatore specificato può eseguire nello stato di gioco dato.
    List<Action> getPlayerActions(int player, GameState state);

    // Restituisce il nuovo stato di gioco risultante dall'applicazione delle azioni specificate allo stato di gioco corrente.
    GameState getNextState(GameState state, List<Action> actions);

    // Restituisce un valore booleano che indica se lo stato di gioco specificato è uno stato terminale.
    boolean isTerminal(GameState state);

    // Restituisce l'utilità (punteggio o ricompensa) per il giocatore specificato nello stato di gioco dato.
    double getUtility(GameState state, int player);

}
