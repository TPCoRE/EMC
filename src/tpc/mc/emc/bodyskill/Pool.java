package tpc.mc.emc.bodyskill;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import tpc.mc.emc.EMC$Util;

/**
 * BodySkill Pool
 * */
public enum Pool {
	
	/**
	 * A skill that allow steve JUMP+DIRECTION itself
	 * */
	DIRECTION {
		
		@Override
		public boolean able(EntityPlayer player) {
			return !EMC$Util.monitor(player, this, 2);
		}
		
		@Override
		public void activating(EntityPlayer player, Object model, int activeTime) {
			if(model != null) return;
			World w = player.worldObj;
			
			if(0 < activeTime && activeTime < 15) {
				Random rnd = player.getRNG();
				AxisAlignedBB bb = player.boundingBox;
				double x = (bb.maxX + bb.minX) * 0.5;
				double y = (bb.maxY + bb.minY) * 0.5;
				double z = (bb.maxZ + bb.minZ) * 0.5;
				
				for(int i = 0, l = 80 + rnd.nextInt(40); i < l; ++i) {
					w.spawnParticle("enchantmenttable", x + rnd.nextDouble() * (rnd.nextBoolean() ? 0.65 : -0.65), y + rnd.nextDouble() * (rnd.nextBoolean() ? 0.89 : -0.89), z + rnd.nextDouble() * (rnd.nextBoolean() ? 0.65 : -0.65), 0, 0, 0);
				}
				
				Vec3 v = player.getLookVec();
				player.motionX = v.xCoord * 0.4;
				player.motionY = v.yCoord * 0.4;
				player.motionZ = v.zCoord * 0.4;
			} else if(activeTime > 0) EMC$Util.act(player, null);
			
			player.fallDistance = 0;
			player.capabilities.isFlying = true;
			player.moveEntity(0, 0, 0);
		}
		
		@Override
		public void activated(EntityPlayer player, Object model) {
			player.capabilities.isFlying = false;
			player.motionX = player.motionY = player.motionZ = 0;
		}
		
		@Override
		public boolean monitor(EntityPlayer player, Object model) {
			return player.onGround;
		}
	}, 
	
	/**
	 * A skill that allow steve accel its speed
	 * */
	ACCEL {
		
		@Override
		public boolean able(EntityPlayer player) {
			return !player.isPotionActive(Potion.weakness);
		}
		
		@Override
		public void activate(EntityPlayer player, Object model) {
			if(model != null) return;
			World w = player.worldObj;
			
			if(w.isRemote) {
				Random rnd = player.getRNG();
				AxisAlignedBB bb = player.boundingBox;
				double x = (bb.maxX + bb.minX) * 0.5;
				double y = (bb.maxY + bb.minY) * 0.5;
				double z = (bb.maxZ + bb.minZ) * 0.5;
				
				for(int i = 0, l = 60 + rnd.nextInt(20); i < l; ++i) {
					w.spawnParticle("enchantmenttable", x + rnd.nextDouble() * (rnd.nextBoolean() ? 0.65 : -0.65), y + rnd.nextDouble() * (rnd.nextBoolean() ? 0.89 : -0.89), z + rnd.nextDouble() * (rnd.nextBoolean() ? 0.65 : -0.65), 0, 0, 0);
				}
				
				Vec3 v = player.getLookVec();
				player.addVelocity(v.xCoord * 1.065D, v.yCoord * 1.065D, v.zCoord * 1.065D);
				EMC$Util.act(player, null);
			} else player.addPotionEffect(new PotionEffect(Potion.weakness.id, 60));
						
			player.fallDistance = 0;
			player.moveEntity(0, 0, 0);
		}
		
		@Override
		public boolean monitor(EntityPlayer player, Object model) {
			return player.onGround;
		}
	}, 
	
	/**
	 * A skill that allow steve jump third
	 * */
	TRIPLEJUMP {
		
		@Override
		public boolean able(EntityPlayer player) {
			return !EMC$Util.monitor(player, this, 2);
		}
		
		@Override
		public void activate(EntityPlayer player, Object model) {
			DOUBLEJUMP.activate(player, model);
		}
		
		@Override
		public boolean monitor(EntityPlayer player, Object model) {
			return player.onGround;
		}
	}, 
	
	
	/**
	 * A skill that allow steve jump twice
	 * */
	DOUBLEJUMP {
		
		@Override
		public boolean able(EntityPlayer player) {
			return !EMC$Util.monitor(player, this);
		}
		
		@Override
		public void activate(EntityPlayer player, Object model) {
			if(model != null) return;
			World w = player.worldObj;
			
			if(w.isRemote) {
				AxisAlignedBB bb = player.boundingBox;
				Random rnd = player.getRNG();
				double y = bb.minY + 0.5D;
				double x = (bb.maxX + bb.minX) * 0.5D;
				double z = (bb.maxZ + bb.minZ) * 0.5D;
				double r = 0.65D;
				int di = 2;
				
				for(int lay = 0; lay < 3; ++lay) {
					y -= lay * r;
					di += 1;
					
					for(int i = 0; i < 180; i += di) {
						double scale = rnd.nextBoolean() ? 0.45 : -0.45;
						
						w.spawnParticle("enchantmenttable", x + MathHelper.sin(i) * r + rnd.nextDouble() * scale, y + rnd.nextDouble() * 0.3, z + MathHelper.cos(i) * r + rnd.nextDouble() * scale, 0, 0, 0);
					}
					
					//post fix
					r -= 0.2D;
				}
				
				//vel up
				player.addVelocity(0, 1, 0);
				EMC$Util.act(player, null);
			}
			
			player.fallDistance = 0F;
			player.moveEntity(0, 0, 0); //不然onGround没更新，按快可以出三段跳(不清楚)
		}
		
		@Override
		public boolean monitor(EntityPlayer player, Object model) {
			return player.onGround;
		}
	};
	
	
	
	//the type of the model is ModelBiped, if the model is nonnull, it means you are in a client-model context
	public boolean able(EntityPlayer player) { return true; } //both side, return true means the skill is able to start
	public void activate(EntityPlayer player, Object model) {} //start to activate, both side, client side: model may be null still, if it isn't null, it means you can handle the model
	public void activating(EntityPlayer player, Object model, int activeTime) {} //during update, in server activeTime equals -1, both side, client side:...
	public void activated(EntityPlayer player, Object model) {} //on stop, both side, client side:...
	public boolean monitor(EntityPlayer player, Object model) { return true; } //after stop, the skill will be put in a queue, it allow the used skill to do some post processing, return true means the skill could be remove from the queue, both side
}
