package model.interpreter.interpreter.commands.singlecommands;

import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.expression.builders.ExpressionBuilder;
import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.BindSymbol;
import model.interpreter.interpreter.symbols.SymbolTable;
/**
 * The Command was created in the purpose of 
 * having the ability to assign values to our variables in
 * our newly invented Language.
 * @param String varName.
 * @param MathExpression varValue.
 * @author Arik Regev
 * @author Amit Koren
 */
public class ExpressionCommand implements Command {
	
	private static interface ExpressionExecutor{
		public void executeExpression(SymbolTable symTable) throws ExecutionException;
	}
	private ExpressionExecutor ee;
	 
	public ExpressionCommand(MathExpression varValue) {
		this.ee = (symTable) -> {varValue.calculateNumber(symTable);};
	}
	
	public ExpressionCommand(String symbolName, String symbolPath) {
		this.ee = (symTable) -> {
			symTable.addSymbol(symbolName, BindSymbol.createInstance(symbolName, symbolPath, symTable));
		};
	}
	
	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		this.ee.executeExpression(symTable);
		return true;
	}
	public static class Factory extends CommandFactory{
		@Override
		public Command create(List<String> tokens) throws ParseException {
			if (tokens.size() == 4 && tokens.get(1).equals("=") && tokens.get(2).equals("bind")) {
				// case: a = bind /path
				return new ExpressionCommand(tokens.get(0), tokens.get(3));
			}
			// case: a = 4 + 5
			MathExpression exp = new ExpressionBuilder().createMathExpression(tokens);
			if (!tokens.isEmpty())
				throw new ParseException("Invalid expression at: " + tokens.get(0));
			return new ExpressionCommand(exp);
		}
		
	}

}
