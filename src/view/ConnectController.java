package view;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectController implements Observer{
	private String ip = null;
	private int telnetPort = 0;
	private int listenPort = 0;
	private ViewController mainWindow = null;
	private final Pattern p = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	@FXML
	Button connect;
	@FXML
	TextField ipText;
	@FXML
	TextField portText;
	@FXML
	Label status;
	@FXML
	TextField listeningport;
	
	
	public void execute() {
		if (ipValidityCheck(this.ipText.getText())) {
			this.ip = ipText.getText();
			try {
				this.telnetPort = Integer.parseInt(this.portText.getText());
				this.listenPort = Integer.parseInt(this.listeningport.getText());
			} catch (Exception e) {
				status.setText("Please enter a valid port");
				return;
			}
			connect(this.ip, this.telnetPort, this.listenPort);
			this.mainWindow.closeConnectWindow();
		} else {
			status.setText("Please enter IP and port");
		}
	}

	private void connect(String ip, int telnetPort, int listenPort) {
		this.ip = ip;
		this.telnetPort = telnetPort;
		this.listenPort = listenPort;
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	
}
