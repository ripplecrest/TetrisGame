package org.TetrisGame;

import javax.swing.*;
import java.awt.*;

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

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Shape.Tet shape = shapeAt(j, BOARD_HEIGHT -i -1);
                if(shape != Shape.Tet.NoShape){
                    drawSquare(g, j* squareWidth(),
                            boardTop + i*squareHeight(), shape);
                }
            }
        }
        if(curPiece.getShape() != Shape.Tet.NoShape){
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY + curPiece.y(i);

                drawSquare(g, x*squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1)*squareHeight(),
                        curPiece.getShape());
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tet shape){
        Color colors[] = { new Color(0,0,0) , new Color(204,102,102),
                new Color(102,204,102), new Color(102,102,204), new Color (204,204,102),
                new Color(204,102,204), new Color(218,170,0)
        };

        var color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x+1, y+1, squareWidth()-2, squareHeight()-2);
        g.setColor(color.brighter());
        g.drawLine(x, y+squareHeight()-1, x,y);
        g.drawLine(x,y,x+squareWidth()-1,y);

        g.setColor(color.darker());
        g.drawLine(x+1, y+squareHeight()-1, x+ squareWidth() -1, y+squareHeight()-1);
        g.drawLine(x+squareWidth()-1, y+squareHeight()-1, x + squareWidth()-1, y+1);

    }

    private void dropDown(){
        int newY = curY;
        while (newY > 0){
            if(!tryMove(curPiece, curX, newY-1)){
                break;
            }
            newY--;
        }
        pieceDropped();
    }
    private void oneLineDown() {
        if(!tryMove(curPiece, curX, curY - 1)){
            pieceDropped();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT*BOARD_WIDTH; i++) {
            board[i] = Shape.Tet.NoShape;
        }
    }
    private void pieceDropped(){
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY + curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }
        removeFullLines();

        if(!isFallingFinished){
            newPiece();
        }
    }

    private void newPiece(){
        curPiece.setRandomShape();
        curX = BOARD_WIDTH/2 +1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if(!tryMove(curPiece, curX, curY)){
            curPiece.setShape(Shape.Tet.NoShape);
            timer.stop();

            var msg = String.format("Game Over, Score %d", numLinedRemoved);
            statusbar.setText(msg);

        }
    }
    
}
