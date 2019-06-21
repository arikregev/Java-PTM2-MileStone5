package view;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import viewmodel.ViewModel;

public class ConnectSolverController implements Observer{
	private String ip = null;
	private int Port = 0;
	private int rateNum = 0;
	private ViewController mainWindow = null;
	private ViewModel vm = null;
	private final Pattern p = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	@FXML
	Button connect;
	@FXML
	TextField ipText, portText;
	@FXML
	Label status;


	public void execute() {
		if (ipValidityCheck(this.ipText.getText())) {
			this.ip = ipText.getText();
			try {
				this.Port = Integer.parseInt(this.portText.getText());
			} catch (Exception e) {
				this.status.setText("Please enter a valid port");
				
				return;
			}
			if(connect(this.ip, this.Port)) {
				this.mainWindow.vm.setConnected(true);
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
	private boolean connect(String ip, int port) {
		//this.vm.connectToSolver(ip, port);
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
		return Port;
	}


	public void setDefualts() {
		this.ipText.setText("127.0.0.1");
		this.portText.setText("3373");
	}
	public void clearTextandValues() {
		this.ipText.clear();
		this.portText.clear();
		this.ip = null;
		this.Port = 0;
		this.rateNum = 0;
	}
	@Override
	public void update(Observable o, Object arg) {

		
	}
	
	
}
