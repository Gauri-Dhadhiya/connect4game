package com.gauri.Connect4game;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int Columns=7;
	private static  final int Rows=6;
	private static final int Circle_Diameter=80;
	private static final String disc_Color_One="#24303e";
	private static final String disc_Color_Two="#4caa8a";

	private static String Player_One="Player One";
	private static String Player_Two="Player two";

	private Boolean isPlayerOneTurn=true;
	private  final Disc[][] insertedDiscArray=new Disc[Rows][Columns];
	private boolean isAllowedToInsert=true;  //flag to avoid same color disc being inserted

	@FXML
	public TextField text1,text2;
	@FXML
	public Button setBtn;
	@FXML
	public GridPane rootGridPane;
    @FXML
    public Pane insertedDiscPane;
    @FXML
    public Label PlayerNameLabel;


public void createPlayGround(){

	Shape rectangleWithHoles=createGameStructuralGrid();
 rootGridPane.add(rectangleWithHoles,0,1);
 List<Rectangle> rectangleList=createClickableColumn();
 for(Rectangle rectangle:rectangleList){
	 rootGridPane.add(rectangle,0,1);
 }
	}

private Shape createGameStructuralGrid(){
	Shape rectangleWithHoles=new Rectangle((Columns+1)*Circle_Diameter,(Rows+1)*Circle_Diameter);
	for(int row=0;row<Rows;row++){
		for(int col=0;col<Columns;col++){
			Circle circle=new Circle();
			circle.setRadius(Circle_Diameter/2);
			circle.setCenterX(Circle_Diameter/2);
			circle.setCenterY(Circle_Diameter/2);
			circle.setSmooth(true);

			circle.setTranslateX(col*(Circle_Diameter+5)+Circle_Diameter/4);
			circle.setTranslateY(row*(Circle_Diameter+5)+Circle_Diameter/4);
			rectangleWithHoles= Shape.subtract(rectangleWithHoles,circle);
		}
	}
	rectangleWithHoles.setFill(Color.WHITE);
	return rectangleWithHoles;
}

 private List<Rectangle> createClickableColumn(){
	List<Rectangle> rectangleList=new ArrayList<>();

	for(int col=0;col<Columns;col++){

	Rectangle rectangle=new Rectangle(Circle_Diameter,(Rows+1)*Circle_Diameter);
	rectangle.setFill(Color.TRANSPARENT);
	rectangle.setTranslateX(col*(Circle_Diameter+5)+(Circle_Diameter/4));

	rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#e5ffe566")));
	rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));

	final int column=col;
	rectangle.setOnMouseClicked(mouseEvent ->{
			if(isAllowedToInsert){
				isAllowedToInsert=false;
				insertDisc(new Disc(isPlayerOneTurn),column);
			}
			});

	rectangleList.add(rectangle);
}
	return rectangleList;
}

   private  void insertDisc(Disc disc,int column){
	int row=Rows-1;
	while(row>=0){
		if(getDiscIfPresent(row ,column )==null)
			break;

		row--;
		}
	if(row<0)
		return;

   insertedDiscArray[row][column]=disc;
   insertedDiscPane.getChildren().add(disc);
   disc.setTranslateX(column*(Circle_Diameter+5)+Circle_Diameter/4);

   int currentRow=row;
	   TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.4),disc);
	   translateTransition.setToY(row*(Circle_Diameter+5)+Circle_Diameter/4);
	   translateTransition.setOnFinished(actionEvent -> {
           isAllowedToInsert=true;  //finally ,when the disc is dropped allow next player to insert disc
	   	if(gameEnded(currentRow,column)){
          gameOver();
          return;
	    }
		   isPlayerOneTurn = !isPlayerOneTurn;
		   PlayerNameLabel.setText(isPlayerOneTurn?Player_One:Player_Two);
	   });
	   translateTransition.play();
}

	private void gameOver() {
		String winner = isPlayerOneTurn ? Player_One : Player_Two;
		System.out.println("Winner is:" + winner);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect four Game");
		alert.setHeaderText("Winner is:" + winner);
		alert.setContentText("Want To play Again");

		ButtonType yesBtn = new ButtonType("Yes");
		ButtonType noBtn = new ButtonType("No,Exit");
		alert.getButtonTypes().setAll(yesBtn, noBtn);

		Platform.runLater(() -> {   //helps to resolve all the illegalStateExceptions
			Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get() == yesBtn) {
				//reset the game
				resetGame();
			} else {
				//exit the game
				Platform.exit();
				System.exit(0);
			}
		});
	}
	public void resetGame() {
	 insertedDiscPane.getChildren().clear();  //Remove all inserted disc from the pane
		for(int row=0;row<insertedDiscArray.length;row++){  //structurally make all element of insertedDiscArray to null
			for(int col=0;col<insertedDiscArray[row].length;col++){
				insertedDiscArray[row][col] = null;
			}
		}
		isPlayerOneTurn=true;  //let player one start the game
		PlayerNameLabel.setText(Player_One);

		createPlayGround();
	}

	private boolean gameEnded(int row, int column) {
	//vertical point.A small Example:Player has inserted its last disc at row=2,column=3

		//index of each element present in column[row][column]:

		List<Point2D> verticalPoints= IntStream.rangeClosed(row-3,row+3)  //range of row values =0,1,2,3,4,5
				.mapToObj(r->new Point2D(r,column))  //0,3   1,3  2,3  3,3  4,3  5,3 ->point2D x,y
				.collect(Collectors.toList());

		List<Point2D> horizontalPoints= IntStream.rangeClosed(column-3,column+3)
				.mapToObj(col->new Point2D(row,col))
				.collect(Collectors.toList());

		Point2D startPoint1=new Point2D(row-3,column+3);
		List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6)
				.mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());

		Point2D startPoint2=new Point2D(row-3,column-3);
		List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6)
				.mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());

		boolean isEnded=CheckCombinations(verticalPoints) || CheckCombinations(horizontalPoints)
				||CheckCombinations(diagonal1Points)
				 || CheckCombinations(diagonal2Points);

		return isEnded;
	}

	private boolean CheckCombinations(List<Point2D> Points) {
	int chain=0;
	for(Point2D point:Points){
		int rowIndexForArray= (int) point.getX();
		int columnIndexForArray= (int) point.getY();
		Disc disc=getDiscIfPresent(rowIndexForArray,columnIndexForArray);
		if(disc!=null && disc.isPlayerOneMove==isPlayerOneTurn) // if the last inserted disc belongs to the current player
		{
			chain++;
			if(chain==4)
				return true;
		}
		else {
			chain=0;
		}
	}
	return false;
	}

	private Disc getDiscIfPresent(int row,int column){  //to prevent ArrayIndexoutOfboundException
	if(row>=Rows || row<0 || column>=Columns || column<0) //if rows or column index is invalid
		return null;

	return insertedDiscArray[row][column];
	}

	private static class Disc extends Circle{
	  private final boolean isPlayerOneMove;
	     public Disc(boolean isPlayerOneMove){
		    this.isPlayerOneMove=isPlayerOneMove;
		      setRadius(Circle_Diameter/2);
		         setFill(isPlayerOneMove?Color.valueOf(disc_Color_One):Color.valueOf(disc_Color_Two));
		           setCenterX(Circle_Diameter/2);
		             setCenterY(Circle_Diameter/2);
	}

}
    @Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
     setBtn.setOnAction(new EventHandler<ActionEvent>() {
	     @Override
	     public void handle(ActionEvent actionEvent) {
		     Player_One=text1.getText();
		     Player_Two=text2.getText();
		     PlayerNameLabel.setText(Player_One);
	     }
     });
    }
}
