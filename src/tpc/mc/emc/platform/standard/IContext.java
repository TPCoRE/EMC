package tpc.mc.emc.platform.standard;

import java.util.function.Consumer;

import tpc.mc.emc.err.ClosedException;
import tpc.mc.emc.tech.Technique;

/**
 * The player(EntityPlayer, PlayerModel(May Null))'s handler, notice that if the context was closed, the follow operations will cause exception
 * */
public interface IContext extends AutoCloseable  {
	
	/**
	 * Check if the context belongs to the given {@link IOption}
	 * */
	boolean verify(IOption opt);
	
	/**
	 * Whether the context was closed, it will never throw exception
	 * */
	boolean released();
	
	/**
	 * Close the current context, dup close will throw exception
	 * */
	void close();
	
	/**
	 * Whether the player isn't on the ground
	 * */
	boolean airborne();
	
	boolean weak();
	
	/**
	 * Get the tickes that the player existed
	 * */
	int tickes();
	
	/**
	 * Accel the player with the given velocity
	 * */
	void accel(double vx, double vy, double vz);
	
	void accel(double vl);
	
	double velocityX();
	double velocityY();
	double velocityZ();
	
	/**
	 * Spawn a common lightning bolt, return the handler
	 * */
	Object lightning(double lx, double ly, double lz);
	
	/**
	 * Spawn a special lightning bolt, it will cause more damage
	 * */
	Object lightningSpeci(double lx, double ly, double lz);
	
	/**
	 * Randomly get a living base entity
	 * */
	Object randLivingBase();
	
	double posX(Object obj);
	double posY(Object obj);
	double posZ(Object obj);
	
	double[] raycast();
	
	/**
	 * Clear fall down status
	 * */
	void clearFallStatus();
	
	void tired();
	
	void assault();
	
	/**
	 * Spawn some particles, needs model enable
	 * */
	void particle(double lx, double ly, double lz, double vx, double vy, double vz);
	
	/**
	 * Act the given tech, no matter if it is available
	 * */
	void act(Technique tech);
	
	/**
	 * Across the acting teches
	 * */
	void check(Consumer<Technique> proxy);
	
	/**
	 * Throw exception if illegel
	 * */
	default IContext idoubt() {
		if(this.released()) throw new ClosedException();
		
		return this;
	}
}
