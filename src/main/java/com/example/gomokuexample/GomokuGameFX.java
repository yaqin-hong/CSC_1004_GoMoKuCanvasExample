package com.example.gomokuexample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GomokuGameFX extends Application {
    private static final int CELL_SIZE = 40;
    private static final int BOARD_SIZE = 15;
    private GomokuGame game;
    private static final int BOARD_LENGTH = CELL_SIZE * BOARD_SIZE;

    @Override
    public void start(Stage primaryStage) {
        game = new GomokuGame(BOARD_SIZE);
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE);
        root.getChildren().add(canvas);

        drawBoard(canvas.getGraphicsContext2D());

        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / CELL_SIZE);
            int y = (int) (e.getY() / CELL_SIZE);
            // System.out.printf("%f,%f,%d,%d,\r\n",e.getX(),e.getY(),x,y);
            if (game.move(x, y)) {
                drawBoard(canvas.getGraphicsContext2D());
                if (game.isGameOver()) {
                    System.out.println("Game over! The winner is player " + game.getWinner() + "!");
                }
            } else {
                System.out.println("Invalid move!");
            }
        });

        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setMinWidth(150);

        VBox root_first = new VBox();
        Button btns = new Button("start a new game");
        Button btne1 = new Button("exit");
        root_first.setSpacing(10);
        root_first.getChildren().addAll(btns,btne1);

        VBox rootv = new VBox();
        Button btnr = new Button("restart");
        Button btne2 = new Button("exit");
        rootv.setSpacing(50);
        rootv.getChildren().addAll(btnr,btne2);
        hbox.getChildren().addAll(rootv,canvas);
        root.getChildren().add(hbox);
        Scene scene = new Scene(root, BOARD_LENGTH + 150, BOARD_LENGTH);
        Scene first = new Scene(root_first, 500, 200);

        btns.setOnAction(e -> {
            primaryStage.setScene(scene);
        });
        btne2.setOnAction(e -> {
            primaryStage.setScene(first);
        });
        btne1.setOnAction(e -> {
            System.exit(0);
        });
        btnr.setOnAction(e -> {
            game = new GomokuGame(BOARD_SIZE);
            drawBoard(canvas.getGraphicsContext2D());
        });

        primaryStage.setTitle("Gomoku Game");
        primaryStage.setScene(first);
        primaryStage.show();
    }

    private void drawBoard(GraphicsContext gc) {
        gc.clearRect(0, 0, CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < BOARD_SIZE; i++) {
            gc.strokeLine(i * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2, i * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE * BOARD_SIZE - CELL_SIZE / 2);
            gc.strokeLine(CELL_SIZE / 2, i * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE * BOARD_SIZE - CELL_SIZE / 2, i * CELL_SIZE + CELL_SIZE / 2);
        }
        int[][] board = game.getBoard();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                double x = (i + 0.1) * CELL_SIZE;
                double y = (j + 0.1) * CELL_SIZE;
                double w = CELL_SIZE * 0.8;
                double h = CELL_SIZE * 0.8;
                if (board[i][j] == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x, y, w, h);
                    gc.strokeOval(x, y, w, h);
                } else if (board[i][j] == 2) {
                    gc.setFill(Color.WHITE);
                    gc.fillOval(x, y, w, h);
                    gc.strokeOval(x, y, w, h);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}