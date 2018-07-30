package tpc.mc.emc.tech;

import java.security.SecureRandom;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * Basic Tech, notice that if a {@link Class#isPrimitive()} field in the ITech class has a modifier 'final' and not 'transient' will be store in the disk and flush to network, 
 * MethodName+MethodDesc changed, field type, field name changed, super class changed will cause serialize failed, then your tech may be {@link #NOP}
 * */
public abstract class ITech {
	
	private static final SecureRandom RNG = new SecureRandom();
	
	/**
	 * A tech that do nothing
	 * */
	public static final ITech NOP = new ITech(null) {
		
		@Override
		public Stepable tack(IOption opt) {
			return null;
		}
	};
	
	/**
	 * Create a tech, notice that each one is the only one
	 * */
	public ITech() {
		synchronized(RNG) {
			long id;
			
			while((id = RNG.nextLong()) == 0);
			
			this.identifier = id;
		}
	}
	
	/**
	 * Internal Constructor
	 * */
	private ITech(Object reserved) {
		this.identifier = 0;
	}
	
	/**
	 * Get a tech progress with the given option
	 * */
	public abstract Stepable tack(IOption opt);
	
	/**
	 * Get the identifier of the tech
	 * */
	public final long identifier() {
		return this.identifier;
	}
	
	/**
	 * Whether the two are the same
	 * */
	@Override
	public final boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(!(obj instanceof ITech)) return false;
		
		return this.identifier == ((ITech) obj).identifier;
	}
	
	/**
	 * Get the hashcode of the tech
	 * */
	@Override
	public final int hashCode() {
		long id = this.identifier;
		
		return (int) (id ^ (id >>> 32));
	}
	
	/**
	 * The id of the tech, notice that the id of NOP is zero
	 * */
	private final long identifier;
}
