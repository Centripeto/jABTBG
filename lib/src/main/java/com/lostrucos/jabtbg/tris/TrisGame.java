package com.lostrucos.jabtbg.tris;

import com.lostrucos.jabtbg.algorithms.mcts.MCTSAlgorithm;
import com.lostrucos.jabtbg.core.*;

import java.util.ArrayList;
import java.util.List;

public class TrisGame extends AbstractGame<TrisGameState, TrisAction> implements Game<TrisGameState, TrisAction> {
    private final List<com.lostrucos.jabtbg.core.Player<TrisGameState, TrisAction>> players;
    private TrisGameState currentState;
    private int currentPlayer; //id del giocatore di turno;
    private final BasicStrategy utilityStrategy;

    public TrisGame(int playerCount) {
        super(playerCount);
        currentState = new TrisGameState(new Board(), 0, new BasicStrategy());
        currentPlayer = 0;
        players = new ArrayList<>();
        utilityStrategy = new BasicStrategy();
        initializePlayers();
    }

    private void initializePlayers() {
        Player player1 = new Player(0);
        players.add(player1);

        MCTSAlgorithm<TrisAction, TrisGameState> mcts = new MCTSAlgorithm<>(1000, Math.sqrt(2.0));
        MCTSPlayer player2 = new MCTSPlayer(1, mcts, utilityStrategy);
        players.add(player2);
    }

    public void playGame() {
        System.out.println("Benvenuti in tris, di seguito la configurazione iniziale");
        currentState.getBoard().display();

        do {
            currentPlayer = currentState.getCurrentPlayer();
            TrisAction action = players.get(currentPlayer).getAction(currentState);
            currentState = this.getNextState(currentState, action);
            System.out.println();
            currentState.getBoard().display();
        } while (!currentState.isTerminalNode());

        System.out.println("La partita è terminata");
        if (!currentState.isTie())
            System.out.println("Il vincitore è il Player " + currentPlayer);
        else System.out.println("E' un pareggio");
        currentState.getBoard().display();
    }

    @Override
    public TrisGameState getNextState(TrisGameState state, TrisAction action) {
        switch (currentPlayer) {
            case 0:
                state.getBoard().setSymbol(action.getX(), action.getY(), Symbol.CROSS);
                state.setCurrentPlayer(1);
                break;
            case 1:
                state.getBoard().setSymbol(action.getX(), action.getY(), Symbol.CIRCLE);
                state.setCurrentPlayer(0);
                break;
        }
        return state;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public InformationSet<TrisGameState, TrisAction> getInformationSet(int playerIndex, TrisGameState gameState) {
        return null;
    }
}
