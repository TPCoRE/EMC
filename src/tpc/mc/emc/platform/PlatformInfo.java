package tpc.mc.emc.platform;

import java.net.URL;

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
	
	/**
	 * Get the currently running minecraft platform's vendor_url
	 * */
	public static final URL vurl() {
		return VENDOR_URL;
	}
	
	private static final String VERSION = null;
	private static final String VENDOR = null;
	private static final URL VENDOR_URL = null;
}
