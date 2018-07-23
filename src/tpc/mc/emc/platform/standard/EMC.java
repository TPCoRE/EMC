package tpc.mc.emc.platform.standard;

/**
 * Current&Prev EMC Information
 * */
public enum EMC {
	
	ALPHA_0_0("1.6.4");
	
	/**
	 * Get the current EMC information
	 * */
	public static final EMC current() {
		return ALPHA_0_0;
	}
	
	//-----------------------------------------------------
	
	/**
	 * The MCVERSION we support
	 * */
	private final String[] support;
	
	/**
	 * Init an enum
	 * */
	private EMC(String... supports) {
		this.support = supports;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("EMC Version '");
		
		//Basic
		buffer.append(this.name());
		buffer.append("' SupportedMCV[");
		
		//supportedMCV
		int i, l;
		String[] support = this.support;
		for(i = 0, l = support.length - 1; i < l; ++i) {
			buffer.append(support[i]);
			buffer.append(" ");
		}
		
		//the last supported
		buffer.append(support[l]);
		buffer.append("]");
		return buffer.toString();
	}
}
