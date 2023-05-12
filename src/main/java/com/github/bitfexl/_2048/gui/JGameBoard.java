package com.github.bitfexl._2048.gui;

import com.github.bitfexl._2048.GameBoard;
import com.github.bitfexl._2048.GameVisualizer;

import javax.swing.*;
import java.awt.*;

public class JGameBoard extends JPanel implements GameVisualizer {
    private GameBoard gameBoard;

    @Override
    public void paintComponent(Graphics g) {
        if (gameBoard == null) {
            return;
        }

        final Graphics2D g2d = (Graphics2D) g;
        final int w = getWidth();
        final int h = getHeight();
        final int gw = gameBoard.getWidth();
        final int gh = gameBoard.getHeight();
        final int fw = w / gw;
        final int fh = h / gh;

        g2d.setColor(new Color(0xdfdfdf));
        g2d.fillRect(0, 0, w, h);

        g2d.setFont(new Font("monospaced", Font.BOLD, 25));

        g2d.setColor(new Color(0x6e6e6e));
        g2d.setStroke(new BasicStroke(4));

        for (int x = 0; x < gw; x++) {
            for (int y = 0; y < gh; y++) {
                g2d.drawRect(x * fw, y * fh, fw, fh);

                int value = gameBoard.get(x, y);
                if (value != 0) {
                    g2d.drawString(String.valueOf(value), (int) ((x + 0.4) * fw), (int) ((y + 0.6) * fh));
                }
            }
        }
    }

    @Override
    public void update(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        repaint();
    }
}
