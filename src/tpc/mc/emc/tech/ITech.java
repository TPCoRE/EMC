package tpc.mc.emc.tech;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.WeakHashMap;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * Basic Tech
 * */
public abstract class ITech implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Special Tech, a tech that do nothing
	 * */
	public static final ITech NOP = new ITech() {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public Stepable tack(IOption opt) {
			return null;
		}
	};
	
	/**
	 * Get a tech progress with the given option, return null is allowed
	 * */
	public abstract Stepable tack(IOption opt);
	
	//-----------------------------------------------------------------------------------
	
	/**
	 * Get the id of the tech
	 * */
	public final UUID identifier() {
		return this.identifier;
	}
	
	/**
	 * Get the copy of the attributes, if attributes non-existed, it will return null
	 * */
	public final byte[] attributes() {
		return this.attributes != null ? this.attributes.clone() : null;
	}
	
	/**
	 * Compare if the two are the same
	 * */
	@Override
	public final boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(!(obj instanceof ITech)) return false;
		
		return this.identifier.equals(((ITech) obj).identifier);
	}
	
	/**
	 * Return the hashcode of the tech
	 * */
	@Override
	public final int hashCode() {
		return this.identifier.hashCode();
	}
	
	/**
	 * Create a tech, notice that each tech is the only one, attributes can be null
	 * */
	public ITech(byte[] attributes) {
		this.attributes = attributes == null ? null : attributes.clone();
		this.identifier = UUID.randomUUID();
	}
	
	/**
	 * Internal Constructor, for special tech
	 * */
	private ITech() {
		this.attributes = null;
		this.identifier = new UUID(0, 0);
	}
	
	private UUID identifier;
	private byte[] attributes;
	
	/**
	 * Override serialize method, writeObject
	 * */
	protected final void writeObject(ObjectOutputStream os) throws IOException {
		os.writeLong(this.identifier.getMostSignificantBits());
		os.writeLong(this.identifier.getLeastSignificantBits());
		os.writeInt(this.attributes.length);
		os.write(this.attributes);
	}
	
	/**
	 * Override serialize method, readObject
	 * */
	protected final void readObject(ObjectInputStream is) throws IOException {
		this.identifier = new UUID(is.readLong(), is.readLong());
		this.attributes = new byte[is.readInt()];
		is.read(this.attributes);
	}
	
	/**
	 * Override serialize method, readResolve
	 * */
	protected final Object readResolve() {
		synchronized(INTERNER) {
			ITech tmp = INTERNER.get(this);
			
			//check dup
			if(tmp == null) {
				INTERNER.put(this, this);
				return this;
			}
			
			return tmp;
		}
	}
	
	/**
	 * Read twice?
	 * */
	private static final WeakHashMap<ITech, ITech> INTERNER = new WeakHashMap<>();
}
