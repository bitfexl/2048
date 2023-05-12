package com.github.bitfexl._2048;

import java.io.PrintStream;

public class TerminalVisualizer implements GameVisualizer {
    private final PrintStream out;

    public TerminalVisualizer(PrintStream out) {
        this.out = out;
    }


    @Override
    public void update(GameBoard gameBoard) {
        final int w = gameBoard.getWidth();
        final int h = gameBoard.getHeight();

        final String hSep1 = "+-------".repeat(w) + "+";
        final String hSep2 = "|       ".repeat(w) + "|";

        for (int y = 0; y < h; y++) {
            out.println(hSep1);
            out.println(hSep2);

            for (int x = 0; x < w; x++) {
                out.print("|");

                int value = gameBoard.get(x, y);

                if (value == 0) {
                    out.print(" ".repeat(7));
                } else {
                    out.print(center(String.valueOf(value), 7));
                }
            }
            out.println("|");

            out.println(hSep2);
        }
        out.println(hSep1);
    }

    private String center(String s, int w) {
        int r = (w - s.length()) / 2;
        int l = w - s.length() - r;
        return " ".repeat(l) + s + " ".repeat(r);
    }
}
