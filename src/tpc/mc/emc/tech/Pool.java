package tpc.mc.emc.tech;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * Tech Pool
 * */
public enum Pool {
	
	DOUBLEJUMP {
		
		@Override
		public Stepable take(IOption opt) {
			return new Stepable() {
				
				@Override
				public Stepable next() {
					System.out.println(opt.ticks() + ":" + opt.client() + ":" + opt.model()); //TODO
					return this;
				}
			};
		}
	};
	
	/**
	 * Get the steps of the tech
	 * */
	public abstract Stepable take(IOption opt);
}
