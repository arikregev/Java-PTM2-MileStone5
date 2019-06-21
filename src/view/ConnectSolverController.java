package view;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import viewmodel.ViewModel;

public class ConnectSolverController{
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
		if (!ipValidityCheck(this.ipText.getText())) {
			status.setText("Invalid IP - Please enter IP and port");
			return;
		}
		if (!portValidityCheck(this.portText.getText())) {
			status.setText("Invalid port number - Please enter IP and port");
			return;
		}
		this.mainWindow.closeConnectSolverWindow();
		this.mainWindow.executeCalculate();
	}
	public void setViewModel(ViewModel vm) {
		this.vm = vm;
		this.vm.solverIp.bind(this.ipText.textProperty());
		this.vm.solverPort.bind(this.portText.textProperty());
	}
	private boolean ipValidityCheck(String ip) {
		return this.p.matcher(ip).matches();
	}
	private boolean portValidityCheck(String port) {
		try {
			Integer.parseInt(this.portText.getText());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void setMainWindow(ViewController mainWindow) {
		this.mainWindow = mainWindow;
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
	
	
}
