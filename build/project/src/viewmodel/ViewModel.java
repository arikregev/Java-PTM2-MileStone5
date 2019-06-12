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
import view.ConnectController;
import view.SimPaths;
import view.ViewController;

public class ViewModel extends Observable implements Observer{
	private Interpreter i; // Model MS-4
	public StringProperty autoPilotText;
	public DoubleProperty throttle, rudder, alieron, elevator;
	
	public ViewModel(Interpreter i){
		this.i = i;
		this.autoPilotText = new SimpleStringProperty();
		this.throttle = new SimpleDoubleProperty();
		this.rudder = new SimpleDoubleProperty();
		this.alieron = new SimpleDoubleProperty();
		this.elevator = new SimpleDoubleProperty();
		this.i.addObserver(this);
	}
	
	public void getAutoPilotText(String s) {
		String[] lines = s.split("\n");
		for(int i = 0; i < lines.length; i++) {
			try {
				this.i.interpretLine(lines[i]);
			} catch (ExecutionException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ConnectController) {
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
		}
		
		
	}
}
