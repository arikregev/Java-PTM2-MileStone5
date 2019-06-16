package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GuideMap extends Canvas{
	
	private double[][] worldData;
	private double[][] rawData;
	private double startX, startY;
	private double blockSize;
	private int maxValue;
	private GraphicsContext gc = getGraphicsContext2D();
	
	private double checkMax(double arr[][]) {
		double max = arr[0][0];
		for(double[] is: arr)
			for (double is2 : is)
				if(is2 > max)
					max = is2;
		return max;
	}
	public void adaptMapToApp(double[][] source, double maxNum){
		if(source != null){
			this.worldData = new double[source.length][source[0].length];
			for(int i = 0; i < source.length; i++)
				for(int j = 0; j < source[i].length; j++)
					this.worldData[i][j] = source[i][j] / maxNum;
		}
	}
	public void redraw() {
		double W = this.getWidth();
		double H = this.getHeight();
		double w = W / this.rawData[0].length;
		double h = H / this.rawData.length;
		this.adaptMapToApp(rawData, this.checkMax(rawData));
		if (h > 20)
			gc.setFont(new Font(h * 0.5));
		if(this.worldData != null)
			for(int i = 0 ; i < this.worldData.length ; i++)
				for(int j = 0 ; j < this.rawData[i].length; j++) {
					Color c = null;
					if(this.worldData[i][j] < 0.5) {
						String s = String.format("%02X", (int)(2 * 0xff * this.worldData[i][j]));
						c = Color.web("#FF" + s + "00");
					} else {
						String s = String.format("%02X", (int)(2 * 0xff * (1-this.worldData[i][j])));
						c = Color.web("#" + s + "FF00");
					}
					gc.setFill(c);
					gc.fillRect(j*w, i*h, w, h);
					if (h > 20) {
						gc.setFill(Color.BLACK);
						gc.fillText((int) this.rawData[i][j] + " ",  j*w + 4, i*h + w - 4);
					}
				}
	}
							
	public void generateMap(File mapCsvFile) {
		try {
			Scanner scanner = new Scanner(mapCsvFile);
			scanner.useDelimiter("\r\n");
			
			String line0 = scanner.next();
			String line1 = scanner.next();
			LinkedList<String> csvLines = new LinkedList<String>();
			while (scanner.hasNext())
				csvLines.add(scanner.next());
			scanner.close();
			
			String[] startPos = line0.split(",");
			// assert startPos.length == 2
			startX = Double.parseDouble(startPos[0]);
			startY = Double.parseDouble(startPos[1]);
			
			String s = line1.split(",")[0];
			blockSize = Double.parseDouble(s);
			
			this.rawData = new double[csvLines.size()][csvLines.get(0).split(",").length];
			
			int i = 0, j = 0; 
			
			for (String line: csvLines) {
				j = 0;
				for (String cell: line.split(",")) {
					this.rawData[i][j] = Double.parseDouble(cell);
					j++;
				}
				i++;
			}

	    	this.redraw();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
