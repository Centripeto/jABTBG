package com.lostrucos.jabtbg.tris;

import com.lostrucos.jabtbg.core.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a tic-tac-toe game state.
 */
public class TrisGameState implements GameState<TrisAction> {

    private final Board board;
    private int currentPlayer;
    private boolean isTie = false;
    private final BasicStrategy utilityStrategy;

    public TrisGameState(Board board, int currentPlayer, BasicStrategy utilityStrategy) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.utilityStrategy = utilityStrategy;
    }

    @Override
    public boolean isTie() {
        return isTie;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isCellFree(int x, int y) {
        return this.board.getBoard()[x][y] == Symbol.FREE;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getBoard()[i][j] == Symbol.FREE)
                    return false;
            }
        }
        return true;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isActionLegal(TrisAction action) {
        if (action.getX() < 0 || action.getX() > 2 || action.getY() < 0 || action.getY() > 2)
            return false;
        if (this.board.getBoard()[action.getX()][action.getY()] != Symbol.FREE)
            return false;
        return true;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public List<TrisAction> getAvailableActions(int playerIndex) {
        List<TrisAction> availableActions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.isCellFree(i, j)) {
                    availableActions.add(new TrisAction(i, j, playerIndex));
                }
            }
        }
        return availableActions;
    }

    @Override
    public boolean isTerminalNode() {
        if (checkRow() || checkColumns() || checkDiagonals())
            return true;
        else if (isBoardFull()) {
            isTie = true;
            return true;
        }
        return false;
    }

    @Override
    public GameState<TrisAction> applyAction(TrisAction action) {
        switch (currentPlayer) {
            case 0:
                this.getBoard().setSymbol(action.getX(), action.getY(), Symbol.CROSS);
                currentPlayer++;
                break;
            case 1:
                this.getBoard().setSymbol(action.getX(), action.getY(), Symbol.CIRCLE);
                currentPlayer--;
                break;
        }
        return this;
    }

    @Override
    public GameState<TrisAction> deepCopy() {
        Board copyBoard = new Board();
        int copyPlayer = currentPlayer;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copyBoard.getBoard()[i][j] = this.board.getBoard()[i][j];
            }
        }
        return new TrisGameState(copyBoard, copyPlayer, utilityStrategy);
    }

    private boolean checkRow() {
        for (int i = 0; i < 3; i++) {
            if (board.getBoard()[i][0] == board.getBoard()[i][1] && board.getBoard()[i][1] == board.getBoard()[i][2] && board.getBoard()[i][0] != Symbol.FREE)
                return true;
        }
        return false;
    }

    private boolean checkColumns() {
        for (int i = 0; i < 3; i++) {
            if (board.getBoard()[0][i] == board.getBoard()[1][i] && board.getBoard()[1][i] == board.getBoard()[2][i] && board.getBoard()[0][i] != Symbol.FREE)
                return true;
        }
        return false;
    }

    private boolean checkDiagonals() {
        if (board.getBoard()[0][0] == board.getBoard()[1][1] && board.getBoard()[1][1] == board.getBoard()[2][2] && board.getBoard()[0][0] != Symbol.FREE)
            return true;
        if (board.getBoard()[0][2] == board.getBoard()[1][1] && board.getBoard()[1][1] == board.getBoard()[2][0] && board.getBoard()[0][2] != Symbol.FREE)
            return true;
        return false;
    }

    public int checkForWinner() {
        // Check horizontal, vertical, and diagonal lines
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getBoard()[i][j] == Symbol.CIRCLE) {
                    if (checkRowCircle() || checkColumnsCircle() || checkDiagonalsCircle()) {
                        return 0;
                    }
                } else if (board.getBoard()[i][j] == Symbol.CROSS) {
                    if (checkRowCross() || checkColumnsCross() || checkDiagonalsCross()) {
                        return 1;
                    }
                }
            }
        }
        return -1; // No winner
    }

    private boolean checkRowCircle() {
        for (int i = 0; i < 3; i++) {
            if (board.getBoard()[i][0] == board.getBoard()[i][1] && board.getBoard()[i][1] == board.getBoard()[i][2] && board.getBoard()[i][0] == Symbol.CIRCLE)
                return true;
        }
        return false;
    }

    private boolean checkColumnsCircle() {
        for (int i = 0; i < 3; i++) {
            if (board.getBoard()[0][i] == board.getBoard()[1][i] && board.getBoard()[1][i] == board.getBoard()[2][i] && board.getBoard()[0][i] == Symbol.CIRCLE)
                return true;
        }
        return false;
    }

    private boolean checkDiagonalsCircle() {
        if (board.getBoard()[0][0] == board.getBoard()[1][1] && board.getBoard()[1][1] == board.getBoard()[2][2] && board.getBoard()[0][0] == Symbol.CIRCLE)
            return true;
        if (board.getBoard()[0][2] == board.getBoard()[1][1] && board.getBoard()[1][1] == board.getBoard()[2][0] && board.getBoard()[0][2] == Symbol.CIRCLE)
            return true;
        return false;
    }

    private boolean checkRowCross() {
        for (int i = 0; i < 3; i++) {
            if (board.getBoard()[i][0] == board.getBoard()[i][1] && board.getBoard()[i][1] == board.getBoard()[i][2] && board.getBoard()[i][0] == Symbol.CROSS)
                return true;
        }
        return false;
    }

    private boolean checkColumnsCross() {
        for (int i = 0; i < 3; i++) {
            if (board.getBoard()[0][i] == board.getBoard()[1][i] && board.getBoard()[1][i] == board.getBoard()[2][i] && board.getBoard()[0][i] == Symbol.CROSS)
                return true;
        }
        return false;
    }

    private boolean checkDiagonalsCross() {
        if (board.getBoard()[0][0] == board.getBoard()[1][1] && board.getBoard()[1][1] == board.getBoard()[2][2] && board.getBoard()[0][0] == Symbol.CROSS)
            return true;
        if (board.getBoard()[0][2] == board.getBoard()[1][1] && board.getBoard()[1][1] == board.getBoard()[2][0] && board.getBoard()[0][2] == Symbol.CROSS)
            return true;
        return false;
    }

    @Override
    public List<Integer> getPlayersInGame() {
        return null;
    }

    @Override
    public boolean isPlayerStillInGame(int player) {
        return false;
    }

    @Override
    public double getUtility(int playerIndex) {
        return utilityStrategy.calculateUtility(this, playerIndex);
    }
}
