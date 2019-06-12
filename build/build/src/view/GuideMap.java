package view;

import javafx.scene.canvas.Canvas;

public class GuideMap extends Canvas{
	
	private int[][] worldData;
	
	public void setWorldData(int[][] worldData) {
		this.worldData = worldData;
		this.redraw();
	}
	public void redraw() {
		
	}

}
