package viewmodel;

import model.pathsolver.PathSolver;
import model.pathsolver.PathSolverFactory;
import model.pathsolver.ServerPathSolver;

public class ServerPathSolverFactory implements PathSolverFactory {
	private ViewModel vm;
	
	public void setViewModel(ViewModel vm) {
		this.vm = vm;
	}
	
	@Override
	public PathSolver create() {
		ServerPathSolver ps = new ServerPathSolver(this.vm.solverIp.get(), Integer.parseInt(this.vm.solverPort.get()));
		ps.addObserver(vm);
		return ps;
	}

}
