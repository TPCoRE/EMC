package tpc.mc.emc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.src.AbstractClientPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet;
import tpc.mc.emc.bodyskill.Pool;

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
			EMC$Util.act0(Minecraft.getMinecraft().thePlayer, skill);
		}
	}
	
	@Override
	public int getPacketSize() {
		return 4;
	}
}
