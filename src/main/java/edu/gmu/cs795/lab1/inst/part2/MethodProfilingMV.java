package edu.gmu.cs795.lab1.inst.part2;

import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

public class MethodProfilingMV extends MethodVisitor {

	public String className;
	public String methodName;
	public String methodDesc;

	public MethodProfilingMV(String className, String methodName, String methodDesc, MethodVisitor mv) {
		super(ASM5, mv);
		this.className = className;
		this.methodName = methodName;
		this.methodDesc = methodDesc;
	}

}
