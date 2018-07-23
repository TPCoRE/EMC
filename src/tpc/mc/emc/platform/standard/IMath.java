package tpc.mc.emc.platform.standard;

/**
 * The interface of MathHelper
 * */
public interface IMath {
	
	public float sin(float i);
	public float cos(float i);
	
	/**
	 * It is safe
	 * */
	public static final IMath INSTANCE = null;
}
