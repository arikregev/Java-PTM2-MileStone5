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
 * Defining how our IF Command will behave 
 * 
 * @author Arik Regev
 * @author Amit Koren
 *
 */
public class IfCommand extends ControlCommand{
	private BooleanExpression exp;
	
	public IfCommand(BooleanExpression exp) {
		this.exp = exp;
	}
	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		if(exp.calculateLogic(symTable)) { 
			return innerCommand.execute(symTable);
		}
		return true;
	}
	public static class Factory extends CommandFactory{
		@Override
		public Command create(List<String> tokens) throws ParseException {
			BooleanExpression booleanExp = new ExpressionBuilder().createBooleanExpression(tokens);
			if (!tokens.isEmpty())
				throw new ParseException("Invalid expression at: " + tokens.get(0));
			return new IfCommand(booleanExp);
			
		}
		
	}

}
