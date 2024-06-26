package com.lostrucos.jabtbg.tris;
import com.lostrucos.jabtbg.algorithms.mcts.MCTSAlgorithm;
import com.lostrucos.jabtbg.core.*;

import java.util.ArrayList;
import java.util.List;
public class TrisGame extends AbstractGame<TrisGameState,TrisAction> implements Game<TrisGameState,TrisAction> {
    private final List<Agent<TrisGameState,TrisAction>> players;
    private TrisGameState currentState;
    private int currentPlayer; //id del giocatore di turno;


    public TrisGame(int playerCount){
        super(playerCount);
        currentState = new TrisGameState(new Board(),0);
        currentPlayer = 0;
        players = new ArrayList<>();
        initializePlayers();
    }


    private void initializePlayers(){
        Player player1 = new Player(0);
        //MCTSAlgorithm<TrisAction,TrisGameState> mcts2 = new MCTSAlgorithm<>(5000,Math.sqrt(2.0));
        MCTSAlgorithm<TrisAction,TrisGameState> mcts = new MCTSAlgorithm<>(1000,Math.sqrt(2.0));
        MCTSPlayer player2 = new MCTSPlayer(1,mcts);
        //MCTSPlayer player3 = new MCTSPlayer(1,mcts2);
        players.add(player1);
        players.add(player2);
        //players.add(player3);
    }

    public void playGame(){
        System.out.println("Benvenuti in tris, di seguito la configurazione iniziale");
        currentState.getBoard().display();

        do{
            currentPlayer=currentState.getCurrentPlayer();
            TrisAction action = players.get(currentPlayer).getAction(currentState);
            currentState=this.getNextState(currentState,action);
            currentState.getBoard().display();
        }while(!currentState.isTerminalNode());

        System.out.println("La partita è terminata");
        if(!currentState.isTie())
            System.out.println("Il vincitore è il Player "+currentPlayer);
        else System.out.println("E' un pareggio");
        currentState.getBoard().display();
    }


    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public TrisGameState getCurrentState() {
        return currentState;
    }

    //Qui diamo per scontato che la action è valida, quindi la mossa si può effettuare
    //Devo aggionrare il current player in state e in game
    @Override
    public TrisGameState getNextState(TrisGameState state, TrisAction action) {
        switch (currentPlayer){
            case 0:
                state.getBoard().setSymbol(action.getX(),action.getY(), Symbol.CROSS);
                state.setCurrentPlayer(1);
                break;
            case 1:
                state.getBoard().setSymbol(action.getX(),action.getY(), Symbol.CIRCLE);
                state.setCurrentPlayer(0);
                break;
        }
        return state;
    }


// non utilizzato, per ora lascio qui
    @Override
    public List<TrisAction> getPlayerActions(int player, TrisGameState state) {
        List<TrisAction> availableActions = new ArrayList<>();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(state.isCellFree(i,j)){
                    availableActions.add(new TrisAction(i,j,player));
                }
            }
        }
        return availableActions;
    }

    @Override
    public InformationSet getInformationSet(int playerIndex, TrisGameState gameState) {
        return null;
    }

    @Override
    public TrisGameState getNextState(TrisGameState state, List<TrisAction> actions) {
        return null;
    }


    @Override
    public boolean isTerminal(TrisGameState state) {
        return state.isTerminalNode();
    }

    //Non mi serve
    @Override
    public double getUtility(TrisGameState state, int player) {
        //Devo lanciare un'eccezione se lo stato non è terminale
        if(!state.isTerminalNode())
            return -1;
        //Altrimenti, in base al vincitore, do una ricompensa
    return 0;

    }


    //Non mi serve
    @Override
    public TrisGameState getInitialState() {
        return null;
    }


}
