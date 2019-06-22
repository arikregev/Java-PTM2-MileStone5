package model.pathsolver;

import java.util.LinkedList;
import java.util.List;

public class StubPathSolver extends PathSolver {

	@Override
	public void solve(int[][] map, int[] src, int[] dest) {
		List<String> directions = new LinkedList<String>();
		directions.add("DOWN");
		directions.add("RIGHT");
		directions.add("DOWN");
		directions.add("DOWN");
		directions.add("RIGHT");
		directions.add("RIGHT");
		directions.add("UP");
		directions.add("RIGHT");
		
		this.notifySolution(directions);

	}

}
