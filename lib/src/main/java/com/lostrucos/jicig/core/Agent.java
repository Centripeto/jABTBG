package com.lostrucos.jicig.core;

public interface Agent {

    // Restituisce l'azione che l'agente sceglierà di eseguire nello stato di gioco specificato.
    Action getAction(GameState state);

    // Consente all'agente di aggiornare il suo stato interno dopo che un'azione è stata eseguita nello stato di gioco specificato.
    void updateAfterAction(GameState state, Action action);

    // Reimposta lo stato interno dell'agente prima di iniziare una nuova partita.
    void reset();

    // Restituisce l'indice del giocatore
    int getPlayerIndex();
}
