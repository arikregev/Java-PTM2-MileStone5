package model.interpreter.interpreter.commands.singlecommands;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import model.interpreter.interpreter.Interpreter.ParseException;
import model.interpreter.interpreter.commands.Command;
import model.interpreter.interpreter.commands.ExecutionException;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.expression.builders.ExpressionBuilder;
import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;
/**
 * The Print command allows you to print to the screen Strings and MathExpressions.
 * @author Arik Regev
 * @author Amit Koren
 */
public class PrintCommand implements Command {
	/**
	 * Private interface that when implemented can be used to print Strings and MathExpressions<br>
	 * Using Lambda Expression.
	 * @author Arik Regev
	 *
	 */
	private static interface Printable{
		public void printMe(SymbolTable symTable) throws Exceptions.SymbolException;
	}
	

	
	//private Printable value;
	private List<Printable> values;
	
	public PrintCommand(List<Printable> values) {
		this.values = values;
	}
	
	@Override
	public boolean execute(SymbolTable symTable) throws ExecutionException {
		for (Printable value: values)
			value.printMe(symTable);
		return true;
	}
	
	
	public static class Factory extends CommandFactory{
		
		public static interface Printer {
			public void print(String s);
			public void println(String s);
		}
		
		Printer printer;
		
		public Factory(Printer printer) {
			this.printer = printer;
		}
		
		public Factory(PrintStream printStream) {
			this.printer = new Printer() {
				@Override
				public void print(String s) {
					printStream.print(s);
				}
				@Override
				public void println(String s) {
					printStream.println(s);
				}
			};
		}
		
		@Override
		public Command create(List<String> tokens) throws ParseException {
			if (tokens.isEmpty())
				throw new ParseException("Expression must not be empty");
			
			List<Printable> values = new LinkedList<Printable>();
			
			LinkedList<String> currentTokens = new LinkedList<>();
			for (String token: tokens) {
				if (!token.equals(",")) {
					currentTokens.add(token);
					continue;
				}
				values.add(makePrintableFromTokens(currentTokens));
				currentTokens = new LinkedList<>();
			}
			values.add(makePrintableFromTokens(currentTokens));
			values.add((SymbolTable symTable)->{printer.println("");});
			return new PrintCommand(values);
		}
		 
		private Printable makePrintableFromTokens(List<String> currentTokens) throws ParseException{
			if (currentTokens.isEmpty())
				throw new ParseException("Invalid expression at print command");
			if(currentTokens.size() == 1 
					&& currentTokens.get(0).startsWith("\"") 
					&& currentTokens.get(0).endsWith("\"")) {
				String s = currentTokens.get(0);
				return (SymbolTable symTable)->{printer.print(s.substring(1, s.length()-1));};
			} 
			MathExpression mathExp = new ExpressionBuilder().createMathExpression(currentTokens);
			return (SymbolTable symTable)->{printer.print(Double.toString(mathExp.calculateNumber(symTable)));};
		}
	}

}
