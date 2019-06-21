package model.pathsolver;

import java.util.List;
import java.util.Observable;

public abstract class PathSolver extends Observable{
	public static class SolverExceptionHolder {
		public Exception e;

		public SolverExceptionHolder(Exception e) {
			this.e = e;
		}
	}
	
	public abstract void solve(int[][] map, int[] src, int[] dest);
	protected void notifySolution(List<String> directions) {
		this.setChanged();
		this.notifyObservers(directions);
	}
	protected void notifyException(SolverExceptionHolder e) {
		this.setChanged();
		this.notifyObservers(e);
	}
}
