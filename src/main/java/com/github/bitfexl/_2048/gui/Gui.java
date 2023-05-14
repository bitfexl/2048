package com.github.bitfexl._2048.gui;

import com.github.bitfexl._2048.Direction;
import com.github.bitfexl._2048.GameBoard;
import com.github.bitfexl._2048.GameBoardImpl;
import com.github.bitfexl._2048.GameVisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gui extends JFrame implements KeyListener {
    private final JGameBoard jGameBoard = new JGameBoard();

    /**
     * The game board to make moves,
     * if null no moves can be made by the user.
     */
    private GameBoard gameBoard;

    public Gui() {
        addKeyListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        setTitle("2048");

        add(jGameBoard);
        jGameBoard.setPreferredSize(new Dimension(400, 400));

        pack();
        setVisible(true);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        getGameVisualizer().update(gameBoard);
    }

    public GameVisualizer getGameVisualizer() {
        return jGameBoard;
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.setGameBoard(new GameBoardImpl(4));
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameBoard == null) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            gameBoard.move(Direction.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            gameBoard.move(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            gameBoard.move(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            gameBoard.move(Direction.DOWN);
        }

        getGameVisualizer().update(gameBoard);
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
