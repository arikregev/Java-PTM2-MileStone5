package model.interpreter.interpreter.expression.math;

import model.interpreter.interpreter.expression.SymbolExpression;
import model.interpreter.interpreter.symbols.Exceptions;
import model.interpreter.interpreter.symbols.SymbolTable;

public class AssignmentExpression implements MathExpression {
	private SymbolExpression left;
	private MathExpression right;

	public AssignmentExpression(SymbolExpression left, MathExpression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public double calculateNumber(SymbolTable symTable) throws Exceptions.SymbolException {
		double value = right.calculateNumber(symTable);
		left.getSym(symTable).setValue(value);
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AssignmentExpression))
			return false;
		AssignmentExpression other = (AssignmentExpression) obj;
		return left.equals(other.left) && right.equals(other.right);
	}

}
