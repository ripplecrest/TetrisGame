package org.TetrisGame;

import javax.swing.*;

public class Board extends JPanel {
    private final int BOARD_WIDTH = 20;
    private final int BOARD_HEIGHT = 22;
    private final int PERIOD_INTERVAL = 300;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinedRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece;
    private Shape.Tet[] board;


    public Board(Tetris parent) {
        intBoard(parent);
    }
    private void intBoard(Tetris parent) {
        setFocusable(true);
        statusbar = parent.getStatusbar();
    }
    private int squareWidth(){
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }
    private int squareHeight(){
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }
    private Shape.Tet shapeAt(int x, int y){
        return board[(y * BOARD_WIDTH) + x];
    }
    void start(){
        curPiece = new Shape();
        board = new Shape.Tet[BOARD_WIDTH * BOARD_HEIGHT];
        clearBoard();
        newPiece();
    }
    private void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            statusbar.setText("Game Paused");
        } else {
            statusbar.setText(String.valueOf(numLinedRemoved));
        }
        repaint();
    }
}
