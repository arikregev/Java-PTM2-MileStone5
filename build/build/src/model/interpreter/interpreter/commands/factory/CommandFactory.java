package model.interpreter.interpreter.commands.factory;

import java.util.List;
import model.interpreter.interpreter.MainInterpreter.ParseException;
import model.interpreter.interpreter.commands.Command;

/**
 * An abstract class that responsible for creating the commands like Builder Pattern<br>
 * Each and every command has an inner class that Extends CommandFactory and responsible<br>
 * Of Returning an Instance of the relevant Command.
 * @author Arik Regev
 * @author Amit Koren
 *
 */
public abstract class CommandFactory {

	public abstract Command create(List<String> tokens) throws ParseException;
}
