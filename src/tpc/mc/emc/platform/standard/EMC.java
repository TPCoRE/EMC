package tpc.mc.emc.platform.standard;

/**
 * Current&Prev EMC Information
 * */
public enum EMC {
	
	ALPHA("0.0.0");
	
	/**
	 * Get the current EMC information
	 * */
	public static final EMC current() {
		return ALPHA;
	}
	
	//-----------------------------------------------------
	
	/**
	 * The current version
	 * */
	private final String version;
	
	/**
	 * Init an enum
	 * */
	private EMC(String version) {
		assert(version != null);
		
		this.version = version;
	}
	
	/**
	 * Get the current minor version
	 * */
	public final String minor() {
		return this.version;
	}
	
	/**
	 * Get the current major version
	 * */
	public final String major() {
		return this.name();
	}
	
	@Override
	public String toString() {
		return "[" + this.major() + "]" + this.minor();
	}
}
