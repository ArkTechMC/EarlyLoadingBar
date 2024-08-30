package com.iafenvoy.elb.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private final int PERIOD_INTERVAL = 300;
    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece;
    private Shape.Tetrominoe[] board;

    public Board(Tetris parent) {
        this.initBoard(parent);
    }

    private void initBoard(Tetris parent) {
        this.setFocusable(true);
        this.statusbar = parent.getStatusBar();
        this.addKeyListener(new TAdapter());
    }

    private int squareWidth() {
        return (int) this.getSize().getWidth() / this.BOARD_WIDTH;
    }

    private int squareHeight() {
        return (int) this.getSize().getHeight() / this.BOARD_HEIGHT;
    }

    private Shape.Tetrominoe shapeAt(int x, int y) {
        return this.board[(y * this.BOARD_WIDTH) + x];
    }

    void start() {
        this.curPiece = new Shape();
        this.board = new Shape.Tetrominoe[this.BOARD_WIDTH * this.BOARD_HEIGHT];
        this.clearBoard();
        this.newPiece();
        this.timer = new Timer(this.PERIOD_INTERVAL, new GameCycle());
        this.timer.start();
    }

    private void pause() {
        this.isPaused = !this.isPaused;
        this.statusbar.setText(this.isPaused ? "paused" : String.valueOf(this.numLinesRemoved));
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Dimension size = this.getSize();
        int boardTop = (int) size.getHeight() - this.BOARD_HEIGHT * this.squareHeight();
        for (int i = 0; i < this.BOARD_HEIGHT; i++)
            for (int j = 0; j < this.BOARD_WIDTH; j++) {
                Shape.Tetrominoe shape = this.shapeAt(j, this.BOARD_HEIGHT - i - 1);
                if (shape != Shape.Tetrominoe.NoShape)
                    this.drawSquare(g, j * this.squareWidth(), boardTop + i * this.squareHeight(), shape);
            }
        if (this.curPiece.getShape() != Shape.Tetrominoe.NoShape)
            for (int i = 0; i < 4; i++) {
                int x = this.curX + this.curPiece.x(i);
                int y = this.curY - this.curPiece.y(i);
                this.drawSquare(g, x * this.squareWidth(), boardTop + (this.BOARD_HEIGHT - y - 1) * this.squareHeight(), this.curPiece.getShape());
            }
    }

    private void dropDown() {
        int newY = this.curY;
        while (newY > 0) {
            if (!this.tryMove(this.curPiece, this.curX, newY - 1)) break;
            newY--;
        }
        this.pieceDropped();
    }

    private void oneLineDown() {
        if (!this.tryMove(this.curPiece, this.curX, this.curY - 1))
            this.pieceDropped();
    }

    private void clearBoard() {
        for (int i = 0; i < this.BOARD_HEIGHT * this.BOARD_WIDTH; i++)
            this.board[i] = Shape.Tetrominoe.NoShape;
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = this.curX + this.curPiece.x(i);
            int y = this.curY - this.curPiece.y(i);
            this.board[(y * this.BOARD_WIDTH) + x] = this.curPiece.getShape();
        }
        this.removeFullLines();
        if (!this.isFallingFinished) this.newPiece();
    }

    private void newPiece() {
        this.curPiece.setRandomShape();
        this.curX = this.BOARD_WIDTH / 2 + 1;
        this.curY = this.BOARD_HEIGHT - 1 + this.curPiece.minY();
        if (!this.tryMove(this.curPiece, this.curX, this.curY)) {
            this.curPiece.setShape(Shape.Tetrominoe.NoShape);
            this.timer.stop();
            String msg = String.format("Game over. Score: %d", this.numLinesRemoved);
            this.statusbar.setText(msg);
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= this.BOARD_WIDTH || y < 0 || y >= this.BOARD_HEIGHT)
                return false;
            if (this.shapeAt(x, y) != Shape.Tetrominoe.NoShape)
                return false;
        }
        this.curPiece = newPiece;
        this.curX = newX;
        this.curY = newY;
        this.repaint();
        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = this.BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < this.BOARD_WIDTH; j++)
                if (this.shapeAt(j, i) == Shape.Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }
            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < this.BOARD_HEIGHT - 1; k++)
                    for (int j = 0; j < this.BOARD_WIDTH; j++)
                        this.board[(k * this.BOARD_WIDTH) + j] = this.shapeAt(j, k + 1);
            }
        }
        if (numFullLines > 0) {
            this.numLinesRemoved += numFullLines;
            this.statusbar.setText(String.valueOf(this.numLinesRemoved));
            this.isFallingFinished = true;
            this.curPiece.setShape(Shape.Tetrominoe.NoShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetrominoe shape) {
        Color[] colors = {new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };
        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, this.squareWidth() - 2, this.squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + this.squareHeight() - 1, x, y);
        g.drawLine(x, y, x + this.squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + this.squareHeight() - 1, x + this.squareWidth() - 1, y + this.squareHeight() - 1);
        g.drawLine(x + this.squareWidth() - 1, y + this.squareHeight() - 1, x + this.squareWidth() - 1, y + 1);
    }

    private void doGameCycle() {
        this.update();
        this.repaint();
    }

    private void update() {
        if (this.isPaused) return;
        if (this.isFallingFinished) {
            this.isFallingFinished = false;
            this.newPiece();
        } else this.oneLineDown();
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Board.this.doGameCycle();
        }
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (Board.this.curPiece.getShape() == Shape.Tetrominoe.NoShape) return;
            int keycode = e.getKeyCode();
            // Java 12 switch expressions
            switch (keycode) {
                case KeyEvent.VK_P:
                    Board.this.pause();
                    break;
                case KeyEvent.VK_LEFT:
                    Board.this.tryMove(Board.this.curPiece, Board.this.curX - 1, Board.this.curY);
                    break;
                case KeyEvent.VK_RIGHT:
                    Board.this.tryMove(Board.this.curPiece, Board.this.curX + 1, Board.this.curY);
                    break;
                case KeyEvent.VK_DOWN:
                    Board.this.tryMove(Board.this.curPiece.rotateRight(), Board.this.curX, Board.this.curY);
                    break;
                case KeyEvent.VK_UP:
                    Board.this.tryMove(Board.this.curPiece.rotateLeft(), Board.this.curX, Board.this.curY);
                    break;
                case KeyEvent.VK_SPACE:
                    Board.this.dropDown();
                    break;
                case KeyEvent.VK_D:
                    Board.this.oneLineDown();
                    break;
            }
        }
    }
}