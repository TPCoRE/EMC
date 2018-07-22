/**
 * STANDARD FOR EMC'S MINECRAFT PLATFORMS
 */
package tpc.mc.emc.platform.standard;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * The implements of each minecraft platform
 * */
abstract class $Impl {
	
	private static final $Impl STATIC = impl0();
	
	abstract tpc.mc.emc.platform.standard.IMath math();
	abstract byte[] rule(byte[] klass, java.lang.String name);
	
	/**
	 * Get the currently implement
	 * */
	static final $Impl impl() {
		return STATIC;
	}
	
	private static final $Impl impl0() {
		java.lang.String version = tpc.mc.emc.platform.PlatformInfo.version();
		
		try {
			return ($Impl) java.lang.Class.forName($Impl.class.getPackage().getName().concat(".$Impl").concat(version.replace(".", ""))).newInstance();
		} catch(java.lang.Throwable e) {
			assert(false) : "Unspported Minecraft Version: '" + version + "'!";
			
			return null;
		}
	}
}

/**
 * Internal Addition
 * */
abstract class $Context extends tpc.mc.emc.platform.standard.IContext {
	
	protected boolean released;
	protected boolean peek;
	protected tpc.mc.emc.tech.Board content;
	protected tpc.mc.emc.tech.Board backup;
	
	/**
	 * Internal Only
	 * */
	protected synchronized void op() {
		this.peek = true;
	}
}

/**
 * ASM HELPER
 * */
final class $ASM {
	
	/**
	 * toArrayByte
	 * */
	static final byte[] write(ClassNode cn) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		
		return cw.toByteArray();
	}
	
	/**
	 * Create ClassNode from byte[]
	 * */
	static final ClassNode read(byte[] b) {
		ClassReader cr = new ClassReader(b);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		return cn;
	}
	
	/**
	 * Find a special method
	 * */
	static final MethodNode find(String name, String desc, java.util.Iterator<MethodNode> mns) {
		while(mns.hasNext()) {
			MethodNode mn = mns.next();
			
			if((name == null || mn.name.equals(name)) && (desc == null || mn.desc.equals(desc))) return mn;
		}
		
		//no found
		return null;
	}
	
	/**
	 * 批量注入
	 * */
	static final void inject(java.lang.String name, java.lang.String desc, InsnList ns, java.util.Iterator<MethodNode> mns) {
		while(mns.hasNext()) {
			MethodNode mn = mns.next();
			
			if((name == null || mn.name.equals(name)) && (desc == null || mn.desc.equals(desc))) mn.instructions.insert(copy(ns));
		}
	}
	
	/**
	 * 复制一个InsnList
	 * */
	static final InsnList copy(InsnList src) {
		InsnList r = new InsnList();
		java.util.Map<LabelNode, LabelNode> map = new java.util.HashMap<>();
		java.util.Iterator<AbstractInsnNode> iter;
		
		//roll codes and find labels
		iter = src.iterator();
		while(iter.hasNext()) {
			AbstractInsnNode n = iter.next();
			
			if(n.getType() == AbstractInsnNode.LABEL && !map.containsKey(n)) map.put((LabelNode) n, new LabelNode());
		}
		
		//clone
		iter = src.iterator();
		while(iter.hasNext()) r.add(iter.next().clone(map));
		
		return r;
	}
	
	/**
	 * Inject before *
	 * */
	static final void inject(InsnList produce, InsnList accepter, int op) {
		//roll all codes in dest
		for(int i = 0, l = accepter.size(); i < l; ++i) {
			AbstractInsnNode n = accepter.get(i);
			
			if(n.getOpcode() == op) accepter.insertBefore(n, copy(produce)); //find, then inject
		}
	}
}

/**
 * Reflect API Helper
 * */
final class $Ref {
	
	/**
	 * Accessible a object
	 * */
	static final <T extends java.lang.reflect.AccessibleObject> T access(T access) {
		try {
			access.setAccessible(true);
			
			return access;
		} catch(java.lang.Throwable e) {}
		
		return null;
	}
	
	/**
	 * Get a field safely
	 * */
	static final java.lang.reflect.Field located(java.lang.Class<?> klass, java.lang.String name) {
		try {
			java.lang.reflect.Field f;
			
			try {
				f = klass.getField(name);
			} catch(java.lang.Throwable e) {
				f = access(klass.getDeclaredField(name));
			}
			
			return f;
		} catch(java.lang.Throwable e) {}
		
		//safely return
		return null;
	}
	
	/**
	 * Get a field of the given class safely
	 * */
	static final java.lang.reflect.Field located(java.lang.String klass, java.lang.String name) {
		try {
			return located(java.lang.Class.forName(klass), name);
		} catch(java.lang.Throwable e) {}
		
		//safely return
		return null;
	}
	
	/**
	 * Return in the future
	 * */
	static final java.util.concurrent.Callable<java.lang.reflect.Field> ilocate(java.lang.Class<?> klass, java.lang.String name) {
		return new java.util.concurrent.Callable<java.lang.reflect.Field>() {
			
			private java.lang.reflect.Field cache = null;
			
			@Override
			public java.lang.reflect.Field call() throws Exception {
				return cache == null ? cache = $Ref.located(klass, name) : cache;
			}
		};
	}
	
	/**
	 * Return in the future
	 * */
	static final java.util.concurrent.Callable<java.lang.reflect.Field> ilocate(java.lang.String klass, java.lang.String name) {
		return new java.util.concurrent.Callable<java.lang.reflect.Field>() {
			
			private java.lang.reflect.Field cache = null;
			
			@Override
			public java.lang.reflect.Field call() throws Exception {
				return cache == null ? cache = $Ref.located(klass, name) : cache;
			}
		};
	}
}
