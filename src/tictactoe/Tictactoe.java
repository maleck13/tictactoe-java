/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.util.Optional;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author craigbrookes
 */
public class Tictactoe extends Application {

    Scene board = null;
    String playerTurn = "X";
    int occupied = 0;
    boolean winnerFound = false;
    Pane root;

    public void setPlayerTurn(String turn) {
        playerTurn = turn;
    }

    public void checkGameState() {
        ObservableList<Node> squares = root.getChildren();
        int[] seq = {0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 3, 6, 1, 4, 7, 2, 5, 8, 0, 4, 8, 2, 4, 6};
        for (int i = 0; i < seq.length; i += 3) {
            int intXMatches = 0;
            int intYMatches = 0;
            for (int k = 0; k < 3; k++) {
                Square b = (Square) squares.get(seq[i + k]);

                if (b.isOccupied()) {
                    if (b.getText().equals("X")) {
                        System.out.println("square " + b.getId() + " is occuppied by " + b.getText() + " seq " + String.valueOf(seq[i + k]));
                        intXMatches++;
                    } else {
                        intYMatches++;
                    }

                }
                if (intXMatches == 3) {
                    winnerFound = true;
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(GAME__OVER);
                    alert.setHeaderText("X has won the game");
                    alert.setContentText("X has won the game. Play again?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        restartGame();
                    }
                    break;
                } else if (intYMatches == 3) {
                    winnerFound = true;
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(GAME__OVER);
                    alert.setHeaderText("Y has won the game");
                    alert.setContentText("Y has won the game. Play again?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        restartGame();
                    }
                    break;
                }

            }

        }
        if (!winnerFound && 9 == occupied) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(GAME__OVER);
            alert.setHeaderText("The Game was a draw");
            alert.setContentText("The Game was a draw. Play again?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                restartGame();
            }

        }
    }
    private static final String GAME__OVER = "Game Over";

    public void restartGame() {
        winnerFound = false;
        playerTurn = "X";
        occupied = 0;
        ObservableList<Square> sqs = (ObservableList<Square>) (ObservableList<?>) root.getChildren();
        for (Square s : sqs) {
            s.setOccupied(false);
            s.setText("");
        }
    }

    private void handleTurn() {
        if ("X".equals(playerTurn)) {
            playerTurn = "Y";
        } else {
            playerTurn = "X";
        }
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        int id = 0;
        for (int i = 0; i < 3; i++) {

            for (int k = 0; k < 3; k++) {

                Square btn = new Square(playerTurn);
                btn.setText("");
                btn.setLayoutY(i * 100);
                btn.setLayoutX(k * 100);

                btn.setId(String.valueOf(id));
                btn.setMinHeight(100);
                btn.setMinWidth(100);

                btn.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {

                        EventHandler<ActionEvent> ch = new HandleClick(playerTurn);
                        ch.handle(event);
                        occupied++;
                        handleTurn();
                        checkGameState();
                    }
                });

                root.getChildren().add(btn);
                id++;
            }

        }

        board = new Scene(root, 300, 300);

        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(board);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

class HandleClick implements EventHandler<ActionEvent> {

    String piece = null;

    public HandleClick(String piece) {
        this.piece = piece;
    }

    @Override
    public void handle(ActionEvent event) {
        Square eb = (Square) event.getTarget();
        eb.setText(piece);
        eb.setOccupied(true);
        System.out.println(eb.getId() + " has piece " + piece);

    }

}

class Square extends Button {

    public Square(String text) {
        super(text);
    }

    private boolean occupied = false;

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

}
