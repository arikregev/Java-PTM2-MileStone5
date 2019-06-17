/**
 * Sample Skeleton for 'View.fxml' Controller Class
 */

package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Pair;
import viewmodel.ViewModel;

public class ViewController implements Observer{
	Stage connectWindow = null;
	ViewModel vm = null;
	File csv = null;
	File txt = null;
	//int[][] map;
	DoubleProperty alieronVal, elevatorVal, flapsval;
	//-----------------------FXML Objects-----------------------
	@FXML
	Button connect, textfile;
	@FXML
	RadioButton manual, autopilot;
	@FXML
	Label statlabel, airspeed, altitude;
	@FXML
	Button executecommands;
	@FXML
	TextArea txtcommands, history;
	@FXML
	Slider throttle, rudder, flaps;
	@FXML
	Circle joystick, joystickBorder;
	@FXML
	GuideMap guidemap;
	//-----------------------Repeated Objects-----------------------
	String MCL = "Manual Controls locked! - To manualy control the aircraft you need to press the Manual Controls button first";
	String APL = "Cannot Execute! = To use the AutoPilot option you need to press the AutoPilot mode Button first";

	public ViewController() {
		this.guidemap = new GuideMap();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ViewModel) {
			this.history.appendText(arg + "\n");
		}
		
	}
	@SuppressWarnings("static-access")
	public void setViewModel(ViewModel vm) {
		this.vm = vm;
		this.alieronVal = new SimpleDoubleProperty();
		this.elevatorVal = new SimpleDoubleProperty();
		this.flapsval = new SimpleDoubleProperty();
		this.vm.throttle.bind(this.throttle.valueProperty());
		this.vm.rudder.bind(this.rudder.valueProperty());
		this.vm.alieron.bind(this.alieronVal);
		this.vm.elevator.bind(this.elevatorVal);
		this.vm.flaps.bind(this.flapsval);
		this.vm.addObserver(this);
		this.airspeed.textProperty().bind(this.vm.stringPropertiesMap.get(this.vm.AIRSPEED));
		this.altitude.textProperty().bind(this.vm.stringPropertiesMap.get(this.vm.ALT));
		
	}
	
	public void joystickOnMouseDrag(MouseEvent event) {
		if(this.manual.isSelected()) {
			if (event.getX() <= 100 && event.getX() >= -100)
				if (event.getY() <= 100 && event.getY() >= -100) {
					joystick.setCenterX(event.getX());
					joystick.setCenterY(event.getY());
					statlabel.setText("(Alieron = " + event.getX()/100 + " Elevator = " + event.getY()/100 + ")");
					this.elevatorVal.set(event.getY()/-100);
					this.alieronVal.set(event.getX()/100);
					this.vm.elevatorChanged();
					this.vm.aileronChanged();
				}
		} else this.statlabel.setText(MCL);
	}

	public void joystickOnMouseRelease(MouseEvent event) {
		joystick.setCenterX(0);
		joystick.setCenterY(0);	
	}

	public void connect() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Connect.fxml"));
		AnchorPane newWindow = (AnchorPane) loader.load();
		ConnectController controller = loader.getController();
		controller.setMainWindow(this);
		controller.setViewModel(this.vm);
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
			this.statlabel.setText("Connection sent successfully to Interpreter");
		}
	}

	public void loadData() {
		this.csv = this.fileLoader();
		if (this.csv != null) {
			this.statlabel.setText("CSV File updaoded Successfuly");
			this.guidemap.generateMap(csv);
		} else
			this.statlabel.setText("Error with CSV file, Try Again");
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
		fc.setInitialDirectory(new File("./resources/"));
		File chosen = fc.showOpenDialog(null);
		if (chosen == null)
			return null;
		return chosen;
	}

	public void executeCommandsPressed() {
		if(this.autopilot.isSelected()) {
			String line = this.txtcommands.getText();
			this.vm.autoPilotText.setValue(line);
			this.vm.getAutoPilotText();
		} else this.statlabel.setText(APL);
	}

	public void ManualIsPressed(){
		/*if(this.manual.isSelected())
			return;*/
		if(this.autopilot.isSelected()) {
			this.autopilot.setSelected(false);
			this.vm.changeTab();
		}
		this.manual.setSelected(true);
		this.statlabel.setText("Manual Controls mode - Initiated.");
	}
	public void AutoPilotIsPressed(){
		if(this.manual.isSelected()) {
			this.manual.setSelected(false);
			this.vm.changeTab();
		}
		this.autopilot.setSelected(true);
		this.statlabel.setText("AutoPilot mode - Initiated.");
		

	}
	public void changeThrottleOnRelease(MouseEvent event) {
		if(this.manual.isSelected()) {
			System.out.println("Throttle is set to = " + this.throttle.getValue());
		}else {
			this.statlabel.setText(MCL);
		}
	}
	public void throttleValueDragged(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.vm.throttleChanged();
		} else {
			this.throttle.setValue(0.0);
		}
	}
	public void rudderValueDragged(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.vm.rudderChanged();
		}else {
			this.rudder.setValue(0.0);
		}
	}
	public void changeRudderOnRelease(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.statlabel.setText("Rudder is set to = " + this.rudder.getValue());
		} else {
			this.statlabel.setText(MCL);
		}
	}
	public void resetThrottle() {
		if(this.manual.isSelected()) {
			this.throttle.setValue(0.0);
			this.vm.throttleChanged();
			this.statlabel.setText("Throttle has been set back to 0");
		} else this.statlabel.setText(MCL);
	}
	public void resetRudder() {
		if(this.manual.isSelected()) {
			this.rudder.setValue(0.0);
			this.vm.rudderChanged();
			this.statlabel.setText("Rudder has been set back to 0");
		} else this.statlabel.setText(MCL);
	}
	public void flapsValueDragged(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.flapsval.setValue(this.flaps.getValue());
			this.vm.flapsChanged();
		} else {
			this.flaps.setValue(0.0);
		}
	}
	public void changeFlapsOnRelease(MouseEvent event) {
		if(this.manual.isSelected()) {
			this.statlabel.setText("Flaps is set to = " + this.flaps.getValue());
		} else {
			this.statlabel.setText(MCL);
		}
	}
	public void markDestOnMap(MouseEvent e) {
		this.guidemap.markDest(e.getX(), e.getY());
	}

}
