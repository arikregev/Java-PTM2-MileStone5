package model.interpreter.interpreter.commands.singlecommands;

import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.expression.builders.ExpressionBuilder;
import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.SymbolTable;

public class ReturnCommand implements Command{
	
	private MathExpression exp;
	
	public ReturnCommand(MathExpression e) {
		this.exp = e;
	}
	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		symTable.setReturnValue(this.exp.calculateNumber(symTable));
		return false;
	}
	 
	public static class Factory extends CommandFactory{
		@Override
		public Command create(List<String> tokens) throws ParseException {
			MathExpression exp = new ExpressionBuilder().createMathExpression(tokens);
			if (!tokens.isEmpty())
				throw new ParseException("Invalid expression at: " + tokens.get(0));
			return new ReturnCommand(exp);
		}
		
	}


}
