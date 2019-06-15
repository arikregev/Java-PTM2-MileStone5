package model.interpreter.interpreter;
import model.interpreter.interpreter.MainInterpreter.ParseException;
import model.interpreter.interpreter.commands.ExecutionException;

public interface Interpreter {
	public void interpretLine(String line) throws ExecutionException, ParseException;
	public void resetInterpreter();
}
