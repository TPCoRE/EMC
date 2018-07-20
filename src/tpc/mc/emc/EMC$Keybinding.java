package tpc.mc.emc;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Minecraft;
import net.minecraft.src.MovementInput;
import tpc.mc.emc.bodyskill.Pool;

public final class EMC$Keybinding {
	
	public static final KeyBinding BREAKSKILL = new KeyBinding("Break Skill", Keyboard.KEY_F);
	private static boolean JUMPSTATE = false;
	private static long WWTIMER = 0;
	
	/**
	 * Handle Keyboard/Mouse Input
	 * */
	public static final void handleInput(MovementInput input) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if(player == null) return;
		
		//prepare
		GameSettings sett = Minecraft.getMinecraft().gameSettings;
		final int jump = sett.keyBindJump.keyCode;
		final int forward = sett.keyBindForward.keyCode;
		
		//modify speed
		if(!input.sneak) player.moveFlying(0, input.moveForward, 0.05F);
		
		//break skill check
		if(GameSettings.isKeyDown(BREAKSKILL)) EMC$Util.act(player, null);
		
		//double jump check
		if(!player.onGround && Keyboard.getEventKey() == jump) {
			if(!Keyboard.getEventKeyState()) {
				JUMPSTATE = true;
			} else if(JUMPSTATE) {
				JUMPSTATE = false;
				
				EMC$Util.act(player, Pool.DOUBLEJUMP);
			}
		} else JUMPSTATE = false;
		
		//direction check
		if(!player.onGround) {
			if(Keyboard.isKeyDown(forward)) EMC$Util.act(player, Pool.DIRECTION);
		}
		
		//accel check, join in BETA-VERSTION
		if(Keyboard.getEventKey() == forward) {
			if(Keyboard.getEventKeyState()) {
				long t = System.currentTimeMillis();
				
				if(WWTIMER < 0 && t + WWTIMER < 250) EMC$Util.act(player, Pool.ACCEL);
				WWTIMER = t;
			} else if(WWTIMER > 0) WWTIMER = -WWTIMER;
		}
	}
}
