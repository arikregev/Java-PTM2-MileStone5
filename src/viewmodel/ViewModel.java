package viewmodel;

import java.util.Observable;
import java.util.Observer;

import model.interpreter.interpreter.Interpreter;
import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.ExecutionException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


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
		this.i.addObserver(this);
	}
	
	public void getAutoPilotText() {
		String[] lines = this.autoPilotText.getValue().split("\n");
		for(int i = 0; i < lines.length; i++) {
			try {
				System.out.println(lines[i]);
				this.i.interpretLine(lines[i]);
			} catch (ExecutionException | ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Error sending Command: " + lines[i] + " -- " + e.getMessage());
			}
			
		}
	}
	public void sendCommandToInterpreter(String s) { //being Used mainly by Connect Command
		try {
			this.i.interpretLine(s);
		} catch (ExecutionException | ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Error sending Command: " + s);
		}
	}
	public void throttleChanged() {
		try {
			this.i.interpretLine("throttle = " + this.throttle.get());
		} catch (ExecutionException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void rudderChanged() {
		try {
			this.i.interpretLine("rudder = " + this.rudder.get());
		} catch (ExecutionException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void aileronChanged() {
		try {
			this.i.interpretLine("aileron = " + this.alieron.get());
		} catch (ExecutionException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void elevatorChanged() {
		try {
			this.i.interpretLine("elevator = " + this.elevator.get());
		} catch (ExecutionException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void flapsChanged() {
		try {
			System.out.println("flaps = " + this.flaps.get());
			this.i.interpretLine("flaps = " + this.flaps.get());
			
		} catch (ExecutionException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		
		
		/*if(o instanceof ConnectController) {
			ConnectController connect = (ConnectController)o;
			try {
				i.interpretLine("openDataServer " + String.valueOf(connect.getListenPort()) + "10");
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				i.interpretLine("connect " + connect.getIp() + " " + String.valueOf(connect.getTelnetPort()));
			} catch (ExecutionException | ParseException e) {
				e.printStackTrace();
			}	
		}*/
		
		
	}
}
