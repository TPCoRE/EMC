package tpc.mc.emc.tech.io;

import java.io.DataInput;

import tpc.mc.emc.tech.ITech;

/**
 * Allowed to read tech object
 * */
public interface TechInput extends DataInput {
	
	/**
	 * Read from stream
	 * */
	public default ITech readTech() {
		try {
			return IO.read(this);
		} catch(Throwable e) {
			return ITech.NOP;
		}
	}
}
