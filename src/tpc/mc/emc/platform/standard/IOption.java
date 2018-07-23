package tpc.mc.emc.platform.standard;

/**
 * It stands for a EntityPlayer in EMC
 * */
public interface IOption extends Cloneable {
	
	/**
	 * Whether the player is in client
	 * */
	public boolean client();
	
	/**
	 * Whether the player is in client-model, client-model means you can handle the model of the player
	 * */
	public boolean model();
	
	/**
	 * It will alloc a {@link IContext}
	 * */
	public IContext alloc();
	
	/**
	 * Get a copy, See {@link Cloneable}
	 * */
	public IOption clone();
}
