package model.interpreter.interpreter.expression.logic;

import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public class NotExpression implements BooleanExpression {
	private BooleanExpression param;
	
	public NotExpression(BooleanExpression param) {
		this.param = param;
	}

	@Override
	public boolean calculateLogic(SymbolTable symTable) throws Exceptions.SymbolException {
		return !param.calculateLogic(symTable);
	}

}
