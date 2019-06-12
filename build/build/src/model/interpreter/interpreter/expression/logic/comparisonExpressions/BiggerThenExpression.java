package model.interpreter.interpreter.expression.logic.comparisonExpressions;

import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public class BiggerThenExpression extends ComparisonExpression {

	public BiggerThenExpression(MathExpression left, MathExpression right) {
		super(left, right);
	}

	@Override
	public boolean calculateLogic(SymbolTable symTable) throws Exceptions.SymbolException {
		return left.calculateNumber(symTable) > right.calculateNumber(symTable); //Returns True or False for an Bigger Then Expression
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BiggerThenExpression))
			return false;
		BiggerThenExpression other = (BiggerThenExpression) obj;
		return left.equals(other.left) && right.equals(other.right);
	}

}
