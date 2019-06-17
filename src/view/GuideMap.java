package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.text.Font;

public class GuideMap extends Canvas{
	
	private double[][] worldData;
	private double[][] rawData;
	private double startX, startY;
	private double blockSize;
	private double BlockW, BlockH;
	private int maxValue;
	private double destX, destY;
	private double currPosX, currPosY;
	private Image plane;
	private Image target;
	private GraphicsContext gc = getGraphicsContext2D();
	private boolean isMarked = false;
	
	public GuideMap() {
		this.currPosX = 0.0;
		this.currPosY = 0.0;
		try {
			this.plane = new Image(new FileInputStream("./resources/Plane-1.png"));
			this.target = new Image(new FileInputStream("./resources/close.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int realToBlockX(double posX) {
		return (int)(posX / this.BlockW);
	}
	
	private int realToBlockY(double posY) {
		return (int)(posY / this.BlockH);
	}
	
	private int realToMiddleBlockX(double posX) {
		return (int)(realToBlockX(posX) * this.BlockW) + (int)(this.BlockW / 2);
	}
	
	private int realToMiddleBlockY(double posY) {
		return (int)(realToBlockY(posY) * this.BlockH) + (int)(this.BlockH / 2);
	}
	
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
		this.gc.clearRect(0, 0, W, H);
		this.BlockW = W / this.rawData[0].length;
		this.BlockH = H / this.rawData.length;
		this.adaptMapToApp(rawData, this.checkMax(rawData));
		if (this.BlockH > 20)
			gc.setFont(new Font(this.BlockH * 0.5));
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
					this.gc.setFill(c);
					this.gc.fillRect(j*this.BlockW, i*this.BlockH, this.BlockW, this.BlockH);
					if (this.BlockH > 20) {
						this.gc.setFill(Color.BLACK);
						this.gc.fillText((int) this.rawData[i][j] + " ",  j*this.BlockW + 4, i*this.BlockH + this.BlockW - 4);
					}
				}
		this.setPlane(this.currPosX, this.currPosY);
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
	public void setPlane(double posX, double posY) {
			this.gc.drawImage(this.plane, posX, posY, 25, 25);		
	}
	public void markDest(double posX, double posY) {
		this.redraw();
		this.destX = posX;
		this.destY = posY;
		int size = 15;
		GraphicsContext gc = getGraphicsContext2D();
		gc.drawImage(this.target, 
				//realToBlockX(destX) * this.BlockW + (int)(this.BlockW / 2) - size / 2, 
				//realToBlockY(destY) * this.BlockH + (int)(this.BlockH / 2) - size / 2, 
				realToMiddleBlockX(destX) - size / 2,
				realToMiddleBlockY(destY) - size / 2,
				size, 
				size);// draw the dest
		
		// TEST
		List<String> directions = new LinkedList<String>();
		directions.add("DOWN");
		directions.add("RIGHT");
		directions.add("DOWN");
		directions.add("DOWN");
		directions.add("RIGHT");
		directions.add("RIGHT");
		directions.add("UP");
		directions.add("RIGHT");
		drawPath(directions);
	}
	
	public void drawPath(List<String> directions) {
		GraphicsContext gc = getGraphicsContext2D();
		
		double curX = realToMiddleBlockX(currPosX);
		double curY = realToMiddleBlockY(currPosY);
		
		gc.beginPath();
		gc.moveTo(curX, curY);
		gc.stroke();
		
		for (String d: directions) {
			switch (d.toUpperCase()) {
			case "UP":
				curY -= BlockH;
				break;
			case "DOWN":
				curY += BlockH;
				break;
			case "LEFT":
				curX -= BlockW;
				break;
			case "RIGHT":
				curX += BlockW;
				break;
			}
			gc.lineTo(curX, curY);
			gc.stroke();
		}
		gc.closePath();
		
	}

}
