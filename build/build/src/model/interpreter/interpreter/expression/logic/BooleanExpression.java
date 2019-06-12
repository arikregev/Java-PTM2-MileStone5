package model.interpreter.interpreter.expression.logic;

import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public interface BooleanExpression {
	public boolean calculateLogic(SymbolTable symTable) throws Exceptions.SymbolException;
}
