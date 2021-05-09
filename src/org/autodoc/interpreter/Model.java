package org.autodoc.interpreter;

import java.util.ArrayList;

import org.autodoc.model.Instruction;
import org.autodoc.model.InstructionSet;

/**
*
* @author yogesh.gunge
*/
public class Model {
	private static InstructionSet code;
	public static void initModel() {
		code = new InstructionSet();
	}
	public static void addInstruction(String opcode, ArrayList<String> operands) {
		Instruction i = new Instruction(opcode);
		i.setOperands(operands);
		code.addInstruction(i);
	}
	public static InstructionDecoder createInstructionDecoderInstance() {
		InstructionDecoder idec = new InstructionDecoder(code);
		return idec;
	}
	
}