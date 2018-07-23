package tpc.mc.emc.platform.standard;

/**
 * It stands for a player(EntityPlayer, PlayerModel) in EMC
 * */
public abstract class IOption implements Cloneable {
	
	/**
	 * Whether the player is in client
	 * */
	public abstract boolean client();
	
	/**
	 * Whether the player is in client-model, client-model means you can handle the model of the player
	 * */
	public abstract boolean model();
	
	/**
	 * It will alloc a {@link IContext}
	 * */
	public abstract IContext alloc();
	
	/**
	 * Get a copy, See {@link Cloneable}
	 * */
	public abstract IOption clone();
}
