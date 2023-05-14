package com.github.bitfexl;

import com.github.bitfexl._2048.*;

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

        gameVisualizer.update(gameBoard);
        System.out.println("GAME OVER! Highest tile: " + gameBoard.getHighest());
    }

    private static Direction readMove() {
        while (true) {
            System.out.print("Enter move (l, u, r, d): ");

            switch (stdin.nextLine().charAt(0)) {
                case 'l': return Direction.LEFT;
                case 'u': return Direction.UP;
                case 'r': return Direction.RIGHT;
                case 'd': return Direction.DOWN;
            }
        }
    }
}