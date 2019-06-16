package viewmodel;

import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.interpreter.interpreter.Interpreter;


public class ViewModel extends Observable implements Observer{
	private Interpreter i; // Model MS-4
	public StringProperty autoPilotText;
	public DoubleProperty throttle, rudder, alieron, elevator, flaps;
	
	
	public ViewModel(Interpreter i){
		this.i = i;
		this.autoPilotText = new SimpleStringProperty();
		this.throttle = new SimpleDoubleProperty();
		this.rudder = new SimpleDoubleProperty();
		this.alieron = new SimpleDoubleProperty();
		this.elevator = new SimpleDoubleProperty();
		this.flaps = new SimpleDoubleProperty();
		//this.i.addObserver(this);
	}
	
	public void getAutoPilotText() {
		String[] lines = this.autoPilotText.getValue().split("\n");
		for(int i = 0; i < lines.length; i++) {
			this.i.interpretLine(lines[i]);
		}
	}
	public void changeTab() {
		this.i.resetInterpreter();
	}
	public void sendCommandToInterpreter(String s) { //being Used mainly by Connect Command
		this.i.interpretLine(s);
	}
	public void throttleChanged() {
		this.i.interpretLine("throttle = " + this.throttle.get());

	}
	public void rudderChanged() {
		this.i.interpretLine("rudder = " + this.rudder.get());
	}
	public void aileronChanged() {
		this.i.interpretLine("aileron = " + this.alieron.get());
	}
	public void elevatorChanged() {
		this.i.interpretLine("elevator = " + this.elevator.get());
	}
	public void flapsChanged() {
		System.out.println("flaps = " + this.flaps.get());
		this.i.interpretLine("flaps = " + this.flaps.get());
	}
	@Override
	public void update(Observable o, Object arg) {
		
	}
}
