package tpc.mc.emc.platform;

/**
 * Platform Info, Version, Vendor information
 * */
public final class PlatformInfo {
	
	/**
	 * Get the Currently Running Minecraft Platform's Version
	 * */
	public static final String version() {
		return VERSION;
	}
	
	/**
	 * Get the Currently Running Minecraft Platform's Vendor
	 * */
	public static final String vendor() {
		return VENDOR;
	}
	
	private static final String VERSION = null;
	private static final String VENDOR = null;
}
