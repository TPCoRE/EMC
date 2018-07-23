package tpc.mc.emc.platform;

import java.net.URL;

import tpc.mc.emc.err.UnsupportedException;

/**
 * Platform Info, Version, Vendor information
 * */
public final class PlatformInfo {
	
	/**
	 * Get the Currently Running Minecraft Platform's Version
	 * */
	public static final String version() {
		throw new UnsupportedException("Inline Failed or Undefind!");
	}
	
	/**
	 * Get the Currently Running Minecraft Platform's Vendor
	 * */
	public static final String vendor() {
		throw new UnsupportedException("Inline Failed or Undefind!");
	}
	
	/**
	 * Get the currently running minecraft platform's vendor_url
	 * */
	public static final URL vurl() {
		throw new UnsupportedException("Inline Failed or Undefind!");
	}
}
