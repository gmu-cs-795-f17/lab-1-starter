package edu.gmu.cs795.lab1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class ProfileLogger {
	static HashSet<String> methodsHit;

	/**
	 * This method is called when a method is hit.
	 * The format for 'method' should be:
	 * java/class/name/in/InternalName.methodName(Lthe/method/descriptor;IJKL)V
	 * @param method
	 */
	public static void methodHit(String method) {
		methodsHit.add(method);
	}

	/**
	 * Return the list of methods covered since the last invocation of dump()
	 * @return
	 */
	public static HashSet<String> dump() {
		HashSet<String> ret = methodsHit;
		methodsHit = new HashSet<String>();
		return ret;
	}
}