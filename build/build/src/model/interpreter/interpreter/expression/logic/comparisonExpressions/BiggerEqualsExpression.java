package model.interpreter.interpreter.expression.logic.comparisonExpressions;

import model.interpreter.interpreter.expression.math.MathExpression;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public class BiggerEqualsExpression extends ComparisonExpression {

	public BiggerEqualsExpression(MathExpression left, MathExpression right) {
		super(left, right);
	}

	@Override
	public boolean calculateLogic(SymbolTable symTable) throws Exceptions.SymbolException {
		return left.calculateNumber(symTable) >= right.calculateNumber(symTable); //Returns True or False for an BiggerEquals Then Expression
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BiggerEqualsExpression))
			return false;
		BiggerEqualsExpression other = (BiggerEqualsExpression) obj;
		return left.equals(other.left) && right.equals(other.right);
	}

}
