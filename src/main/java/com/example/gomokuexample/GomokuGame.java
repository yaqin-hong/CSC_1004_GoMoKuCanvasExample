package com.example.gomokuexample;

import java.util.Scanner;

class GomokuGame {
    private int[][] board;          // 0: empty, 1: player1's stone, 2: player2's stone
    private int currentPlayer;      // 1: player1, 2: player2
    private boolean gameOver;       // true: game over, false: game not over
    private int winner;             // 0: no winner, 1: player 1 wins, 2: player 2 wins

    private int boardSize;              // size of the board

    private Scanner scanner;        // scanner for human input

    private String[] userName;      // 1: player1, 2: player2

    public GomokuGame(int boardSize) {
        if (boardSize < 5 || boardSize > 20) {
            throw new IllegalArgumentException("Board size should be between 5 and 20.");
        }
        this.boardSize = boardSize;
        board = new int[boardSize][boardSize];            // init to be all zeros
        userName = new String[3];
        currentPlayer = 1;
        gameOver = false;
        winner = 0;
        scanner = new Scanner(System.in);
    }

    public GomokuGame() {
        this(15);
    }

    public boolean checkWin(int x, int y){
        // Assume that the current player has just placed a piece at (x, y), and the cell is already updated
        // For directions: self-cartesian product of {0, 1, -1} except for (0, 0)
        int[][] directions = {{0, 1}, {0, -1},                // horizontal
                              {1, 0}, {-1, 0},                // vertical
                              {1, 1}, {-1, -1},               // diagonal
                              {1, -1}, {-1, 1}};              // anti-diagonal
        for (int[] direction : directions) {
            int count = 1;
            int dx = direction[0];
            int dy = direction[1];
            for (int i = 1; i < 5; i++) {
                int newX = x + i * dx;
                int newY = y + i * dy;
                if (!isValidPosition(newX, newY)) {
                    break;
                }
                if (board[newX][newY] == board[x][y]) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    public boolean move(int x, int y) {
        // place a piece at (x, y) for the current player, and then switch to the other player
        if (gameOver) {
            return false;
        }

        if (!isValidPosition(x, y)) {
            return false;
        }

        if (board[x][y] != 0) {
            return false;
        }
        
        board[x][y] = currentPlayer;
        if (checkWin(x, y)) {
            gameOver = true;
            winner = currentPlayer;
        }
        currentPlayer = currentPlayer == 1 ? 2 : 1;      // switch player
        return true;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWinner() {
        return winner;
    }

    public int[][] getBoard() {
        return board;
    }

//    public void setName(int id, String name) {
//        userName[id] = name;
//    }
//
//    public String getName(int id) {
//        return userName[id];
//    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void render() {                      // render the board, console version now
        System.out.println();
        // print the separation line
        System.out.println("---".repeat(boardSize+1));       // 1 + boardSize, 1 for the first column (preserved for row num)
        // print the column number, 1-start
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            String prefix = " ";
            String suffix = (i < 9 | i == boardSize - 1) ? " " : "";
            sb.append(prefix).append(i + 1).append(suffix);
        }
        System.out.println(sb.toString());
        for (int i = 0; i < boardSize; i++) {
            // print the row number
            int rowNum = i + 1;
            System.out.print(rowNum + (rowNum < 10 ? "  ":" "));
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    System.out.print(" + ");    // empty
                } else if (board[i][j] == 1) {
                    System.out.print(" X ");    // player1
                } else {
                    System.out.print(" O ");    // player2
                }
            }
            System.out.println();
        }
    }

    public int[] getHumanInput() { // get the human input, console version now
        int[] input = new int[2];
        System.out.println();   // print a new line for better appearance
        System.out.println("Player " + currentPlayer + "'s turn.");
        System.out.println("Please input the row number and column number, separated by space:");
        input[0] = scanner.nextInt();
        input[1] = scanner.nextInt();
        // `scanner.close()` will also close `System.in`, so don't close it here
        return input;
    }

    public void play() {
        while (!gameOver) {
            render();
            int[] input = getHumanInput();
            boolean moveSuccess = move(input[0]-1, input[1]-1);
            if (!moveSuccess) {
                System.out.println("Invalid move! Please try again.");
            }
        }
        render();
        System.out.println("Game over! The winner is player " + winner + "!");
        scanner.close();
    }
}