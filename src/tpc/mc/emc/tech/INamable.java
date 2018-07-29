package tpc.mc.emc.tech;

import java.util.SortedMap;

/**
 * The tech that contains some information
 * */
public abstract class INamable extends ITech {
	
	/**
	 * Get a read-only map
	 * */
	public abstract SortedMap<String, String> infos();
}
