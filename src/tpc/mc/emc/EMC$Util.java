package tpc.mc.emc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.src.AbstractClientPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.WorldServer;
import tpc.mc.emc.bodyskill.Packet0;

public final class EMC$Util {
	
	/**
	 * Shutdown
	 * */
	public static final void exit(int status) {
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
	
	/**
	 * Animation
	 * */
	public static final void animation(Object obj, Object model) {
		if(!(obj instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) obj;
		
		//call body skill support
		tpc.mc.emc.bodyskill.Pool bodyskill = EMC$Util.bodyskill((EntityPlayer) player);
		if(bodyskill != null) {
			boolean flag = model != null;
			
			if(flag && activetime((AbstractClientPlayer) player) == 0) bodyskill.activate(player, model);
			bodyskill.activating(player, model, player.worldObj.isRemote ? EMC$Util.activetime((AbstractClientPlayer) player, flag) : -1);
			
			if(flag) {
				ConcurrentLinkedQueue<tpc.mc.emc.bodyskill.Pool> olds = olds((AbstractClientPlayer) player);
				
				while(!olds.isEmpty()) {
					olds.poll().activated(player, model);
				}
			}
		}
		
		//call item support
	}
	
	/**
	 * Get the used skill queue
	 * */
	public static final ConcurrentLinkedQueue<tpc.mc.emc.bodyskill.Pool> olds(AbstractClientPlayer player) {
		try {
			return (ConcurrentLinkedQueue<tpc.mc.emc.bodyskill.Pool>) BODYSKILL_OLD.get(player);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	static final int activetime(AbstractClientPlayer player, boolean flag) {
		try {
			int r = BODYSKILL_ACTIVETIME.getInt(player);
			if(!flag) BODYSKILL_ACTIVETIME.setInt(player, ++r);
			
			return r;
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Start to act, will stop the old skill
	 * */
	public static final void act(EntityPlayer player, tpc.mc.emc.bodyskill.Pool skill) {
		if(player.worldObj.isRemote) { //client side, send to server
			 Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet0(player, skill));
		} else { //server side, send to tracked client
			tpc.mc.emc.bodyskill.Pool old = EMC$Util.bodyskill(player);
			if(old != null) old.activated(player, null);
			try { EMC$Util.BODYSKILL.set(player, skill); } catch(Throwable e) {}
			skill.activate(player, null);
			
			((WorldServer) player.worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(player, new Packet0(player, skill));
		}
	}
	
	/**
	 * Get bodyskill active time, client update, server decides start and stop
	 * */
	public static final int activetime(AbstractClientPlayer player) {
		try {
			return BODYSKILL_ACTIVETIME.getInt(player);
		} catch(Throwable e) { throw new RuntimeException(e); }
	}
	
	/**
	 * Get the current active bodyskill of the given player
	 * */
	public static final tpc.mc.emc.bodyskill.Pool bodyskill(EntityPlayer player) {
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
			return Class.forName(name, false, EMC$Starter.class.getClassLoader()).getField(name);
		} catch(Throwable e) {}
		
		return null;
	}
	
	public static final Field BODYSKILL = EMC$Util.locate(EntityPlayer.class, "emc_active_bodyskill");
	public static final Field BODYSKILL_ACTIVETIME = EMC$Util.locate("net/minecraft/src/AbstractClientPlayer", "emc_active_bodyskill");
	public static final Field BODYSKILL_OLD = EMC$Util.locate("net/minecraft/src/AbstractClientPlayer", "emc_active_bodyskill_old");
	public static final Class<?>[] EMPTY = new Class[0];
}
