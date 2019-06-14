package model.interpreter.interpreter.commands.servercommands;

import java.io.IOException;

import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.expression.builders.ExpressionBuilder;
import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.SymbolTable;
/**
 * The purpose of this class is to open a listening port that the simulator<br>
 * can connect to.<br>
 * Through that connection the simulator pushes data to the program that contains Information about current status of the AirCraft. 
 * @param MathExpression port
 * @param MathExpression rate
 * @author Arik Regev
 * @author Amit Koren
 *
 */
public class OpenDataServerCommand implements Command {
	private MathExpression port;
	private MathExpression rate;

	public OpenDataServerCommand(MathExpression port, MathExpression rate) {
		this.port = port;
		this.rate = rate;
	}

	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		try {
			symTable.simCom.openDataServer((int)port.calculateNumber(symTable),
					(int)rate.calculateNumber(symTable));
		} catch (IOException e) {
			throw new IOExceptionWrapper(e);
		}
		return true;
	}
	
	public static class Factory extends CommandFactory {
		@Override
		public Command create(List<String> tokens) throws ParseException {
			MathExpression portExp = new ExpressionBuilder().createMathExpression(tokens);
			MathExpression rateExp = new ExpressionBuilder().createMathExpression(tokens);
			if (!tokens.isEmpty())
				throw new ParseException("Invalid expression at: " + tokens.get(0));
			return new OpenDataServerCommand(portExp, rateExp);
		}
	}
}
