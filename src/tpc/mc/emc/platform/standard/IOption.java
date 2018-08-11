package tpc.mc.emc.platform.standard;

/**
 * Stand for the attributes of a player, things in it are all unmodifiable
 * */
public interface IOption {
	
	/**
	 * Whether the player is in client-model, client-model means you can handle the model of the player
	 * */
	boolean model();
	
	/**
	 * It will alloc a {@link IContext}
	 * */
	IContext alloc();
}
