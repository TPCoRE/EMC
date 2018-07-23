package tpc.mc.emc.tech;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tech Board, notice that it is thread-unsafe
 * */
public final class Board implements Cloneable {
	
	private boolean peeker;
	private Pool[] quick = new Pool[3];
	private Set<Pool> status = new HashSet<>();
	
	/**
	 * Create an instance with teches
	 * */
	public Board(Pool... avails) {
		Set<Pool> status = this.status;
		
		//INIT
		for(int i = 0, l = avails.length; i < l; ++i) {
			status.add(avails[i]);
		}
	}
	
	/**
	 * Get the tech that instead in player's head, if there is nothing, it will return null
	 * */
	public final Pool headtech() {
		return this.quick[0];
	}
	
	/**
	 * Get the tech that instead in player's body, if there is nothing, it will return null
	 * */
	public final Pool bodytech() {
		return this.quick[1];
	}
	
	/**
	 * Get the tech that instead in player's feet, if there is nothing, it will return null
	 * */
	public final Pool feettech() {
		return this.quick[2];
	}
	
	/**
	 * Set the new, return the old
	 * */
	public final Pool headtech(Pool tech) {
		Pool old = this.check0().quick[0];
		this.quick[0] = tech;
		
		return old;
	}
	
	/**
	 * Set the new, return the old
	 * */
	public final Pool bodytech(Pool tech) {
		Pool old = this.check0().quick[1];
		this.quick[1] = tech;
		
		return old;
	}
	
	/**
	 * Set the new, return the old
	 * */
	public final Pool feettech(Pool tech) {
		Pool old = this.check0().quick[2];
		this.quick[2] = tech;
		
		return old;
	}
	
	/**
	 * Whether a tech is available
	 * */
	public final boolean available(Pool tech) {
		assert(tech != null);
		
		return this.status.contains(tech);
	}
	
	/**
	 * Set the available status of the given tech, return the old
	 * */
	public final boolean toggle(Pool tech, boolean available) {
		if(tech == null) throw new IllegalArgumentException("Null tech!");
		boolean old = this.status.contains(tech);
		
		if(available) this.status.add(tech);
		else this.status.remove(tech);
		
		return old;
	}
	
	/**
	 * Change the available status of the given tech, return the old, See {@link #toggle(Pool, boolean)}
	 * */
	public final boolean toggle(Pool tech) {
		return this.toggle(tech, !this.status.contains(tech));
	}
	
	/**
	 * Change the current data into the given data, return itself, notice that it will clean the peeker status
	 * */
	public final Board accept(Board newval) {
		assert(newval != null);
		
		this.quick = newval.quick.clone();
		this.status = new HashSet<>(newval.status);
		this.peeker = false;
		
		return this;
	}
	
	/**
	 * Have a look, return the given peeker, you can't change the data or it will throw exception, but you can look through it
	 * */
	public final Board peek(Board peeker) {
		assert(peeker != null);
		
		peeker.peeker = true;
		peeker.quick = this.quick;
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
	 * Check the peeker status, if true, throw exception
	 * */
	private final Board check0() {
		if(this.peeker) throw new IllegalStateException("Peeker Status!");
		
		return this;
	}
}
