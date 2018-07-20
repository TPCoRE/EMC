package tpc.mc.emc;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.base.Predicate;

final class EMC$CodeRuler {
	
	static final byte[] codefix(byte[] classbuf, String name) {
		if(name == null || classbuf == null) return null;
		ClassNode cn = null;
		
		switch(name) {
		case "net/minecraft/src/ModelBiped": //skill animation(model) hack
			cn = EMC$CodeRuler.read(classbuf);
			MethodNode mn0 = EMC$CodeRuler.find("setRotationAngles", "(FFFFFFLnet/minecraft/src/Entity;)V", cn.methods.iterator());
			
			//check&prepare
			if(mn0 == null) throw new NoSuchMethodError("net.minecraft.src.ModelBiped.setRotationAngles(FFFFFFLnet/minecraft/src/Entity;)V wasn't be Found!");
			InsnList ns = new InsnList();
			
			//coding
			ns.add(new VarInsnNode(Opcodes.ALOAD, 7));
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tpc/mc/emc/EMC$Util", "animation", "(Ljava/lang/Object;Ljava/lang/Object;)V"));
			
			//inject
			EMC$CodeRuler.inject(ns, mn0.instructions, new $P0(Opcodes.RETURN));
			break;
		case "net/minecraft/src/EntityPlayer": //body skill common hack
			cn = EMC$CodeRuler.read(classbuf);
			mn0 = EMC$CodeRuler.find("onUpdate", "()V", cn.methods.iterator());
			
			//check&prepare
			if(mn0 == null) throw new NoSuchMethodError("net.minecraft.src.EntityPlayer.onUpdate()V wasn't be Found!");
			ns = new InsnList();
			
			//coding
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new InsnNode(Opcodes.ACONST_NULL));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tpc/mc/emc/EMC$Util", "animation", "(Ljava/lang/Object;Ljava/lang/Object;)V"));
			
			//inject
			mn0.instructions.insert(ns);
			cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "emc_active_bodyskill", "Ltpc/mc/emc/bodyskill/Pool;", null, null));
			break;
		case "net/minecraft/src/AbstractClientPlayer": //body skill client hack
			cn = EMC$CodeRuler.read(classbuf);
			Iterator<MethodNode> iter = cn.methods.iterator();
			boolean flag0 = false;
			
			while(iter.hasNext()) {
				mn0 = iter.next();
				
				if(mn0.name.equals("<init>")) {
					ns = new InsnList();
					
					//coding
					ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
					ns.add(new TypeInsnNode(Opcodes.NEW, "java/util/concurrent/ConcurrentLinkedQueue"));
					ns.add(new InsnNode(Opcodes.DUP));
					ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/concurrent/ConcurrentLinkedQueue", "<init>", "()V"));
					ns.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/src/AbstractClientPlayer", "emc_active_bodyskill_old", "Ljava/util/concurrent/ConcurrentLinkedQueue;"));
					
					EMC$CodeRuler.inject(ns, mn0.instructions, new $P0(Opcodes.RETURN));
					flag0 = true;
				}
			}
			
			//no found!
			if(!flag0) throw new NoSuchMethodError("net.minecraft.src.AbstractClientPlayer.<init>* wasn't be Found!");
			
			cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "emc_active_bodyskill_time", "I", null, new Integer(0)));
			cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "emc_active_bodyskill_old", "Ljava/util/concurrent/ConcurrentLinkedQueue;", null, null));
			break;
		case "net/minecraft/src/Packet": //register packet
			cn = EMC$CodeRuler.read(classbuf);
			mn0 = EMC$CodeRuler.find("<clinit>", "()V", cn.methods.iterator());
			
			//check&prepare
			if(mn0 == null) throw new NoSuchMethodError("net.minecraft.src.Packet.<clinit>()V wasn't be Found!");
			ns = new InsnList();
			
			//coding
			ns.add(new LdcInsnNode(new Integer(-1)));
			ns.add(new InsnNode(Opcodes.ICONST_1));
			ns.add(new InsnNode(Opcodes.ICONST_1));
			ns.add(new LdcInsnNode(Type.getType("Ltpc/mc/emc/bodyskill/Packet0;")));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/src/Packet", "addIdClassMapping", "(IZZLjava/lang/Class;)V"));
			
			//inject
			EMC$CodeRuler.inject(ns, mn0.instructions, new $P0(Opcodes.RETURN));
			break;
		case "net/minecraft/src/GameSettings": //register keybinding
			cn = EMC$CodeRuler.read(classbuf);
			
			
			break;
		case "net/minecraft/src/Minecarft": //check keypressed
			cn = EMC$CodeRuler.read(classbuf);
			
			
			break;
		}
		
		//null表示没修改
		return cn == null ? null : EMC$CodeRuler.write(cn);
	}
	
	/**
	 * toArrayByte
	 * */
	static final byte[] write(ClassNode cn) {
		ClassWriter cw = new ClassWriter(0);
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
	 * 搜索特殊方法
	 * */
	static final MethodNode find(String name, String desc, Iterator<MethodNode> mns) {
		while(mns.hasNext()) {
			MethodNode mn = mns.next();
			
			if(mn.name.equals(name) && mn.desc.equals(desc)) return mn;
		}
		
		//no found
		return null;
	}
	
	/**
	 * 在dest中的特殊位置插入injected
	 * */
	static final void inject(InsnList injected, InsnList dest, Predicate<AbstractInsnNode> proxy) {
		
		//roll all codes in dest
		for(int i = 0, l = dest.size(); i < l; ++i) {
			AbstractInsnNode n = dest.get(i);
			
			if(proxy.apply(n)) dest.insertBefore(n, copy(injected)); //find, then inject
		}
	}
	
	/**
	 * 复制一个InsnList
	 * */
	static final InsnList copy(InsnList src) {
		InsnList r = new InsnList();
		Map<LabelNode, LabelNode> map = new HashMap<>();
		Iterator<AbstractInsnNode> iter;
		
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
	 * Internal Only
	 * */
	private static class $P0 implements Predicate<AbstractInsnNode> {
		
		private final int OP;
		private $P0(int op) { OP = op; }
		
		@Override
		public boolean apply(AbstractInsnNode n) {
			return n.getOpcode() == OP;
		}
	}
}
