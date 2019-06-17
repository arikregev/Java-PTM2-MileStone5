package model.interpreter.interpreter.commands.singlecommands;

import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.expression.builders.ExpressionBuilder;
import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.BindSymbol;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.RegularSymbol;
import model.interpreter.interpreter.symbols.SymbolTable;
/**
 * Every Var Command represents a link to a parameter in the aircraft.<br>
 * TODO ...
 * @param String SymbolName
 * @param SymbolFactory
 * @author Arik Regev
 * @author Amit Koren
 */
public class VarCommand implements Command {
	/**
	 * Symbol Factory is a static interface for the purpose of creating new symbols<br>
	 * using Lambda Expressions and inserting the newly created symbol into the Symbol Table.
	 * @author Arik Regev
	 */
	private static interface SymbolFactory{
		public void createSymbol(SymbolTable symTable) throws Exceptions.SymbolException;
	}
	private SymbolFactory s;
	 
	public VarCommand(String symName) {
		this.s = (symTable)->{
			symTable.addSymbol(symName, null);
		};
	}
	
	public VarCommand(String symName, MathExpression exp) {
		this.s = (symTable)->{
			symTable.addSymbol(symName, new RegularSymbol(symName));
			//exp.calculateNumber(symTable);
			symTable.getSymbol(symName).setValue(exp.calculateNumber(symTable));
		};
	}
		
	public VarCommand(String symName, String path) {
		this.s = (symTable)->{
			symTable.addSymbol(symName, BindSymbol.createInstance(symName, path, symTable));
		};
		// a = bind hgjkgkjh
	}

	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		s.createSymbol(symTable);
		return true;
	}
	public static class Factory extends CommandFactory{
		@Override
		public Command create(List<String> tokens) throws ParseException {
			if (tokens.isEmpty())
				throw new ParseException("Expression must not be empty");
			String symName = tokens.get(0);
			if (tokens.size() == 1)
				return new VarCommand(symName);
			if (tokens.get(2).equals("bind")) {
				if (tokens.size() > 4)
					throw new ParseException("Var (bind) expression too long");
				String bindValue = tokens.get(3);
				if (bindValue.startsWith("\"") && bindValue.endsWith("\""))
					bindValue = bindValue.substring(1, bindValue.length()-1);
				return new VarCommand(symName, bindValue);
			}
			MathExpression exp = new ExpressionBuilder().createMathExpression(tokens);
			if (!tokens.isEmpty())
				throw new ParseException("Invalid expression at: " + tokens.get(0));
			return new VarCommand(symName, exp);
		}
		
	}

}
