package tpc.mc.emc.platform.standard;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.src.*;
import tpc.mc.emc.Deque;
import tpc.mc.emc.Stepable;
import tpc.mc.emc.tech.Board;
import tpc.mc.emc.tech.Pool;

/**
 * For Minecraft 1.6.4, Invoke it is your own risk, but other mods are allow to visit
 * */
public final class $Impl164 extends $Impl {
	
	/**
	 * Internal Invoke, update steps
	 * */
	public static final void $I0(EntityPlayer player, Object model) throws Throwable {
		Deque steps = model != null ? (Deque<Object>) MODELSTEP.call().get(player) : (Deque<Object>) COMMOSTEP.call().get(player);
		
		//lock
		synchronized(steps) {
			Stepable mark = null;
			Object head;
			
			//roll
			while((head = steps.head()) != mark) {
				if(head instanceof Pool) head = ((Pool) head).take(new $Option164(player, model));
				Stepable nextstep = ((Stepable) head).next();
				
				steps.head(null);
				if(nextstep != null) {
					if(mark == null) mark = nextstep;
					
					steps.tail(nextstep);
				}
			}
		}
	}
	
	/**
	 * Internal Invoke, update keyboard/mouse input status
	 * */
	public static final void $1(MovementInput input) throws Throwable {
		if(org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_F)) {
			try(IContext context = new $Context164($player())) {
				//TODO
			}
		}
	}
	
	/**I DON'T KNOW WHAY, IF I JUST USE Minecraft.getMinecraft().thePlayer, AbstractClientPlayer will be load in a worry time, IT WILL CAUSE RETRANSFORM ERROR!*/
	private static final EntityPlayer $player() throws Throwable {
		return (EntityPlayer) $Ref.located(Minecraft.class, "thePlayer").get(Minecraft.getMinecraft());
	}
	
	/**
	 * 164 MathHelper support
	 * */
	@Override
	IMath math() {
		return new IMath() {
			
			@Override
			public float sin(float i) {
				return MathHelper.sin(i);
			}
			
			@Override
			public float cos(float i) {
				return MathHelper.cos(i);
			}
		};
	}
	
	/**
	 * 164 CodeRuler for implement EMC's standard functions
	 * */
	@Override
	byte[] rule(byte[] klass, String name) {
		if(name == null) return null;
		ClassNode cn = null;
		
		//special handle
		switch(name) {
		case "net/minecraft/src/ModelBiped": //这里调用client-model的stepable
			cn = $ASM.read(klass);
			MethodNode mn = $ASM.find("setRotationAngles", "(FFFFFFLnet/minecraft/src/Entity;)V", cn.methods.iterator());
			
			//check&prepare
			assert(mn != null);
			InsnList ns = new InsnList();
			
			//coding, 调用stepable
			ns.add(new VarInsnNode(Opcodes.ALOAD, 7));
			ns.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/src/EntityPlayer"));
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tpc/mc/emc/platform/standard/$Impl164", "$I0", "(Lnet/minecraft/src/EntityPlayer;Ljava/lang/Object;)V"));
			
			//inject
			$ASM.inject(ns, mn.instructions, Opcodes.RETURN);
			break;
		case "net/minecraft/src/EntityPlayer": //这里储存，调用client|server的stepable，添加context lock, tech board
			cn = $ASM.read(klass);
			mn = $ASM.find("onUpdate", "()V", cn.methods.iterator());
			
			//check&prepare
			assert(mn != null);
			ns = new InsnList();
			
			//coding, 调用stepable
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new InsnNode(Opcodes.ACONST_NULL));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tpc/mc/emc/platform/standard/$Impl164", "$I0", "(Lnet/minecraft/src/EntityPlayer;Ljava/lang/Object;)V"));
			mn.instructions.insert(ns); //inject
			
			//coding <init>* for new field
			ns = new InsnList();
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new InsnNode(Opcodes.DUP));
			//for common stepable
			ns.add(new TypeInsnNode(Opcodes.NEW, "tpc/mc/emc/Deque"));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "tpc/mc/emc/Deque", "<init>", "()V"));
			ns.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/src/EntityPlayer", "EMC_COMMOSTEP", "Ltpc/mc/emc/Deque;"));
			//for context lock
			ns.add(new TypeInsnNode(Opcodes.NEW, "java/util/concurrent/atomic/AtomicBoolean"));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new InsnNode(Opcodes.ICONST_0));
			ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/concurrent/atomic/AtomicBoolean", "<init>", "(Z)V"));
			ns.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/src/EntityPlayer", "EMC_CONTELOCK", "Ljava/util/concurrent/atomic/AtomicBoolean;"));
			//for techboard
			ns.add(new TypeInsnNode(Opcodes.NEW, "tpc/mc/emc/tech/Board"));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "tpc/mc/emc/tech/Board", "<init>", "()V"));
			ns.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/src/EntityPlayer", "EMC_TECHBOARD", "Ltpc/mc/emc/tech/Board;"));
			
			
			
			//inject, common stepable, context lock, techboard
			$ASM.inject("<init>", null, ns, cn.methods.iterator());
			cn.fields.add(new FieldNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "EMC_COMMOSTEP", "Ltpc/mc/emc/Deque;", null, null));
			cn.fields.add(new FieldNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "EMC_CONTELOCK", "Ljava/util/concurrent/atomic/AtomicBoolean;", null, null));
			cn.fields.add(new FieldNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "EMC_TECHBOARD", "Ltpc/mc/emc/tech/Board;", null, null));
			break;
		case "net/minecraft/src/AbstractClientPlayer": //这里储存client-model的stepable
			cn = $ASM.read(klass);
			
			//coding for model stepable
			ns = new InsnList();
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new TypeInsnNode(Opcodes.NEW, "tpc/mc/emc/Deque"));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "tpc/mc/emc/Deque", "<init>", "()V"));
			ns.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/src/AbstractClientPlayer", "EMC_MODELSTEP", "Ltpc/mc/emc/Deque;"));
			
			//inject, model stepable
			$ASM.inject("<init>", null, ns, cn.methods.iterator());
			cn.fields.add(new FieldNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "EMC_MODELSTEP", "Ltpc/mc/emc/Deque;", null, null));
			break;
		case "net/minecraft/src/Packet": //这里注册client&server之间的通信包
			cn = $ASM.read(klass);
			mn = $ASM.find("<clinit>", "()V", cn.methods.iterator());
			
			//check&prepare
			assert(mn != null);
			ns = new InsnList();
			
			//coding 注册包0
			ns.add(new LdcInsnNode(new Integer(-1)));
			ns.add(new InsnNode(Opcodes.ICONST_1));
			ns.add(new InsnNode(Opcodes.ICONST_1));
			ns.add(new LdcInsnNode(Type.getType("Ltpc/mc/emc/platform/standard/$Impl164$$Packet0_164;")));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/src/Packet", "addIdClassMapping", "(IZZLjava/lang/Class;)V"));
			
			//inject
			$ASM.inject(ns, mn.instructions, Opcodes.RETURN);
			break;
		case "net/minecraft/src/GameSettings": //这里注册头部，身体，腿部科技的绑定按键
			//NOTING TO DO
			break;
		case "net/minecraft/src/MovementInputFromOptions": //这里做按键检测，客户端会给服务端发包
			cn = $ASM.read(klass);
			mn = $ASM.find("updatePlayerMoveState", "()V", cn.methods.iterator());
			
			//check&prepare
			assert(mn != null);
			ns = new InsnList();
			
			//coding hook
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tpc/mc/emc/platform/standard/$Impl164", "$1", "(Lnet/minecraft/src/MovementInput;)V"));
			
			//inject
			$ASM.inject(ns, mn.instructions, Opcodes.RETURN);
			break;
		}
		
		return cn == null ? null : $ASM.write(cn);
	}
	
	private static final Callable<Field> MODELSTEP = $Ref.ilocate("net.minecraft.src.AbstractClientPlayer", "EMC_MODELSTEP");
	private static final Callable<Field> COMMOSTEP = $Ref.ilocate("net.minecraft.src.EntityPlayer", "EMC_COMMOSTEP");
	private static final Callable<Field> CONTELOCK = $Ref.ilocate("net.minecraft.src.EntityPlayer", "EMC_CONTELOCK");
	private static final Callable<Field> TECHBOARD = $Ref.ilocate("net.minecraft.src.EntityPlayer", "EMC_TECHBOARD");
	private static final Board TMP = new Board();
	
	/**
	 * The implement of {@link IContext} in mc1.6.4
	 * */
	private static final class $Context164 extends $Context {
		
		private EntityPlayer valid;
		private $Context164(EntityPlayer player) {
			try {
				//init context
				this.released = false;
				this.peek = true;
				
				//lock content
				AtomicBoolean bool = (AtomicBoolean) CONTELOCK.call().get(player);
				while(!bool.compareAndSet(false, true));
				
				//init content
				this.valid = player;
				this.content = (Board) TECHBOARD.call().get(player);
				this.backup = this.content.clone();
			} catch(java.lang.Throwable e) {
				assert(false) : "Init Context164 Failed!";
			}
		}
		
		@Override
		public final synchronized boolean released() {
			return this.released;
		}
		
		@Override
		public final synchronized boolean verify(IOption opt) {
			if(!(opt instanceof $Option164)) return false;
			
			return (($Option164) opt).player.equals(this.valid);
		}
		
		@Override
		public final synchronized void accept(Board newval) {
			(($Context) this.check()).content.accept(newval);
			
			this.peek = false;
		}
		
		@Override
		public final synchronized void export(Board accepter) {
			assert(accepter != null);
			
			accepter.accept((($Context) this.check()).content);
		}
		
		@Override
		public final synchronized void peek(Board peeker) {
			assert(peeker != null);
			
			(($Context) this.check()).content.peek(peeker);
		}
		
		@Override
		public final synchronized void close() {
			if(this.released) throw new java.lang.IllegalStateException("Already released!");
			
			try {
				//flush status, it will check the differences between the backup and current content, send the disable tech data, able tech data, notice that it will just send the differences
				if(!this.peek) {
					Set<Pool> old = this.backup.availiables();
					Set<Pool> cur = this.content.availiables();
					Pool tech;
					
					//check lost
					Iterator<Pool> iter0 = old.iterator();
					while(iter0.hasNext()) {
						tech = (Pool) iter0.next();
						
						if(!cur.contains(tech)) this.queue0(tech, (byte) 0);
					}
					
					//check get
					iter0 = cur.iterator();
					while(iter0.hasNext()) {
						tech = iter0.next();
						
						if(!old.contains(tech)) this.queue0(tech, (byte) 1);
					}
				}
				
				//released
				((AtomicBoolean) CONTELOCK.call().get(this.valid)).set(false);
				this.released = true;
			} catch(Throwable e) {
				this.released = false;
				assert(false) : e;
			}
		}
		
		/**
		 * Internal Helper, add to send queue
		 * */
		private final void queue0(Pool tech, byte mode) {
			EntityPlayer player = this.valid;
			
			if(player.worldObj.isRemote) { //now is in client
				Minecraft.getMinecraft().getNetHandler().addToSendQueue(new $Packet0_164(tech, null, mode));
			} else { //now is in server
				((WorldServer) player.worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(player, new $Packet0_164(tech, player, mode));
			}
		}
	}
	
	/**
	 * The implement of {@link IOption} in mc1.6.4
	 * */
	public static final class $Option164 extends IOption {
		
		private final Object model;
		private final EntityPlayer player;
		public $Option164(EntityPlayer player, Object model) {
			assert(player != null);
			
			this.player = player;
			this.model = model;
		}
		
		@Override
		public final boolean client() {
			return this.player.worldObj.isRemote;
		}
		
		@Override
		public final boolean server() {
			return !this.client();
		}
		
		@Override
		public final boolean model() {
			return this.model != null;
		}
		
		@Override
		public final void accel(double vx, double vy, double vz) {
			
			//notice that velocity is the ability of client in 1.6.4
			if(this.client()) {
				this.player.addVelocity(vx, vy, vz);
			} else this.player.moveEntity(vx, vy, vz);
		}
		
		@Override
		public final void accel(double v) {
			Vec3 vec = this.player.getLookVec();
			
			//notice that velocity is the ability of client in 1.6.4
			if(this.client()) {
				this.player.addVelocity(vec.xCoord * v, vec.yCoord * v, vec.zCoord * v);
			} else this.player.moveEntity(vec.xCoord * v, vec.yCoord * v, vec.zCoord * v);
		}
		
		@Override
		public final void halt() {
			if(this.client()) this.player.motionX = this.player.motionY = this.player.motionZ = 0; //the same
		}
		
		@Override
		public final void particle(double lx, double ly, double lz, double vx, double vy, double vz) {
			World w = this.player.worldObj;
			AxisAlignedBB bb = player.boundingBox;
			double x = (bb.maxX + bb.minX) * 0.5;
			double y = (bb.maxY + bb.minY) * 0.5;
			double z = (bb.maxZ + bb.minZ) * 0.5;
			
			//config in
			x += lx;
			y += ly;
			z += lz;
			
			if(w.isRemote) {
				w.spawnParticle("enchantmenttable", x, y, z, vx, vy, vz);
			} else assert(false) : "Sorry...";
		}
		
		@Override
		public final int ticks() {
			return this.player.ticksExisted;
		}
		
		@Override
		public final IContext alloc() {
			return new $Context164(this.player);
		}
	}
	
	/**
	 * A internal implement for the communication between client&server
	 * */
	public static final class $Packet0_164 extends net.minecraft.src.Packet {
		
		private byte mode; //0: lost, 1: get, 2: act, 3:get&act
		private int entityID; //server to client only, otherwise is -1
		private Pool tech;
		
		/**
		 * Create an instance
		 * */
		public $Packet0_164(Pool tech, EntityPlayer player, byte mode) {
			assert(tech != null);
			assert(0 <= mode && mode <= 3);
			
			this.tech = tech;
			this.entityID = player == null ? -1 : player.entityId;
			this.mode = mode;
		}
		
		@Override
		public void readPacketData(DataInput var1) throws IOException {
			assert(var1 != null);
			
			this.tech = Pool.values()[var1.readInt()];
			this.entityID = var1.readInt();
			this.mode = var1.readByte();
		}
		
		@Override
		public void writePacketData(DataOutput var1) throws IOException {
			assert(var1 != null);
			
			var1.writeInt(this.tech.ordinal());
			var1.writeInt(this.entityID);
			var1.writeByte(this.mode);
		}
		
		@Override
		public void processPacket(NetHandler var1) {
			assert(var1 != null);
			//server side, process and send to all tracked clients
			//client side, process
			
			byte mode = this.mode;
			Pool tech = this.tech;
			EntityPlayer target = this.entityID == -1 ? ((NetServerHandler) var1).playerEntity : (EntityPlayer) Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID);
			IOption opt = new $Option164(target, null);
			
			//process the tech with given mode
			try(IContext context = opt.alloc()) {
				Board techboard = context.iexport(TMP);
				
				//handle
				if(mode == 0) { //lost the tech
					techboard.toggle(tech, false);
				} else if((mode & 0x1) != 0) { //get
					techboard.toggle(tech, true);
				} else if((mode & 0x2) != 0) { //act
					//add to step queue
					try {
						Deque queue = (Deque<Stepable>) COMMOSTEP.call().get(target);
						queue.head(tech.take(opt));
						
						//client only, for client-model, and the step will be init in client-model with a new opt(the opt contains model)
						if(!var1.isServerHandler()) {
							queue = (Deque<Object>) MODELSTEP.call().get(target);
							queue.head(tech);
						}
					} catch(Throwable e) {
						assert(false);
					}
				} else assert(false);
				
				//flush
				context.accept(techboard);
				(($Context) context).op();
			}
			
			//server only, sennd to all
			if(var1.isServerHandler()) {
				((WorldServer) target.worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(target, new $Packet0_164(tech, target, mode));
			}
		}
		
		@Override
		public int getPacketSize() {
			return 9;
		}
	}
}
