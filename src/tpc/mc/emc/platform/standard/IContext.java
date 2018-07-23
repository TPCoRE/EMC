package tpc.mc.emc.platform.standard;

import java.util.function.Predicate;

import tpc.mc.emc.err.ClosedException;
import tpc.mc.emc.tech.Board;
import tpc.mc.emc.tech.Pool;

/**
 * The player's handler, notice that if the context was closed, the follow operations will cause exception
 * */
public interface IContext extends AutoCloseable {
	
	//---------------------------------Basic------------------------------
	
	/**
	 * Check if the context belongs to the given {@link IOption}
	 * */
	public boolean verify(IOption opt);
	
	/**
	 * Whether the context was closed, it will never throw exception
	 * */
	public boolean released();
	
	/**
	 * Close the current context, dup close will throw exception
	 * */
	public void close();
	
	//---------------------------------Techboard Handler------------------
	
	/**
	 * Just have a look, it will change the given {@link Board} into a peeker, and cover the data in it, as a peeker the {@link Board} can't be change or it will throw exception, notice that the peeker is still available after the context close
	 * */
	public void peek(Board peeker);
	
	/**
	 * Similar to peeker, but the accepter can change data and it will no effect the data in the current context
	 * */
	public void export(Board accepter);
	
	/**
	 * Accept data from the given exporter
	 * */
	public void accept(Board exporter);
	
	//---------------------------------Player Handler---------------------
	
	/**
	 * Whether the player isn't on the ground
	 * */
	public boolean airborne();
	
	/**
	 * Get the tickes that the player existed
	 * */
	public int tickes();
	
	/**
	 * Accel the player with the given velocity
	 * */
	public void accel(double vx, double vy, double vz);
	
	/**
	 * Accel the player forwardly
	 * */
	public void accel(double vl);
	
	/**
	 * Mul the velocity with the given factor, notice that if the factor is zero, the player will stop suddenly
	 * */
	public void scale(double factor);
	
	/**
	 * Spawn some particles
	 * */
	public void particle(double lx, double ly, double lz, double vx, double vy, double vz);
	
	/**
	 * Act the given tech, no matter if it is available
	 * */
	public void act(Pool tech);
	
	/**
	 * Across the acting teches
	 * */
	public boolean check(Predicate<Pool> proxy);
	
	/**
	 * Will be remove in the future, it makes the model a jump action
	 * */
	@Deprecated
	public void jump();
	
	/**
	 * Will be remove in the future, it makes the model a rush action
	 * */
	@Deprecated
	public void rush();
	
	//---------------------------------Helper(Basic)------------------------------
	
	/**
	 * See {@link #verify(IOption)}, return itself
	 * */
	public default IContext iverify(IOption opt) {
		return this.iverify(opt, true);
	}
	
	/**
	 * See {@link #verify(IOption)}, return itself
	 * */
	public default IContext iverify(IOption opt, boolean expect) {
		if(this.verify(opt) != expect) throw new IllegalAccessError();
		
		return this;
	}
	
	/**
	 * Check if the context has closed, if it was closed the method will throw an exception, return itself
	 * */
	public default IContext icheck() {
		if(this.released()) throw new ClosedException("Current Context has already closed!");
		
		return this;
	}
	
	//---------------------------------Helper(Techboard Handler)-------------------
	
	/**
	 * See {@link #peek(Board)}, return the given peeker
	 * */
	public default Board ipeek(Board peeker) {
		this.peek(peeker);
		
		return peeker;
	}
	
	/**
	 * See {@link #peek(Board)}, return a new one
	 * */
	public default Board ipeek() {
		return this.ipeek(new Board());
	}
	
	/**
	 * See {@link #export(Board)}, return the given accepter
	 * */
	public default Board iexport(Board accepter) {
		this.export(accepter);
		
		return accepter;
	}
	
	/**
	 * See {@link #export(Board)}, return a new one
	 * */
	public default Board iexport() {
		return this.iexport(new Board());
	}
	
	/**
	 * See {@link #accept(Board)}, return itself
	 * */
	public default IContext iaccept(Board exporter) {
		this.accept(exporter);
		
		return this;
	}
	
	//---------------------------------Helper(Player Handler)-----------------------
	
	/**
	 * See {@link #accel(double, double, double)}, return itself
	 * */
	public default IContext iaccel(double vx, double vy, double vz) {
		this.accel(vx, vy, vz);
		
		return this;
	}
	
	/**
	 * See {@link #accel(double, double, double)}, the method is the same as xx.accel(vx * vs, vy * vs, vz * vs), return itself
	 * */
	public default IContext iaccel(double vx, double vy, double vz, double vs) {
		return this.iaccel(vx * vs, vy * vs, vz * vs);
	}
	
	/**
	 * See {@link #accel(double)}, return itself
	 * */
	public default IContext iaccel(double vl) {
		this.accel(vl);
		
		return this;
	}
	
	/**
	 * See {@link #scale(double)}, return itself
	 * */
	public default IContext iscale(double factor) {
		this.scale(factor);
		
		return this;
	}
	
	/**
	 * See {@link #scale(double)}, return itself, xx.scale(0)
	 * */
	public default IContext ihalt() {
		return this.iscale(0);
	}
	
	/**
	 * See {@link #particle(double, double, double, double, double, double)}, return itself
	 * */
	public default IContext iparticle(double lx, double ly, double lz, double vx, double vy, double vz) {
		this.particle(lx, ly, lz, vx, vy, vz);
		
		return this;
	}
	
	/**
	 * See {@link #particle(double, double, double, double, double, double)}, return itself, the given v* mul vs
	 * */
	public default IContext iparticle(double lx, double ly, double lz, double vx, double vy, double vz, double vs) {
		return this.iparticle(lx, ly, lz, vx * vs, vy * vs, vz * vs);
	}
	
	/**
	 * See {@link #particle(double, double, double, double, double, double)}, return itself, the v* is zero
	 * */
	public default IContext iparticle(double lx, double ly, double lz) {
		this.particle(lx, ly, lz, 0, 0, 0);
		
		return this;
	}
	
	/**
	 * See {@link #act(Pool)}, return itself
	 * */
	public default IContext iact(Pool tech) {
		this.act(tech);
		
		return this;
	}
}
