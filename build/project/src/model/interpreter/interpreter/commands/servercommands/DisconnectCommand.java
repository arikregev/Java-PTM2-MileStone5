package model.interpreter.interpreter.commands.servercommands;

import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.symbols.SymbolTable;

/**
 * The Command was created in the purpose of having the ability to Disconnect 
 * the Telnet connection to the FlightGear Server and terminate our OpenDataServer
 * Using SimCom.
 * 
 * @author Arik Regev
 * @author Amit Koren
 */
public class DisconnectCommand implements Command {

	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		symTable.simCom.disconnect();
		return true;
	}

	public static class Factory extends CommandFactory {
		@Override
		public Command create(List<String> tokens) throws ParseException {
			if (!tokens.isEmpty())
				throw new ParseException("Expression must be empty");
			return new DisconnectCommand();
		}
	}
}
