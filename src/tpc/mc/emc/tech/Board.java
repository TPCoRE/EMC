package tpc.mc.emc.tech;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Tech Board, notice that it is thread-unsafe
 * */
public final class Board implements Cloneable, Iterable<ITech> {
	
	private Set<ITech> status = new HashSet<>();
	
	/**
	 * Create an instance with teches
	 * */
	public Board(ITech... avails) {
		Set<ITech> status = this.status;
		
		//INIT
		for(int i = 0, l = avails.length; i < l; ++i) {
			ITech avail = avails[i];
			
			Objects.requireNonNull(avail);
			status.add(avail);
		}
	}
	
	/**
	 * Whether a tech is available
	 * */
	public final boolean available(ITech tech) {
		assert(tech != null);
		
		return this.status.contains(tech);
	}
	
	/**
	 * Set the available status of the given tech, return the old
	 * */
	public final boolean toggle(ITech tech, boolean available) {
		Objects.requireNonNull(tech);
		boolean old = this.status.contains(tech);
		
		if(available) this.status.add(tech);
		else this.status.remove(tech);
		
		return old;
	}
	
	/**
	 * Change the available status of the given tech, return the old, See {@link #toggle(ITech, boolean)}
	 * */
	public final boolean toggle(ITech tech) {
		return this.toggle(tech, !this.status.contains(tech));
	}
	
	/**
	 * Change the current data into the given data, return itself, notice that it will clean the peeker status
	 * */
	public final Board accept(Board newval) {
		assert(newval != null);
		
		this.status = new HashSet<>(newval.status);
		
		return this;
	}
	
	/**
	 * Have a look, return the given peeker, you can't change the data or it will throw exception, but you can look through it
	 * */
	public final Board peek(Board peeker) {
		assert(peeker != null);
		
		peeker.status = Collections.unmodifiableSet(this.status);
		
		return peeker;
	}
	
	/**
	 * Get a copy
	 * */
	@Override
	public final Board clone() {
		return new Board().accept(this);
	}
	
	/**
	 * Peek all available teches
	 * */
	@Override
	public final Iterator<ITech> iterator() {
		return Collections.unmodifiableSet(this.status).iterator();
	}
}
