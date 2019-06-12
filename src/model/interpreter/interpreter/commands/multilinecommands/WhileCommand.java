package model.interpreter.interpreter.commands.multilinecommands;

import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.expression.builders.ExpressionBuilder;
import model.interpreter.interpreter.expression.logic.BooleanExpression;
import model.interpreter.interpreter.symbols.SymbolTable;

/**
 * Defining how our While Command will behave 
 * 
 * @author Arik Regev
 * @author Amit Koren
 */
public class WhileCommand extends ControlCommand{
	
	private BooleanExpression exp;
	
	public WhileCommand(BooleanExpression exp) {
		this.exp = exp;
	}
	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		while(exp.calculateLogic(symTable)) {
			if(!innerCommand.execute(symTable))
				return false;
		}
		return true;
	}
	public static class Factory extends CommandFactory{
		@Override
		public Command create(List<String> tokens) throws ParseException {
			BooleanExpression booleanExpr = new ExpressionBuilder().createBooleanExpression(tokens);
			if (!tokens.isEmpty())
				throw new ParseException("Invalid expression at: " + tokens.get(0));
			return new WhileCommand(booleanExpr);
		}
		
	}

}
