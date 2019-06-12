package model.interpreter.interpreter.expression;

import model.interpreter.interpreter.expression.logic.BooleanExpression;
import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.Symbol;
import model.interpreter.interpreter.symbols.SymbolTable;

public class SymbolExpression implements MathExpression, BooleanExpression {
	String symbol;
	
	public SymbolExpression(String symbol) {
		this.symbol = symbol;
	}
	@Override
	public double calculateNumber(SymbolTable symTable) throws Exceptions.SymbolException {
		return getSym(symTable).getValue();
	}
	@Override
	public boolean calculateLogic(SymbolTable symTable) throws Exceptions.SymbolException {
		return getSym(symTable).getValue()!=0;
	}
	public Symbol getSym(SymbolTable symTable) throws Exceptions.SymbolNotExistException {
		return symTable.getSymbol(symbol);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SymbolExpression))
			return false;
		SymbolExpression other = (SymbolExpression) obj;
//		if (sym == null && other.sym != null) {
//			return false;
//		} 
		return symbol.equals(other.symbol);
	}
	
}
