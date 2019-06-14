package view;
	
import model.interpreter.interpreter.Interpreter;

import java.io.EOFException;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import viewmodel.ViewModel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxml = new FXMLLoader();
		BorderPane root = fxml.load(getClass().getResource("View.fxml").openStream());
		Interpreter i = new Interpreter(new SimPaths().getPaths());
		ViewModel vm = new ViewModel(i);
		ViewController vc = fxml.getController();
		vc.setViewModel(vm);
		Scene scene = new Scene(root,935,340);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
