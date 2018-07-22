package tpc.mc.emc.tech;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tech Board, notice that it is thread-unsafe
 * */
public final class Board implements Cloneable {
	
	private Pool head;
	private Pool body;
	private Pool feet;
	
	private boolean peeker;
	private Set<Pool> status = new HashSet<>();
	
	public Board() {
		status.add(Pool.DOUBLEJUMP); //JUST FOR ALPHA VERSION
		
	}
	
	/**
	 * Get the tech that instead in player's head, if there is nothing, it will return null
	 * */
	public final Pool headtech() {
		return this.status.contains(this.head) ? this.head : null;
	}
	
	/**
	 * Get the tech that instead in player's body, if there is nothing, it will return null
	 * */
	public final Pool bodytech() {
		return this.status.contains(this.body) ? this.body : null;
	}
	
	/**
	 * Get the tech that instead in player's feet, if there is nothing, it will return null
	 * */
	public final Pool feettech() {
		return this.status.contains(this.feet) ? this.feet : null;
	}
	
	/**
	 * Set the new, return the old
	 * */
	public final Pool headtech(Pool tech) {
		assert(this.status.contains(tech));
		assert(!this.peeker);
		
		Pool old = this.head;
		this.head = tech;
		
		return old;
	}
	
	/**
	 * Set the new, return the old
	 * */
	public final Pool bodytech(Pool tech) {
		assert(this.status.contains(tech));
		assert(!this.peeker);
		
		Pool old = this.body;
		this.body = tech;
		
		return old;
	}
	
	/**
	 * Set the new, return the old
	 * */
	public final Pool feettech(Pool tech) {
		assert(this.status.contains(tech));
		assert(!this.peeker);
		
		Pool old = this.feet;
		this.feet = tech;
		
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
		assert(tech != null);
		
		return available ? this.status.add(tech) : this.status.remove(tech);
	}
	
	/**
	 * Change the available status of the given tech, return the old, See {@link #toggle(Pool, boolean)}
	 * */
	public final boolean toggle(Pool tech) {
		return this.toggle(tech, !this.status.contains(tech));
	}
	
	/**
	 * Get all available teches, it is a view
	 * */
	public final Set<Pool> availiables() {
		return Collections.unmodifiableSet(this.status);
	}
	
	/**
	 * Change the current data into the given data, return 'this'
	 * */
	public final Board accept(Board newval) {
		assert(newval != null);
		
		this.head = newval.head;
		this.body = newval.body;
		this.feet = newval.feet;
		
		this.status = new HashSet<>(newval.status);
		this.peeker = false;
		
		return this;
	}
	
	/**
	 * Have a look, See {@link #peek()}, return the given {@link Board}, you can't change the status
	 * */
	public final Board peek(Board peeker) {
		assert(peeker != null);
		
		peeker.head = this.head;
		peeker.body = this.body;
		peeker.feet = this.feet;
		
		peeker.status = Collections.unmodifiableSet(this.status);
		peeker.peeker = true;
		
		return peeker;
	}
	
	@Override
	public final Board clone() {
		return new Board().accept(this);
	}
}
