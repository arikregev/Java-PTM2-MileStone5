package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import viewmodel.ViewModel;

public class GuideMap extends Canvas{
	
	private int[][] rawTable;
	private double rawZeroX, rawZeroY;
	private double rawScale;
	private double blockGuiWidth, blockGuiHeight;
	private int maxRawValue;
	private double srcGuiX, srcGuiY;
	private double destGuiX, destGuiY;
	private double srcGuiXCandidate, srcGuiYCandidate;
	private double destGuiXCandidate, destGuiYCandidate;
	private double currGuiPosX, currGuiPosY;
	private double currSimPosX, currSimPosY;
	private Image plane;
	private Image target;
	private ChangeListener<Number> positionXChangeListener;
	private ChangeListener<Number> positionYChangeListener;
	private List<String> directions = null;
	private boolean isDestPressed = false;
	private boolean isDestCandidateAvail = false;
	private ViewModel vm = null;
	
	public GuideMap() {
		this.currGuiPosX = 0.0;
		this.currGuiPosY = 0.0;
		this.srcGuiX = 0.0; 
		this.srcGuiY = 0.0;
		try {
			this.plane = new Image(new FileInputStream("./resources/Plane-1.png"));
			this.target = new Image(new FileInputStream("./resources/close.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		positionXChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double temp = ((Double)newValue - rawZeroX) * blockGuiWidth * rawScale;
				currSimPosX = (Double)newValue;
				if(temp < 0)
					currGuiPosX = temp*-1;
				else 
					currGuiPosX = temp;
			}
		};
		positionYChangeListener = new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double temp = ((Double)newValue - rawZeroY) * blockGuiHeight * rawScale;
				currSimPosY = (Double)newValue;
				if(temp < 0)
					currGuiPosY = temp*-1;
				else 
					currGuiPosY = temp;
				//System.out.println("Sim (" + currSimPosX+","+ currSimPosY+") Gui (" + currGuiPosX + "," + currGuiPosY + ")");
				onPlanePositionChange();
			}
		};
	}
	public void setViewModel(ViewModel vm) {
		this.vm = vm;
		vm.doublePropertiesMap.get(ViewModel.LATITUDE).addListener(positionXChangeListener);
		vm.doublePropertiesMap.get(ViewModel.LONGITUDE).addListener(positionYChangeListener);
	}
	
	public void onPlanePositionChange() {
		drawMap();
		drawPlane();
		drawDest();
		drawDestCandidate();
		drawPath();
	}
	
	public void onNewMapSelected() {
		resetPathAndDests();
	}
	
	public void onDestPressed(double posX, double posY) {
		this.resetPathAndDests();
		this.destGuiXCandidate = posX;
		this.destGuiYCandidate = posY;
		this.srcGuiXCandidate = this.currGuiPosX;
		this.srcGuiYCandidate = this.currGuiPosY;
		this.isDestCandidateAvail = true;
		this.drawDestCandidate();
	}
	
	public void onPathGenerated() {
		this.destGuiX = this.destGuiXCandidate;
		this.destGuiY = this.destGuiYCandidate;
		this.srcGuiX = this.srcGuiXCandidate;
		this.srcGuiY = this.srcGuiYCandidate;
		this.isDestPressed = this.isDestCandidateAvail;
		this.isDestCandidateAvail = false;
		drawMap();
		drawPlane();
		drawDest();
		drawPath();
	}
	
	public void setDirections(List<String> directions) {
		this.directions = directions;
	}
	public int[][] getRawTable() {
		return rawTable;
	}
	
	public int getSrcXIndex() {
		return realToBlockIndexX(srcGuiXCandidate);
	}
	public int getSrcYIndex() {
		return realToBlockIndexY(srcGuiYCandidate);
	}
	public int getDestXIndex() {
		return realToBlockIndexX(destGuiXCandidate);
	}
	public int getDestYIndex() {
		return realToBlockIndexY(destGuiYCandidate);
	}

	private int realToBlockIndexX(double posX) {
		return (int)(posX / this.blockGuiWidth);
	}
	
	private int realToBlockIndexY(double posY) {
		return (int)(posY / this.blockGuiHeight);
	}
	
	private double realToMiddleBlockX(double posX) {
		return (realToBlockIndexX(posX) * this.blockGuiWidth) + (this.blockGuiWidth / 2);
	}
	
	private double realToMiddleBlockY(double posY) {
		return (realToBlockIndexY(posY) * this.blockGuiHeight) + (this.blockGuiHeight / 2);
	}
	
	private void drawMap() {
		if(this.rawTable == null)
			return;
		GraphicsContext gc = getGraphicsContext2D();
		double guiWidth = this.getWidth();
		double guiHeight = this.getHeight();
		gc.clearRect(0, 0, guiWidth, guiHeight);
		this.blockGuiWidth = guiWidth / this.rawTable[0].length;
		this.blockGuiHeight = guiHeight / this.rawTable.length;
		//this.adaptMapToApp(rawData, this.checkMax(rawData));
		if (this.blockGuiHeight > 20)
			gc.setFont(new Font(this.blockGuiHeight * 0.5));
		for(int i = 0 ; i < this.rawTable.length ; i++)
			for(int j = 0 ; j < this.rawTable[i].length; j++) {
				Color c = null;
				if(this.rawTable[i][j] < 0.5*this.maxRawValue) {
					String s = String.format("%02X", (int)(2 * 0xff * (this.rawTable[i][j]/(double)this.maxRawValue)));
					c = Color.web("#FF" + s + "00");
				} else {
					String s = String.format("%02X", (int)(2 * 0xff * (1-this.rawTable[i][j]/(double)this.maxRawValue)));
					c = Color.web("#" + s + "FF00");
				}
				gc.setFill(c);
				gc.fillRect(j*this.blockGuiWidth, i*this.blockGuiHeight, this.blockGuiWidth, this.blockGuiHeight);
				if (this.blockGuiHeight > 20) {
					gc.setFill(Color.BLACK);
					gc.fillText(this.rawTable[i][j] + " ",  j*this.blockGuiWidth + 4, i*this.blockGuiHeight + this.blockGuiWidth - 4);
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
			rawZeroY = Double.parseDouble(startPos[0]);
			rawZeroX = Double.parseDouble(startPos[1]);
			//TODO: set current position (relative to X,Y from Simulator)
			
			String s = line1.split(",")[0];
			rawScale = Double.parseDouble(s);
			
			this.rawTable = new int[csvLines.size()][csvLines.get(0).split(",").length];
			
			int i = 0, j = 0; 
			this.maxRawValue = 0;
			for (String line: csvLines) {
				j = 0;
				for (String cell: line.split(",")) {
					int currValue = Integer.parseInt(cell);
					this.rawTable[i][j] = currValue;
					if(currValue > this.maxRawValue) {
						this.maxRawValue = currValue;
					}
					j++;
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawPlane() {
		GraphicsContext gc = getGraphicsContext2D();
		double size = 25;
		gc.drawImage(this.plane,
				realToMiddleBlockX(this.currGuiPosX) - size / 2, 
				realToMiddleBlockY(this.currGuiPosY) - size / 2,
				size, 
				size);		
	}
	
	private void drawDestInner(double posX, double posY) {
		int size = 15;
		GraphicsContext gc = getGraphicsContext2D();
		gc.drawImage(this.target, 
				posX - size / 2,
				posY - size / 2,
				size, 
				size);// draw the dest	
	}
	
	private void drawDest() {
		if (!isDestPressed)
			return;
		drawDestInner(realToMiddleBlockX(destGuiX), realToMiddleBlockY(destGuiY));
	}
	
	private void drawDestCandidate() {
		if (!isDestCandidateAvail)
			return;
		drawDestInner(realToMiddleBlockX(destGuiXCandidate), realToMiddleBlockY(destGuiYCandidate));	
	}
	
	private void resetPathAndDests() {
		this.directions = null;
		this.isDestPressed = false;
		this.isDestCandidateAvail = false;
		drawMap();
		drawPlane();
	}
	

	
	private void drawPath() {
		if (directions == null) {
			return;
		}
		GraphicsContext gc = getGraphicsContext2D();
		double onCalcPosX, onCalcPosY;
		onCalcPosX = realToMiddleBlockX(srcGuiX);
		onCalcPosY = realToMiddleBlockY(srcGuiY);
		
		gc.beginPath();
		gc.moveTo(onCalcPosX, onCalcPosY);
		
		//gc.stroke();
		
		for (String d: directions) {
			switch (d.toUpperCase()) {
			case "UP":
				onCalcPosY -= blockGuiHeight;
				break;
			case "DOWN":
				onCalcPosY += blockGuiHeight;
				break;
			case "LEFT":
				onCalcPosX -= blockGuiWidth;
				break;
			case "RIGHT":
				onCalcPosX += blockGuiWidth;
				break;
			}
			gc.lineTo(onCalcPosX, onCalcPosY);
		}
		gc.stroke();
		gc.closePath();
		
	}

}
