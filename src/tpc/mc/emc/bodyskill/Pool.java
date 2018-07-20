package tpc.mc.emc.bodyskill;

import net.minecraft.src.EntityPlayer;

/**
 * BodySkill Pool
 * */
public enum Pool {
	
	DOUBLEJUMP {
		
		@Override
		public void activate(EntityPlayer player, Object model) {
			
		}
		
		@Override
		public void activating(EntityPlayer player, Object model, int activeTime) {
			
		}
		
		@Override
		public void activated(EntityPlayer player, Object model) {
			
		}
	};
	
	public abstract void activate(EntityPlayer player, Object model); //start to activate, both side, client side: model may be null still, if it isn't null, it means you can handle the model
	public abstract void activating(EntityPlayer player, Object model, int activeTime); //during update, in server activeTime equals -1, both side, client side:...
	public abstract void activated(EntityPlayer player, Object model); //on stop, both side, client side:...
}
