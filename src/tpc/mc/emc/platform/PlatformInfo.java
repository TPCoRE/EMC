package tpc.mc.emc.platform;

import java.net.URL;

/**
 * Platform Info
 * */
public final class PlatformInfo {
	
	/**
	 * Get the Currently Running Minecraft Platform's Version
	 * */
	public static final String version() {
		String result = null;
		
		//get from forge
		try {
			result = support_forge();
		} catch(Throwable e) {}
		
		//get from user
		if(result == null) result = support_user();
		
		//return the result
		return result;
	}
	
	/**
	 * Get the Currently Running Minecraft Platform's Vendor
	 * */
	public static final String vendor() {
		return "Mojang"; //FOR SURE
	}
	
	/**
	 * Get the currently running minecraft platform's vendor_url
	 * */
	public static final URL vurl() {
		try {
			return new URL("https://www.mojang.com/");
		} catch(Throwable e) {
			assert(false) : "vendor Url Get Failed!";
			
			return null;
		}
	}
	
	/**
	 * Get the mcversion from forge(if forge instead), if failed, return null
	 * */
	private static final String support_forge() {
		try {
			return (String) Class.forName("net.minecraftforge.common.MinecraftForge").getField("MC_VERSION").get(null);
		} catch(Throwable e) {
			assert(false) : "Forge Undefind!";
			
			return null;
		}
	}
	
	/**
	 * Get the mcversion from user
	 * */
	private static final String support_user() {
		assert(false) : "Inline Failed or Undefind!";
		
		return null;
	}
}
