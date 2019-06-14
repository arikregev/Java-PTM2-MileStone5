package model.interpreter.interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.interpreter.interpreter.commands.*;
import model.interpreter.interpreter.commands.factory.CommandFactory;
import model.interpreter.interpreter.commands.multilinecommands.*;
import model.interpreter.interpreter.commands.servercommands.*;
import model.interpreter.interpreter.commands.singlecommands.*;
import model.interpreter.interpreter.symbols.SymbolTable;

public class Interpreter extends Observable  {
	@SuppressWarnings("serial")
	public static class ParseException extends Exception {

		public ParseException(String string) {
			super(string);
		}}
	
	private HashMap<String, CommandFactory> commandMap;
	private SymbolTable symTable;
	private Stack<MultiLineCommand> blockStack;
	private ControlCommand ctrl;
	private List<String> tokenStream = new LinkedList<String>();
	private final String lexerRegex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|-?\\d+\\.?\\d*|-?\\d*\\.?\\d+|\".*\"|==|!=|<=|>=|<|>|\\+|-|\\*|\\/|&&|\\|\\||!|=|\\(|\\)|\\{|\\}|\\w+)";
	
	public Interpreter(String[] simPaths) {
		commandMap = new HashMap<>();
		blockStack = new Stack<>();
		symTable = new SymbolTable(simPaths);
		
		commandMap.put("var", new VarCommand.Factory());
		commandMap.put("openDataServer", new OpenDataServerCommand.Factory());
		commandMap.put("connect", new ConnectCommand.Factory());
		commandMap.put("while", new WhileCommand.Factory());
		commandMap.put("if", new IfCommand.Factory());
		commandMap.put("print", new PrintCommand.Factory());
		commandMap.put("sleep", new SleepCommand.Factory());
		commandMap.put("return", new ReturnCommand.Factory());
		commandMap.put("disconnect", new DisconnectCommand.Factory());
	}
	
	//public abstract String getLine() throws EOFException;
	
	public void interpretLine(String line) throws ExecutionException, ParseException{	
		tokenStream.addAll(lexer(line));
		for (Command c: parseTokens(tokenStream)) { 
			c.execute(symTable);
			
		}
	}
	
	public double getReturnValue() {
		return symTable.getReturnValue();
	}
	
	// breaks a line into separate tokens
	private List<String> lexer(String line) throws ParseException {
		Pattern r = Pattern.compile(lexerRegex);
		List<String> tokens = new LinkedList<String>();
		Matcher m = r.matcher(line);
		// TODO: check that no meaningful letter exists between tokens
		while (m.find()) {
			tokens.add(line.substring(m.start(), m.end()));
		}
		return tokens;
	}
	
	// parses as many commands as possible from tokenStream
	private List<Command> parseTokens(List<String> tokenStream) throws ParseException{
		List<Command> commands = new LinkedList<Command>();
		while (!tokenStream.isEmpty()) {
			String openingToken = tokenStream.get(0); 
			if (openingToken.equals("{")) {
				MultiLineCommand n = new MultiLineCommand();
				if(ctrl != null) {
					ctrl.addInner(n);
					n.setOwner(ctrl);
				}
				blockStack.push(n);
				
				tokenStream.remove(0);
				ctrl = null;
				
				continue;
			}
			if (openingToken.equals("}")) {
				if(blockStack.isEmpty()) throw new ParseException("Syntax error: too many '}'"); 
				MultiLineCommand block = blockStack.pop();
				if(blockStack.isEmpty())
					if(block.getOwner() == null)
						commands.add(block);
					else
						commands.add(block.getOwner());
				
				tokenStream.remove(0);
				ctrl = null;
				
				continue;
			}
			List<String> commandTokens = new LinkedList<>();
			while (!tokenStream.isEmpty() && !tokenStream.get(0).equals("}")
					&& !tokenStream.get(0).equals("{")) {
				commandTokens.add(tokenStream.remove(0));
			}
			
			Command cmd = parseCommand(commandTokens);
			
			if(!blockStack.isEmpty())
				blockStack.peek().addSubCommand(cmd);
			else if (!(cmd instanceof ControlCommand))
				commands.add(cmd);
			
			if (cmd instanceof ControlCommand)
				ctrl = (ControlCommand) cmd;
			else
				ctrl = null;
		}
		return commands;
	}
	private Command parseCommand(List<String> tokens) throws ParseException{
		if(!commandMap.containsKey(tokens.get(0)))
			return new ExpressionCommand.Factory().create(tokens);
		return commandMap.get(tokens.remove(0)).create(tokens);
	}
	
}
