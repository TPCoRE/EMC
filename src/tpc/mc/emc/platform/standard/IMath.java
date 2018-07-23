package tpc.mc.emc.platform.standard;

/**
 * The interface of MathHelper
 * */
public abstract class IMath {
	
	public abstract float sin(float i);
	public abstract float cos(float i);
	
	/**
	 * It is safe
	 * */
	public static final IMath INSTANCE = null;
}
