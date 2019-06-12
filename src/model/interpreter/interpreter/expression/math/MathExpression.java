package model.interpreter.interpreter.expression.math;

import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public interface MathExpression {
	public double calculateNumber(SymbolTable symTable) throws Exceptions.SymbolException;
}
