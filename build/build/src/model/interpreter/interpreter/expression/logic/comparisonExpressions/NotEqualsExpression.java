package model.interpreter.interpreter.expression.logic.comparisonExpressions;

import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public class NotEqualsExpression extends ComparisonExpression {

	public NotEqualsExpression(MathExpression left, MathExpression right) {
		super(left, right);
	}

	@Override
	public boolean calculateLogic(SymbolTable symTable) throws Exceptions.SymbolException {
		return (left.calculateNumber(symTable) - right.calculateNumber(symTable)) > 1e-3;
		
	}

}
