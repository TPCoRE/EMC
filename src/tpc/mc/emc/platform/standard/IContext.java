package tpc.mc.emc.platform.standard;

import java.util.function.Predicate;

import tpc.mc.emc.err.ClosedException;
import tpc.mc.emc.tech.ITech;

/**
 * The player(EntityPlayer, PlayerModel(May Null))'s handler, notice that if the context was closed or the visitor thread isn't the owner of the context, the follow operations will cause exception
 * */
public abstract class IContext implements AutoCloseable {
	
	//---------------------------------Basic------------------------------
	
	/**
	 * Check if the given thread is the owner of the context
	 * */
	public abstract boolean verify(Thread owner);
	
	/**
	 * Check if the context belongs to the given {@link IOption}
	 * */
	public abstract boolean verify(IOption opt);
	
	/**
	 * Whether the context was closed, it will never throw exception
	 * */
	public abstract boolean released();
	
	/**
	 * Close the current context, dup close will throw exception
	 * */
	public abstract void close();
	
	//---------------------------------Player Handler---------------------
	
	/**
	 * Whether the player isn't on the ground
	 * */
	public abstract boolean airborne();
	
	/**
	 * Get the tickes that the player existed
	 * */
	public abstract int tickes();
	
	/**
	 * Accel the player with the given velocity
	 * */
	public abstract void accel(double vx, double vy, double vz);
	
	/**
	 * Accel the player forwardly
	 * */
	public abstract void accel(double vl);
	
	/**
	 * Mul the velocity with the given factor, notice that if the factor is zero, the player will stop suddenly
	 * */
	public abstract void scale(double factor);
	
	/**
	 * Spawn some particles
	 * */
	public abstract void particle(double lx, double ly, double lz, double vx, double vy, double vz);
	
	/**
	 * Act the given tech, no matter if it is available
	 * */
	public abstract void act(ITech tech);
	
	/**
	 * Across the acting teches
	 * */
	public abstract void check(Predicate<ITech> proxy);
	
	//---------------------------------Helper(Basic)------------------------------
	
	/**
	 * See {@link #verify(IOption)}, return itself
	 * */
	public IContext iverify(IOption opt) {
		return this.iverify(opt, true);
	}
	
	/**
	 * See {@link #verify(IOption)}, return itself
	 * */
	public IContext iverify(IOption opt, boolean expect) {
		if(this.verify(opt) != expect) throw new IllegalAccessError();
		
		return this;
	}
	
	/**
	 * Check if the context has closed, and check the ownership, if it was closed the method will throw an exception, return itself
	 * */
	public IContext idoubt() {
		if(this.released()) throw new ClosedException();
		if(!this.verify(Thread.currentThread())) throw new IllegalAccessError();
		
		return this;
	}
	
	//---------------------------------Helper(Player Handler)-----------------------
	
	/**
	 * See {@link #accel(double, double, double)}, return itself
	 * */
	public IContext iaccel(double vx, double vy, double vz) {
		this.accel(vx, vy, vz);
		
		return this;
	}
	
	/**
	 * See {@link #accel(double, double, double)}, the method is the same as xx.accel(vx * vs, vy * vs, vz * vs), return itself
	 * */
	public IContext iaccel(double vx, double vy, double vz, double vs) {
		return this.iaccel(vx * vs, vy * vs, vz * vs);
	}
	
	/**
	 * See {@link #accel(double)}, return itself
	 * */
	public IContext iaccel(double vl) {
		this.accel(vl);
		
		return this;
	}
	
	/**
	 * See {@link #scale(double)}, return itself
	 * */
	public IContext iscale(double factor) {
		this.scale(factor);
		
		return this;
	}
	
	/**
	 * See {@link #scale(double)}, return itself, xx.scale(0)
	 * */
	public IContext ihalt() {
		return this.iscale(0);
	}
	
	/**
	 * See {@link #particle(double, double, double, double, double, double)}, return itself
	 * */
	public IContext iparticle(double lx, double ly, double lz, double vx, double vy, double vz) {
		this.particle(lx, ly, lz, vx, vy, vz);
		
		return this;
	}
	
	/**
	 * See {@link #particle(double, double, double, double, double, double)}, return itself, the given v* mul vs
	 * */
	public IContext iparticle(double lx, double ly, double lz, double vx, double vy, double vz, double vs) {
		return this.iparticle(lx, ly, lz, vx * vs, vy * vs, vz * vs);
	}
	
	/**
	 * See {@link #particle(double, double, double, double, double, double)}, return itself, the v* is zero
	 * */
	public IContext iparticle(double lx, double ly, double lz) {
		this.particle(lx, ly, lz, 0, 0, 0);
		
		return this;
	}
	
	/**
	 * See {@link #act(ITech)}, return itself
	 * */
	public IContext iact(ITech tech) {
		this.act(tech);
		
		return this;
	}
	
	/**
	 * See {@link #check(Predicate)}, return itself
	 * */
	public IContext icheck(Predicate<ITech> proxy) {
		this.check(proxy);
		
		return this;
	}
}
