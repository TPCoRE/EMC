package tpc.mc.emc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.src.AbstractClientPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.WorldServer;
import tpc.mc.emc.bodyskill.Pool;

public final class EMC$Util {
	
	private static final Object LOCK0 = new Object();
	private static final Object LOCK1 = new Object();
	
	/**
	 * Add a new element
	 * */
	public static final <T> T[] concat(T[] a, T b) {
		T[] result = Arrays.copyOf(a, a.length + 1);
		result[a.length] = b;
		return result;
	}
	
	/**
	 * Shutdown
	 * */
	public static final void exit(int status) {
		synchronized(LOCK0) {
			try {
				try {
					Class.forName("net.minecraft.client.Minecraft").getMethod("shutdownMinecraftApplet").invoke(null);
				} catch(Throwable e) {}
				
				System.exit(status);
			} catch(Throwable e) {
				try {
					Method shutdown = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", int.class);
					shutdown.setAccessible(true);
					shutdown.invoke(null, status);
				} catch(Throwable ie) {
					ie.addSuppressed(e);
					throw new RuntimeException("No idea!", ie);
				}
			}
		}
	}
	
	/**
	 * Animation, could be invoke by client, client-model, server
	 * */
	public static final void animation(Object obj, Object model) {
		synchronized(LOCK1) {
			if(!(obj instanceof EntityPlayer)) return;
			EntityPlayer player = (EntityPlayer) obj;
			
			//call body skill support
			tpc.mc.emc.bodyskill.Pool bodyskill = EMC$Util.bodyskill(player);
			if(bodyskill != null) { //check start act(client-model only), and acting in both side
				if(model != null && activetime((AbstractClientPlayer) player) == 0) bodyskill.activate(player, model);
				
				bodyskill.activating(player, model, player.worldObj.isRemote ? EMC$Util.activetime((AbstractClientPlayer) player, model != null) : -1);
			}
			
			//client, process the skill that needs to invoke activated
			if(player.worldObj.isRemote) {
				Iterator<Object[]> olds = needsED((AbstractClientPlayer) player).iterator();
				AbstractClientPlayer cp = (AbstractClientPlayer) player;
				
				while(olds.hasNext()) {
					Object[] entry = olds.next();
					
					if(model == null && !(boolean) entry[0]) {
						((tpc.mc.emc.bodyskill.Pool) entry[2]).activated(cp, null);
						entry[0] = true;
					} if(model != null && !(boolean) entry[1]) {
						((tpc.mc.emc.bodyskill.Pool) entry[2]).activated(cp, model);
						entry[1] = true;
					}
					
					if((boolean) entry[0] && (boolean) entry[1]) olds.remove();
				}
			}
			
			//process the used skill both side
			Iterator<tpc.mc.emc.bodyskill.Pool> olds = olds(player).iterator();
			while(olds.hasNext()) {
				if(olds.next().monitor(player, model)) olds.remove();
			}
			
			//call item support
		}
	}
	
	/**
	 * Make the skill join in the queue
	 * */
	static final void activated(EntityPlayer player, tpc.mc.emc.bodyskill.Pool skill) {
		if(skill == null) throw new NullPointerException();
		
		if(player.worldObj.isRemote) needsED((AbstractClientPlayer) player).add(new Object[] { false, false, skill });
		else skill.activated(player, null);
		olds(player).add(skill);
	}
	
