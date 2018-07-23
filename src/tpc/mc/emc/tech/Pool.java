package tpc.mc.emc.tech;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * Tech Pool, all teches are here
 * */
public enum Pool {
	
	/**
	 * A tech that allow player jump twice
	 * */
	DOUBLEJUMP {
		
		@Override
		public Stepable tack(IOption opt) {
			return null; //TODO
		}
	};
	
	/**
	 * Get a tech progress with the given option
	 * */
	public abstract Stepable tack(IOption opt);
}
