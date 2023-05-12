package com.github.bitfexl;

import com.github.bitfexl._2048.GameBoard;
import com.github.bitfexl._2048.GameBoardImpl;
import com.github.bitfexl._2048.GameVisualizer;
import com.github.bitfexl._2048.TerminalVisualizer;

import java.util.Scanner;

public class Main {
    private static final Scanner stdin = new Scanner(System.in);

    public static void main(String[] args) {
        GameVisualizer gameVisualizer = new TerminalVisualizer(System.out);
        GameBoard gameBoard = new GameBoardImpl(4);

        do {
            gameVisualizer.update(gameBoard);
            gameBoard.move(readMove());
            System.out.println();
        } while (!gameBoard.isGameOver());

        System.out.println("GAME OVER! Highest tile: " + gameBoard.getHighest());
    }

    private static GameBoard.Direction readMove() {
        while (true) {
            System.out.print("Enter move (l, u, r, d): ");

            switch (stdin.nextLine().charAt(0)) {
                case 'l': return GameBoard.Direction.LEFT;
                case 'u': return GameBoard.Direction.UP;
                case 'r': return GameBoard.Direction.RIGHT;
                case 'd': return GameBoard.Direction.DOWN;
            }
        }
    }
}