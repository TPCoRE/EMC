package tpc.mc.emc.tech;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;
import java.util.WeakHashMap;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * Tech, {@link Stepable} Factory, notice that each one is the only one, each of them has a different {@link UUID}
 * */
public final class Technique implements Serializable {
	
	/**
	 * A tech that do nothing
	 * */
	public static final Technique NOP = new Technique(null);
	
	/**
	 * Create a tech with given {@link IAttribute}, passing null means create a {@link #NOP}
	 * */
	public Technique(IAttribute attri) {
		this.identifier = (this.attribute = attri) == null ? new UUID(0, 0) : UUID.randomUUID();
	}
	
	/**
	 * Get a new one, May return {@link Stepable#NOP NOP}
	 * */
	public Stepable tack(IOption opt) {
		if(this.attribute == null) return null;
		else return this.attribute.tack(opt);
	}
	
	/**
	 * Get the information of the {@link Technique}, return null means the {@link Locale} not supported
	 * */
	public Iterable<String> info(Locale loc) {
		if(this.attribute == null) return null;
		else return this.attribute.info(loc);
	}
	
	/**
	 * Get the identifier of the {@link Technique}
	 * */
	public UUID identifier() {
		return this.identifier;
	}
	
	/**
	 * If the two are the same
	 * */
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(!obj.getClass().equals(Technique.class)) return false;
		
		return this.identifier.equals(((Technique) obj).identifier);
	}
	
	/**
	 * Get the hash code of the {@link Technique}
	 * */
	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}
	
	private final UUID identifier;
	private final IAttribute attribute;
	
	private static final long serialVersionUID = 1L;
	private static final WeakHashMap<Technique, Technique> INTERNER = new WeakHashMap<>();
	
	/**
	 * Override, readResolve, avoid read twice
	 * */
	private Object readResolve() {
		synchronized(INTERNER) {
			Technique tmp = INTERNER.get(this);
			if(tmp == null) INTERNER.put(tmp = this, this);
			return tmp;
		}
	}
}
