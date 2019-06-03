/**
 * Sample Skeleton for 'View.fxml' Controller Class
 */

package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import interpreter.Interpreter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewController {
	Stage connectWindow = null;
	File csv = null;
	File txt = null;
	int[][] map;
	private Interpreter i;
	
	//-----------------------FXML Objects-----------------------
	@FXML
	Button connect;
	@FXML
	Button textfile;
	@FXML
	RadioButton manual;
	@FXML
	RadioButton autopilot;
	@FXML
	Label statlabel;
	@FXML
	Button executecommands;
	@FXML
	TextArea txtcommands;
	@FXML
	Slider throttle;
	@FXML
	Slider rudder;
	@FXML
	Circle joystick;
	@FXML
	Circle joystickBorder;
	
	//-----------------------Repeated Objects-----------------------
	String MCL = "Manual Controls locked! - To manualy control the aircraft you need to press the Manual controls button first";


	public ViewController() {
		this.map = new int[4][7];
		
	}

	public void joystickOnMouseDrag(MouseEvent event) {
		if(this.manual.isSelected()) {
			if (event.getX() < 100 && event.getX() > -100)
				if (event.getY() < 100 && event.getY() > -100) {
					joystick.setCenterX(event.getX());
					joystick.setCenterY(event.getY());
					statlabel.setText("(Elevator = " + event.getX() + " Alieron = " + event.getY() + ")");
					//statlabel.setText("(" + joystick.getCenterX() + "," + joystick.getCenterY() + ")");
				}
		} else this.statlabel.setText(MCL);
	}

	public void joystickOnMouseRelease(MouseEvent event) {
		System.out.println("Joystick released!!!");
		joystick.setCenterX(0);
		joystick.setCenterY(0);
	}

	public void connect() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Connect.fxml"));
		AnchorPane newWindow = (AnchorPane) loader.load();
		ConnectController controller = loader.getController();
		controller.setMainWindowandInterpreter(this, i);
		this.connectWindow = new Stage();
		this.connectWindow.initModality(Modality.WINDOW_MODAL);
		this.connectWindow.initOwner(connect.getScene().getWindow());
		Scene scene = new Scene(newWindow);
		this.connectWindow.setScene(scene);
		this.connectWindow.show();
	}
	public void closeConnectWindow() {
		if(this.connectWindow != null) {
			this.connectWindow.close();
			this.connectWindow = null;
			this.statlabel.setText("Connection successfull");
		}
	}

	public void loadData() {
		this.csv = this.fileLoader();
		if (this.csv == null) {
			this.statlabel.setText("Error with CSV file, Try Again");
		} else
			this.statlabel.setText("CSV File updaoded Successfuly");
	}

	public void calcData() {
		if (this.csv != null) {

		} else {
			/* Throw Exception */
		}
	}

	public void loadTextFile() throws IOException {
		this.txt = this.fileLoader();
		if (this.txt != null) {
			BufferedReader br = new BufferedReader(new FileReader(this.txt));
			StringBuilder sb = new StringBuilder();
			String st;
			while ((st = br.readLine()) != null) {
				sb.append(st + "\n");
			}
			this.txtcommands.setText(sb.toString());
			br.close();
			this.statlabel.setText("File Loaded Successfuly");
		} else
			this.statlabel.setText("Error with text file, Try Again");
	}

	public File fileLoader() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Select File:");
		fc.setInitialDirectory(new File("/"));
		File chosen = fc.showOpenDialog(null);
		if (chosen == null)
			return null;
		return chosen;
	}

	public void executeCommandsPressed() {

	}

	public void ManualIsPressed(){
		if(this.autopilot.isSelected()) {
			this.autopilot.setSelected(false);
		}
		this.manual.setSelected(true);
		this.statlabel.setText("Manual Controls mode - Initiated.");
	}
	public void AutoPilotIsPressed(){
		if(this.manual.isSelected()) {
			this.manual.setSelected(false);
		}
		this.autopilot.setSelected(true);
		this.statlabel.setText("AutoPilot mode - Initiated.");

	}
	public void changeThrottleOnRelease(MouseEvent event) {
		if(this.manual.isSelected()) {
			System.out.println("Throttle is set to = " + this.throttle.getValue());
		}else this.statlabel.setText(MCL);
	}
	public void throttleValueDragged(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.statlabel.setText("Throttle = " + this.throttle.getValue());
		} else this.statlabel.setText(MCL);
	}
	public void rudderValueDragged(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.statlabel.setText("Rudder = " + this.rudder.getValue());
		} else this.statlabel.setText(MCL);
	}
	public void changeRudderOnRelease(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.statlabel.setText("Rudder is set to = " + this.rudder.getValue());
		} else this.statlabel.setText(MCL);
	}
	public void resetThrottle() {
		this.throttle.setValue(0.0);
		this.statlabel.setText("Throttle has been set back to 0");
		
	}
	public void resetRudder() {
		this.rudder.setValue(0.0);
		this.statlabel.setText("Rudder has been set back to 0");
		
	}
}
