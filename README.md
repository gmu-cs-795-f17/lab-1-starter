# CS 795 Program Analysis for Software Testing, Lab 1
This in-class lab assignment will get you acquainted with Java bytecode instrumentation and ASM.

This repository contains a starter project for you to build off of. To reduce pain, you should import it into your favorite IDE. It's a maven project; the easiest way to do this in Eclipse is to type `mvn eclipse:eclipse` in this directory, then in Eclipse, go to File->Import->Existing Projects->Select this directory. If you are in Eclipse, you might also appreciate installing the [bytecode outline plugin](http://andrei.gmxhome.de/bytecode/index.html).

This project should work on any platform (Mac, Linux, Windows). If you are on Windows and having trouble making things work, I'd suggest using a linux VM. I've included a configuration for a simple Linux VM in this repository, to use it, install [VirtualBox](https://www.virtualbox.org) and [Vagrant](https://www.vagrantup.com), then type `vagrant up`.

The project is configured with a single class visitor and method visitor. There is a simple `Main` class that will accept as an argument a class file to instrument, then output the instrumented file to the `instrumented` directory (you will use this for part 1 only). There is also a `PreMain` class that will automatically instrument classes as they are loaded into the JVM (to be used with the remainder of this lab). To use the `PreMain`, you need to specify the jar as a `javaagent` on the command line (`java -javaagent:target/cs795-asm-lab1-0.0.1-SNAPSHOT.jar ....` after compiling with `mvn package`). The project is ALSO configured with some simple test cases, in the `test` directory. Since these tests are (in maven speak) Integration Tests, they end in the suffix `IT` rather than `Test`.

To run the instrumenter for part 1, you can run the `Main` class in your IDE, or package and run on the command line (`mvn package; java -jar target/cs795-asm-lab1-0.0.1-SNAPSHOT.jar`).

## Getting Started
The first part of this lab will be to experiment with adding new instructions to a class. Take a look at the class `SimpleClass`. Our goal is to make this class also output `Hello, ASM!`, but without changing the java source file itself. For this part of the lab, you'll write your code in `PartOneMV`, and experiment running this instrumenter by calling the `Main` class, passing it the path to the compiled `SimpleClass`.

### Determining where to insert the new code
To get started, we are going to make use of the `ASMifier` utility. This utility will read in a class file, and write out to the console a Java program that uses ASM to generate the exact same class file. 
After compiling the project (`mvn package`), you can run ASMifier like this:
`java -cp target/cs795-asm-lab1-0.0.1-SNAPSHOT.jar org.objectweb.asm.util.ASMifier target/test-classes/SimpleClass.class `
From this output, you should be able to see the body of the `main` method (which is the method we want to insert code into). Looking closer, determine which instruction comes right after the `println` - what we want to do is insert a println that will output `Hello ASM` right *before* that other instruction (alternatively, you could decide to put the printout *after* the println itself).

Whichever instruction you think it should be - go into the `PartOneMV` and override the appropriate `visit*` instruction. For example, if you decide the correct function is `visitCode` then write
`@Override
public void visitCode() {
    super.visitCode();
}`

When you think you have the right place, confirm that you do, by adding a simple, 'no-operation' instruction there, which will not effect the program in any way, but will show up in the bytecode as `NOP`. To do this, add a call to `super.visitInsn(NOP)` at that point.

Now, run the instrumenter. Recall that for this part of the lab, we will be running the `Main` class to instrument the code: it will read in the `SimpleClass.class` file, and write out an instrumented version of it. To run the instrumenter, compile the project (`mvn package`) then run `java -jar target/cs795-asm-lab1-0.0.1-SNAPSHOT.jar target/test-classes/SimpleClass.class`. When this completes, it will result in a new folder, `instrumented`, with a copy of `SimpleClass.class` in it.

After running the instrumenter and `javap` on that newly instrumented file (`javap -verbose instrumented/SimpleClass.class`), the output here should be:

```
0: getstatic     #19                 // Field java/lang/System.out:Ljava/io/PrintStream;
3: ldc           #21                 // String Hello, World!
5: invokevirtual #27                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
8: nop
9: return
```

### Inserting new code
To insert the code to output "Hello, ASM!", use the output from the ASMifier in the previous step as a template, and change the words that are printed. Then, run the instrumenter again. When you are done, the output of the program should be "Hello, World!" followed by "Hello, ASM!" printed exactly once.
You can run the instrumented `SimpleClass` by running:
`java -cp instrumented/ SimpleClass` on the command line.


## Recording methods that are called
Now that you have a very basic grasp of ASM, we're going to increase the complexity somewhat. Now, instead of just printing something to the console, we will record, into a `HashSet`, the list of methods that are called.

You'll find the interface for the recorder in `edu.gmu.cs795.lab1.ProfileLogger`: the `methodHit` method is called from code, as methods are executed; the `dump` method is called from test code, to inspect what methods were called.

You'll also find that there is a test case pre-written for this step, in `edu.gmu.cs795.lab1.test.MethodTraceIT`. When you run `mvn verify` from the command line, it will compile your instrumenter, and then run the test cases (endign it IT), using the `PreMain` to instrument every class file as its loaded. The `PreMain` class is configured to call your `MethodProfilingCV`. For this step, you should not run the `Main` instrumenter, or run `java -jar ...` anything: the whole thing is set up to run automatically with your tests, instrumenting them as necessary.

(Note: when you first run `mvn verify` you'll get some message that a test failed. That's because I wrote a test for you to show you how this step should work. When you haven't implemented it, you will see that the test fails)

Implement `MethodProfilingMV` so that `ProfileLogger.methodHit(Ljava/lang/String;)V` is called whenever a method is entered. Once you get the existing tests to pass, add several more test methods in `MethodTraceIT` to test more complex functionality (like covering methods in different classes, and covering different methods with the same name but different parameters).

Note: this ClassVisitor/MethodVisitor will be called for *every* class that's loaded (except java.* classes). If you add your instrumentation to *every* class, you'll end up with StackOverflow exceptions: for instance, if you call `ProfileLogger.methodHit` at the start of every method, including `ProfileLogger.methodHit`. For simplicity, you should *only* add instrumentation to classes starting with "edu.gmu.cs795.lab1.test" (this should prevent all of these issues).

# Submitting your lab
This lab will be graded on a pass/fail basis: if you try to do it _and you submit it_, you passed! Hence, it's important that you submit it. This will be good practice for Homework 1, too, since you will use the same submission mechanism.

To submit this lab:
Perform all of your work in your lab-1 git repository. Commit and push your assignment. Once you are ready to submit, create a release, tagged "lab-1." Unless you want to submit a different version of your code, leave it at “master” to release the most recent code that you’ve pushed to GitHub. Make sure that your name is specified somewhere in the release notes.

Make sure that your released code includes all of your files and builds properly. You can do this by clicking to download the archive, and inspecting/trying to build it on your own machine/vm. There is no need to submit binaries/jar files (the target/ directory is purposely ignored from git).
