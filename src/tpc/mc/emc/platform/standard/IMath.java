package tpc.mc.emc.platform.standard;

/**
 * Invoke Minecraft's MathHelper
 * */
public abstract class IMath {
	
	private static final IMath STATIC = $Impl.impl().math();
	
	IMath() {}
	
	public abstract float sin(float i);
	public abstract float cos(float i);
	
	/**
	 * Get the instance so that you can use it
	 * */
	public static final IMath instance() {
		return STATIC;
	}
}
