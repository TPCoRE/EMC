package tpc.mc.emc.platform.standard;

/**
 * For taking a tech from {@link tpc.mc.emc.tech.Pool TechPool}, notice that it is thread-unsafe
 * */
public abstract class IOption {
	
	IOption() {}
	
	/**
	 * Whether the option is for client, will return the same value for all the time
	 * */
	public abstract boolean client();
	
	/**
	 * Whether the option is for server, will return the same value for all the time
	 * */
	public abstract boolean server();
	
	/**
	 * Whether the option if or client-model, client-model means the model operation is available, will return the same value for all the time
	 * */
	public abstract boolean model();
	
	/**
	 * Add the velocity of the player
	 * */
	public abstract void accel(double vx, double vy, double vz);
	
	/**
	 * Add the velocity that depend on the lookvec of the player
	 * */
	public abstract void accel(double v);
	
	/**
	 * Make the motion of the player break off
	 * */
	public abstract void halt();
	
	/**
	 * Spawn particle in
	 * */
	public abstract void particle(double lx, double ly, double lz, double vx, double vy, double vz);
	
	/**
	 * Return the tick existed
	 * */
	public abstract int ticks();
	
	/**
	 * Alloc a {@link IContext}
	 * */
	public abstract IContext alloc();
}
