package model.interpreter.interpreter.expression.logic.comparisonExpressions;

import model.interpreter.interpreter.expression.logic.BooleanExpression;
import model.interpreter.interpreter.expression.math.MathExpression;

public abstract class ComparisonExpression implements BooleanExpression {
	protected MathExpression left;
	protected MathExpression right;
	
	public ComparisonExpression(MathExpression left, MathExpression right) {
		this.left = left;
		this.right = right;
	}

}
