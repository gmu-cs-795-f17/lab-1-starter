package edu.gmu.cs795.lab1.inst.part1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class PartOneCV extends ClassVisitor {
	public PartOneCV(ClassVisitor cv) {
		super(ASM5, cv);
	}
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		mv = new PartOneMV(mv, name);
		return mv;
	}
}
