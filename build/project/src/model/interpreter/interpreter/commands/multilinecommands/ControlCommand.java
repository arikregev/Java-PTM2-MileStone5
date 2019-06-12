package model.interpreter.interpreter.commands.multilinecommands;

import model.interpreter.interpreter.commands.Command;

public abstract class ControlCommand implements Command {
	protected Command innerCommand;
	
	public void addInner(Command c) {
	this.innerCommand = c;	
	}
}
