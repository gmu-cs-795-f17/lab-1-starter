package edu.gmu.cs795.lab1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import edu.gmu.cs795.lab1.inst.part1.PartOneCV;

public class Main {

	public static void main(String[] args) {
		if(args.length != 1)
			throw new IllegalArgumentException("Expect exactly one argument: name of class to instrument");
		String clazz = args[0];
		try {
			ClassReader cr = new ClassReader(new FileInputStream(clazz));
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassVisitor cv = new PartOneCV(cw);
			cr.accept(cv, 0);
			byte[] instrumentedClass = cw.toByteArray();
			File instDir = new File("instrumented");
			if(!instDir.exists())
				instDir.mkdir();
			String instPath  ="instrumented/"+cr.getClassName()+".class";
			instPath = instPath.replace('/', File.separatorChar); // for windows
			FileOutputStream fos = new FileOutputStream(instPath);
			fos.write(instrumentedClass);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
