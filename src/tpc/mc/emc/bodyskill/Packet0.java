package tpc.mc.emc.bodyskill;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.src.AbstractClientPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet;
import tpc.mc.emc.EMC$Util;

/**
 * Change skill
 * */
public final class Packet0 extends Packet {
	
	private Pool skill;
	
	public Packet0(Pool skill) {
		this.skill = skill;
	}
	
	@Override
	public void readPacketData(DataInput var1) throws IOException {
		int i = var1.readInt();
		if(i != -1) skill = Pool.values()[i];
	}
	
	@Override
	public void writePacketData(DataOutput var1) throws IOException {
		var1.writeInt(skill == null ? -1 : skill.ordinal());
	}
	
	@Override
	public void processPacket(NetHandler var1) {
		if(var1.isServerHandler()) {
			EMC$Util.act(((NetServerHandler) var1).playerEntity, skill);
		} else { //client side
			AbstractClientPlayer player = Minecraft.getMinecraft().thePlayer;
			
			try {
				//mark old
				Pool old = EMC$Util.bodyskill(player);
				if(old != null) {
					EMC$Util.activated(player, old);
					EMC$Util.olds(player).add(old);
				}
				
				//reset activeTime
				EMC$Util.BODYSKILL_ACTIVETIME.set(player, 0);
				
				//start new
				EMC$Util.BODYSKILL.set(player, skill); //set new
				if(skill != null) skill.activate(player, null); //client act
			} catch(Throwable e) { throw new RuntimeException(e); }
		}
	}
	
	@Override
	public int getPacketSize() {
		return 4;
	}
}
