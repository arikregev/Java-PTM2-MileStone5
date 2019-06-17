package viewmodel;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.interpreter.interpreter.Interpreter;


public class ViewModel extends Observable implements Observer{
	private Interpreter i; // Model MS-4
	public StringProperty autoPilotText;
	public DoubleProperty throttle, rudder, alieron, elevator, flaps;
	private volatile boolean isConnected = false;
	public HashMap<String, StringProperty> stringPropertiesMap;
	public HashMap<String, DoubleProperty> doublePropertiesMap;
	public Timer t;
	public TimerTask timerTask;
	public static final String AIRSPEED = "airspeed";
	public static final String ALT = "alt";
	
	public ViewModel(Interpreter i){
		this.i = i;
		this.autoPilotText = new SimpleStringProperty();
		this.throttle = new SimpleDoubleProperty();
		this.rudder = new SimpleDoubleProperty();
		this.alieron = new SimpleDoubleProperty();
		this.elevator = new SimpleDoubleProperty();
		this.flaps = new SimpleDoubleProperty();
		this.i.addObserver(this);
		this.stringPropertiesMap = new HashMap<String, StringProperty>();
		this.doublePropertiesMap = new HashMap<String, DoubleProperty>();
		this.stringPropertiesMap.put(AIRSPEED, new SimpleStringProperty());
		this.stringPropertiesMap.put(ALT, new SimpleStringProperty());
		//add more
		this.t = new Timer(true);
		this.timerTask = new TimerTask() {

			@Override
			public void run() {
				ViewModel.this.i.interpretLine("print \"airspeed=\", airspeed");		
				ViewModel.this.i.interpretLine("print \"alt=\", alt");
			}
			
		};
	}
	
	public void startWatchedVals() {
		this.t.scheduleAtFixedRate(this.timerTask, 10000, 500);
	}
	public void stopWatchVals() {
		this.t.cancel();
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
		if (o instanceof Interpreter && arg instanceof String) {
			try {
				String printed = (String)arg;
				String[] parsed = printed.split("=");
				if (parsed.length != 2)
					return;
				String key = parsed[0].trim();
				Double d = Double.parseDouble(parsed[1].trim()); 
				if (this.stringPropertiesMap.containsKey(key))
					this.stringPropertiesMap.get(key).set(String.format("%.1f", d));
				if (this.doublePropertiesMap.containsKey(key))
					this.doublePropertiesMap.get(key).set(d);
			} 
			catch (NumberFormatException e) {
				return;
			}
		}
	}
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
		this.startWatchedVals();
	}
}
