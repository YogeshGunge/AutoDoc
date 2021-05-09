package org.autodoc.interpreter;

import java.util.ArrayList;
import java.util.ListIterator;

import org.autodoc.model.Instruction;
import org.autodoc.model.InstructionSet;

/**
*
* @author yogesh.gunge
*/
public class InstructionDecoder {
	
	private ListIterator<Instruction> codeIter;
	
	public InstructionDecoder(InstructionSet iset) {
		this.codeIter = iset.getInstructionList().listIterator();
	}
	
	public void next() {
		codeIter.next();
	}
	
	public boolean hasNext() {
		return (codeIter.hasNext());
	}
	
	public int getOpCodeId() {
		int id = Architecture
				.getOpCodeId(
						codeIter.next()
						.getOpcode());
		codeIter.previous();
		return id;
	}
	
	public String getOpCode() {
		String opcode = codeIter.next()
						.getOpcode();
		codeIter.previous();
		return opcode;
	}
	
	public ArrayList<String> getOperandList() {
		ArrayList<String> operandList = codeIter.next().getOperands();
		codeIter.previous();
		return operandList;
	}

}