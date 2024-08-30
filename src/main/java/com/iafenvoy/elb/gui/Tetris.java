package com.iafenvoy.elb.gui;

import javax.swing.*;
import java.awt.*;

/*
Java Tetris game clone

Author: Jan Bodnar
Website: https://zetcode.com
 */
public class Tetris extends JFrame {
    private JLabel statusbar;

    public Tetris() {
        this.initUI();
    }

    private void initUI() {
        this.statusbar = new JLabel(" 0");
        this.statusbar.setBackground(Color.LIGHT_GRAY);
        this.add(this.statusbar, BorderLayout.SOUTH);

        Board board = new Board(this);
        this.add(board);
        board.start();

        this.setTitle("Tetris");
        this.setSize(200, 400);
        this.setForeground(Color.WHITE);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    JLabel getStatusBar() {
        return this.statusbar;
    }
}