package tpc.mc.emc.bodyskill;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;
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
			if(model != null) { rush((ModelBiped) model); return; }
			World w = player.worldObj;
			
			if(0 < activeTime && activeTime < 4) {
				Random rnd = player.getRNG();
				AxisAlignedBB bb = player.boundingBox;
				double x = (bb.maxX + bb.minX) * 0.5;
				double y = (bb.maxY + bb.minY) * 0.5;
				double z = (bb.maxZ + bb.minZ) * 0.5;
				
				for(int i = 0, l = 80 + rnd.nextInt(40); i < l; ++i) {
					w.spawnParticle("enchantmenttable", x + rnd.nextDouble() * (rnd.nextBoolean() ? 0.65 : -0.65), y + rnd.nextDouble() * (rnd.nextBoolean() ? 0.89 : -0.89), z + rnd.nextDouble() * (rnd.nextBoolean() ? 0.65 : -0.65), 0, 0, 0);
				}
				
				Vec3 v = player.getLookVec();
				player.motionX = v.xCoord * 8;
				player.motionY = v.yCoord * 8;
				player.motionZ = v.zCoord * 8;
			} else if(activeTime == 4) EMC$Util.act(player, null);
			
			player.fallDistance = 0;
		}
		
		@Override
		public boolean monitor(EntityPlayer player, Object model) {
			return player.onGround;
		}
		
		@Override
		public void activated(EntityPlayer player, Object model) {
			player.motionX = player.motionY = player.motionZ = 0;
			player.fallDistance = 0;
			
			//reset model
			if(model != null) rush_ed((ModelBiped) model);
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
			} else ;//player.addPotionEffect(new PotionEffect(Potion.weakness.id, 60));
						
			player.fallDistance = 0;
		}
		
		@Override
		public void activating(EntityPlayer player, Object model, int activeTime) {
			if(model != null) rush((ModelBiped) model);
			else if(activeTime == 3) EMC$Util.act(player, null);
		}
		
		@Override
		public void activated(EntityPlayer player, Object model) {
			if(model != null) rush_ed((ModelBiped) model);
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
		public void activating(EntityPlayer player, Object model, int activeTime) {
			DOUBLEJUMP.activating(player, model, activeTime);
		}
		
		@Override
		public void activated(EntityPlayer player, Object model) {
			DOUBLEJUMP.activated(player, model);
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
				player.motionY = 0.725;
			}
			
			player.fallDistance = 0F;
		}
		
		@Override
		public void activating(EntityPlayer player, Object model, int activeTime) {
			if(model != null) jump((ModelBiped) model);
			else if(activeTime == 5) EMC$Util.act(player, null);
		}
		
		@Override
		public void activated(EntityPlayer player, Object model) {
			if(model != null) jump_ed((ModelBiped) model);
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
	
	private static final void jump(ModelBiped m) {
		ModelRenderer mr;
		
		//hand right up
		mr = m.bipedRightArm;
		mr.rotateAngleZ = 0.9773844F;
		mr.rotationPointY = 3;
		
		//hand left up
		mr = m.bipedLeftArm;
		mr.rotateAngleZ = -0.9773844F;
		mr.rotationPointY = 3;
		
		//modify right leg
		mr = m.bipedRightLeg;
		mr.rotateAngleX = 0.6320364F;
		mr.rotateAngleY = 0;
		mr.rotateAngleZ = -0.0743572F;
		mr.setRotationPoint(-2, 10, -3);
		
		//modify left leg
		mr = m.bipedLeftLeg;
		mr.rotateAngleX = mr.rotateAngleY = mr.rotateAngleZ = 0;
	}
	
	private static final void jump_ed(ModelBiped m) {
		ModelRenderer mr;
		
		//hand down
		m.bipedRightArm.rotateAngleZ = m.bipedLeftArm.rotateAngleZ = 0;
		m.bipedRightArm.rotationPointY = m.bipedLeftArm.rotationPointY = 2;
		
		//leg down
		mr = m.bipedRightLeg;
		mr.rotateAngleX = 0;
		mr.rotateAngleZ = 0;
		mr.setRotationPoint(-2, 12, 0);
	}
	
	private static final void rush(ModelBiped m) {
		ModelRenderer mr;
		
		//modify right arm
		mr = m.bipedRightArm;
		mr.rotateAngleX = -1.784573F;
		mr.rotateAngleY = mr.rotateAngleZ = 0;
		
		//modify left arm
		mr = m.bipedLeftArm;
		mr.rotateAngleX = 0.9666439F;
		mr.rotateAngleY = mr.rotateAngleZ = 0;
		
		//modify right leg
		mr = m.bipedRightLeg;
		mr.rotateAngleX = 0.4833219F;
		mr.rotateAngleY = mr.rotateAngleZ = 0;
		mr.setRotationPoint(-2F, 10F, -3.5F);
		
		//modify left leg
		mr = m.bipedLeftLeg;
		mr.rotateAngleX = 0.2974289F;
		mr.rotateAngleY = mr.rotateAngleZ = 0;
		
	}
	
	private static final void rush_ed(ModelBiped m) {
		ModelRenderer mr;
		
		//modify right arm
		mr = m.bipedRightArm;
		mr.rotateAngleX = mr.rotateAngleY = mr.rotateAngleZ = 0;
		
		//modify left arm
		mr = m.bipedLeftArm;
		mr.rotateAngleX = mr.rotateAngleY = mr.rotateAngleZ = 0;
		
		//modify right leg
		mr = m.bipedRightLeg;
		mr.rotateAngleX = mr.rotateAngleY = mr.rotateAngleZ = 0;
		mr.setRotationPoint(-2F, 12F, 0F);
		
		//modify left leg
		mr = m.bipedLeftLeg;
		mr.rotateAngleX = mr.rotateAngleY = mr.rotateAngleZ = 0;
	}
}
