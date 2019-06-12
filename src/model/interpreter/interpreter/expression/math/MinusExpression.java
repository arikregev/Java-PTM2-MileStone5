package model.interpreter.interpreter.expression.math;

import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public class MinusExpression extends BinaryExpression {

	public MinusExpression(MathExpression left, MathExpression right) {
		super(left, right);
	}

	@Override
	public double calculateNumber(SymbolTable symTable) throws Exceptions.SymbolException {
		return this.left.calculateNumber(symTable) - this.right.calculateNumber(symTable);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MinusExpression))
			return false;
		MinusExpression other = (MinusExpression) obj;
		return left.equals(other.left) && right.equals(other.right);
	}
}
