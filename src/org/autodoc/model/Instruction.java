package org.autodoc.model;

import java.util.ArrayList;

public class Instruction {
	private String opcode;
	private ArrayList<String> operands;
	
	public Instruction(String opcode) {
		setOpcode(opcode);
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public ArrayList<String> getOperands() {
		return operands;
	}

	public void setOperands(ArrayList<String> operands) {
		this.operands = operands;
	}
	
	public void addOperand(String operand) {
		this.operands.add(operand);
	}
}