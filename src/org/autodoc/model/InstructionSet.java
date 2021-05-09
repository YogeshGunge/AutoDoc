package org.autodoc.model;

import java.util.ArrayList;

public class InstructionSet {
	private ArrayList<Instruction> instructionList;
	public InstructionSet() {
		setInstructionList(new ArrayList<Instruction>());
	}
	public ArrayList<Instruction> getInstructionList() {
		return instructionList;
	}
	public void setInstructionList(ArrayList<Instruction> instructionlist) {
		this.instructionList = instructionlist;
	}
	public void addInstruction(Instruction i) {
		this.instructionList.add(i);
	}
}
