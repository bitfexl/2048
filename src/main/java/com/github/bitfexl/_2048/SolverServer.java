package com.github.bitfexl._2048;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SolverServer {
    private final GameBoardImpl gameBoard = new GameBoardImpl(4);
    private final GameSolverImpl gameSolver = new GameSolverImpl();

    private final Gson gson = new Gson();

    private final HttpServer httpServer;

    private final int port;

    public SolverServer(int port) throws IOException {
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }), port), 10);
        this.httpServer.createContext("/", this::handleRequest);
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestMethod() + "  " + exchange.getRequestURI());

        if (exchange.getRequestURI().toString().equals("/solve")) {
            byte[] response;
            int responseCode = 200;

            try {
                byte[] save = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), byte[].class);
                gameBoard.load(save);
                final Direction bestMove = gameSolver.bestMove(gameBoard);
                response = bestMove.toString().getBytes(StandardCharsets.UTF_8);
            } catch (Exception ex) {
                ex.printStackTrace();
                response = ex.getMessage().getBytes(StandardCharsets.UTF_8);
                responseCode = 500;
            }

            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(responseCode, response.length);

            exchange.getResponseBody().write(response);
        } else if (exchange.getRequestURI().toString().equals("/clearCache")) {
            gameSolver.clearCache();

            exchange.sendResponseHeaders(200, -1);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public int getPort() {
        return port;
    }

    public static void main(String[] args) throws IOException {
        SolverServer server = new SolverServer(3000);
        server.start();
        new Scanner(System.in).nextLine();
        server.stop();
    }
}
