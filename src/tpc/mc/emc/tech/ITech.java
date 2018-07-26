package tpc.mc.emc.tech;

import java.util.HashMap;
import java.util.UUID;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * Tech
 * */
public abstract class ITech implements Cloneable, Comparable<ITech> {
	
	/**
	 * Get a tech progress with the given option
	 * */
	public abstract Stepable tack(IOption opt);
	
	/**
	 * Check if the two are same
	 * */
	@Override
	public final boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(!(obj instanceof ITech)) return false;
		
		return this.CACHE.equals(((ITech) obj).CACHE);
	}
	
	/**
	 * The hash value of the tech
	 * */
	@Override
	public final int hashCode() {
		return this.CACHE.hashCode();
	}
	
	@Override
	public final int compareTo(ITech other) {
		assert(other != null);
		
		return this.CACHE.compareTo(other.CACHE);
	}
	
	@Override
	public final ITech clone() {
		return this;
	}
	
	/**
	 * Get the id of the tech
	 * */
	public final UUID identifier() {
		return this.CACHE;
	}
	
	/**
	 * Get from EMPOWERED
	 * */
	public static final ITech deal(UUID id) {
		assert(id != null);
		
		return EMPOWERED.getOrDefault(id, NOP);
	}
	
	/**
	 * A tech that do nothing
	 * */
	public static final ITech NOP = new ITech() {
		
		@Override
		public Stepable tack(IOption opt) {
			return null;
		}
	};
	
	/**
	 * Truly Get
	 * */
	private final UUID identifier0() {
		char[] arr = this.getClass().getName().toCharArray();
		byte[] arr0 = new byte[arr.length * 2];
		
		for(int i = 0, l = arr.length; i < l; ++i) {
			char c = arr[i];
			
			arr0[i] = (byte) (c & 0xFF);
			arr0[i + 1] = (byte) (c >>> 4);
		}
		
		return UUID.nameUUIDFromBytes(arr0);
	}
	
	/**
	 * Internal Init
	 * */
	{
		synchronized(EMPOWERED) {
			final UUID cache = this.CACHE = this.identifier0();
			
			if(EMPOWERED.containsKey(cache)) {
				EMPOWERED.remove(cache);
				
				throw new IllegalStateException("THE TECH HAS ALREADY EMPOWERED!");
			} else EMPOWERED.put(cache, this);
		}
	}
	
	private final UUID CACHE;
	private static final HashMap<UUID, ITech> EMPOWERED = new HashMap<>();
}
