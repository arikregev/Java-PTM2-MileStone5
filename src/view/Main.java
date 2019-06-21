package view;
	
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.interpreter.interpreter.Interpreter;
import model.pathsolver.PathSolver;
import model.pathsolver.ServerPathSolver;
import model.pathsolver.StubPathSolver;
import viewmodel.ViewModel;


public class Main extends Application {
	Thread t = null;
	@Override
	public void start(Stage primaryStage) throws IOException {
		Interpreter i = new Interpreter(new SimPaths().getPaths());
		PathSolver ps = new ServerPathSolver("127.0.0.1", 3373);
		ViewModel vm = new ViewModel(i, ps);
		FXMLLoader fxml = new FXMLLoader();
		BorderPane root = fxml.load(getClass().getResource("View.fxml").openStream());
		t = new Thread(()->{ i.run(); });
		t.setDaemon(true);
		t.start();
		ViewController vc = fxml.getController();
		vc.setViewModel(vm);
		Scene scene = new Scene(root,935,340);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		//Integer[] r = {1,2,3,4,5};
		//List<Integer> rr = Arrays.asList(r);
		
		//System.out.println(Arrays.toString(r).replaceAll("\\[|\\]|\\s", ""));
		//System.out.println(rr.size());
		//System.out.println(rr.contains(1));
		
		
		
		launch(args);
	}
}
