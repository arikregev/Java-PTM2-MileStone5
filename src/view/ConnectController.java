package view;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.interpreter.interpreter.MainInterpreter;
import viewmodel.ViewModel;

public class ConnectController implements Observer{
	private String ip = null;
	private int telnetPort = 0;
	private int listenPort = 0;
	private int rateNum = 0;
	private ViewController mainWindow = null;
	private ViewModel vm = null;
	private final Pattern p = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	@FXML
	Button connect;
	@FXML
	TextField ipText, portText, listeningport, rate;
	@FXML
	Label status;


	public void execute() {
		if (ipValidityCheck(this.ipText.getText())) {
			this.ip = ipText.getText();
			try {
				this.telnetPort = Integer.parseInt(this.portText.getText());
				this.listenPort = Integer.parseInt(this.listeningport.getText());
				this.rateNum = Integer.parseInt(this.rate.getText());
			} catch (Exception e) {
				this.status.setText("Please enter a valid port");
				
				return;
			}
			if(connect(this.ip, this.telnetPort, this.listenPort, this.rateNum)) {
				this.mainWindow.closeConnectWindow();	
			}
			else status.setText("Connect Error");
		} else {
			status.setText("Please enter IP and port");
		}
	}
	public void setViewModel(ViewModel vm) {
		this.vm = vm;
	}
	private boolean connect(String ip, int telnetPort, int listenPort, int rate) {
		this.ip = ip;
		this.telnetPort = telnetPort;
		this.listenPort = listenPort;
		if(this.vm != null) {
			this.vm.sendCommandToInterpreter("openDataServer " + listenPort + " " + rate);
			this.vm.sendCommandToInterpreter("connect "+ ip + " " + telnetPort);
			this.vm.sendCommandToInterpreter("var throttle = bind \"/controls/engines/current-engine/throttle\"");
			this.vm.sendCommandToInterpreter("var rudder = bind \"/controls/flight/rudder\"");
			this.vm.sendCommandToInterpreter("var aileron = bind \"/controls/flight/aileron\"");
			this.vm.sendCommandToInterpreter("var elevator = bind \"/controls/flight/elevator\"");
			this.vm.sendCommandToInterpreter("var flaps = bind \"/controls/flight/flaps\"");
			return true;
		}
		return false;
	}

	private boolean ipValidityCheck(String ip) {
		return this.p.matcher(ip).matches();
	}

	public void setMainWindow(ViewController mainWindow) {
		this.mainWindow = mainWindow;
	}

	public String getIp() {
		return ip;
	}

	public int getTelnetPort() {
		return telnetPort;
	}

	public int getListenPort() {
		return listenPort;
	}
	public void setDefualts() {
		this.ipText.setText("127.0.0.1");
		this.portText.setText("5402");
		this.listeningport.setText("5400");
		this.rate.setText("10");
	}
	public void clearTextandValues() {
		this.ipText.clear();
		this.portText.clear();
		this.listeningport.clear();
		this.rate.clear();
		this.ip = null;
		this.telnetPort = 0;
		this.listenPort = 0;
		this.rateNum = 0;
	}
	@Override
	public void update(Observable o, Object arg) {

		
	}
	
	
}
