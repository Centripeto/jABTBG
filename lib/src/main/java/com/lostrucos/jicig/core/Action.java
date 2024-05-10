package com.lostrucos.jicig.core;

public interface Action {

    // Restituisce un nuovo oggetto GameState che rappresenta lo stato del gioco dopo l'applicazione di questa azione allo stato di gioco corrente.
    GameState applyAction(GameState state);

    // Restituisce l'indice del giocatore che esegue questa azione.
    int getPlayer();

}
