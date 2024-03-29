package Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

import MainMenu.MainMenuGUI;
import Settings.Player;
import Settings.Settings;
import Settings.SettingsGUI;
import Status.GameGUIStatus;
import Status.GridStatus;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * <h1>Game GUI class</h1> The program is used to implement GUI for the grid.
 *
 *
 */
public class GameGUI extends Application {

	public Grid grid;
	private int noOfplayers;
	private String size;
	public Player[] players;
	public SettingsGUI setting;
	public int count;
	int m = 0, n = 0;

	/**
	 * This constructor is used to initialize the grid of the game.
	 * 
	 * @param c
	 *            size of the grid.
	 * @param _n
	 *            number of players.
	 * @param _setting
	 *            setting class of the game.
	 */
	public GameGUI(String c, String _n, SettingsGUI _setting) {

		size = c;
		this.setting = _setting;
		noOfplayers = Integer.parseInt(_n);
		if (_setting != null) {
			players = setting.setting.players;
		} else {
			players = new Player[noOfplayers];
			Color[] clr = new Color[8];
			clr[0] = Color.RED;
			clr[1] = Color.GREEN;
			clr[2] = Color.BLUE;
			clr[3] = Color.GOLD;
			clr[4] = Color.VIOLET;
			clr[5] = Color.SILVER;
			clr[6] = Color.PINK;
			clr[7] = Color.FIREBRICK;
			for (int i = 0; i < noOfplayers; i++) {
				players[i] = new Player(clr[i]);
			}
		}
		count = 0;
		if (size.equals("9X6")) {
			m = 6;
			n = 9;
		} else {
			m = 10;
			n = 15;
		}
		grid = new Grid(m, n, players, count);

	}

	/**
	 * This constructor is used to initialize the grid of the game for undo and
	 * resume functionality.
	 * 
	 * @param _size
	 *            size of the grid.
	 * @param _n
	 *            number of players.
	 * @param plist
	 *            array containing players.
	 * @param turn
	 *            integer having the turn stored as a number.
	 * @param _grid
	 *            Object of the class of gridStatus which stores status of grid
	 *            at any point of time.
	 */
	public GameGUI(String _size, int _n, Player[] plist, int turn, GridStatus _grid) {

		size = _size;
		noOfplayers = _n;
		players = plist;
		count = turn;
		grid = new Grid(_grid, players);
		if (size.equals("9X6")) {
			m = 6;
			n = 9;
		} else {
			m = 10;
			n = 15;
		}
	}

	/**
	 * This method is used to get the number of players playing the game.
	 * 
	 * @return noOfplayers players playing the game.
	 * 
	 */
	public int getNoOfplayers() {
		return noOfplayers;
	}

	/**
	 * This method is used to assign number of players.
	 * 
	 * @param noOfplayers
	 *            number of players to include.
	 */
	public void setNoOfplayers(int noOfplayers) {
		this.noOfplayers = noOfplayers;
	}

	/**
	 * This method is used to get the size of the grid.
	 * 
	 * @return size size of the game.
	 * 
	 */
	public String getSize() {
		return size;
	}

	/**
	 * This method is used to assign size to the grid.
	 * 
	 * @param size
	 *            size of grid.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * This method is used to launch the GUI for Game Class.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		launch(args);

	}

	/**
	 * This method is used as the application initialization method. This method
	 * is called immediately after the Application class is loaded and
	 * constructed.
	 * 
	 * @param mainStage
	 *            mainStage of GUI
	 */
	@Override
	public void start(Stage mainStage) throws Exception {

		mainStage.setTitle("Chain Reaction");
		Player[] playerCopy = new Player[players.length];
		for (int i = 0; i < playerCopy.length; i++) {
			playerCopy[i] = players[i];
		}
		HBox hbox = new HBox(5);
		Button undoBtn = new Button("UNDO");
		MenuButton dropdownMenu = new MenuButton();
		MenuItem m1 = new MenuItem("Start Again");
		MenuItem m2 = new MenuItem("Exit");
		dropdownMenu.setText("|||");
		dropdownMenu.getItems().addAll(m1, m2);
		undoBtn.setId("Undo");
		hbox.getChildren().addAll(undoBtn, dropdownMenu);
		hbox.setId("DDMenu");
		if (m == 6)
			hbox.setPadding(new Insets(10, 0, 5, 180));
		else
			hbox.setPadding(new Insets(10, 0, 5, 250));

		BorderPane root = new BorderPane();
		Scene mainScene = new Scene(root);
		mainStage.setScene(mainScene);
		mainScene.getStylesheets().add(getClass().getResource("/assets/stylesheetGame.css").toExternalForm());

		root.setTop(hbox);
		root.setCenter(grid);

		m1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Grid gNew = new Grid(m, n, playerCopy, 0);
				root.setCenter(gNew);
			}
		});
		GameGUIStatus gs = new GameGUIStatus(noOfplayers, grid.gridst.players, size, grid.gridst.grid);

		m2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainStage.close();
				try {
					gs.grid = grid.gridst.grid;
					gs.players = grid.gridst.players;
					gs.grid.winner = grid.winner;
					GameGUIStatus.serialize("GamePlay", gs);
					new MainMenuGUI().start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		mainStage.setOnCloseRequest(event -> {
			try {
				grid.gridst.grid.print();
				gs.grid = grid.gridst.grid;
				gs.players = grid.gridst.players;
				gs.grid.winner = grid.winner;
				GameGUIStatus.serialize("GamePlay", gs);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		undoBtn.setOnMouseClicked(event -> {
			try {
				GameGUIStatus gsundo = grid.ReturnUndo();
				gsundo.grid.count -= 1;
				grid = new Grid(gsundo.grid, gsundo.players);
				grid.gridst = gsundo;
				root.setCenter(grid);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		boolean check = true;
		mainStage.show();
	}

}