	/**
	 * Get the queue of client used skill, for activated the used skill with model
	 * */
	private static final ConcurrentLinkedQueue<Object[]> needsED(AbstractClientPlayer player) {
		try {
			return (ConcurrentLinkedQueue<Object[]>) BODYSKILL_NEEDSED.get(player);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Get the used skill queue
	 * */
	private static final ConcurrentLinkedQueue<tpc.mc.emc.bodyskill.Pool> olds(EntityPlayer player) {
		try {
			return (ConcurrentLinkedQueue<tpc.mc.emc.bodyskill.Pool>) BODYSKILL_OLD.get(player);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Check if a skill is in monitor status
	 * */
	public static final boolean monitor(EntityPlayer player, tpc.mc.emc.bodyskill.Pool skill) {
		if(skill == null) throw new NullPointerException();
		
		try {
			return ((ConcurrentLinkedQueue<tpc.mc.emc.bodyskill.Pool>) BODYSKILL_OLD.get(player)).contains(skill);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Check if a skill is in monitor status for the given times
	 * */
	public static final boolean monitor(EntityPlayer player, tpc.mc.emc.bodyskill.Pool skill, int times) {
		synchronized(LOCK1) {
			Iterator<tpc.mc.emc.bodyskill.Pool> olds = olds(player).iterator();
			int counter = 0;
			
			while(olds.hasNext()) {
				if(olds.next().equals(skill)) counter++;
			}
			
			return counter == times;
		}
	}
	
	private static final int activetime(AbstractClientPlayer player, boolean flag) {
		try {
			int r = BODYSKILL_ACTIVETIME.getInt(player);
			if(!flag) BODYSKILL_ACTIVETIME.setInt(player, ++r);
			
			return r;
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Start to act
	 * */
	public static final void act(EntityPlayer player, tpc.mc.emc.bodyskill.Pool skill) {
		synchronized(LOCK1) {
			if(player.worldObj.isRemote) { //client side, send to server
				if(skill == null && EMC$Util.bodyskill(player) == null) return;
				if(skill == null || (skill != null && skill.able(player))) Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet0(skill));
			} else { //server side, send to tracked client
				tpc.mc.emc.bodyskill.Pool old = EMC$Util.bodyskill(player);
				if(skill == null && old == null) return;
				
				//process the old
				if(old != null) activated(player, old);
				if(skill != null && !skill.able(player)) return; //check legal
				
				//server didn't have activeTime, so no need to reset
				
				//start new
				try { EMC$Util.BODYSKILL.set(player, skill); } catch(Throwable e) { throw new RuntimeException(e); }
				if(skill != null) skill.activate(player, null);
				
				//send to clients
				((WorldServer) player.worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(player, new Packet0(skill));
			}
		}
	}
	
	static final void act0(AbstractClientPlayer player, tpc.mc.emc.bodyskill.Pool skill) {
		synchronized(LOCK1) {
			try {
				//mark old
				Pool old = EMC$Util.bodyskill(player);
				if(old != null) EMC$Util.activated(player, old);
				
				//reset activeTime
				EMC$Util.BODYSKILL_ACTIVETIME.set(player, 0);
				
				//start new
				EMC$Util.BODYSKILL.set(player, skill); //set new
				if(skill != null) skill.activate(player, null); //client act
			} catch(Throwable e) { throw new RuntimeException(e); }
		}
	}
	
	/**
	 * Get bodyskill active time, client update, server decides start and stop
	 * */
	private static final int activetime(AbstractClientPlayer player) {
		try {
			return BODYSKILL_ACTIVETIME.getInt(player);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Get the current active bodyskill of the given player
	 * */
	private static final tpc.mc.emc.bodyskill.Pool bodyskill(EntityPlayer player) {
		try {
			return (tpc.mc.emc.bodyskill.Pool) BODYSKILL.get(player);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Invoke a method safely
	 * */
	public static final Object invokeSafely(Class<?> klass, String methodName, Class<?>[] paramTypes, Object _this, Object... args) {
		try {
			return klass.getMethod(methodName, paramTypes).invoke(_this, args);
		} catch(Throwable e) {}
		
		return null;
	}
	
	/**
	 * Invoke a method safely
	 * */
	public static final Object invokeSafely(Method invoked, Object _this, Object... args) {
		try {
			return invoked.invoke(_this, args);
		} catch(Throwable e) {}
		
		return null;
	}
	
	/**
	 * Get a field safely
	 * */
	public static final Field locate(Class<?> klass, String name) {
		try {
			return klass.getField(name);
		} catch(Throwable e) {}
		
		return null;
	}
	
	/**
	 * Get a field safely
	 * */
	public static final Field locate(String klass, String name) {
		try {
			return Class.forName(klass).getField(name);
		} catch(Throwable e) {}
		
		return null;
	}
	
	/**
	 * Get a field safely
	 * */
	public static final Field locate$(String klass, String name) {
		try {
			Field f = Class.forName(klass).getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch(Throwable e) {}
		
		return null;
	}
	
	static final Field BODYSKILL = EMC$Util.locate(EntityPlayer.class, "emc_active_bodyskill");
	static final Field BODYSKILL_OLD = EMC$Util.locate(EntityPlayer.class, "emc_active_bodyskill_old");
	static final Field BODYSKILL_ACTIVETIME = EMC$Util.locate("net.minecraft.src.AbstractClientPlayer", "emc_active_bodyskill_time");
	static final Field BODYSKILL_NEEDSED = EMC$Util.locate("net.minecraft.src.AbstractClientPlayer", "emc_active_bodyskill_needsED");
	public static final Class<?>[] EMPTY = new Class[0];
}
