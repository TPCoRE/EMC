package tpc.mc.emc.platform.standard;

import tpc.mc.emc.tech.Board;

/**
 * For thread-safe, Tech Board
 * */
public abstract class IContext implements AutoCloseable {
	
	IContext() {}
	
	public abstract boolean released();
	public abstract boolean verify(IOption opt);
	public abstract void accept(Board newval);
	public abstract void export(Board accepter);
	public abstract void peek(Board peeker);
	public abstract void close();
	
	/**
	 * Have a look, See {@link #peek(Board)}
	 * */
	public final Board peek() {
		Board result = new Board();
		this.peek(result);
		
		return result;
	}
	
	/**
	 * Check if it was released, if it has already released, it will throw an exception, return 'this' if succeed
	 * */
	public final IContext check() {
		assert(!this.released());
		
		return this;
	}
	
	/**
	 * Check if it was valid, if it was invalid, it will throw an exception, return 'this' if succeed
	 * */
	public final IContext verifi(IOption opt) {
		assert(this.verify(opt));
		
		return this;
	}
	
	/**
	 * See {@link #export(Board)}, it return a new one
	 * */
	public final Board export() {
		Board result = new Board();
		this.export(result);
		
		return result;
	}
	
	/**
	 * See {@link #export(Board)}, return the given accepter
	 * */
	public final Board iexport(Board accepter) {
		this.export(accepter);
		
		return accepter;
	}
	
	/**
	 * See {@link #peek(Board)}, return the given peeker
	 * */
	public final Board ipeek(Board peeker) {
		this.peek(peeker);
		
		return peeker;
	}
}
