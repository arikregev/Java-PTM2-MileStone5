package model.pathsolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ServerPathSolver extends PathSolver {
	
	private int port;
	private String ip;
	private Thread t;
	private volatile int curRequest = 0; 
	
	public ServerPathSolver(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void solve(int[][] map, int[] src, int[] dest) {
		++curRequest;
		t = new Thread(()->{sendData(map, src, dest);});
		t.setDaemon(true);
		t.start();
	}
	
	public void sendData(int[][] map, int[] src, int[] dest) {
		int myRequestNumber = curRequest;
		try (Socket s = new Socket(ip, port)){
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			for (int[] row: map) {
				pw.println(arrayToString(row));
			}
			pw.println("end");
			pw.println(arrayToString(src));
			pw.println(arrayToString(dest));
			pw.flush();
			
			String sol = br.readLine();
			if (sol != null && myRequestNumber == curRequest) {
				notifySolution(Arrays.asList(sol.split(",")));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			notifyException(new SolverExceptionHolder(e));
		}
	}
	
	private String arrayToString(int[] r) {
		return Arrays.toString(r).replaceAll("\\[|\\]|\\s", "");
	}

}
