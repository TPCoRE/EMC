package tpc.mc.emc.platform.standard;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import sun.reflect.Reflection;
import tpc.mc.emc.platform.PlatformInfo;

/**
 * Invoke it is your own risk, it is the thing that out of the standard
 * */
public final class $Unsafe {
	
	/**
	 * Boost method
	 * */
	public static final void premain(final String arg, Instrumentation inst) throws Throwable {
		if(!Reflection.getCallerClass(2).getName().equals("sun.instrument.InstrumentationImpl")) throw new IllegalAccessError();
		
		//inline classruler
		inst.addTransformer(new ClassFileTransformer() {
			
			@Override
			public byte[] transform(ClassLoader arg0, String className, Class<?> arg2, ProtectionDomain arg3, byte[] klass) throws IllegalClassFormatException {
				try {
					return inline(klass, className, arg);
				} catch(Throwable e) {
					e.printStackTrace();
					exit(1);
					
					//no need
					return null;
				}
			}
		}, true);
		
		//roll back inlined
		inst.retransformClasses(PlatformInfo.class);
		
		//impl classruler
		final $Impl impl = $Impl.impl();
		inst.addTransformer(new ClassFileTransformer() {
			
			@Override
			public byte[] transform(ClassLoader arg0, String className, Class<?> arg2, ProtectionDomain arg3, byte[] klass) throws IllegalClassFormatException {
				try {
					return impl.rule(klass, className);
				} catch(Throwable e) {
					e.printStackTrace();
					exit(1);
					
					//no need
					return null;
				}
			}
		}, true);
		
		//roll back
		Class[] cs = inst.getAllLoadedClasses();
		for(Class c : cs) {
			if(inst.isModifiableClass(c)) inst.retransformClasses(c);
		}
	}
	
	/**
	 * Special Inline method
	 * */
	private static final byte[] inline(byte[] b, String className, String arg) {
		if(className == null) return b;
		ClassNode cn = null;
		
		switch(className) {
		case "tpc/mc/emc/platform/PlatformInfo": //Inline0: support_user
			if(arg == null) break;
			
			//prepare
			cn = $ASM.read(b);
			MethodNode mn = $ASM.find("support_user", "()Ljava/lang/String;", cn.methods.iterator());
			InsnList ns = mn.instructions;
			
			//coding
			ns.clear();
			ns.add(new LdcInsnNode(arg));
			ns.add(new InsnNode(Opcodes.ARETURN));
			
			//done
			break;
		}
		
		return cn == null ? b : $ASM.write(cn);
	}
	
	/**
	 * Shutdown
	 * */
	private static final synchronized void exit(int status) {
		try {
			System.exit(status);
		} catch(Throwable e) {
			try {
				Method shutdown = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", int.class);
				shutdown.setAccessible(true);
				shutdown.invoke(null, status);
			} catch(Throwable ie) {
				ie.addSuppressed(e);
				
				assert(false) : "No idea!";
			}
		}
	}
}
