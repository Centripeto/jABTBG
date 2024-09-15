package com.lostrucos.jabtbg.algorithms.crm;

import com.lostrucos.jabtbg.core.Action;
import com.lostrucos.jabtbg.core.Game;
import com.lostrucos.jabtbg.core.GameState;
import com.lostrucos.jabtbg.core.Player;

public class CFRMAgent<T extends GameState<E>, E extends Action> implements Player<T, E> {
    private int playerIndex;
    private CFRMAlgorithm<T, E> cfrAlgorithm;
    private Game<T, E> game;

    public CFRMAgent(int playerIndex, Game<T, E> game, int iterations) {
        this.playerIndex = playerIndex;
        this.game = game;
        this.cfrAlgorithm = new CFRMAlgorithm<>(iterations, 1.0);
    }

    @Override
    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public E getAction(T state) {
        // Assicurati che l'algoritmo sia aggiornato con lo stato corrente del gioco
        cfrAlgorithm.updateAfterAction(state, null);

        // Usa l'algoritmo CFR per scegliere l'azione migliore
        return cfrAlgorithm.chooseAction(state);
    }

    public void train(T initialState) {
        // Esegui il training dell'algoritmo CFR prima di iniziare a giocare
        cfrAlgorithm.initialize(initialState);
        // Il training effettivo avviene internamente in updateAfterAction
    }

    @Override
    public String toString() {
        return "CFR Agent (Player " + playerIndex + ")";
    }
}