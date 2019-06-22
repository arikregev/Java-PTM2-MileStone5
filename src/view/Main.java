package view;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.interpreter.interpreter.Interpreter;
import viewmodel.ServerPathSolverFactory;
import viewmodel.ViewModel;


public class Main extends Application {
	Thread t = null;
	@Override
	public void start(Stage primaryStage) throws IOException {
		Interpreter i = new Interpreter(new SimPaths().getPaths());
		ServerPathSolverFactory ps = new ServerPathSolverFactory();
		ViewModel vm = new ViewModel(i, ps);
		ps.setViewModel(vm);
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
		launch(args);
	}
}
