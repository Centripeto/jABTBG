package com.lostrucos.jicig.core;

public interface Algorithm {

    // Inizializza l'algoritmo con il gioco e l'agente specificati.
    void initialize(Game game, Agent agent);

    // Restituisce l'azione che l'algoritmo sceglierà di eseguire nello stato di gioco specificato.
    Action getAction(GameState state);

    // Consente all'algoritmo di aggiornare il suo stato interno dopo che un'azione è stata eseguita nello stato di gioco specificato.
    void updateAfterAction(GameState state, Action action);

}
