package com.gauri.Connect4game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
       FXMLLoader loader=new FXMLLoader(getClass().getResource("Game.fxml"));
       GridPane rootgrid =loader.load();
       controller=loader.getController();
       controller.createPlayGround();
        MenuBar menu=CreateMenu();
        Pane menuPane= (Pane) rootgrid.getChildren().get(0);
        menuPane.getChildren().add(menu);
        menu.prefWidthProperty().bind(primaryStage.widthProperty());
       Scene scene=new Scene(rootgrid);
        primaryStage.setTitle("Connect 4");
        primaryStage.setScene( scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
public MenuBar CreateMenu(){
        //File Menu
       Menu fileMenu= new Menu("File");
    MenuItem newGame=new MenuItem("New Game");
    newGame.setOnAction(actionEvent -> controller.resetGame());
    MenuItem resetGame=new MenuItem("Reset game");
    resetGame.setOnAction(actionEvent -> controller.resetGame());
    SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
    MenuItem exitGame=new MenuItem("Exit");
    exitGame.setOnAction(actionEvent -> exitGame());
    fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);


    //help menu
    Menu helpMenu=new Menu("Help");
    MenuItem aboutGame=new MenuItem("About Connect4 game");
    aboutGame.setOnAction(actionEvent -> aboutConnect4());
    SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
    MenuItem aboutMe=new MenuItem("About Me");
    aboutMe.setOnAction(actionEvent -> aboutMe());
    helpMenu.getItems().addAll(aboutGame,separatorMenuItem1,aboutMe);

    // MenuBar
    MenuBar menuBar=new MenuBar();
    menuBar.getMenus().addAll(fileMenu,helpMenu);
    return menuBar;

}

    private void aboutMe() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Gauri Dhadhiya");
        alert.setContentText("I Love to play around with Code and create Games." +
                "Connect 4 is one of them." +
                "in free time I like to spend time with nears and dears.");
        alert.show();
    }

    private void aboutConnect4() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect4");
        alert.setHeaderText("How To Play");
        alert.setContentText("Connect4 is a two-player connection game in which " +
                "Players first choose a color and then take turns dropping color discs" +
                "from the top into a seven-column,six-rows vertically suspended grid." +
                "The pieces fall straight down,occupying the next available spaces within the column." +
                "The objective of the game is to be the first to form a horizontal,vertical" +
                "or diagonal line of four of one's own disc.connect four is a solved game." +
                "The first player can always win by playing the right move. ");
        alert.show();

    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

