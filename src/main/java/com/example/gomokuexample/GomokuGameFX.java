package com.example.gomokuexample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class GomokuGameFX extends Application {
    private static final int CELL_SIZE = 40;
    private static final int BOARD_SIZE = 20;
    private GomokuGame game;
    private String name1_str;
    private String name2_str;
    private static final int BOARD_LENGTH = CELL_SIZE * BOARD_SIZE;
    private int board_num;

    @Override
    public void start(Stage primaryStage) {
        game = new GomokuGame(BOARD_SIZE);
        StackPane root = new StackPane();
        board_num = BOARD_SIZE;
        Canvas canvas = new Canvas(CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE);
        root.getChildren().add(canvas);

        drawBoard(canvas.getGraphicsContext2D());

        Label playName = new Label("");
        Label win = new Label("");

        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / CELL_SIZE);
            int y = (int) (e.getY() / CELL_SIZE);
            if (game.move(x, y)) {
                drawBoard(canvas.getGraphicsContext2D());

                int current = game.getCurrentPlayer();

                if (2 == current) {
                    playName.setText(name2_str + " is playing.");
                } else {
                    playName.setText(name1_str + " is playing.");
                }

                if (game.isGameOver()) {
                    System.out.println("Game over ! The winner is player " + game.getWinner() + "!");

                    String winner = "Game over ! \r\n The winner is \r\n player ";
                    if (game.getWinner() == 1) {
                        winner += name1_str;
                    } else {
                        winner += name2_str;
                    }
                    win.setText(winner + " !");
                }
            } else {
                System.out.println("Invalid move!");
            }
        });

        HBox hbox = new HBox();
        hbox.setSpacing(70);
        hbox.setMinWidth(500);

        HBox user1 = new HBox();
        Label name1 = new Label("First player's name:");
        TextField t1 = new TextField("");
        user1.getChildren().addAll(name1, t1);
        HBox user2 = new HBox();
        Label name2 = new Label("Second player's name:");
        TextField t2 = new TextField("");
        user2.getChildren().addAll(name2, t2);

        VBox root_first = new VBox();
        Button btns = new Button("start a new game");
        Button btne1 = new Button("exit");
        root_first.setSpacing(10);

        Label size = new Label("");

        VBox rootv = new VBox();
        Button btnr = new Button("restart");
        Button btne2 = new Button("exit");
        rootv.setSpacing(50);
        rootv.getChildren().addAll(btnr, btne2, playName, win, size);
        hbox.getChildren().addAll(rootv, canvas);
        root.getChildren().add(hbox);
        Scene scene = new Scene(root, CELL_SIZE * board_num + 400, CELL_SIZE * board_num);
        Scene first = new Scene(root_first, 500, 200);

        Slider slBoard = new Slider(5,20,1);
        slBoard.setValue(15);
        slBoard.setShowTickLabels(true);
        slBoard.setShowTickMarks(true);
        slBoard.setSnapToTicks(true);
        slBoard.setMajorTickUnit(1);
        slBoard.setMinorTickCount(1);
        slBoard.setBlockIncrement(1);
        root_first.getChildren().addAll(btns, btne1, user1, user2, slBoard);

        btns.setOnAction(e -> {
            name1_str = t1.getText();
            name2_str = t2.getText();
            if (name1_str.length() > 0 && name2_str.length() > 0) {
                game = new GomokuGame(board_num);
                playName.setText(name1_str + " is playing.");
                size.setText("The board is: " + board_num + " * " + board_num);
                drawBoard(canvas.getGraphicsContext2D());
                primaryStage.setScene(scene);
            }
        });

        btne1.setOnAction(e -> {
            System.exit(0);
        });

        btne2.setOnAction(e -> {
            primaryStage.setScene(first);
        });

        btnr.setOnAction(e -> {
            playName.setText(name1_str + " is playing.");
            win.setText("");
            size.setText("The board is: " + board_num + " * " + board_num);
            game = new GomokuGame(BOARD_SIZE);
            drawBoard(canvas.getGraphicsContext2D());
        });

        slBoard.valueProperty().addListener((observable, oldValue, newValue) -> {
            board_num = (int) newValue.intValue();
            System.out.println("newValue = " + newValue.intValue());
            slBoard.setValue(board_num);
        });

        primaryStage.setTitle("Gomoku Game");
        primaryStage.setScene(first);
        primaryStage.show();
    }

    private void drawBoard(GraphicsContext gc) {
        gc.clearRect(0, 0, CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < board_num; i++) {
            gc.strokeLine(i * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2,
                    i * CELL_SIZE + CELL_SIZE / 2,
                    CELL_SIZE * board_num - CELL_SIZE / 2);
            gc.strokeLine(CELL_SIZE / 2, i * CELL_SIZE + CELL_SIZE / 2,
                    CELL_SIZE * board_num - CELL_SIZE / 2,
                    i * CELL_SIZE + CELL_SIZE / 2);
        }
        int[][] board = game.getBoard();
        for (int i = 0; i < board_num; i++) {
            for (int j = 0; j < board_num; j++) {
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